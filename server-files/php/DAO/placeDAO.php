<?php
/**
 * Classe responsável por operaões CRUD no Banco de Dados da tabela 'places'.
 * É uma classe do tipo singleton.
 */
include_once '../db.php';
include_once '../models/place.php';
class PlaceDAO {
	private static $instance = NULL;
	const DB_TABLE = "places";
	const COL_AMENITY = "amenity";
	const COL_DESCRIPTION = "description";
	const COL_ID = "place_id";
	const COL_LATITUDE = "latitude";
	const COL_LOCNAME = "loc_name";
	const COL_LONGITUDE = "longitude";
	const COL_NAME = "name";
	const COL_SHORTNAME = "short_name";
	private function __construct() {
	}
	public static function getInstance() {
		if (! isset ( self::$instance )) {
			return new PlaceDAO ();
		} else {
			return self::$instance;
		}
	}
	public function insertPlace(Place $place) {
		$columns = '(' . self::COL_ID . ',' . self::COL_AMENITY . ',' . self::COL_DESCRIPTION . ',' . self::COL_LATITUDE . ',' 
				. self::COL_LOCNAME . ',' . self::COL_LONGITUDE . ',' . self::COL_NAME . ',' . self::COL_SHORTNAME . ')';
		
		$connection = DBHelper::connection ();
		
		$SQL = $connection->prepare ( 'INSERT INTO ' . self::DB_TABLE . $$columns . ' VALUES (?,?,?,?,?,?,?,?)' );
		if ($connection->error) {
			echo $connection->error;
			die ();
		}
		
		// Variaveis para guardar dados do obj Place
		$id = $place->getId ();
		$amenity = $place->getAmenity ();
		$description = Utf8Encoder::encode ( $place->getDescription () );
		$latitude = $place->getLatitude ();
		$locName = Utf8Encoder::encode ( $place->getLocalName () );
		$longitude = $place->getLongitude ();
		$name = Utf8Encoder::encode ( $place->getName () );
		$shortName = Utf8Encoder::encode ( $place->getShortName () );
		
		$SQL->bind_param ( 'issdsdss', $id, $amenity, $description, $latitude, $locName, $longitude, $name, $shortName );
		$SQL->execute ();
		
		if ($SQL->affected_rows > 0) {
			echo "Inserção bem sucedida.";
			return true;
		} else {
			echo "Nenhuma linha afetada.";
			return false;
		}
		// Logs
	}
	
	
	public function insertPlaceToModeration(Place $place) {
		$columns = '(' . self::COL_ID . ',' . self::COL_AMENITY . ',' . self::COL_DESCRIPTION . ',' . self::COL_LATITUDE . ',' 
				. self::COL_LOCNAME . ',' . self::COL_LONGITUDE . ',' . self::COL_NAME . ',' . self::COL_SHORTNAME . ')';
		
		$db_table = "places_mod";
		$connection = DBHelper::connection ();
		
		$SQL = $connection->prepare ( 'INSERT INTO ' . $db_table . $$columns . ' VALUES (?,?,?,?,?,?,?,?)' );
		if ($connection->error) {
			echo $connection->error;
			die ();
		}
		
		// Variaveis para guardar dados do obj Place
		$id = $place->getId ();
		$amenity = $place->getAmenity ();
		$description = Utf8Encoder::encode ( $place->getDescription () );
		$latitude = $place->getLatitude ();
		$locName = Utf8Encoder::encode ( $place->getLocalName () );
		$longitude = $place->getLongitude ();
		$name = Utf8Encoder::encode ( $place->getName () );
		$shortName = Utf8Encoder::encode ( $place->getShortName () );
		
		$SQL->bind_param ( 'issdsdss', $id, $amenity, $description, $latitude, $locName, $longitude, $name, $shortName );
		$SQL->execute ();
		
		if ($SQL->affected_rows > 0) {
			echo "Inserção bem sucedida.";
			return true;
		} else {
			echo "Nenhuma linha afetada.";
			return false;
		}
		// Logs
	}
}

?>
