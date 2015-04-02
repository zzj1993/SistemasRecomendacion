'use strict';

angular.module('yelpRecommenderWebApp').factory('AddReviewsService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:8080/recommendation/rating';
  	var defaultParams = {};
  	var actions= {
  		addReview: {method: 'POST'}
  	};
  	return $resource(url, defaultParams, actions);
 }]);