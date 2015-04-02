'use strict';

angular.module('yelpRecommenderWebApp').factory('UserReviewsService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:8080/recommendation/user/:userId';
  	var defaultParams = {};
  	var actions= {
  		getUserReviews: {method: 'GET', params:{name: '@userId'} ,isArray: true}
  	};
  	return $resource(url, defaultParams, actions);
 }]);