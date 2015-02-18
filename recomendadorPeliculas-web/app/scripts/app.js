'use strict';

/**
 * @ngdoc overview
 * @name recomendadorPeliculasWebApp
 * @description
 * # recomendadorPeliculasWebApp
 *
 * Main module of the application.
 */
angular
  .module('recomendadorPeliculasWebApp', [
    'ngAnimate',
    'ngAria',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
