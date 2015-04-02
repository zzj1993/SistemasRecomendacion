'use strict';

angular.module('yelpRecommenderWebApp').factory('RecommendationService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:8080/recommendation';
  	var defaultParams = {};
  	var actions= {
  		getRecommendations: {method: 'GET', isArray: true}
  	};
  	return $resource(url, defaultParams, actions);
 }]);