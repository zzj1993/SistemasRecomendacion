'use strict';

usersModule.controller('LoginCtrl', ['$scope', '$state', 'LoginService', 
    'localStorageService', 'ErrorService', function ($scope, $state, LoginService, localStorageService, ErrorService) {
    
    function handleError(data) {
        var message = '';
        console.error('handleError: ' + JSON.stringify(data));
        // if (data.status == 0) {
        //     message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';
        // } else {
        //     message = data.data.errorMessage;
        // }
        ErrorService.setErrorMessage(message);
    }

    function onSuccess(data) {
		console.debug('Data: ' + JSON.stringify(data));
        localStorageService.add('Token', data);
        $state.go('user-home');
    }

    $scope.login = function(loginData){
    	LoginService.login({userId: loginData.email, username: loginData.email, password: loginData.password}, 
    		onSuccess, handleError);
    };
}]);