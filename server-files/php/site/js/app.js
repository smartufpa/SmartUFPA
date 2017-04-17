var validationApp = angular.module('validationApp', [ 'ngPassword' ]);
validationApp
		.controller(
				'mainController',
				function($scope, $http) {
					$scope.regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*(_|[-+_!@#$%^&*.,?]))./;
					$scope.submitForm = function(isValid) {
						if (isValid) {						
							$http.post("process-userform.php",$scope.user, [{'Content-Type' : 'application/json'}])
							.then(function success(data, status, headers, config){
								console.log(status);
								alert("Conta criada com sucesso.");
							}, function error(data, status, headers, config){
								alert("Erro interno do servidor.");
							});

						}
					}

				});

