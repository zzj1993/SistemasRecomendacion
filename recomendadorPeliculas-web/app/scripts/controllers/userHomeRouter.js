'use strict';

usersModule.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state('param', {
        url: '/param',
        views: {
            'messagesView': {
                controller: 'ErrorCtrl',
                templateUrl: 'views/messages.html'
            },
            'mainView': {
                controller: 'ParamCtrl',
                templateUrl: 'views/params.html'
            }
        }
    });
});