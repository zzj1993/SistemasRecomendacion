'use strict';

moviesModule.factory('MovieService', ['$resource', function ($resource) {

  	var url = 'http://localhost:8080/movies';
  	var defaultParams = {};
  	var actions= {
  		getAllMovies: {method: 'GET', isArray: true}
  	};

  	return $resource(url, defaultParams, actions);
	
  	/*
  	var service = {};
  	
  	service.query = function(){
  		return $http.get('http://localhost:8080/movie');
  	};
  	
  	return service;*/
  }]);