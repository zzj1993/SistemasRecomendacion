'use strict';

/**
 * @ngdoc overview
 * @name recomendadorPeliculasWebApp
 * @description
 * # recomendadorPeliculasWebApp
 *
 * Main module of the application.
 */

var recomendadorWebApp = angular.module('recomendadorPeliculasWebApp', [
    'ngAnimate',
    'ngAria',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'LocalStorageModule'
  ]);

recomendadorWebApp.config(['localStorageServiceProvider', function(localStorageServiceProvider){
    localStorageServiceProvider.setPrefix('ls');
  }])
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
	  .when('/contact', {
        templateUrl: 'views/contact.html',
        controller: 'ContactCtrl'
      })
	  .when('/registro', {
        templateUrl: 'views/registro.html',
        controller: 'RegistroCtrl'
      })
	  .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });