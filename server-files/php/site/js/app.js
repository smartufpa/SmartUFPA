var validationApp= angular.module('validationApp',['ngPassword']);

validationApp.controller('mainController',function($scope, $http){
  $scope.regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*(_|[-+_!@#$%^&*.,?]))./;


  $scope.submitForm = function(isValid){
    if (isValid) {
      $http({
        method:'POST',
        url: 'process-userform.php',
        data: $scope.user,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      });      

    }
  }



});
