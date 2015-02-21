'use strict';

angular.module('recomendadorPeliculasWebApp').controller('SignupCtrl', ['$scope', '$state', 'SignupService', 'localStorageService', 'ErrorService', 
	function ($scope, $state, SignupService, localStorageService, ErrorService) {
    
    var success = function(data){
    	window.alert(data);
    	$state.go('user-home');
    };
    
    var error = function(data){
    	ErrorService.setErrorMessage(data.errorMessage);
    };
    
    $scope.signup = function(signupData){
    	SignupService.signup(signupData)
    		.success(success)
    		.error(error);
    };
    
    /*
    $scope.signupData = {};
    function handleError(data) {
        var message = '';
        console.error('handleError: ' + JSON.stringify(data));
        if (data.status == 0) {
            message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';
        } else {
            message = data.data.errorMessage;
        }
        ErrorService.setErrorMessage(message);
        window.alert(data);
        window.alert(message);
    };
    
    function onLoginSuccess(data) {
    	window.alert(data);
    	//$scope.sessions.push($scope.session);
    	//localStorageService.add('Token', data.id);
        $state.go('user-home');
    };

    $scope.signup = function(signupData){
    	SignupService.signup(signupData);
    };
    */
  }]);
