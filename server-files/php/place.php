<?php

/**
 * Super classe que servirá de base para as locações dentro da universidade
 */
require_once("./db.php");
require_once("./utf8_encode.php");

class Place {

  var $amenity;
  var $description;
  var $id;
  var $latitude;
  var $locName;
  var $longitude;
  var $name;
  var $shop;
  var $shortName;


    const dbTable = "places";
    const amenity_row = "amenity";
    const description_row = "description";
    const id_row = "place_id";
    const latitude_row = "latitude";
    const locName_row = "loc_name";
    const longitude_row = "longitude";
    const name_row = "name";
    const shortName_row = "short_name";

    function __construct($amenity,$description, $id,$latitude,$longitude,$locName,
          $name,$shortName) {
        $this->amenity = $amenity;
        $this->description = $description;
        $this->id = $id;
        $this->latitude = $latitude;
        $this->longitude = $longitude;
        $this->locName = $locName;
        $this->name = $name;
        // $this->shop = $shop;
        $this->shortName = $shortName;
    }

    public static function getAllPlaces() {
        $connection = DB::connection();

        $statement = $connection->prepare(
                'SELECT * FROM ' . self::dbTable
        );

        $statement->execute();
        $statement->bind_result($id,$amenity,$description,$latitude,$locName,
              $longitude,$name,$shortName);
        $places = [];
        while ($statement->fetch()) {
            $place['amenity'] = Utf8Encoder::encode($amenity);
            $place['description'] = Utf8Encoder::encode($description);
            $place['id'] = $id;
            $place['latitude'] = $latitude;
            $place['longitude'] = $longitude;
            $place['locName'] =  Utf8Encoder::encode($locName);
            $place['name'] = Utf8Encoder::encode($name);
            $place['short_name'] = Utf8Encoder::encode($shortName);
            $places[] = $place;
        }


        return $places;
    }


    public static function getPlaceByName($name){
      // TODO
    }

    public static function insertPlace($place) {
       $columns =  '(place_id,amenity,description,latitude,loc_name,longitude,
                          name,short_name)';

       $connection = DB::connection();

       $statement = $connection->prepare(
                'INSERT INTO ' . self::dbTable . $columns
                . ' VALUES (?,?,?,?,?,?,?,?)'
        );

       if (isset($connection->error)) {
         echo $connection->error;
       }


       $locName = Utf8Encoder::encode($place->locName);
       $name = Utf8Encoder::encode($place->name);
       $shortName = Utf8Encoder::encode($place->shortName);
       $description = Utf8Encoder::encode($place->description);

       $statement->bind_param('issdsdss', $place->id,$place->amenity,$description,
            $place->latitude, $locName,$place->longitude,$name,$shortName);
       $statement->execute();

      //  mysql_close($connection);
        //Logs
        echo "Inserção bem sucedida.";

    }

}

?>
