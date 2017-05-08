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
	<a id="logout" class="btn btn-danger" "href="../logout.php"> 
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
			
			<div class="col-lg-2 col-md-3 col-sm-12 col-xs-12">
				<label>Nome:</label>
				<input class="editable-input form-control" value="<?php echo $singlePlace->getName();?>" readonly				
				ng-init="place[<?php echo $ID?>].name= '<?php echo $singlePlace->getName();?>'" 
				ng-model="place[<?php echo $ID?>].name"
				>
			</div>
			
			<div class="col-lg-2 col-md-4 col-sm-12 col-xs-12">
				<label>Amenity - Tag OSM:</label>
				<input class="editable-input form-control" value="<?php echo $singlePlace->getAmenity();?>" readonly 
				ng-init="place[<?php echo $ID?>].amenity= '<?php echo $singlePlace->getAmenity();?>'"
				ng-model="place[<?php echo $ID?>].amenity"
				>
			</div>
			
			<div class="col-lg-2 col-md-4 col-sm-12 col-xs-12">
				<label>Descrição:</label>
				<input class="editable-input form-control" value="<?php echo $singlePlace->getDescription();?>" readonly 
				ng-init="place[<?php echo $ID?>].description= '<?php echo $singlePlace->getDescription();?>'"
				ng-model="place[<?php echo $ID?>].description"
				>
			</div>
			
			
			<div class="col-lg-2 col-md-4 col-sm-6 col-xs-6">
				<label>Latitude e Longitude:</label>
				<input class="editable-input form-control" value="<?php echo $singlePlace->getLatitude();?>" readonly 
				ng-init="place[<?php echo $ID?>].latitude= '<?php echo $singlePlace->getLatitude();?>'"
				ng-model="place[<?php echo $ID?>].latitude"
				>
				<input class="editable-input form-control" value="<?php echo $singlePlace->getLongitude();?>" readonly
				 ng-init="place[<?php echo $ID?>].longitude= '<?php echo $singlePlace->getLongitude();?>'"
				 ng-model="place[<?php echo $ID?>].longitude"
				 >
			</div>
			
			
			<div class="col-lg-2 col-md-6 col-sm-12 col-xs-12">
				<label>Nome Local:</label>
				<input class="editable-input form-control" value="<?php echo $singlePlace->getLocalName();?>" readonly t
				ng-init="place[<?php echo $ID?>].localName= '<?php echo $singlePlace->getLocalName();?>'"
				ng-model="place[<?php echo $ID?>].localName"
				>
			</div>
			
			
			<div class="col-lg-1 col-md-6 col-sm-4 col-xs-12">
				<label>Abreviação:</label>
				<input class="editable-input form-control" value="<?php echo "\t" . $singlePlace->getShortName();?>" readonly 
				ng-init="place[<?php echo $ID?>].shortName= '<?php echo $singlePlace->getShortName();?>'"
				ng-model="place[<?php echo $ID?>].shortName"
				>
				
			</div>
	</div>
	<br>
	<div class="row">
	<button class="btn btn-success btn-save" style="float:right;" type="button" hidden="true">Salvar</button>
	
	<button class="btn btn-default btn-edit" style="float:right;" type="button">Editar</button>
	
	<a class="btn btn-link" 
	href="http://www.openstreetmap.org/#map=17/<?php echo $singlePlace->getLongitude();?>/<?php echo $singlePlace->getLatitude();?>"
	target="_blank"
	>
		Mostrar no mapa
	</a>
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
<script src="js/moderation.js"></script>




<?php include "footer.php" ; ?>
