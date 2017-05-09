<?php

/*
 * TODO db | logs: registrar uma conexão ao banco!
 */
	/**
	 * 
	 * @author kaeuchoa
	 *
	 */
    class DBHelper {
    /**
     * Retorna uma conexão válida ao banco de dados selecionado usando paramêtros default.
     * @return mysqli - Um objeto <b>mysqli</b>.
     * 
     */	
    	
    public static function connection(){
        $dbServer = '127.0.0.1';
        $dbUsuario = 'kae';
        $dbSenha = 'Kae171094';
        $dbNome = 'smart_ufpa';

        $connection = mysqli_connect($dbServer, $dbUsuario, $dbSenha,$dbNome);
        $connection->set_charset('utf8');
        
        if ($connection->connect_errno) {
        	echo "Falha ao conectar ao MySQL: (" . $connection->connect_errno . ") " . $connection->connect_error;
        }

        // DEBUG
        //echo "Success: A proper connection to MySQL was made!" . PHP_EOL;
        //echo "Host information: " . mysqli_get_host_info($connection) . PHP_EOL;
       

        return $connection;

    }
    


}

?>
