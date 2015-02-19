'use strict';

loginModule.factory('LoginService', ['$resource', function($resource) {

    var url = 'http://localhost:9001/facturala/api/login';
    var defaultParams = {};
    var actions = {
        login: { method: 'POST' }
    };
    return $resource(url, defaultParams, actions);
}]);
