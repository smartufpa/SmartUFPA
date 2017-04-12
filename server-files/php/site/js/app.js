var validationApp= angular.module('validationApp',[]);

validationApp.controller('mainController',function($scope){
  $scope.regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*(_|[-+_!@#$%^&*.,?]))./;
  $scope.submitForm = function(isValid){
    if (isValid) {
      // do something
    }
  }

});
