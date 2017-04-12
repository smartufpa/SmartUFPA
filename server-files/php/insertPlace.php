<?php
include_once '.\DAO\placeDAO.php';
include_once '.\models\place.php';

header('Content-type: application/json;charset=utf-8"');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
  $json = file_get_contents('php://input');
  if($json){
    $obj = json_clean_decode($json);
    $place = new Place($obj->amenity,$obj->description, $obj->id,$obj->latitude,$obj->longitude,$obj->locName,
          $obj->name,$obj->shortName);
  }
}



?>




<?php
// Tratamento do JSON
// header("Content-Type: application/json;charset=utf-8");
//
// // Collect what you need in the $data variable.
//
// $json = json_encode($data);
// if ($json === false) {
//     // Avoid echo of empty string (which is invalid JSON), and
//     // JSONify the error message instead:
//     $json = json_encode(array("jsonError", json_last_error_msg()));
//     if ($json === false) {
//         // This should not happen, but we go all the way now:
//         $json = '{"jsonError": "unknown"}';
//     }
//     // Set HTTP response status code to: 500 - Internal Server Error
//     http_response_code(500);
// }
// echo $json;
?>
