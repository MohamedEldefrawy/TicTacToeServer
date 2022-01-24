CREATE DATABASE `tictactoe` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE `usergames` (
  `id` int NOT NULL AUTO_INCREMENT,
  `playerOneId` int DEFAULT NULL,
  `playerTwoId` int DEFAULT NULL,
  `WinnerId` int DEFAULT NULL,
  `gameBoard` varchar(9) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `playerOneId` (`playerOneId`),
  KEY `playerTwoId` (`playerTwoId`),
  CONSTRAINT `usergames_ibfk_1` FOREIGN KEY (`playerOneId`) REFERENCES `users` (`id`),
  CONSTRAINT `usergames_ibfk_2` FOREIGN KEY (`playerTwoId`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `wins` int DEFAULT NULL,
  `losses` int DEFAULT NULL,
  `draws` int DEFAULT NULL,
  `isLoggedIn` tinyint DEFAULT false,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userName` (`userName`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


