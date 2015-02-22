'use strict';

usersModule.controller('UserHomeCtrl', ['$scope', 'localStorageService', 'MovieService', 'RatingService', 'ErrorService',
	function ($scope, localStorageService, MovieService, RatingService, ErrorService) {
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
    	}

    	function onSuccessRating(data) {
			console.debug('Data: ' + JSON.stringify(data));
    	}

    	function loadMovies(){
    		if(localStorageService.get('movies') != null && localStorageService.get('movies').length!=0){
    			return localStorageService.get('movies').slice(ini, fin);
    		}else{
    			return MovieService.getAllMovies(onSuccess, handleError);
    		}
    	}

    	function compareMovies(a,b) {
  			if (a.avgRating === b.avgRating && a.changed === b.changed)
     			return 0;
  			else
  				return 1;
		}

   		$scope.movies = loadMovies();
   		
   		$scope.$watch('movie', function(newValue, oldValue){
   			if(newValue!==oldValue){
   				// for(var i = 0 ; i < newValue.length ; i++){
   					if(compareMovies(newValue, oldValue)===1){
   						MovieService.createRating({username: $scope.user.username, itemId: newValue.id, 
   							rating: newValue.avgRating}, onSuccessRating, handleError);
   					}
   				// }
   			}
   			// localStorageService.put();
   			// window.alert('changed');
   		}, true);

   		$scope.user = localStorageService.get('Token');

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