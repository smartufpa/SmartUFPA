<?php

class User {
	
	private $userId;
	private $username;
	private $password;
	private $permission;

	
	function __construct($userId,$username,$password,$permission) {
		$this->userId = $userId;
		$this->username = $username;
		$this->password = $password;
		$this->permission = $permission;
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
	function getPermission(){
		return $this->permission;
	}
}


?>