'use strict';

moviesModule.factory('RatedMovieService', ['$resource', function ($resource) {

  	var url = 'http://localhost:8080/movies/rated';
  	var defaultParams = {};
  	var actions= {
      	getRatedMovies: {method: 'GET', isArray: true}
  	};

  	return $resource(url, defaultParams, actions);
}]);