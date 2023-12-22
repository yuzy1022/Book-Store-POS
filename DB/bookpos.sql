CREATE DATABASE  IF NOT EXISTS `bookpos` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bookpos`;
-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bookpos
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `isbn` varchar(20) NOT NULL,
  `bookName` varchar(50) NOT NULL,
  `price` int NOT NULL,
  PRIMARY KEY (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES ('2090000117462','보그(2022년 11월호)',7500),('9788901260716 ','역행자',17500),('9788934974369 ','내 멋대로 선생님 뽑기',12000),('9788934993711','내 멋대로 아빠 뽑기',11500),('9788936438838 ','아버지의 해방일지',15000),('9788936479190 ','나의 문화유산답사기 11:서울편(3)',22000),('9788937460050 ','동물농장',8000),('9788937461033 ','인간 실격',9000),('9788954619585 ','단순한 열정',10000),('9788954699914 ','하얼빈',16000),('9788959897094 ','트렌드 코리아 2023',19000),('9788964964767 ','오싹오싹 크레용!',13000),('9788966351411 ','숲속 100층짜리 집',13000),('9788970509471 ','명품 JAVA Programming',33000),('9788998658335 ','사람을 얻는 지혜',13800),('9791156643388 ','쉽게 배우는 JSP 웹 프로그래밍',27000),('9791156645849 ','Andorid Studio를 활용한 안드로이드 프로그래밍',33000),('9791158741693 ','조금 서툴더라도 네 인생을 응원해',16800),('9791158741709 ','나 혼자만 알고 싶은 실전 심리학',16000),('9791158883591 ','부자 아빠 가난한 아빠 1',15800),('9791160407891 ','영의 자리',14000),('9791161571188','불편한 편의점',14000),('9791161571379','불편한 편의점2',14000),('9791167070791 ','인생의 허무를 어떻게 할 것인가',16000),('9791168340510 ','파친코 1 ',15800),('9791168340541 ','파친코 2',15800),('9791168414150 ','그건 부당합니다',17000),('9791187142560 ','데일 카네기 인간관계론',11500),('9791187694205 ','세상 거의 모든 치즈',27000),('9791192300245 ','마흔에 읽는 니체',16000),('9791197834202 ','다이어트 사이언스',22000);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branch`
--

