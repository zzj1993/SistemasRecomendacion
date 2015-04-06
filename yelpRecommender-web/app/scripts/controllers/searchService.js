'use strict';

angular.module('yelpRecommenderWebApp').factory('SearchService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:8080/search';
  	var defaultParams = {};
  	var actions= {
  		search: {method: 'GET'}
  	};
  	return $resource(url, defaultParams, actions);
 }]);