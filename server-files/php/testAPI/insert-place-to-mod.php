

<?php
/*
 * TODO insert-place-to-mod | enviar um json contendo o erro lançado pela inserção
 */

/**
 * @author kaeuchoa
 * @desc Script para inserção de conteúdo para moderação. 
 * 
 */

include_once dirname ( __DIR__ ) . '/DAO/placeDAO.php';
include_once dirname ( __DIR__ ) . '/models/place.php';

$json = file_get_contents ( 'php://input' );
if ($json) {
	$jsonObj = json_decode ( $json );
	$place = new Place ( $jsonObj->amenity, 
			$jsonObj->description, 
			$jsonObj->id, 
			$jsonObj->latitude, 
			$jsonObj->longitude, 
			$jsonObj->locName, 
			$jsonObj->name, 
			$jsonObj->shortName );
	// insere local para moderação
	$placeDao = PlaceDAO::getInstance ();
	if ($placeDao->insertPlaceToModeration ( $place )) { // inserção bem sucedida retorna true
		
	} else { // inserção mal sucedida retorna false
 
	}
}

?>

