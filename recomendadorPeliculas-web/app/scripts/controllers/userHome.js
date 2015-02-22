'use strict';

usersModule.controller('UserHomeCtrl', ['$scope', 'localStorageService', 'MovieService', 'RatingService', 'ErrorService',
	function ($scope, localStorageService, MovieService, RatingService, ErrorService) {
		// json define los params de la url y query params
  		// callback success
  		//callback error
  		var count = 1;
  		var ini = 1;
  		var fin = 6;

  		$scope.currentPage = 0;
    	$scope.pageSize = 5;
    
    	$scope.numberOfPages=function(){
        	return Math.ceil($scope.movies.length/$scope.pageSize);                
    	}

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
			localStorageService.add('userMovies', data);
			scope.movies = data;//.slice(ini, fin);
    	}

    	function onSuccessRating(data) {
			console.debug('Data: ' + JSON.stringify(data));
    	}

    	function loadMovies(){
    		$scope.user = localStorageService.get('Token');
    		localStorageService.remove('movies');
    		if(localStorageService.get('userMovies') != null && localStorageService.get('userMovies').length!=0){
    			return localStorageService.get('userMovies');//.slice(ini, fin);
    		}else{
    			return MovieService.getAllUserMovies($scope.user.username, onSuccess, handleError);
    		}
    	}

   		$scope.movies = loadMovies();
   		
   		$scope.$watch('movie', function(newValue, oldValue){
   			if(newValue!==oldValue){
   				var obj = {
   					user: $scope.user.username,
   					item: newValue.id,
   					rating: newValue.avgRating
   				};
   				MovieService.createRating(obj, onSuccessRating, handleError);
   				var data = localStorageService.get('userMovies');
   				for(var i = 0 ; i < data.length ; i++){
   					if(newValue.id === data[i].id){
   						data[i].avgRating = newValue.avgRating;
   					}
   				}
   				localStorageService.set('userMovies', data)
   			}
   		}, true);

    	$scope.next = function(){
    		var data = localStorageService.get('userMovies');
    		count = count + 1;
    		ini = fin;
    		fin = count*5+1; 
    		$scope.movies = data.slice(ini, fin);
    	};

    	$scope.back = function(){
    		var data = localStorageService.get('userMovies');
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