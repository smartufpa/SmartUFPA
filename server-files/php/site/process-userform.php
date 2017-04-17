<?php
// TODO Criar modelo do usuário e seu DAO 
require_once './utils/Bcrypt.php';
$input = json_decode(file_get_contents('php://input'),true);

$hash_pass = Bcrypt::hash($input["password"], 11);
$username = $input["username"];

// TODO Enviar resposta para a página do app.js
header("HTTP/1.0 404 Not Found");

?>