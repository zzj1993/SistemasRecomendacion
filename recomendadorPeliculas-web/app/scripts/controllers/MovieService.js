'use strict';

moviesModule.factory('MovieService', ['$resource', function ($resource) {

  	var url = 'http://localhost:8080/movies';
  	var defaultParams = {};
  	var actions= {
  		getAllMovies: {method: 'GET', isArray: true},
      createRating: {method: 'POST'}
  	};

  	return $resource(url, defaultParams, actions);
}]);