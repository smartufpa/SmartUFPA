<?php

/**
 *   @author kaeuchoa
 *   Script contendo o código para inserção de usuários no DB;
 *   Recebe via método POST um arquivo json contendo as 
 *   informações do modelo estipulado para "Locais".
 *   
 *   TODO insert-user | Enviar um json contendo o erro lançado pela inserção
 */


include_once dirname ( __DIR__ ) . '/utils/Bcrypt.php';
include_once dirname ( __DIR__ ) . '/DAO/userDAO.php';

$jsonObj = utf8_encode ( file_get_contents ( 'php://input' ) );
$input = json_decode ( $jsonObj );

if (json_last_error () === JSON_ERROR_NONE) {
	$hashPassword = Bcrypt::hash ( $input->password, 11 );
	$username = $input->username;
	
	$userDao = UserDAO::getInstance ();
	if($userDao->insertUser ( new User ( "", $username, $hashPassword, false ) )){
		
	}

} else {
	print ("Erro ao processar JSON") ;
}

?>