<?php
require_once './place.php';

const GET_ALL_PLACES = 0;

if ($_GET["method"] == GET_ALL_PLACES){
  $places = Place::getAllPlaces();
  $json_str = json_encode($places,JSON_UNESCAPED_UNICODE);
  return $json_str;
}

?>
