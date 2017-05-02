<?php
$pageTitle = "Smart UFPA  - Moderação";
include 'header.php';


if (! $loggedIn) {
	header ( "Location: login.php" );
} else {
	include_once ROOT . 'DAO/placeDAO.php';
	$placeDAO = PlaceDAO::getInstance ();
	$placesToModerate = $placeDAO->getAllPlacesToModeration ();
}
?>

<div class="row">
	<a id="logout" class="btn btn-danger"  style="float: right;"href="../logout.php"> 
		Sair
	</a>
</div>

<section ng-app="validationApp" ng-controller="moderationController" ng-submit="submitPlaces()">
	<h2>Locais inseridos</h2>
	<hr>
	<!-- CARDS	 -->
	<form name="placesForm" >
	<?php foreach ($placesToModerate as $singlePlace){
		$ID = $singlePlace->getId();
		?>
	<section>
	<input type="checkbox" style="float: left" ng-model="place[<?php echo $ID ?>].checked" ng-click="addToPlaces(place[<?php echo $ID ?>])" >
	<div class="card row">
			<input hidden ng-init="place[<?php echo $ID?>].id= '<?php echo $ID;?>'">
			<div class="col-lg-2 col-md-4 col-sm-4 col-xs-6">
				<input hidden ng-init="place[<?php echo $ID?>].name= '<?php echo $singlePlace->getName();?>'">
				<label>Nome:</label><?php echo "\t" . $singlePlace->getName();?>
			</div>
			<div class="col-lg-2 col-md-4 col-sm-4 col-xs-6">
				<input hidden ng-init="place[<?php echo $ID?>].amenity= '<?php echo $singlePlace->getAmenity();?>'">
				<label>Amenity - Tag OSM:</label><?php echo "\t" . $singlePlace->getAmenity();?>
			</div>
			<div class="col-lg-2 col-md-4 col-sm-4 col-xs-6">
				<input hidden ng-init="place[<?php echo $ID?>].description= '<?php echo $singlePlace->getDescription();?>'">
				<label>Descrição:</label><?php echo "\t" . $singlePlace->getDescription();?>
			</div>
			<div class="col-lg-2 col-md-4 col-sm-4 col-xs-6">
				<label>Latitude e Longitude:</label>
				<input hidden ng-init="place[<?php echo $ID?>].latitude= '<?php echo $singlePlace->getLatitude();?>'">
				<input hidden ng-init="place[<?php echo $ID?>].longitude= '<?php echo $singlePlace->getLongitude();?>'">
				<?php echo "\t" . $singlePlace->getLatitude() . ', ' . $singlePlace->getLongitude();?>
			</div>
			<div class="col-lg-2 col-md-4 col-sm-4 col-xs-6">
				<input hidden ng-init="place[<?php echo $ID?>].localName= '<?php echo $singlePlace->getLocalName();?>'">
				<label>Nome Local:</label><?php echo "\t" . $singlePlace->getLocalName();?>
			</div>
			<div class="col-lg-1 col-md-4 col-sm-4 col-xs-6">
				<input hidden ng-init="place[<?php echo $ID?>].shortName= '<?php echo $singlePlace->getShortName();?>'">			
				<label>Abreviação:</label><?php echo "\t" . $singlePlace->getShortName();?>
			</div>
	</div>
	</section>
	<br>
	<?php } ?>
	<div class="row">
		<button type="submit" class="btn btn-primary" style="float: right">Confirmar</button>
	</div>
	</form>
</section>



<script src="js/validation-app.js"></script>

<?php include "footer.php" ; ?>
