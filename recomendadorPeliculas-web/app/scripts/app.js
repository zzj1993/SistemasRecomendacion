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
    'ngTouch',
    'LocalStorageModule'
  ])
  .config(['localStorageServiceProvider', function(localStorageServiceProvider){
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
/*
var loginModule = angular.module('loginModule', []);
var recomendadorPeliculasWebApp = angular.module('recomendadorPeliculasWebApp', [
	'ngAnimate',
    'ngAria',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
	'ui.router',
	'loginModule']);

recomendadorPeliculasWebApp.config(function($httpProvider, $stateProvider, $urlRouterProvider) {
    $httpProvider.defaults.headers.common['Session-Id'] = localStorage.getItem("session_id");
    $httpProvider.defaults.useXDomain = true;
    $urlRouterProvider.otherwise('/main');
    $stateProvider.state('main', {
        url: '/main',
        views: {
            'messagesView': {
                controller: 'ErrorController',
                templateUrl: '../views/messages.html'
            },
            'mainView': {
                templateUrl: '../views/main.html'
            }
        }
    });
});*/
