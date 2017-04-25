<?php
/**
 * @author kaeuc
 * Classe responsável por operações CRUD no Banco de Dados da tabela 'places'.
 * É uma classe do tipo singleton.
 * TODO: Padronizar retorno de erros na inserção (buscar melhor prática)
 */

include_once dirname(__DIR__). '/db.php';
include_once dirname(__DIR__) . '/models/place.php';


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
	
	
	// CREATE 
	
	
	/**
	 * Função para inserir um objeto do tipo Place no banco de dados(SQL) direto na tabela principal.
	 * @param Place $place
	 * @return boolean
	 * true para inserção bem sucedida;
	 * false para falha na inserção.
	 */
	
	
	public function insertPlace(Place $place) {
		if (self::isPlaceSet ( $place->getName () )) {
			return false;
		} else {
			$columns = '(' . self::COL_ID . ',' . self::COL_AMENITY . ',' . self::COL_DESCRIPTION . ',' . self::COL_LATITUDE . ',' . self::COL_LOCNAME . ',' . self::COL_LONGITUDE . ',' . self::COL_NAME . ',' . self::COL_SHORTNAME . ')';
			
			$connection = DBHelper::connection ();
			
			$SQL = $connection->prepare ( 'INSERT INTO ' . $dbTable . $columns . ' VALUES (?,?,?,?,?,?,?,?)' );
			if ($connection->error) {
				echo $connection->error;
				die ();
			}
			
			// Variaveis para guardar dados do obj Place
			$id = $place->getId ();
			$amenity = $place->getAmenity ();
			$description = $place->getDescription();
			$latitude = $place->getLatitude ();
			$locName = $place->getLocalName ();
			$longitude = $place->getLongitude ();
			$name = $place->getName ();
			$shortName = $place->getShortName ();
			
			$SQL->bind_param ( 'issdsdss', $id, $amenity, $description, $latitude, $locName, $longitude, $name, $shortName );
			$SQL->execute ();
			
			
			$connection->close();
			if ($SQL->affected_rows > 0) {
				// para debug
				//echo "Inserção bem sucedida.<br>";
				return true;
			} else {
				// para debug
				//echo "Nenhuma linha afetada.<br>";
				return false;
			}
			// Logs
		}
	}
	
	/**
	 * Função para inserir um objeto do tipo Place no banco de dados(SQL) na tabela de moderação.
	 * @param Place $place
	 * @return boolean
	 * true para inserção bem sucedida;
	 * false para falha na inserção.
	 */
	
	
	public function insertPlaceToModeration(Place $place) {
		if (self::isPlaceSet ( $place->getName () )) {
			return false;
		} else {
			$columns = '(' . self::COL_ID . ',' . self::COL_AMENITY . ',' . self::COL_DESCRIPTION . ',' . self::COL_LATITUDE . ',' . self::COL_LOCNAME . ',' . self::COL_LONGITUDE . ',' . self::COL_NAME . ',' . self::COL_SHORTNAME . ')';
			
			$dbTable = "places_mod";
			$connection = DBHelper::connection ();
			
			$SQL = $connection->prepare ( 'INSERT INTO ' . $dbTable . $columns . ' VALUES (?,?,?,?,?,?,?,?)' );
			if ($connection->error) {
				echo $connection->error;
				die ();
			}
			
			// Variaveis para guardar dados do obj Place
			$id = $place->getId ();
			$amenity = $place->getAmenity ();
			$description = $place->getDescription();
			$latitude = $place->getLatitude ();
			$locName = $place->getLocalName ();
			$longitude = $place->getLongitude ();
			$name = $place->getName ();
			$shortName = $place->getShortName ();
			
			$SQL->bind_param ( 'issdsdss', $id, $amenity, $description, $latitude, $locName, $longitude, $name, $shortName );
			$SQL->execute ();
			
			
			$connection->close();
			if ($SQL->affected_rows > 0) {
				// para debug
				//echo "Inserção bem sucedida.<br>";
				return true;
			} else {
				// para debug
				//echo "Nenhuma linha afetada.<br>";
				return false;
			}
			// Logs
		}
	}
	
	// READ 
	
	
	
	// UPDATE
	
	
	
	
	// DELETE
	
	
	
	
	
	
	
	
	
	/**
	 * Função interna para checar se um local já existe no banco de dados pelo nome.
	 * @param string $placeName
	 * @return boolean
	 * true se já existir;
	 * false se não.
	 */
	private function isPlaceSet($placeName) {
		$connection = DBHelper::connection ();
		$SQL = $connection->prepare ( 'SELECT ' . self::COL_NAME . ' FROM ' . self::DB_TABLE . ' WHERE '
										. self::COL_NAME . ' = ?' );
		if ($connection->error) {
			die ($connection->error);
		}
		
		$SQL->bind_param ( 's', $placeName );
		$SQL->execute ();
		$result = $SQL->fetch();
		$connection->close();
		if (isset($result)) {
			// para debug
			//echo "Local já existe no DB.<br>";
			return true;
		} else {
			// para debug
			//echo "Nenhum local existente foi encontrado.<br>";
			return false;
		}
	}
}

?>
