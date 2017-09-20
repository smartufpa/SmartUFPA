<?php
class SessionClient {
	public static function checkIfLoggedIn() {
		session_start();
		if (isset($_SESSION['loggedIn']) == false) {
			return false;
		} else {
			return true;
		}
	}
	public static function setLoggedIn() {
		session_start();
		$_SESSION["loggedIn"] = true;
	}
	public static function finishSession() {
		session_start();
		// remove all session variables
		session_unset();
		// destroy the session
		session_destroy();
		header("Location:login.php");
	}
}
?>