'use strict';

moviesModule.factory('RatingService', ['$resource', function ($resource) {

  	var url = 'http://localhost:8080/rating';
  	var defaultParams = {};
  	var actions= {
		createRating: {method: 'POST'},
		getRecommendations: {method: 'GET', isArray: true}
  	};

  	return $resource(url, defaultParams, actions);
}]);