'use strict';

/**
 * @ngdoc function
 * @name recomendadorPeliculasWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the recomendadorPeliculasWebApp
 */
angular.module('recomendadorPeliculasWebApp')
  .controller('LoginCtrl', function ($scope) {

  
    function onLoginSuccess(data) {
        localStorage.setItem("session_id", data.id);
    };

    $scope.login = function(loginData) {
        LoginService.login(loginData, onLoginSuccess, handleError);
    };
  });
