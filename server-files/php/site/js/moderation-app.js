/**
 * @author kaeuchoa
 * 
 */


/*
 * 
 * Modelo da requisição POST
 $http.post("../testAPI/script.php",$scope.data,
					[{'Content-Type' : 'application/json'}]
			).then(function success(data, status, headers, config){
				
			}, function error(data, status, headers, config){
			
			});
*/


var moderationApp = angular.module('moderationApp', []);
moderationApp.controller('moderationController', function($scope,$http){
	$scope.places = [];
	
	$scope.addToPlaces = function(place){
			$scope.places.push(place);
	}
	
	$scope.submitPlaces = function(){
		// TODO executar script para adicionar à tabela de locais
		$http.post("../testAPI/insert-place.php",
				$scope.places,
				[{'Content-Type' : 'application/json'}]
		).then(function success(data, status, headers, config){
			console.log(status);
			alert("Locais adicionados à tabela principal com sucesso.");
			location.reload();
		}, function error(data, status, headers, config){
			console.log(status);
			alert("Houve um erro ao adicionar os locais à tabela principal com sucesso.");
			location.reload();
		});
	}
	
});