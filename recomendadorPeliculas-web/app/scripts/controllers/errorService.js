'use strict';

commonsModule.factory('ErrorService', function() {

    var errorService = {};
    var errorMessage = '';
    
    errorService.getErrorMessage = function() {
        return errorMessage;
    };

    errorService.setErrorMessage = function(message) {
        errorMessage = message;
    };

    return errorService;
});