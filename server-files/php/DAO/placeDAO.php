<?php
/*
 * TODO placeDAO | Padronizar retorno de erros na inserção (buscar melhor prática)
 * TODO placeDAO | Tratar erros nas funções
 * TODO placeDAO | Gerar Logs
 */

include_once dirname(__DIR__) . '/db.php';
include_once dirname(__DIR__) .'/models/place.php';

/**
 * @author kaeuchoa
 *
 * @desc Classe responsável por operações CRUD no Banco de Dados da tabela 'places'.
 * <br>É uma classe do tipo <i>Singleton</i>.
 *
 */
class PlaceDAO {
	
	private static $instance = NULL;
	const DB_TABLE = "places";
	const DB_MOD_TABLE = "places_mod";
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
	
	/**
	 * @return PlaceDAO  - Nova instância da classe ou a instância já existente.
	 */
	public static function getInstance() {
		if (! isset ( self::$instance )) {
			return new PlaceDAO ();
		} else {
			return self::$instance;
		}
	}
	
	
	// CREATE 
	
	
	/**
	 * Insere um objeto do tipo Place no banco de dados(SQL) direto na tabela principal.
	 * @param Place $place
	 * @return boolean - <b>true</b> para inserção bem sucedida ou <b>false</b> para falha na inserção.
	 */
	
	
	public function insertPlace(Place $place) {
		if (self::isPlaceSet ( $place->getName () )) {
			return false;
		} else {
			$columns = '(' . self::COL_ID . ',' . self::COL_AMENITY . ',' . self::COL_DESCRIPTION . ',' . self::COL_LATITUDE . ',' . self::COL_LOCNAME . ',' . self::COL_LONGITUDE . ',' . self::COL_NAME . ',' . self::COL_SHORTNAME . ')';
			
			$connection = DBHelper::connection ();
			
			$SQL = $connection->prepare ( 'INSERT INTO ' . self::DB_TABLE . $columns . ' VALUES (?,?,?,?,?,?,?,?)' );
			if ($connection->error) {
				die ($connection->error);
			}
			
			if(!$SQL){
				die($connection->error);
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
			
			if(!$SQL->bind_param ( 'issdsdss', $id, $amenity, $description, $latitude, $locName, $longitude, $name, $shortName )){
				die($SQL->error);
			}
			if(!$SQL->execute ()){
				die($SQL->error);
			}
			
			
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
	 * Insere um objeto do tipo Place no banco de dados(SQL) na tabela de moderação.
	 * @param Place $place
	 * @return boolean - <b>true</b> para inserção bem sucedida ou <b>false</b> para falha na inserção.
	 */
	
	
	public function insertPlaceToModeration(Place $place) {
		if (self::isPlaceSet ( $place->getName () )) {
			return false;
		} else {
			$columns = '(' . self::COL_ID . ',' . self::COL_AMENITY . ',' . self::COL_DESCRIPTION . ',' . self::COL_LATITUDE . ',' . self::COL_LOCNAME . ',' . self::COL_LONGITUDE . ',' . self::COL_NAME . ',' . self::COL_SHORTNAME . ')';
			
			$connection = DBHelper::connection ();
			
			$SQL = $connection->prepare ( 'INSERT INTO ' . self::DB_MOD_TABLE . $columns . ' VALUES (?,?,?,?,?,?,?,?)' );
			if (!$SQL) {
				die ($connection->error);
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
			
			if(!$SQL->bind_param ( 'issdsdss', $id, $amenity, $description, $latitude, $locName, $longitude, $name, $shortName )){
				die($SQL->error);
			}
			if(!$SQL->execute ()){
				die($SQL->error);
			}
			
			
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
	
	/**
	 * Retorna todos os lugares que estão na tabela de moderação atualmente.
	 * @return Place[] - Array de Place
	 */
	
	public function getAllPlacesToModeration(){
		$connection = DBHelper::connection();
		$SQL = $connection->prepare("SELECT * FROM " . self::DB_MOD_TABLE . " ORDER BY " . self::COL_NAME );
		if(!$SQL){
			die($connection->error);
		}
		
		if(!$SQL->execute()){
			die($SQL->error);
		}
		
		if(!$SQL->bind_result($id,$amenity,$description,$latitude,$locName,$longitude,$name,$shortName)){
			die($SQL->error);
		}
		
		$placesToModeration = [];
		while($SQL->fetch()){
			$placesToModeration[$id] = new Place($amenity, $description, $id, $latitude, $longitude, $locName, $name, $shortName);
		}
		
		return $placesToModeration;
		
	}
	
	
	
	/**
	 * Retorna um lugar que está na tabela de moderação pela sua ID.
	 * @param integer $id - ID única do lugar para a busca.
	 * @return Place|NULL - Um objeto <b>Place</b> se encontrou ou <b>NULL</b> se nenhum lugar foi encontrado.
	 */
	
	public function getPlaceFromModerationById(integer $id){
		$connection = DBHelper::connection();
		$SQL = $connection->prepare("SELECT * FROM " . self::DB_MOD_TABLE . " WHERE " .self::COL_ID . " = ? " );
		if(!$SQL){
			die($connection->error);
		}
		
		if(!$SQL->bind_param("i", $id)){
			die($SQL->error);
		}
		
		if(!$SQL->execute()){
			die($SQL->error);
		}
		
		if(!$SQL->bind_result($id,$amenity,$description,$latitude,$locName,$longitude,$name,$shortName)){
			die($SQL->error);
		}
		
		if(!$SQL->fetch()){
			die($SQL->error);
		}
		$connection->close();
		
		if ($SQL->affected_rows > 0) {
			// para debug
			//echo " Local " . $id . " excluído com sucesso da tabela de moderação.<br>";
			return new Place($amenity, $description, $id, $latitude, $longitude, $locName, $name, $shortName);;
		} else {
			// para debug
			//echo "Nenhuma linha afetada.<br>";
			return NULL;
		}
		
	}
	
	/**
	 * Retorna um lugar pela sua ID.
	 * @param integer $id - ID única do lugar para a busca.
	 * @return Place|NULL - Um objeto <b>Place</b> se encontrou ou <b>NULL</b> se nenhum lugar foi encontrado.
	 */
	
	public function getPlaceById(integer $id){
		$connection = DBHelper::connection();
		$SQL = $connection->prepare("SELECT * FROM " . self::DB_TABLE . " WHERE " .self::COL_ID . " = ? " );
		if(!$SQL){
			die($connection->error);
		}
		
		if(!$SQL->bind_param("i", $id)){
			die($SQL->error);
		}
		
		if(!$SQL->execute()){
			die($SQL->error);
		}
		
		if(!$SQL->bind_result($id,$amenity,$description,$latitude,$locName,$longitude,$name,$shortName)){
			die($SQL->error);
		}
		
		if(!$SQL->fetch()){
			die($SQL->error);
		}
		
		$connection->close();
		if ($SQL->affected_rows > 0) {
			// para debug
			//echo " Local " . $id . " excluído com sucesso da tabela de moderação.<br>";
			return new Place($amenity, $description, $id, $latitude, $longitude, $locName, $name, $shortName);;
		} else {
			// para debug
			//echo "Nenhuma linha afetada.<br>";
			return NULL;
		}
		
		
		
	}
	
	/**
	 * Retorna um lugar que está na tabela de moderação pelo seu nome.
	 * @param string $placeName - Nome do lugar para a busca.
	 * @return Place - objeto da classe Place
	 */
	
	public function getPlaceByName(string $placeName){
		$connection = DBHelper::connection();
		$SQL = $connection->prepare("SELECT * FROM " . self::DB_MOD_TABLE . " WHERE " .self::COL_NAME . " = ? " );
		if(!$SQL){
			die($connection->error);
		}
		
		if(!$SQL->bind_param("s", $placeName)){
			die($SQL->error);
		}
		
		if(!$SQL->execute()){
			die($SQL->error);
		}
		
		if(!$SQL->bind_result($id,$amenity,$description,$latitude,$locName,$longitude,$name,$shortName)){
			die($SQL->error);
		}
		
		if(!$SQL->fetch()){
			die($SQL->error);
		}
		$connection->close();
		
		if($SQL->affected_rows > 0){		
			return new Place($amenity, $description, $id, $latitude, $longitude, $locName, $name, $shortName);
		}else{
			return NULL;
		}
		
	}
	
	// UPDATE
	
	
	
	
	// DELETE
	
	/**
	 * Deleta um lugar da tabela de moderação pela sua ID
	 * @param integer $id - ID única do lugar para deletar.
	 * @return boolean - <b>true</b> se o local foi deletado ou <b>false</b> se o local não foi deletado
	 */
	
	public function deletePlaceFromModeration(integer $id){
		$connection = DBHelper::connection();
		$SQL = $connection->prepare("DELETE FROM " . self::DB_MOD_TABLE . " WHERE " . self::COL_ID . "= ?");
		if(!$SQL){
			die($connection->error);
		}
		
		if(!$SQL->bind_param("i", $id)){
			die($SQL->error);
		}
		
		if(!$SQL->execute()){
			die($SQL->error);
		}
		
		$connection->close();
		if ($SQL->affected_rows > 0) {
			// para debug
			//echo " Local " . $id . " excluído com sucesso da tabela de moderação.<br>";
			return true;
		} else {
			// para debug
			//echo "Nenhuma linha afetada.<br>";
			return false;
		}
		// Logs
	}
	
	
	
	
	
	
	// AUXILIAR
	/**
	 * Checa se um local já existe no banco de dados pelo nome.
	 * @param string $placeName
	 * @return boolean - true se existe ou false se não existe.
	 */
	private function isPlaceSet(string $placeName) {
		$connection = DBHelper::connection ();
		$SQL = $connection->prepare ( 'SELECT ' . self::COL_NAME . ' FROM ' . self::DB_TABLE . ' WHERE '
										. self::COL_NAME . ' = ?' );
		if ($connection->error) {
			die ($connection->error);
		}
		
		if(!$SQL->bind_param ( 's', $placeName )){
			die($SQL->error);
		}
		if(!$SQL->execute ()){
			die($SQL->error);;
		}
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
