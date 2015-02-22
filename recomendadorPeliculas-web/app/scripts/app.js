'use strict';


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

}]);