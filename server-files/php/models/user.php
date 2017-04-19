<?php

class User {
	
	private $userId;
	private $username;
	private $hashPassword;

	
	function __construct($username,$hashPassword) {
		$this->username = $username;
		$this->hashPassword = $hashPassword;
	}
	
	function getUsername() {
		return $this->username;
	}
	
	function getUserId() {
		return $this->userId;
	}
	
	function getHashPassword() {
		return $this->hashPassword;
	}
}


?>