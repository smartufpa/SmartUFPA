

<?php
/**
 * Script para inserção de conteúdo para moderação
 * Recebe via método POST um arquivo json contendo as 
 * informações do modelo estipulado para "Locais".
 * 
 */

include_once dirname(__DIR__) . '/DAO/placeDAO.php';
include_once dirname(__DIR__) . '/models/place.php';


if ($_SERVER['REQUEST_METHOD'] === 'POST') {
  $json = file_get_contents('php://input');
  if($json){
  	$jsonObj = json_decode($json);
    $place = new Place(
    		$jsonObj->amenity,
    		$jsonObj->description, 
    		$jsonObj->id,
    		$jsonObj->latitude,
    		$jsonObj->longitude,
    		$jsonObj->locName,
	        $jsonObj->name,
    		$jsonObj->shortName
    		);
    //insere local para moderação
	$placeDao = PlaceDAO::getInstance();
	$placeDao->insertPlaceToModeration($place);
  }else if(count($_POST) > 0){ // TODO fazer a chamada a API também pela URL
  	
  }
}

?>




<?php
// Tratamento do JSON
// header("Content-Type: application/json;charset=utf-8");
//
// // Collect what you need in the $data variable.
//
// $json = json_encode($data);
// if ($json === false) {
//     // Avoid echo of empty string (which is invalid JSON), and
//     // JSONify the error message instead:
//     $json = json_encode(array("jsonError", json_last_error_msg()));
//     if ($json === false) {
//         // This should not happen, but we go all the way now:
//         $json = '{"jsonError": "unknown"}';
//     }
//     // Set HTTP response status code to: 500 - Internal Server Error
//     http_response_code(500);
// }
// echo $json;
?>
