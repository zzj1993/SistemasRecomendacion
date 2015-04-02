'use strict';

angular.module('yelpRecommenderWebApp').factory('UserService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:8080/user';
  	var defaultParams = {};
  	var actions= {
  		getUsers: {method: 'GET', isArray: true}
  	};
  	return $resource(url, defaultParams, actions);
 }]);