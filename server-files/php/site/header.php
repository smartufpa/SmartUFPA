<?php
define("ROOT", dirname(__DIR__) . '/');
include dirname(__DIR__) . '/session.php';
$loggedIn = SessionClient::checkIfLoggedIn();
?>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>
    	<?php 
    	if(isset($pageTitle)){  echo $pageTitle; }
    	else{echo "Smart UFPA";}
    	
    	?>
    </title>
    <link rel="icon" 
      type="image/png" 
      href="/imgs/favicon.png" />
      <!-- Latest compiled and minified CSS bootstrap -->
      <link rel="stylesheet" href="css/bootstrap-3.3.7-dist/css/bootstrap.min.css"
      integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
      crossorigin="anonymous">
      
        <!-- Angular js e código js -->
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
      
      <script src="js/angular-password/angular-password.min.js"></script>
	  <script
	  src="https://code.jquery.com/jquery-3.2.1.min.js"
	  integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
	  crossorigin="anonymous"></script>
	  
	  <link rel="stylesheet" href="css/site.css">
	  
  </head>
  <body>
    <div class="container"><!-- START CONTAINER  -->