'use strict';

angular.module('yelpRecommenderWebApp').factory('RecommendationService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:8080/recommendation';
  	var defaultParams = {};
  	var actions= {
  		updateRecommender: {method: 'POST', params:{name: '@name'}},
  		getTrainingProgress: {method: 'GET', params:{name: '@name'}}
  	};
  	return $resource(url, defaultParams, actions);
 }]);