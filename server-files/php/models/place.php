<?php

/**
 * @author kaeuc
 * Super classe que servirá de base para as locações dentro da universidade.
 * Apresenta o modelo com os atributos selecionados para o projeto,
 * baseado naquele usado a partir do OSM.
 */

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
    
    public function setAmenity($amenity) { $this->amenity = $amenity; }
    
    public function setDescription($description) { $this->description = $description; }
    
    public function setId($id) { $this->id = $id; }
    
    public function setLatitude($latitude) { $this->latitude = $latitude; }
    
    public function setLocalName($localName) { $this->locName = $localName; }
    
    public function setLongitude($longitude) { $this->longitude = $longitude; }
    
    public function setName($name) { $this->name = $name; }
    
    public function setShortName($shortName) { $this->shortName = $shortName; }



}

?>
