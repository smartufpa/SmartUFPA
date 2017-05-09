<?php

/**
 * @author kaeuchoa
 * 
 * 
 * @desc Super classe que serve de base para os lugares dentro da universidade.
 * O modelo é construído a partir dos atributos selecionados para o projeto,
 * baseado naquele usado no OSM.
 */

class Place {

  private $amenity;
  private $description;
  private $id;
  private $latitude;
  private $localName;
  private $longitude;
  private $name;
  private $shop;
  private $shortName;

  /**
   * Construtor base.
   * @param string $amenity - Tag utilizada no OSM para identificar o tipo do lugar.
   * @param string $description - Descrição sobre o lugar.
   * @param integer $id - ID registrada no DB.
   * @param double $latitude - Coordenada de latitude do lugar.
   * @param double $longitude - Coordenada de longitude do lugar.
   * @param string $localName - Nome local/Apelido do lugar.
   * @param string $name - Nome oficial do lugar.
   * @param string $shortName - Sigla baseada no nome do lugar.
   */
    function __construct(string $amenity,string $description, integer $id, double $latitude,
    		double $longitude,string $localName,string $name,string $shortName) {
        $this->amenity = $amenity;
        $this->description = $description;
        $this->id = $id;
        $this->latitude = $latitude;
        $this->longitude = $longitude;
        $this->localName = $localName;
        $this->name = $name;
        // $this->shop = $shop;
        $this->shortName = $shortName;
    }
    
	/**
	 * 
	 * @return string $amenity - Tag utilizada no OSM para identificar o tipo do lugar.
	 */
    public function getAmenity() { return $this->amenity; }

    /**
     * 
     * @return string $description - Descrição sobre o lugar.
     */
    public function getDescription() { return $this->description; }
	
    
    /**
     * 
     * @return integer $id - ID registrada no DB.
     */
    public function getId() { return $this->id; }

    /**
     * 
     * @return double $latitude - Coordenada de latitude do lugar.
     */
    public function getLatitude() { return $this->latitude; }
	
    /**
     * 
     * @return string $localName - Nome local/Apelido do lugar.
     */
    public function getLocalName() { return $this->localName; }

    /**
     * 
     * @return double $longitude - Coordenada de longitude do lugar.
     */
    public function getLongitude() { return $this->longitude; }

    /**
     * 
     * @return string $name - Nome oficial do lugar.
     */
    public function getName() { return $this->name; }
	
    /**
     * 
     * @return string $shortName - Sigla baseada no nome do lugar.
     */
    public function getShortName() { return $this->shortName; }
    
    /**
     * 
     * @param string $amenity - Novo valor para o atributo $amenity.
     */
    public function setAmenity(string $amenity) { $this->amenity = $amenity; }
    
    /**
     * 
     * @param string $description - Novo valor para o atributo $description.
     */
    public function setDescription(string $description) { $this->description = $description; }
    
    /**
     * 
     * @param integer $id - Novo valor para o atributo $id.
     */
    public function setId(integer $id) { $this->id = $id; }
    
    /**
     * 
     * @param double $latitude - Novo valor para o atributo $latitude.
     */
    public function setLatitude(double $latitude) { $this->latitude = $latitude; }
    
    /**
     * 
     * @param string $localName - Novo valor para o atributo $localName.
     */
    public function setLocalName(string $localName) { $this->localName = $localName; }
    
    /**
     * 
     * @param double $longitude - Novo valor para o atributo $longitude.
     */
    public function setLongitude(double $longitude) { $this->longitude = $longitude; }
    
    /**
     * 
     * @param string $name - Novo valor para o atributo $name.
     */
    public function setName(string $name) { $this->name = $name; }
    
    /**
     * 
     * @param string $shortName - Novo valor para o atributo $shortName.
     */
    public function setShortName(string $shortName) { $this->shortName = $shortName; }



}

?>
