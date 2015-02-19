'use strict';

/**
 * @ngdoc function
 * @name recomendadorPeliculasWebApp.controller:RegistroCtrl
 * @description
 * # RegistroCtrl
 * Controller of the recomendadorPeliculasWebApp
 */
angular.module('recomendadorPeliculasWebApp')
.controller('RegistroCtrl', function ($scope, localStorageService) {
  var inStore = localStorageService.get('todos');

  
});
