'use strict';

angular.module('recomendadorPeliculasWebApp').config(function($stateProvider, $urlRouterProvider) {

    $stateProvider.state('signup', {
        url: '/signup',
        views: {
            'messagesView': {
                controller: 'ErrorCtrl',
                templateUrl: 'views/messages.html'
            },
            'mainView': {
                controller: 'SignupCtrl',
                templateUrl: 'views/signup.html'
            }
        }
    }).state('user-home', {
        url: '/home',
        views: {
            'messagesView': {
                controller: 'ErrorCtrl',
                templateUrl: 'views/messages.html'
            },
            'mainView': {
                controller: 'UserHomeCtrl',
                templateUrl: 'views/userHome.html'
            }
        }
    });
});