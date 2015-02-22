'use strict';

commonsModule.controller('ErrorCtrl', ['$scope', 'ErrorService', function($scope, ErrorService) {

    function getErrorMessage() {
    	var e = ErrorService.getErrorMessage();
        return e;
    }

    function updateErrorMessage(message) {
        $scope.errorMessage = message;
    }

    $scope.$watch(getErrorMessage, updateErrorMessage);
}]);