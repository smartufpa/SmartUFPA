<?php
include_once 'DAO/userDAO.php';
include_once 'models/user.php';
include_once 'session.php';

$userDao = UserDAO::getInstance();
$user = $userDao->getUserByUsername($_POST["username"]);
var_dump($user);

if ($userDao->validatePassword($user->getUserId(),$_POST["password"]) 
		&& ($user->getPermission() == 1)) {
	SessionClient::setLoggedIn ();
	$loggedIn = $_SESSION ['loggedIn'];
	header ( 'Location: site/moderate.php' );
} else if($user->getPermission() == 0) {
	header ( 'Location: site/login.php?error=2' );
} else{
	header ( 'Location: site/login.php?error=1' );
}

?>