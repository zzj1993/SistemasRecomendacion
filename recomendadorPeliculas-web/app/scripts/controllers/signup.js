'use strict';

usersModule.controller('SignupCtrl', ['$scope', '$state', 'SignupService', 
    'localStorageService', 'ErrorService', 'SignupAuthService', 'LoginService', 
	function ($scope, $state, SignupService, localStorageService, ErrorService, SignupAuthService, LoginService) {
    
    function handleError(data) {
        var message = '';
        console.error('handleError: ' + JSON.stringify(data));
        ErrorService.setErrorMessage(message);
        localStorageService.remove('SignUp'); 
    }

    function onSuccess(data) {
        localStorageService.remove('SignUp'); 
        localStorageService.add('Token', data);
        $state.go('user-home');
    }

    function onSuccessCreate(){
        var signupData = localStorageService.get('SignUp');
        LoginService.login({userId: signupData.username, username: signupData.username, password: signupData.password}, 
            onSuccess, handleError);
    }
    
    $scope.signup = function(signupData){
        localStorageService.add('SignUp', signupData);
        SignupService.signup({id: signupData.username, userId: signupData.username});
        SignupAuthService.create({name: signupData.username, username: signupData.username, password: signupData.password,
            apiClient: false, organizationId: 'user', roleId: 'user'}, onSuccessCreate, handleError);
    };
}]);
