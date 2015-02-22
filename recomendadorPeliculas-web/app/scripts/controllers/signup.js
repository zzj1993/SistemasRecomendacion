'use strict';

usersModule.controller('SignupCtrl', ['$scope', '$state', 'SignupService', 
    'localStorageService', 'ErrorService', 'SignupAuthService',
	function ($scope, $state, SignupService, localStorageService, ErrorService, SignupAuthService) {
    
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
        localStorageService.add('Token', data);
        $state.go('user-home');
    }
    
    $scope.signup = function(signupData){
        SignupAuthService.create({name: signupData.name, username: signupData.email, password: signupData.password,
            apiClient: false, organizationId: 'user', roleId: 'user'});
    	SignupService.signup(signupData, onSuccess, handleError);
    };
}]);
