<?php
require './db.php';
require_once './place.php';

//GET TEST
//$places = Place::getAllPlaces();
//$json_str = json_encode($places,JSON_UNESCAPED_UNICODE);

//POST TEST
$array = file_get_contents('php://input');
$data = json_decode($array,true,JSON_UNESCAPED_UNICODE);
$place = new Place($data['id'],$data["name"],$data["short_name"],$data["description"],
        $data["latitude"],$data["longitude"]);
Place::insertPlace($place);
if (json_last_error() != 0) {
    switch (json_last_error()) {
        case JSON_ERROR_DEPTH:
            echo ' - profundidade maxima excedida';
            die();
        break;
        case JSON_ERROR_STATE_MISMATCH:
            echo ' - state mismatch';
            die();
        break;
        case JSON_ERROR_CTRL_CHAR:
            echo ' - Caracter de controle encontrado';
            die();
        break;
        case JSON_ERROR_SYNTAX:
            echo ' - Erro de sintaxe! String JSON mal-formada!';
            die();
        break;
        case JSON_ERROR_UTF8:
            echo ' - Erro na codificação UTF-8';
            die();
        break;
        default:
            echo ' – Erro desconhecido';
            die();
        break;
}

}
//echo $json_str;
?>
