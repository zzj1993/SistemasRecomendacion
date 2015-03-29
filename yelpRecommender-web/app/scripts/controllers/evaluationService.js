'use strict';

angular.module('yelpRecommenderWebApp').factory('EvaluationService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:8080/evaluation';
  	var defaultParams = {};
  	var actions= {
  		getStatistics: {method: 'GET', isArray: true}
  	};
  	return $resource(url, defaultParams, actions);
 }]);