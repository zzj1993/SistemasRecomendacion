'use strict';

angular.module('yelpRecommenderWebApp').factory('ConfigurationService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:8080/configuration/:name';
  	var defaultParams = {};
  	var actions= {
  		updateRecommender: {method: 'POST', params:{name: '@name'}},
  		getTrainingProgress: {method: 'GET', params:{name: '@name'}}
  	};
  	return $resource(url, defaultParams, actions);
 }]);