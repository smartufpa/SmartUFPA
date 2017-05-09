
<?php
/*
 * TODO insert-place| enviar um json contendo o erro lançado pela inserção
 */


/**
 * @author kaeuchoa
 * @desc Script com a operação para inserção de conteúdo para a tabela definitiva 'places'.
 */

include_once dirname ( __DIR__ ) . '/DAO/placeDAO.php';
include_once dirname ( __DIR__ ) . '/models/place.php';

$json = file_get_contents ( 'php://input' );
if ($json) {
	$jsonObj = json_decode ( $json );
	$placeDao = PlaceDAO::getInstance ();
	foreach ($jsonObj as $place) {
		if($place->checked){
			$newPlace = new Place(
					$place->amenity, 
					$place->description, 
					$place->id, 
					$place->latitude, 
					$place->longitude, 
					$place->localName, 
					$place->name, 
					$place->shortName
					);
			
			if($placeDao->insertPlace($newPlace)){
				// inserção bem sucedida
				$placeDao->deletePlaceFromModeration($newPlace->getId());
			}else{
				// inserção mal sucedida
			}
		}else{
			$placeDao->deletePlaceFromModeration($place->id);
		}
	}
}

?>

