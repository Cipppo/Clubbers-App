package com.example.clubbers.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.clubbers.NavigationApp
import com.example.clubbers.ui.theme.ClubbersTheme
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.WarningViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.net.URLEncoder

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Volley request variables
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private var queue: RequestQueue? = null

    private var requestingData = mutableStateOf(false)

    private val warningViewModel by viewModels<WarningViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                if (requestingData.value) {
                    sendRequest(connectivityManager)
                }
            }
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

    private fun startRequestingData() {
        requestingData.value = true
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        if (isOnline(connectivityManager)) {
            sendRequest(connectivityManager)
        } else {
            warningViewModel.setConnectivitySnackBarVisibility(true)
        }
    }

    private fun sendRequest(connectivityManager: ConnectivityManager) {
        val eventsViewModel by viewModels<EventsViewModel>()
        val location = getSharedPreferences("EventLocation", Context.MODE_PRIVATE)
            .getString("EventLocation", "No Location")
            .toString()
        val encodedInput = URLEncoder.encode(location, "utf-8")

        queue = Volley.newRequestQueue(this)
        val url = "https://nominatim.openstreetmap.org/?addressdetails=1&q=" +
                "${encodedInput}&format=jsonv2&limit=1"

        val jsonArrayRequest = JsonArrayRequest (
            Request.Method.GET, url, null,
            { response ->
                if (response.length() > 0) {
                    val first = response.get(0) as JSONObject
                    val locationN = first.get("display_name").toString()
                    eventsViewModel.setEventLocation(locationN)
                } else {
                    eventsViewModel.setEventLocation("")
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

    private companion object {
        private const val TAG = "OSM_REQUEST"
    }
}