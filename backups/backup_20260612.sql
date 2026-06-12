-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: americoplantas
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alerta_validade`
--

DROP TABLE IF EXISTS `alerta_validade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alerta_validade` (
  `id_alerta` int NOT NULL AUTO_INCREMENT,
  `id_produto` int DEFAULT NULL,
  `mensagem` varchar(255) DEFAULT NULL,
  `data_alerta` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_alerta`),
  KEY `id_produto` (`id_produto`),
  CONSTRAINT `alerta_validade_ibfk_1` FOREIGN KEY (`id_produto`) REFERENCES `produto` (`id_produto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alerta_validade`
--

LOCK TABLES `alerta_validade` WRITE;
/*!40000 ALTER TABLE `alerta_validade` DISABLE KEYS */;
/*!40000 ALTER TABLE `alerta_validade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cpfCnpj` varchar(25) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `cep` varchar(20) DEFAULT NULL,
  `endereco` varchar(50) DEFAULT NULL,
  `numero` varchar(10) DEFAULT NULL,
  `data_nasc` date DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `cpfCnpj` (`cpfCnpj`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (2,'Laura','222.314.938-87','44 8283-9391','lauradossicampos@gmail.com','56460-000','Rua Nossa Senhora','131','2006-03-31'),(3,'Pablo','777.777.777-77','87 9921-2844','pablo@gmail.com','56460-000','Avenida Jardins','000','1997-10-15'),(4,'Julia','','','','','','',NULL),(7,'Ricardo da Silva',NULL,'',NULL,'','','',NULL),(8,'James','346.372.743-99','',NULL,'','','',NULL),(9,'Maria Clara',NULL,'',NULL,'','','',NULL),(10,'Edimilton',NULL,'',NULL,'','','','1980-08-20');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fornecedor`
--

DROP TABLE IF EXISTS `fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fornecedor` (
  `id_fornecedor` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cpfCnpj` varchar(25) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `endereco` varchar(50) DEFAULT NULL,
  `cep` varchar(20) DEFAULT NULL,
  `numero` varchar(10) DEFAULT NULL,
  `tiposDeProdutos` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_fornecedor`),
  UNIQUE KEY `cpfCnpj` (`cpfCnpj`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fornecedor`
--

LOCK TABLES `fornecedor` WRITE;
/*!40000 ALTER TABLE `fornecedor` DISABLE KEYS */;
INSERT INTO `fornecedor` VALUES (3,'Romário','27.214.534/0001-77','65 9430-8566','romarioervas@gmail.com','Rua Nogueira Ferraz','32553-000','234','Hortelã'),(4,'Wanessa','46.786.301/0001-93','75 0193-9133','empresadawannessa@gmail.com','Rua da Paz','32444-884','21','Camomila');
/*!40000 ALTER TABLE `fornecedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_venda`
--

DROP TABLE IF EXISTS `item_venda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_venda` (
  `id_item` int NOT NULL AUTO_INCREMENT,
  `id_venda` int NOT NULL,
  `id_produto` int NOT NULL,
  `quantidade` int NOT NULL,
  `valorUnitario` double NOT NULL,
  `subTotal` double NOT NULL,
  PRIMARY KEY (`id_item`),
  KEY `id_venda` (`id_venda`),
  KEY `id_produto` (`id_produto`),
  CONSTRAINT `item_venda_ibfk_1` FOREIGN KEY (`id_venda`) REFERENCES `venda` (`id_venda`) ON DELETE CASCADE,
  CONSTRAINT `item_venda_ibfk_2` FOREIGN KEY (`id_produto`) REFERENCES `produto` (`id_produto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_venda`
--

LOCK TABLES `item_venda` WRITE;
/*!40000 ALTER TABLE `item_venda` DISABLE KEYS */;
INSERT INTO `item_venda` VALUES (1,2,3,3,11.5,34.5),(2,3,2,2,5,10),(3,4,3,3,11.5,34.5),(4,6,1,25,11.99,299.75),(5,7,2,4,5,20);
/*!40000 ALTER TABLE `item_venda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_auditoria`
--

DROP TABLE IF EXISTS `log_auditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_auditoria` (
  `id_log` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `usuario_nome` varchar(100) NOT NULL,
  `acao` varchar(20) NOT NULL,
  `entidade` varchar(50) NOT NULL,
  `entidade_id` int NOT NULL,
  `detalhes` text,
  `data_hora` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_log`),
  KEY `idx_usuario` (`usuario_id`),
  KEY `idx_entidade` (`entidade`,`entidade_id`),
  KEY `idx_data` (`data_hora`),
  CONSTRAINT `fk_log_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_auditoria`
--

LOCK TABLES `log_auditoria` WRITE;
/*!40000 ALTER TABLE `log_auditoria` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_auditoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produto`
--

DROP TABLE IF EXISTS `produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produto` (
  `id_produto` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `tipo` varchar(100) NOT NULL,
  `estoque` int DEFAULT NULL,
  `preco` decimal(10,2) NOT NULL,
  `estado` varchar(20) NOT NULL,
  `dataFabricacao` date DEFAULT NULL,
  `dataValidade` date DEFAULT NULL,
  PRIMARY KEY (`id_produto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produto`
--

LOCK TABLES `produto` WRITE;
/*!40000 ALTER TABLE `produto` DISABLE KEYS */;
INSERT INTO `produto` VALUES (1,'Hortelã','Ervas',80,11.99,'Disponível','2025-09-20','2028-09-20'),(2,'Camomila','Chá',34,5.00,'Disponível','2026-05-04','2026-12-04'),(3,'Alecrim','Natural',15,11.50,'Indisponível','2018-02-20','2019-02-20'),(5,'Ibisco','Chá',27,0.30,'Disponível','2026-01-20','2027-01-20');
/*!40000 ALTER TABLE `produto` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb3 */ ;
/*!50003 SET character_set_results = utf8mb3 */ ;
/*!50003 SET collation_connection  = utf8mb3_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET SQL_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_validade_produto` AFTER INSERT ON `produto` FOR EACH ROW BEGIN
    IF NEW.dataValidade <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) THEN
        INSERT INTO alerta_validade (id_produto, mensagem)
        VALUES (
            NEW.id_produto,
            CONCAT('O produto ', NEW.nome, ' está próximo do vencimento.')
        );
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `relatorio`
--

DROP TABLE IF EXISTS `relatorio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `relatorio` (
  `id_relatorio` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(50) NOT NULL,
  `data_inicio` date DEFAULT NULL,
  `data_fim` date DEFAULT NULL,
  `data_geracao` datetime DEFAULT CURRENT_TIMESTAMP,
  `conteudo` longtext NOT NULL,
  PRIMARY KEY (`id_relatorio`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relatorio`
--

LOCK TABLES `relatorio` WRITE;
/*!40000 ALTER TABLE `relatorio` DISABLE KEYS */;
INSERT INTO `relatorio` VALUES (1,'Clientes','2026-06-06','2026-06-06','2026-06-06 15:55:44','=== RESUMO ===\nRELATÓRIO DE CLIENTES\n----------------------------------------\nTotal de clientes: 3\nClientes ativos: 3\n\nPeríodo: 06/06/2026 a 06/06/2026\n\n=== DETALHES ===\nID	Nome	CPF	Telefone	Endereço	Email	\n------------------------------------------------------------\n2	Laura	222.314.938-87	131	Rua Nossa Senhora	lauradossicampos@gmail.com	\n3	Pablo	777.777.777-77	87 9921-2844	Avenida Jardins	pablo@gmail.com	\n4	Julia					\n');
/*!40000 ALTER TABLE `relatorio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cpfCnpj` varchar(25) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `endereco` varchar(50) DEFAULT NULL,
  `cep` varchar(20) DEFAULT NULL,
  `numero` varchar(10) DEFAULT NULL,
  `data_nasc` date DEFAULT NULL,
  `tipo_usuario` enum('Administrador','Operador') DEFAULT NULL,
  `login` varchar(20) NOT NULL,
  `senha_hash` varchar(64) NOT NULL,
  `salt` varchar(64) DEFAULT NULL,
  `tentativas_falhas` int DEFAULT '0',
  `bloqueado_ate` datetime DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `login` (`login`),
  UNIQUE KEY `cpfCnpj` (`cpfCnpj`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Administrador','000.000.000-00','75 9293-8320','admin@gmail.com','Rua das Flores','00000-000','21','2000-05-31','Administrador','admin','PhBBqRKKW7sRnJDSL/C/RwbgjWkb6OJrPGt3PYyDvuw=','WUM9E28w3gV0UfymmQQNQw==',0,NULL),(3,'Mylene','555.555.555-55','75 8938-9866','mylene@gmail.com','Lago da Paz','51333-888','34','2005-07-20','Operador','mylene','SZRRwWLpLn3yTRRKcBYp0/woXyXnxXkXPdCo6tDnnSM=','z+xHq7+wJaepWNDiEivu0Q==',0,NULL),(4,'Laura Campos','999.333.319-31','87 9982-5632','lauradcf31@gmail.com','Rua dos Arcanjos','39000-000','44','2005-04-30','Operador','laura','7rLvES73s2nOX0vuSeOXPH+Cq4dDmlxpSuQ5iUPwew8=','XF+KVtUYyOEtbYlMSjrgVw==',0,NULL);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venda`
--

DROP TABLE IF EXISTS `venda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venda` (
  `id_venda` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int DEFAULT NULL,
  `id_vendedor` int DEFAULT NULL,
  `forma_pagamento` enum('Dinheiro','Pix','Cartão','Boleto') NOT NULL,
  `data_venda` datetime DEFAULT CURRENT_TIMESTAMP,
  `total` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id_venda`),
  KEY `id_cliente` (`id_cliente`),
  KEY `id_vendedor` (`id_vendedor`),
  CONSTRAINT `venda_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`),
  CONSTRAINT `venda_ibfk_2` FOREIGN KEY (`id_vendedor`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venda`
--

LOCK TABLES `venda` WRITE;
/*!40000 ALTER TABLE `venda` DISABLE KEYS */;
INSERT INTO `venda` VALUES (1,2,1,'Pix','2026-06-06 00:00:00',23.98),(2,3,3,'Cartão','2026-06-08 00:00:00',34.50),(3,4,1,'Pix','2026-06-08 00:00:00',10.00),(4,2,4,'Cartão','2026-06-08 00:00:00',34.50),(6,4,1,'Boleto','2026-06-08 00:00:00',299.75),(7,9,4,'Dinheiro','2026-06-09 15:30:10',20.00);
/*!40000 ALTER TABLE `venda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'americoplantas'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-12 14:43:02
