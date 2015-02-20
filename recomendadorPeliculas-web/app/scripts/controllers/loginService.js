'use strict';

angular.module('recomendadorPeliculasWebApp').factory('LoginService', ['$resource', function ($resource) {
  	
  	var url = 'http://localhost:8080/login';
  	var defaultParams = {};
  	var actions= {
  		login: {method: 'POST'}
  	};

  	return $resource(url, defaultParams, actions);
  }]);