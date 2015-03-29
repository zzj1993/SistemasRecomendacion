'use strict';

/**
 * @ngdoc overview
 * @name yelpRecommenderWebApp
 * @description
 * # yelpRecommenderWebApp
 *
 * Main module of the application.
 */
angular.module('yelpRecommenderWebApp', [
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.bootstrap',
    'LocalStorageModule'
  ]).config(['localStorageServiceProvider', function(localStorageServiceProvider){
    localStorageServiceProvider.setPrefix('ls');
  }])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/evaluation.html',
        controller: 'EvaluationCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
});