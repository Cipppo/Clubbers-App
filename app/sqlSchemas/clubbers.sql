-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema clubbers
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema clubbers
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `clubbers` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `clubbers` ;

-- -----------------------------------------------------
-- Table `clubbers`.`Admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clubbers`.`Admin` (
  `admin_id` INT NOT NULL,
  `admin_name` VARCHAR(255) NOT NULL,
  `admin_email` VARCHAR(45) NOT NULL,
  `admin_password` VARCHAR(45) NOT NULL,
  `admin_image` VARCHAR(45) NOT NULL,
  `admin_bio` VARCHAR(45) NULL,
  `isAdmin` TINYINT(1) NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE INDEX `admin_name_UNIQUE` (`admin_name` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `clubbers`.`Event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clubbers`.`Event` (
  `event_id` INT NOT NULL,
  `event_name` VARCHAR(45) NOT NULL,
  `event_image` VARCHAR(45) NOT NULL,
  `event_location` VARCHAR(255) NOT NULL,
  `event_description` VARCHAR(255) NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `max_participants` INT NULL,
  `participants` INT NOT NULL,
  `admin_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  INDEX `adminId` (`admin_id` ASC) VISIBLE,
  CONSTRAINT `event_ibfk_1`
    FOREIGN KEY (`admin_id`)
    REFERENCES `clubbers`.`Admin` (`admin_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `clubbers`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clubbers`.`User` (
  `user_id` INT NOT NULL,
  `user_name` VARCHAR(30) NOT NULL,
  `user_email` VARCHAR(60) NOT NULL,
  `user_password` VARCHAR(45) NOT NULL,
  `user_image` VARCHAR(45) NOT NULL,
  `user_bio` VARCHAR(255) NULL,
  `isAdmin` TINYINT(0) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username_UNIQUE` (`user_name` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `clubbers`.`Participates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clubbers`.`Participates` (
  `user_id` INT NOT NULL,
  `event_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `event_id`),
  INDEX `eventId` (`event_id` ASC) VISIBLE,
  CONSTRAINT `partecipa_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `clubbers`.`User` (`user_id`),
  CONSTRAINT `partecipa_ibfk_2`
    FOREIGN KEY (`event_id`)
    REFERENCES `clubbers`.`Event` (`event_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `clubbers`.`Post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clubbers`.`Post` (
  `post_id` INT NOT NULL,
  `post_title` VARCHAR(45) NOT NULL,
  `post_image` VARCHAR(45) NULL,
  `post_caption` VARCHAR(45) NOT NULL,
  `eventId` INT NULL DEFAULT NULL,
  `userId` INT NULL DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  INDEX `eventId` (`eventId` ASC) VISIBLE,
  INDEX `userId` (`userId` ASC) VISIBLE,
  CONSTRAINT `post_ibfk_1`
    FOREIGN KEY (`eventId`)
    REFERENCES `clubbers`.`Event` (`event_id`),
  CONSTRAINT `post_ibfk_2`
    FOREIGN KEY (`userId`)
    REFERENCES `clubbers`.`User` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `clubbers`.`UserFollowsAdmin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clubbers`.`UserFollowsAdmin` (
  `user_id` INT NOT NULL,
  `admin_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `admin_id`),
  INDEX `followedId` (`admin_id` ASC) VISIBLE,
  CONSTRAINT `userfollowsadmin_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `clubbers`.`User` (`user_id`),
  CONSTRAINT `userfollowsadmin_ibfk_2`
    FOREIGN KEY (`admin_id`)
    REFERENCES `clubbers`.`Admin` (`admin_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `clubbers`.`UserFollowsUser`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clubbers`.`UserFollowsUser` (
  `follower_id` INT NOT NULL,
  `followed_id` INT NOT NULL,
  PRIMARY KEY (`follower_id`, `followed_id`),
  INDEX `followedId` (`followed_id` ASC) VISIBLE,
  CONSTRAINT `userfollowsuser_ibfk_1`
    FOREIGN KEY (`follower_id`)
    REFERENCES `clubbers`.`User` (`user_id`),
  CONSTRAINT `userfollowsuser_ibfk_2`
    FOREIGN KEY (`followed_id`)
    REFERENCES `clubbers`.`User` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `clubbers`.`Tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clubbers`.`Tag` (
  `tag_id` INT NOT NULL,
  `tag_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE INDEX `tag_name_UNIQUE` (`tag_name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clubbers`.`EventHasTag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clubbers`.`EventHasTag` (
  `event_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  PRIMARY KEY (`event_id`, `tag_id`),
  INDEX `fk_Event_has_Tag_Tag1_idx` (`tag_id` ASC) VISIBLE,
  INDEX `fk_Event_has_Tag_Event1_idx` (`event_id` ASC) VISIBLE,
  CONSTRAINT `fk_Event_has_Tag_Event1`
    FOREIGN KEY (`event_id`)
    REFERENCES `clubbers`.`Event` (`event_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Event_has_Tag_Tag1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `clubbers`.`Tag` (`tag_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
