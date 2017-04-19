<?php

/**
 * Super classe que servirá de base para as locações dentro da universidade
 */
include_once("../utils/utf8_encode.php");

class Place {

  private $amenity;
  private $description;
  private $id;
  private $latitude;
  private $locName;
  private $longitude;
  private $name;
  private $shop;
  private $shortName;

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

    public function getAmenity() { return $this->amenity; }

    public function getDescription() { return $this->description; }

    public function getId() { return $this->id; }

    public function getLatitude() { return $this->latitude; }

    public function getLocalName() { return $this->locName; }

    public function getLongitude() { return $this->longitude; }

    public function getName() { return $this->name; }

    public function getShortName() { return $this->shortName; }



}

?>
