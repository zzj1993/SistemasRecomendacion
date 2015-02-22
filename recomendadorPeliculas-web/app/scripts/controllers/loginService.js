'use strict';

usersModule.factory('LoginService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:9002/api/users/:userId/tokens';
  	var defaultParams = {};
  	var actions= {
  		login: {method: 'POST', params:{userId: '@userId'}}
  	};

  	return $resource(url, defaultParams, actions);
 }]);