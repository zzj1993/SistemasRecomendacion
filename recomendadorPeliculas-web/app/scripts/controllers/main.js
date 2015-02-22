'use strict';

/**
 * @ngdoc function
 * @name recomendadorPeliculasWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the recomendadorPeliculasWebApp
 */
moviesModule.controller('MainCtrl', ['$scope', 'localStorageService', 'MovieService', 'ErrorService',
  	function ($scope, localStorageService, MovieService, ErrorService) {

  		// json define los params de la url y query params
  		// callback success
  		//callback error
  		var count = 1;
      var ini = 1;
      var fin = 6;

  	function handleError(data) {
        var message = '';
        console.error('handleError: ' + JSON.stringify(data));
        // if (data.status == 0) {
        //     message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';
        // } else {
        //     message = data.data.errorMessage;
        // }
        ErrorService.setErrorMessage(message);
    }

    function onSuccess(data) {
		    //console.debug('Data: ' + JSON.stringify(data));
        localStorageService.add('movies', data);
        $scope.movies = data.slice(ini, fin);
        // $state.go('employee-home');
    }

    function loadMovies(){
      if(localStorageService.get('movies').length!=0){
        return localStorageService.get('movies').slice(ini, fin);
      }else{
        return MovieService.getAllMovies(onSuccess, handleError);
      }
    }

	//$scope.movies = MovieService.getAllMovies(onSuccess, handleError);
  $scope.movies = loadMovies();

  $scope.next = function(){
    var data = localStorageService.get('movies');
    count = count + 1;
    ini = fin;
    fin = count*5+1; 
    $scope.movies = data.slice(ini, fin);
  };

  $scope.back = function(){
    var data = localStorageService.get('movies');
    count = count - 1;
    if(count>0){
      fin = ini;
      ini = fin-5;
      $scope.movies = data.slice(ini, fin);
    }else{
      count = 1;
    }
  };

}]);
