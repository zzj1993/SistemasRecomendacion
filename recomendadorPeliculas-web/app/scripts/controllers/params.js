'use strict';

usersModule.controller('ParamCtrl', ['$scope', '$state','localStorageService', 
	function ($scope, $state, localStorageService) {

	$scope.algorithms = [
        { id: 1, name: 'Jaccard Distance' },
        { id: 2, name: 'Cosine Distance' },
        { id: 3, name: 'Pearson Distante' }
    ];

    function loadAlgo(){
    	if(localStorageService.get('model')!==null){
    		return localStorageService.get('model');
    	}else{
    		return $scope.algorithms[0];
    	}
    }

    $scope.selectedAlgorithm = loadAlgo();

    function loadRecommendarionType(){
    	if(localStorageService.get('recommendationType')!==null){
    		return localStorageService.get('recommendationType');
    	}else{
    		return $scope.recommendationType[0];
    	}
    }

    $scope.recommendationType = [
        { id: 1, name: 'Users' },
        { id: 2, name: 'Movies' }
    ];

    $scope.selectedRecommendation = loadRecommendarionType();

    function loadNeighborhood(){
		if(localStorageService.get('size')!==null){
    		return parseInt(localStorageService.get('size'));
    	}else{
    		return 100;
    	}
    }

    $scope.neighborhood = loadNeighborhood();

    function loadRecomms(){
    	if(localStorageService.get('n')!==null){
    		return parseInt(localStorageService.get('n'));
    	}else{
    		return 10;
    	}
    }

    $scope.nRecomendations = loadRecomms();

    $scope.back = function(){
    	localStorageService.set('model', $scope.selectedAlgorithm);
    	localStorageService.set('recommendationType', $scope.selectedRecommendation);
    	localStorageService.set('size', $scope.neighborhood);
    	localStorageService.set('n', $scope.nRecomendations);
    	localStorageService.remove('userMovies');
    	$state.go('user-home');
    };
}]);