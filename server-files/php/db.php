<?php

    class DB {

    public static function connection(){
        $dbServer = '127.0.0.1';
        $dbUsuario = 'kae';
        $dbSenha = 'Kae171094';
        $dbBanco = 'smart_ufpa';

        $connection = mysqli_connect($dbServer, $dbUsuario, $dbSenha,$dbBanco);

        if(mysqli_connect_errno($connection)){
            echo "Error to connect.";
            die();
        }
        // todo
       // logs logs logs!

        return $connection;

    }


}

?>
