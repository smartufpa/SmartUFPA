<?php

/**
 * Super classe que servirá de base para as locações dentro da universidade
 */
require_once("./db.php");
require_once("./utf8_encode.php");

class Place {

    var $latitude;
    var $longitude;
    var $name;
    var $shortName;
    var $id;
    var $description;

    const dbTable = "places";

    function __construct($id, $name, $shortName, $description, $latitude, $longitude) {
        $this->id = $id;
        $this->name = $name;
        $this->shortName = $shortName;
        $this->description = $description;
        $this->latitude = $latitude;
        $this->longitude = $longitude;
    }

    public static function getAllPlaces() {
        $connection = DB::connection();

        $statement = $connection->prepare(
                'SELECT * FROM ' . self::dbTable
        );

        $statement->execute();
        $statement->bind_result($id, $name, $shortName, $description, $lat, $lon);
        $places = [];
        while ($statement->fetch()) {
            $place = [];
            $place["id"] = $id;
            $place['name'] = Utf8Encoder::encode($name);
            $place['short_name'] = Utf8Encoder::encode($shortName);
            $place['description'] = Utf8Encoder::encode($description);
            $place['latitude'] = $lat;
            $place['longitude'] = $lon;
            $places[] = $place;
        }

        return $places;
    }

    public static function insertPlace($place) {
       $connection = DB::connection();
       $statement = $connection->prepare(
                'INSERT INTO ' . self::dbTable .' (id,name,short_name,description,latitude,longitude)'
                . ' VALUES (?,?,?,?,?,?)'
        );
       
       $name = Utf8Encoder::encode($place->name);
       $shortName = Utf8Encoder::encode($place->shortName);
       $description = Utf8Encoder::encode($place->description);
        $statement->bind_param('isssdd', $place->id,
                $name,
                $shortName,
                $description,$place->latitude,$place->longitude);       
        
        $statement->execute();
        //Logs 
        echo "Inserção bem sucedida.";
        
    }

}

?>