DROP TABLE IF EXISTS `branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branch` (
  `branchNum` int NOT NULL,
  `branchName` varchar(30) NOT NULL,
  PRIMARY KEY (`branchNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch`
--

LOCK TABLES `branch` WRITE;
/*!40000 ALTER TABLE `branch` DISABLE KEYS */;
INSERT INTO `branch` VALUES (1000,'명지전문대점'),(2000,'강남역점'),(3000,'서울역점'),(4000,'광화문점'),(5000,'종로점');
/*!40000 ALTER TABLE `branch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consumer`
--

DROP TABLE IF EXISTS `consumer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consumer` (
  `payNum` bigint NOT NULL,
  `date` date NOT NULL,
  `consumer_posNum` int NOT NULL,
  `consumer_branchNum` int NOT NULL,
  PRIMARY KEY (`payNum`),
  KEY `consumer_posNum_idx` (`consumer_posNum`),
  KEY `consumer_branchNum_idx` (`consumer_branchNum`),
  CONSTRAINT `consumer_branchNum` FOREIGN KEY (`consumer_branchNum`) REFERENCES `branch` (`branchNum`),
  CONSTRAINT `consumer_posNum` FOREIGN KEY (`consumer_posNum`) REFERENCES `pos` (`posNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consumer`
--

LOCK TABLES `consumer` WRITE;
/*!40000 ALTER TABLE `consumer` DISABLE KEYS */;
INSERT INTO `consumer` VALUES (22121510000001,'2022-12-15',1,1000),(22121510000002,'2022-12-15',1,1000),(22121510000003,'2022-12-15',1,1000),(22121510000004,'2022-12-15',1,1000),(22121510000005,'2022-12-15',1,1000),(22121510000006,'2022-12-15',1,1000),(22121510000007,'2022-12-15',1,1000),(22121510000008,'2022-12-15',1,1000),(22121510000009,'2022-12-15',1,1000),(22121510000010,'2022-12-15',1,1000);
/*!40000 ALTER TABLE `consumer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consumer_buy`
--

DROP TABLE IF EXISTS `consumer_buy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consumer_buy` (
  `consumer_buy_isbn` varchar(20) NOT NULL,
  `consumer_buy_payNum` bigint NOT NULL,
  `date` date NOT NULL,
  `buyQuan` int NOT NULL,
  `consumer_buy_branchNum` int NOT NULL,
  PRIMARY KEY (`consumer_buy_isbn`,`consumer_buy_payNum`),
  KEY `consumer_buy_branchNum_idx` (`consumer_buy_branchNum`),
  KEY `consumer_buy_payNum_idx` (`consumer_buy_payNum`),
  CONSTRAINT `consumer_buy_branchNum` FOREIGN KEY (`consumer_buy_branchNum`) REFERENCES `branch` (`branchNum`),
  CONSTRAINT `consumer_buy_isbn` FOREIGN KEY (`consumer_buy_isbn`) REFERENCES `book` (`isbn`),
  CONSTRAINT `consumer_buy_payNum` FOREIGN KEY (`consumer_buy_payNum`) REFERENCES `consumer` (`payNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consumer_buy`
--

LOCK TABLES `consumer_buy` WRITE;
/*!40000 ALTER TABLE `consumer_buy` DISABLE KEYS */;
INSERT INTO `consumer_buy` VALUES ('9788901260716 ',22121510000009,'2022-12-15',1,1000),('9788934974369 ',22121510000007,'2022-12-15',1,1000),('9788934974369 ',22121510000010,'2022-12-15',1,1000),('9788934993711',22121510000005,'2022-12-15',1,1000),('9788936479190 ',22121510000008,'2022-12-15',1,1000),('9788936479190 ',22121510000010,'2022-12-15',1,1000),('9788937460050 ',22121510000010,'2022-12-15',1,1000),('9791156645849 ',22121510000001,'2022-12-15',1,1000),('9791158883591 ',22121510000004,'2022-12-15',1,1000),('9791160407891 ',22121510000006,'2022-12-15',1,1000),('9791167070791 ',22121510000006,'2022-12-15',1,1000),('9791168340541 ',22121510000002,'2022-12-15',1,1000),('9791168414150 ',22121510000003,'2022-12-15',1,1000),('9791187142560 ',22121510000007,'2022-12-15',1,1000),('9791187694205 ',22121510000002,'2022-12-15',1,1000),('9791197834202 ',22121510000005,'2022-12-15',1,1000);
/*!40000 ALTER TABLE `consumer_buy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consumer_pay`
--

DROP TABLE IF EXISTS `consumer_pay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consumer_pay` (
  `consumer_pay_payNum` bigint NOT NULL,
  `consumer_pay_branchNum` int NOT NULL,
  `date` date NOT NULL,
  `totalPay` int NOT NULL,
  `addPoint` int DEFAULT NULL,
  `cardPay` int NOT NULL DEFAULT '0',
  `cashPay` int NOT NULL DEFAULT '0',
  `pointPay` int NOT NULL DEFAULT '0',
  `consumer_pay_phone` char(13) DEFAULT NULL,
  `consumer_pay_empNum` int NOT NULL,
  PRIMARY KEY (`consumer_pay_payNum`),
  KEY `consumer_pay_branchNum_idx` (`consumer_pay_branchNum`),
  KEY `consumer_pay_phone_idx` (`consumer_pay_phone`),
  KEY `consumer_pay_empNum_idx` (`consumer_pay_empNum`),
  CONSTRAINT `consumer_pay_branchNum` FOREIGN KEY (`consumer_pay_branchNum`) REFERENCES `branch` (`branchNum`),
  CONSTRAINT `consumer_pay_empNum` FOREIGN KEY (`consumer_pay_empNum`) REFERENCES `employee` (`empNum`),
  CONSTRAINT `consumer_pay_payNum` FOREIGN KEY (`consumer_pay_payNum`) REFERENCES `consumer` (`payNum`),
  CONSTRAINT `consumer_pay_phone` FOREIGN KEY (`consumer_pay_phone`) REFERENCES `member` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consumer_pay`
--

LOCK TABLES `consumer_pay` WRITE;
/*!40000 ALTER TABLE `consumer_pay` DISABLE KEYS */;
INSERT INTO `consumer_pay` VALUES (22121510000001,1000,'2022-12-15',33000,330,33000,0,0,'010-4665-4112',1032),(22121510000002,1000,'2022-12-15',42800,428,0,22800,20000,'010-5555-6666',1102),(22121510000003,1000,'2022-12-15',17000,NULL,17000,0,0,NULL,1102),(22121510000004,1000,'2022-12-15',15800,NULL,0,15800,0,NULL,1102),(22121510000005,1000,'2022-12-15',33500,335,32750,0,750,'010-9562-9562',1102),(22121510000006,1000,'2022-12-15',30000,NULL,30000,0,0,NULL,1102),(22121510000007,1000,'2022-12-15',23500,NULL,0,23500,0,NULL,1645),(22121510000008,1000,'2022-12-15',22000,220,0,20000,2000,'010-4097-7971',1645),(22121510000009,1000,'2022-12-15',17500,175,10000,5800,1700,'010-4097-7971',1032),(22121510000010,1000,'2022-12-15',42000,420,0,42000,0,'010-1111-2222',1032);
/*!40000 ALTER TABLE `consumer_pay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `empNum` int NOT NULL,
  `empName` varchar(10) NOT NULL,
  `passwd` varchar(20) NOT NULL,
  `employee_branchNum` int NOT NULL,
  PRIMARY KEY (`empNum`),
  KEY `employee_branch_idx` (`employee_branchNum`),
  CONSTRAINT `employee_branchNum` FOREIGN KEY (`employee_branchNum`) REFERENCES `branch` (`branchNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1032,'강성주','akang32',5000),(1102,'유현민','xnvkc21',5000),(1201,'박재현','efj321',1000),(1211,'송형진','defi453',1000),(1221,'정은지','oelfdie3',5000),(1234,'박상원','abc123',1000),(1320,'최민환','hhdie298',5000),(1354,'홍길동','384dde',2000),(1355,'박도희','ggh3k2',3000),(1645,'한준섭','934k23',3000),(1744,'이재휘','lxzmmx9',4000),(1847,'이지은','keid001',2000),(1964,'김희원','iekd9x',4000);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `phone` char(13) NOT NULL,
  `name` varchar(10) NOT NULL,
  `point` int NOT NULL,
  PRIMARY KEY (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES ('010-1111-2222','한라산',571),('010-2222-3333','홍길동',1),('010-4033-8252','원상박',130),('010-4097-7971','현재박',245),('010-4485-8856','문별이',0),('010-4665-4112','진형송',1367),('010-5555-6666','설민수',3009),('010-6685-6685','김용주',999),('010-9562-9562','강산미',341);
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pos`
--

DROP TABLE IF EXISTS `pos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos` (
  `posNum` int NOT NULL,
  `pos_branchNum` int NOT NULL,
  PRIMARY KEY (`posNum`,`pos_branchNum`),
  KEY `branchNum_idx` (`pos_branchNum`),
  CONSTRAINT `pos_branchNum` FOREIGN KEY (`pos_branchNum`) REFERENCES `branch` (`branchNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pos`
--

LOCK TABLES `pos` WRITE;
/*!40000 ALTER TABLE `pos` DISABLE KEYS */;
INSERT INTO `pos` VALUES (1,1000),(2,1000),(3,1000),(1,2000),(2,2000),(1,3000),(2,3000),(3,3000),(4,3000),(1,4000),(2,4000),(3,4000),(1,5000),(2,5000),(3,5000),(4,5000),(5,5000);
/*!40000 ALTER TABLE `pos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation_book`
--

DROP TABLE IF EXISTS `reservation_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation_book` (
  `reservation_book_phone` char(13) NOT NULL,
  `reservation_book_isbn` varchar(20) NOT NULL,
  `date` date NOT NULL,
  `resQuan` int NOT NULL,
  PRIMARY KEY (`reservation_book_phone`,`reservation_book_isbn`,`date`),
  KEY `reservation_book_isbn_idx` (`reservation_book_isbn`),
  CONSTRAINT `reservation_book_isbn` FOREIGN KEY (`reservation_book_isbn`) REFERENCES `book` (`isbn`),
  CONSTRAINT `reservation_book_phone` FOREIGN KEY (`reservation_book_phone`) REFERENCES `member` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation_book`
--

LOCK TABLES `reservation_book` WRITE;
/*!40000 ALTER TABLE `reservation_book` DISABLE KEYS */;
INSERT INTO `reservation_book` VALUES ('010-2222-3333','9791167070791 ','2022-12-15',1),('010-4097-7971','9788936438838 ','2022-12-15',1),('010-6685-6685','9791158741693 ','2022-12-15',1),('010-6685-6685','9791160407891 ','2022-12-15',1);
/*!40000 ALTER TABLE `reservation_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `sales_branchNum` int NOT NULL,
  `date` date NOT NULL,
  `totalPaySales` int NOT NULL DEFAULT '0',
  `totalCardSales` int NOT NULL DEFAULT '0',
  `totalCashSales` int NOT NULL DEFAULT '0',
  `totalPointSales` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`sales_branchNum`,`date`),
  CONSTRAINT `sales_branchNum` FOREIGN KEY (`sales_branchNum`) REFERENCES `branch` (`branchNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'bookpos'
--

--
-- Dumping routines for database 'bookpos'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-22  9:32:55
