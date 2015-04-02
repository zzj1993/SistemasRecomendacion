'use strict';

angular.module('yelpRecommenderWebApp').factory('DeleteReviewsService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:8080/recommendation/review';
  	var defaultParams = {};
  	var actions= {
  		deleteReview: {method: 'POST'}
  	};
  	return $resource(url, defaultParams, actions);
 }]);