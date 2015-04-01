'use strict';

angular.module('yelpRecommenderWebApp').factory('NeighborhoodService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:8080/neighborhood';
  	var defaultParams = {};
  	var actions= {
  		getNeighborhoods: {method: 'GET', isArray: true}
  	};
  	return $resource(url, defaultParams, actions);
 }]);