<?php
include_once '../utils/Bcrypt.php';
require_once '../DAO/userDAO.php';

$contents = utf8_encode(file_get_contents('php://input'));
$input = json_decode($contents,true);

if (json_last_error() === JSON_ERROR_NONE) {
	$hashPassword = Bcrypt::hash($input["password"], 11);
	$username = $input["username"];
	
	$dao = UserDAO::getInstance();
	$result = $dao->insertUser(new User($username, $hashPassword));
	var_dump($result);
	switch ($result) {
		case 1062:
		 http_response_code(304);
		break;		
		case 0:
			http_response_code(500);
		break;
		default:
			http_response_code(200);
		break;
	}
} else {
	print("Erro ao processar JSON");
} 


?>