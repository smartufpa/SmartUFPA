-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 09-Maio-2017 às 17:43
-- Versão do servidor: 10.1.21-MariaDB
-- PHP Version: 7.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `smart_ufpa`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `places`
--

CREATE TABLE `places` (
  `place_id` int(11) NOT NULL,
  `amenity` varchar(20) CHARACTER SET latin1 NOT NULL,
  `description` varchar(140) CHARACTER SET latin1 NOT NULL,
  `latitude` float NOT NULL,
  `loc_name` varchar(30) CHARACTER SET latin1 NOT NULL,
  `longitude` float NOT NULL,
  `name` varchar(35) CHARACTER SET latin1 NOT NULL,
  `short_name` varchar(10) CHARACTER SET latin1 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

--
-- Extraindo dados da tabela `places`
--

INSERT INTO `places` (`place_id`, `amenity`, `description`, `latitude`, `loc_name`, `longitude`, `name`, `short_name`) VALUES
(87, 'library', 'Descrição teste', -48.4527, 'Nome Local do prédio', -1.47346, 'Nome oficial', 'NO');

-- --------------------------------------------------------

--
-- Estrutura da tabela `places_mod`
--

CREATE TABLE `places_mod` (
  `place_id` int(11) NOT NULL,
  `amenity` varchar(20) NOT NULL,
  `description` varchar(140) NOT NULL,
  `latitude` float NOT NULL,
  `loc_name` varchar(30) NOT NULL,
  `longitude` float NOT NULL,
  `name` varchar(35) NOT NULL,
  `short_name` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- --------------------------------------------------------

--
-- Estrutura da tabela `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(12) CHARACTER SET latin1 NOT NULL,
  `hash_password` varchar(72) CHARACTER SET latin1 NOT NULL,
  `permission` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

--
-- Extraindo dados da tabela `users`
--

INSERT INTO `users` (`user_id`, `username`, `hash_password`, `permission`) VALUES
(21, 'kae', '$2a$11$MTQ2OTY4MTM1OGZlOTc0NOUae8fMMoBlyMA.gwq6m6Cf6VEudFQXi', 1),
(24, 'kaeuchoa', '$2a$11$MTgxMDMwODgyMzU5MDYzNukiUStFAKX24zGbeNYYhwb1gcvt8foD6', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `places`
--
ALTER TABLE `places`
  ADD PRIMARY KEY (`place_id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `places_mod`
--
ALTER TABLE `places_mod`
  ADD PRIMARY KEY (`place_id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `places`
--
ALTER TABLE `places`
  MODIFY `place_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=88;
--
-- AUTO_INCREMENT for table `places_mod`
--
ALTER TABLE `places_mod`
  MODIFY `place_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
