-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 19-Abr-2017 às 23:35
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
  `amenity` varchar(20) NOT NULL,
  `description` varchar(140) NOT NULL,
  `latitude` float NOT NULL,
  `loc_name` varchar(30) NOT NULL,
  `longitude` float NOT NULL,
  `name` varchar(35) NOT NULL,
  `short_name` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(12) NOT NULL,
  `hash_password` varchar(72) NOT NULL,
  `permission` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `users`
--

INSERT INTO `users` (`user_id`, `username`, `hash_password`, `permission`) VALUES
(8, 'kae', '$2a$11$ODEwMTAyNjc0NThmN2JmOOiqnQ635XHD/Vvgf3rjwPsCSM/0tryCK', 1),
(19, 'kaes', '$2a$11$MjMwMTMxMzM2NThmN2NhYOhfwpg3N99Ec6x/9Cje/kLPRlYQO9N6a', 0),
(20, 'kaeu', '$2a$11$ODUzODEyNDg5NThmN2NlOO8bqsEGQnHnjKnhnLrP0ZF8pm6To8SJ.', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `places`
--
ALTER TABLE `places`
  ADD PRIMARY KEY (`place_id`);

--
-- Indexes for table `places_mod`
--
ALTER TABLE `places_mod`
  ADD PRIMARY KEY (`place_id`);

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
  MODIFY `place_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `places_mod`
--
ALTER TABLE `places_mod`
  MODIFY `place_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
