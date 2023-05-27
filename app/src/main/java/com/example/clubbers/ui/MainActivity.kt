package com.example.clubbers.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.clubbers.NavigationApp
import com.example.clubbers.data.details.LocationDetails
import com.example.clubbers.ui.theme.ClubbersTheme
import com.example.clubbers.viewModel.LocationsViewModel
import com.example.clubbers.viewModel.WarningViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.net.URLEncoder

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Location variables
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var requestingLocationUpdates = mutableStateOf(false)

    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

    val currentLocation = mutableStateOf(LocationDetails("", 0.toDouble(), 0.toDouble()))

    // Volley request variables
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private var queue: RequestQueue? = null

    private var requestingData = mutableStateOf(false)

    private val warningViewModel by viewModels<WarningViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                warningViewModel.setPermissionSnackBarVisibility(true)
            }
        }

        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).apply {
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                currentLocation.value = LocationDetails(
                    "",
                    p0.locations.first().latitude,
                    p0.locations.first().longitude
                )
                stopLocationUpdates()
                if (isOnline(connectivityManager = connectivityManager)) {
                    requestingData.value = true
                    connectivityManager.registerDefaultNetworkCallback(networkCallback)
                    if (isOnline(connectivityManager)) {
                        warningViewModel.setConnectivitySnackBarVisibility(false)
                        sendRequest(currentLocation.value, connectivityManager)
                    } else {
                        warningViewModel.setConnectivitySnackBarVisibility(true)
                    }
                } else {
                    warningViewModel.setConnectivitySnackBarVisibility(true)
                }
            }
        }

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                if (requestingData.value) {
                    sendRequest(connectivityManager = connectivityManager)
                }
            }
        }

        if (isOnline(connectivityManager)) {
            warningViewModel.setConnectivitySnackBarVisibility(false)
        } else {
            warningViewModel.setConnectivitySnackBarVisibility(true)
        }

        setContent {
            ClubbersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationApp(
                        startRequestingData = { startRequestingData() },
                        startLocationUpdates = { startLocationUpdates() },
                        warningViewModel = warningViewModel
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (requestingData.value) {
            (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .registerDefaultNetworkCallback(networkCallback)
        }
    }

    override fun onStop() {
        super.onStop()
        queue?.cancelAll(TAG)
        if (requestingData.value) {
            (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .unregisterNetworkCallback(networkCallback)
        }
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates.value) startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun startRequestingData() {
        requestingData.value = true
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        if (isOnline(connectivityManager)) {
            warningViewModel.setConnectivitySnackBarVisibility(false)
            sendRequest(connectivityManager = connectivityManager)
        } else {
            warningViewModel.setConnectivitySnackBarVisibility(true)
        }
    }

    private fun sendRequest(
        passedLocation: LocationDetails? = null,
        connectivityManager: ConnectivityManager
    ) {
        val locationsViewModel by viewModels<LocationsViewModel>()

        val location: LocationDetails = passedLocation
            ?: LocationDetails(
                getSharedPreferences("EventLocation", Context.MODE_PRIVATE)
                    .getString("EventLocation", "")
                    .toString(), 0.0, 0.0)
        val encodedInput = URLEncoder.encode(location.name, "utf-8")

        queue = Volley.newRequestQueue(this)

        val url: String
        if (location.latitude != 0.0 && location.longitude != 0.0) {
            url = "https://nominatim.openstreetmap.org/reverse?&lat=" +
                    "${location.latitude}&lon=${location.longitude}&format=jsonv2&limit=1"

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val locationName = response.get("display_name").toString()
                    locationsViewModel.setEventLocation(
                        LocationDetails(
                            locationName,
                            location.latitude,
                            location.longitude
                        )
                    )
                    connectivityManager.unregisterNetworkCallback(networkCallback)
                    requestingData.value = false
                },
                { error ->
                    Log.d("MAINACTIVITY-SENDREQUEST", error.toString())
                }
            )
            jsonObjectRequest.tag = TAG
            queue?.add(jsonObjectRequest)

        } else if (location.name != "") {
            url = "https://nominatim.openstreetmap.org/?addressdetails=1&q=" +
                    "${encodedInput}&format=jsonv2&limit=1"

            val jsonArrayRequest = JsonArrayRequest (
                Request.Method.GET, url, null,
                { response ->
                    if (response.length() > 0) {
                        val first = response.get(0) as JSONObject
                        val locationName = first.get("display_name").toString()
                        val locationLat = first.get("lat").toString().toDouble()
                        val locationLon = first.get("lon").toString().toDouble()
                        locationsViewModel.setEventLocation(
                            LocationDetails(
                                locationName,
                                locationLat,
                                locationLon
                            )
                        )
                    } else {
                        locationsViewModel.setEventLocation(
                            LocationDetails(
                                "",
                                0.0,
                                0.0
                            )
                        )
                    }
                    connectivityManager.unregisterNetworkCallback(networkCallback)
                    requestingData.value = false
                },
                { error ->
                    Log.d("MAINACTIVITY-SENDREQUEST", error.toString())
                }
            )
            jsonArrayRequest.tag = TAG
            queue?.add(jsonArrayRequest)
        }
    }

    private fun startLocationUpdates() {
        requestingLocationUpdates.value = true

        val permission = Manifest.permission.ACCESS_COARSE_LOCATION

        when {
            //permission already granted
            ContextCompat.checkSelfPermission (this, permission) == PackageManager.PERMISSION_GRANTED -> {
                locationRequest =
                    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).apply {
                        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                        setWaitForAccurateLocation(true)
                    }.build()

                val gpsEnabled = checkGPS()
                if (gpsEnabled) {
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                } else {
                    warningViewModel.setGPSAlertDialogVisibility(true)
                }
            }
            //permission already denied
            shouldShowRequestPermissionRationale(permission) -> {
                warningViewModel.setPermissionSnackBarVisibility(true)
            }
            else -> {
                //first time: ask for permissions
                locationPermissionRequest.launch(
                    permission
                )
            }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun isOnline(connectivityManager: ConnectivityManager): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        ) {
            return true
        }
        return false
    }

    private fun checkGPS(): Boolean {
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private companion object {
        private const val TAG = "OSM_REQUEST"
    }
}