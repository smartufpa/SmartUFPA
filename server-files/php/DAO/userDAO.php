<?php
include_once  dirname(__DIR__) . '/db.php';
include_once dirname(__DIR__) . '/models/user.php';

/**
 *
 * @author kaeuc
 * Classe responsável por operações CRUD no Banco de Dados da tabela 'users'.
 * É uma classe do tipo singleton.
 * TODO: Padronizar retorno de erros na inserção (buscar melhor prática)
 */
class UserDAO {
	private static $instance = NULL;
	const DB_TABLE = "users";
	const COL_USERID = "user_id";
	const COL_USERNAME = "username";
	const COL_PASSWORD = "hash_password";
	const DB_ERROR_DUPLICATE = 1062;
	private function __construct() {
	}
	public static function getInstance() {
		if (! isset ( self::$instance )) {
			return new UserDAO ();
		} else {
			return self::$instance;
		}
	}
	
	
	
	/**
	 * Função para inserir um objeto do tipo User no banco de dados(SQL) na tabela principal.
	 * @param User $user
	 * @return boolean
	 * true para inserção bem sucedida;
	 * false para falha na inserção.
	 */
	
	function insertUser(User $user) {
		if (self::isUserSet ( $user->getUsername () )) {
			return false;
		} else {
			$columns = '(' . self::COL_USERNAME . ',' . self::COL_PASSWORD . ')';
			$connection = DBHelper::connection ();
			
			$SQL = $connection->prepare ( 'INSERT INTO ' . self::DB_TABLE . $columns . ' VALUES (?,?) ' );
			
			if ($connection->error) {
				die ( $connection->error);
			}
			$username = $user->getUsername ();
			$password = $user->getHashPassword ();
			$SQL->bind_param ( 'ss', $username, $password );
			$SQL->execute ();
			$connection->close();
			
			if ($SQL->error && ($SQL->errno == 1062)) {
				return self::DB_ERROR_DUPLICATE;
			}
			if ($SQL->affected_rows > 0) {
				echo "Inserção bem sucedida.";
				return 1;
			} else {
				echo "Nenhuma linha afetada.";
				return 0;
			}
			// Logs
		}
	}
	
	
	
	// READ
	
	
	
	// UPDATE
	
	
	
	
	// DELETE
	
	
	
	
	
	
	
	/**
	 * Função interna para checar se um usuário já existe no banco de dados pelo username.
	 * 
	 * @param string $username        	
	 * @return boolean true se já existir;
	 *         false se não.
	 */
	private function isUserSet($username) {
		$connection = DBHelper::connection ();
		$SQL = $connection->prepare ( 'SELECT ' . self::COL_USERNAME . ' FROM ' . self::DB_TABLE . ' WHERE ' . self::COL_USERNAME . ' = ?' );
		if ($connection->error) {
			die ( $connection->error );
		}
		
		$SQL->bind_param ( 's', $username );
		$SQL->execute ();
		$result = $SQL->fetch ();
		$connection->close ();
		if (isset ( $result )) {
			// para debug
			// echo "Usuário já existe no DB.<br>";
			return true;
		} else {
			// para debug
			// echo "Nenhum usuário existente foi encontrado.<br>";
			return false;
		}
	}
}

?>