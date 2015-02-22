'use strict';

moviesModule.factory('RatingService', ['$resource', function ($resource) {

  	var url = 'http://localhost:8080/rating';
  	var defaultParams = {};
  	var actions= {
		createRating: {method: 'POST'}
  	};

  	return $resource(url, defaultParams, actions);
}]);