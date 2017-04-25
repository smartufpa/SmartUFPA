var validationApp = angular.module('validationApp', [ 'ngPassword' ]);
validationApp
		.controller(
				'mainController',
				function($scope, $http) {
					$scope.regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*(_|[-+_!@#$%^&*.,?]))./;
					$scope.submitForm = function(isValid) {
						if (isValid) {						
							$http.post("../testAPI/insert-user.php",$scope.user, [{'Content-Type' : 'application/json'}])
							.then(function success(data, status, headers, config){
								console.log(status);
								alert("Conta criada com sucesso.");
								location.reload();
							}, function error(data, status, headers, config){
								if(data.status == 304){
									alert("Nome de usuário já existe, tente outra vez.");
								}else{
									alert("Erro interno do servidor.");									
								}
							});

						}
					}

				});

