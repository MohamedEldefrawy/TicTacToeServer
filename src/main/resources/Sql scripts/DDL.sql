-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema tictactoe
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tictactoe
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tictactoe` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `tictactoe` ;

-- -----------------------------------------------------
-- Table `tictactoe`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tictactoe`.`users` ;

CREATE TABLE IF NOT EXISTS `tictactoe`.`users` (
  `userName` VARCHAR(50) NOT NULL,
  `password` VARCHAR(20) NULL DEFAULT NULL,
  `wins` INT NULL DEFAULT '0',
  `losses` INT NULL DEFAULT '0',
  `draws` INT NULL DEFAULT '0',
  `isLoggedIn` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`userName`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `tictactoe`.`games`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tictactoe`.`games` ;

CREATE TABLE IF NOT EXISTS `tictactoe`.`games` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `playerOneName` VARCHAR(50) NULL DEFAULT NULL,
  `playerTwoName` VARCHAR(50) NULL DEFAULT NULL,
  `Winner` VARCHAR(50) NOT NULL DEFAULT 'Draw',
  `timeStamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `games_fk_1` (`playerOneName` ASC) VISIBLE,
  INDEX `games_fk_2` (`playerTwoName` ASC) VISIBLE,
  CONSTRAINT `games_fk_1`
    FOREIGN KEY (`playerOneName`)
    REFERENCES `tictactoe`.`users` (`userName`),
  CONSTRAINT `games_fk_2`
    FOREIGN KEY (`playerTwoName`)
    REFERENCES `tictactoe`.`users` (`userName`))
ENGINE = InnoDB
AUTO_INCREMENT = 339
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `tictactoe`.`records`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tictactoe`.`records` ;

CREATE TABLE IF NOT EXISTS `tictactoe`.`records` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `steps` VARCHAR(9) NULL DEFAULT NULL,
  `requesterName` VARCHAR(50) NULL DEFAULT NULL,
  `gameId` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `records_fk_1` (`requesterName` ASC) VISIBLE,
  INDEX `records_fk_2` (`gameId` ASC) VISIBLE,
  CONSTRAINT `records_fk_1`
    FOREIGN KEY (`requesterName`)
    REFERENCES `tictactoe`.`users` (`userName`),
  CONSTRAINT `records_fk_2`
    FOREIGN KEY (`gameId`)
    REFERENCES `tictactoe`.`games` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
