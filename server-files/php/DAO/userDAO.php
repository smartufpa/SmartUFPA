<?php

include_once '../db.php';
include_once '../models/user.php';

class UserDAO {
	
	private static $instance = NULL;
	
	const DB_TABLE = "users";
	const COL_USERID = "user_id";
	const COL_USERNAME = "username";
	const COL_PASSWORD = "hash_password";
	const DB_ERROR_DUPLICATE = 1062;
	
	private function __construct(){}
	
	
	public static function getInstance(){
		if(!isset(self::$instance)){
			return new UserDAO();
		}else {
			return self::$instance;
		}
		
	}
	
	
	function insertUser(User $user){
		$columns = '(' . self::COL_USERNAME . ',' . self::COL_PASSWORD . ')';
		$connection = DBHelper::connection();
		
		$SQL = $connection->prepare(
				'INSERT INTO ' . self::DB_TABLE . $columns
				. ' VALUES (?,?) ');
		
		if($connection->error){
			echo $connection->error;
			die();
		}
		$username = $user->getUsername();
		$password = $user->getHashPassword();
		$SQL->bind_param('ss',$username,$password);
		$SQL->execute();
		
		if($SQL->error && ($SQL->errno == 1062)){
			return self::DB_ERROR_DUPLICATE;
		}
		if($SQL->affected_rows > 0){
			echo "Inserção bem sucedida.";
			return 1;
		}else{
			echo "Nenhuma linha afetada.";
			return 0;
		}
		//Logs
		
		
	}
}

?>