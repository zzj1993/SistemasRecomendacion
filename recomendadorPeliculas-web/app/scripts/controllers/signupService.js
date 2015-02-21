'use strict';

angular.module('recomendadorPeliculasWebApp').factory('SignupService', ['$http', function ($http) {
  	
  	/*
  	var url = 'http://localhost:8080/signup';
  	var defaultParams = {};
  	var actions= {
  		signup: {method: 'POST'}
  	};

  	return $resource(url, defaultParams, actions);
  	*/
  	var service = {};
  	
  	service.signup = function(signupData){
  		return $http.post('http://localhost:8080/signup', signupData);
  	};
  	
  	return service;
  }]);