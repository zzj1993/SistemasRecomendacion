'use strict';


angular.module('recomendadorPeliculasWebApp').controller('LoginCtrl', ['$scope', 'LoginService', function ($scope, LoginService) {
    $scope.loginData = {};
    
    $scope.login = function(loginData){
    	LoginService.login(loginData);
    };
  }]);