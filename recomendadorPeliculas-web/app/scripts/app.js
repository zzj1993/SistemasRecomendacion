'use strict';

/**
 * @ngdoc overview
 * @name recomendadorPeliculasWebApp
 * @description
 * # recomendadorPeliculasWebApp
 *
 * Main module of the application.
 */
 /*
var recomendadorWebApp = angular.module('recomendadorPeliculasWebApp', [
    'ngAnimate',
    'ngAria',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'LocalStorageModule',
    'ui.router'
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
	  .when('/signup', {
        templateUrl: 'views/signup.html',
        controller: 'SignupCtrl'
      })
	  .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
  
var commonsModule = angular.module('commonsModule', []);
var companyModule = angular.module('companyModule', []);
var employeeModule = angular.module('employeeModule', []);
var invoiceModule = angular.module('invoiceModule', []);

var commonsModule = angular.module('commonsModule', []);
var signupModule = angular.module('signupModule', []);*/

var commonsModule = angular.module('commonsModule', []);
var usersModule = angular.module('usersModule', []);
var moviesModule = angular.module('moviesModule', []);

var recomendadorPeliculasWebApp = angular.module('recomendadorPeliculasWebApp', ['ngResource', 'ui.router', 'LocalStorageModule', 
  'commonsModule', 'moviesModule', 'usersModule']);

var app = recomendadorPeliculasWebApp.config(function($httpProvider, $stateProvider, $urlRouterProvider) {
	$httpProvider.interceptors.push('HttpHeadersIntercetor');
    $urlRouterProvider.otherwise('/');
    $stateProvider.state('home', {
        url: '/',
        views: {
            'messagesView': {
                controller: 'ErrorCtrl',
                templateUrl: 'views/messages.html'
            },
            'mainView': {
            	controller: 'MainCtrl',
                templateUrl: 'views/main.html'
            }
        }
    });  
});
 
app.run(['$rootScope', '$state', 'localStorageService', '$location',function ($rootScope, $state, localStorageService, $location) {
/*
    $rootScope.$on("$stateChangeStart", function (event, toState, toParams, fromState, fromParams) {

        var tokenFormLocalStorage = localStorageService.get('Token');

        if (tokenFormLocalStorage == null) {

            var homeState = 'home';

            if(toState.name != homeState) {

                event.preventDefault();
                $state.transitionTo(homeState, {}, {
                    reload: true,
                    inherit: false,
                    notify: true
                });
            }
        }     
    });
   */
}]);