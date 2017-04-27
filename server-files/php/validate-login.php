<?php
include_once 'DAO/userDAO.php';
include_once 'models/user.php';
include_once 'session.php';


$user = new User($_POST ['username'],$_POST ['password']);
$userDao = UserDAO::getInstance();

if ($userDao->validateLogin($user)) {
	SessionClient::setLoggedIn ();
	$loggedIn = $_SESSION ['loggedIn'];
	header ( 'Location: moderate.php' );
} else {
	header ( 'Location: ./site/login.php?error=1' );
}

?>