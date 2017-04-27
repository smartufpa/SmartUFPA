<?php

class User {
	
	private $userId;
	private $username;
	private $password;

	
	function __construct($username,$password) {
		$this->username = $username;
		$this->password = $password;
	}
	
	function getUsername() {
		return $this->username;
	}
	
	function getUserId() {
		return $this->userId;
	}
	
	function getPassword() {
		return $this->password;
	}
}


?>