'use strict';

usersModule.factory('SignupAuthService', ['$resource', function ($resource) {
  	
  	
  	var url = 'http://localhost:9002/api/users';
    var defaultParams = {};
    var actions= {
      create: {method: 'POST'}
    };

    return $resource(url, defaultParams, actions);
}]);