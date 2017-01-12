<?php

/**
 * Super classe que servirá de base para as locações dentro da universidade
 */

 require_once("./db.php");
 require_once("./utf8_encode.php");
class Place
{

   var $latitude;
   var $longitude;
   var $name;
   var $id;
   var $description;

  function __construct($id,$name,$description,$latitude,$longitude)
  {
    $this->id= $id;
    $this->name = $name;
    $this->description = $description;
    $this->latitude = $latitude;
    $this->longitude = $longitude;
  }

  public static function getAllPlaces(){
    $connection = DB::connection();

    $statement = $connection->prepare(
            'SELECT * FROM place'
    );

    $statement->execute();
    $statement->bind_result($id, $name, $description, $lat, $lon);
    $places = [];
    while ($statement->fetch()) {
        $place = [];
        $place["id"] = $id;
        $place['name'] = Utf8Encoder::encode($name);
        $place['description'] = Utf8Encoder::encode($description);
        $place['latitude'] = $lat;
        $place['longitude'] = $lon;
        $places[] = $place;
    }


    return $places;

  }




}



 ?>
