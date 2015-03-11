'use strict';

usersModule.controller('UserHomeCtrl', ['$scope', '$state', 'localStorageService', 'MovieService', 'RatingService', 
  'ErrorService', 'RatedMovieService',
	function ($scope, $state, localStorageService, MovieService, RatingService, ErrorService, RatedMovieService) {

  		var count = 1;
  		var ini = 1;
  		var fin = 6;

  		$scope.currentPage = 0;
    	$scope.pageSize = 5;
      $scope.buttonTitle = 'Rated Movies';
    
    	$scope.numberOfPages=function(){
        	return Math.ceil($scope.userMovies.length/$scope.pageSize);                
    	};

  		function handleError(data) {
  			var message = '';
  			console.error('handleError: ' + JSON.stringify(data));
        ErrorService.setErrorMessage(message);
    	}

    	function onSuccessUser(data) {
			//console.debug('Data: ' + JSON.stringify(data));
         localStorageService.remove('userMovies');
			   localStorageService.add('userMovies', data);
			   $scope.userMovies = data;
    	}

    	function onSuccessRating(data) {
			   console.debug('Data: ' + JSON.stringify(data));
         localStorageService.remove('userMovies');
         $scope.userMovies = loadUserMovies();
    	}

      function loadRecommendations(){
        if($scope.user!==null){
          // if(localStorageService.get('userMovies') !== null && localStorageService.get('userMovies').length!==0){
          //   return localStorageService.get('userMovies');
          // }else{
           var param = {
              userid: $scope.user.username,
              model: localStorageService.get('model').id,
              size:  localStorageService.get('size'),
              n: parseInt(localStorageService.get('n')),
              type: parseInt(localStorageService.get('recommendationType').id)
           };
           return RatingService.getRecommendations(param, onSuccessUser, handleError);
          // }
        }
      }

      function loadRatedMovies(){
        if($scope.user!==null){
          var param = {
              userid: $scope.user.username,
              model: localStorageService.get('model').id,
              size:  localStorageService.get('size'),
              n: parseInt(localStorageService.get('n')),
              type: parseInt(localStorageService.get('recommendationType').id)
           };
          return RatedMovieService.getRatedMovies(param, onSuccessUser, handleError);
        }
      }

      function loadLocalStorage(){
        $scope.user = localStorageService.get('Token');
        if(localStorageService.get('model')===null){
          localStorageService.set('model', { id: 1, name: 'Jaccard Distance' });
        }
        if(localStorageService.get('recommendationType')===null){
          localStorageService.set('recommendationType', { id: 1, name: 'Users' });
        }
        if(localStorageService.get('size')===null){
          localStorageService.set('size', 100);
        }
        if(localStorageService.get('n')===null){
          localStorageService.set('n', 10);
        }
      }

      $scope.changeMovieList = function (){
        if($scope.buttonTitle==='Rated Movies'){
          $scope.buttonTitle = 'Get Recommendations';
          $scope.userMovies = loadRatedMovies();
        }else{
          $scope.buttonTitle = 'Rated Movies';
          $scope.userMovies = loadRecommendations();
        }
      };

    	function loadUserMovies(){
        loadLocalStorage();
        return loadRecommendations();
    	}

   		$scope.userMovies = loadUserMovies();
   		
   		$scope.$watch('movie', function(newValue, oldValue){
   			if(newValue!==oldValue){
   				var obj = {
   					user: $scope.user.username,
   					item: newValue.id,
   					rating: newValue.avgRating
   				};
   				MovieService.createRating(obj, onSuccessRating, handleError);
   			}
   		}, true);

      $scope.param = function(){
        $state.go('param');
      };

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