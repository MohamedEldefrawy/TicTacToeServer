CREATE DATABASE `tictactoe`;

use tictactoe;


CREATE TABLE `users`
(
    `userName`   varchar(50) primary key,
    `password`   varchar(20),
    `wins`       int     DEFAULT 0,
    `losses`     int     DEFAULT 0,
    `draws`      int     DEFAULT 0,
    `isLoggedIn` tinyint DEFAULT false
) ENGINE = InnoDB;

CREATE TABLE `games`
(
    `id`            int primary key AUTO_INCREMENT,
    `playerOneName` varchar(50),
    `playerTwoName` varchar(50),
    `Winner`        int         DEFAULT 3,
    `timeStamp` DATETIME default LOCALTIME(),

    CONSTRAINT `games_fk_1` FOREIGN KEY (`playerOneName`) REFERENCES `users` (`userName`),
    CONSTRAINT `games_fk_2` FOREIGN KEY (`playerTwoName`) REFERENCES `users` (`userName`)
) ENGINE = InnoDB;


CREATE TABLE `records`
(
    `id` int primary key AUTO_INCREMENT,
    `steps` varchar(9),
    `requesterName` varchar(50),
    `gameId` int,
    CONSTRAINT `records_fk_1` FOREIGN KEY (`requesterName`) REFERENCES `users` (`userName`),
    CONSTRAINT `records_fk_2` FOREIGN KEY (`gameId`) REFERENCES `games` (`id`)

)ENGINE = InnoDB;


