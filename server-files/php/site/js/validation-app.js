var validationApp = angular.module('validationApp', [ 'ngPassword' ]);
validationApp.controller('userFormController',function($scope, $http) {
	$scope.places = ["test"]; 
	$scope.regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*(_|[-+_!@#$%^&*.,?]))./;
	$scope.submitUserForm = function(isValid) {
		if (isValid) {
			$http.post(
				"../testAPI/insert-user.php",
				$scope.user,
				[{'Content-Type' : 'application/json'}]
				)
			.then(function success(data, status,headers, config) {
					console.log(status);
					alert("Conta criada com sucesso.");
					location.reload();
					},
				function error(data, status,headers, config) {
					if (data.status == 304) {
						alert("Nome de usuário já existe, tente outra vez.");
					} else {
						alert("Erro interno do servidor.");
					}
			});
		}
	}
});

validationApp.controller('moderationController', function($scope,$http){
	$scope.places = [];
	
	$scope.addToPlaces = function(place){
		if(place.checked == true)
			$scope.places.push(place);
		else
			$scope.places.pop(place);
	}
	
	$scope.submitPlaces = function(){
		if($scope.places.length == 0){
			// TODO remover da tabela de moderação
			alert("remover da tabela de moderação");
		}else{
			// TODO executar script para adicionar à tabela de locais
			alert("executar script para adicionar à tabela de locais");
			
		}
	}
	
});
