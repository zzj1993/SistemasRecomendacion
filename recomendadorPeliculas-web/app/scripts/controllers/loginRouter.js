'use strict';

usersModule.config(function($stateProvider, $urlRouterProvider) {
    $stateProvider.state('login', {
        url: '/login',
        views: {
            'messagesView': {
                controller: 'ErrorCtrl',
                templateUrl: 'views/messages.html'
            },
            'mainView': {
                controller: 'LoginCtrl',
                templateUrl: 'views/login.html'
            }
        }
    });
});