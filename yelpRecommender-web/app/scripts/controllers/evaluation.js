'use strict';

/**
 * @ngdoc function
 * @name yelpRecommenderWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the yelpRecommenderWebApp
 */
 angular.module('yelpRecommenderWebApp').controller('EvaluationCtrl', function ($scope, $interval, $window, EvaluationService, ConfigurationService) {
 	
	var rmse = {
    	bindto: '#rmse',
    	size: {
    		height: 200
    	},
    	data: {
    		columns: [],
    		type: 'bar'
    	},
    	bar: {
    		width: {
    			ratio: 0.5
    		}
    	}
    };

    var mae = {
    	bindto: '#mae',
    	size: {
    		height: 200
   		},
    	data: {
    		columns: [],
    		type: 'bar'
    	},
    	bar: {
    		width: {
    			ratio: 0.5
    		}
    	}
    };

    var precision = {
    	bindto: '#precision',
    	size: {
    		height: 200
    	},
    	data: {
    		columns: [],
    		type: 'bar'
    	},
    	bar: {
    		width: {
    			ratio: 0.5
    		}
    	}
    };

    var recall = {
    	bindto: '#recall',
    	size: {
    		height: 200
    	},
    	data: {
    		columns: [],
    		type: 'bar'
    	},
    	bar: {
    		width: {
    			ratio: 0.5
    		}
    	}
    };

    var recommendation = {
    	bindto: '#recommendation',
    	size: {
    		height: 200
    	},
    	data: {
    		columns: [],
    		type: 'bar'
    	},
    	bar: {
    		width: {
    			ratio: 0.5
    		}
    	}
    };

    var training = {
    	bindto: '#training',
    	size: {
    		height: 200
    	},
    	data: {
    		columns: [],
    		type: 'bar'
    	},
    	bar: {
    		width: {
    			ratio: 0.5
    		}
    	}
    };

    var stopCF, stopItem;

 	$scope.cfCount = 0;
    $scope.itemCount = 0;
    $scope.cfProgress = 0;
    $scope.itemProgress = 0;

 	$scope.datasetSize = [
 		{name: '10%', value: '10'},
 		{name: '20%', value: '20'},
 		{name: '30%', value: '30'},
 		{name: '40%', value: '40'},
 		{name: '50%', value: '50'},
 		{name: '60%', value: '60'},
 		{name: '70%', value: '70'},
 		{name: '80%', value: '80'},
 		{name: '90%', value: '90'},
 		{name: '100%', value: '100'}
 	];

    $scope.algorithms = [
        { name: 'Jaccard Distance' },
        { name: 'Cosine Distance' },
        { name: 'Pearson Distance' },
        { name: 'Euclidean Distance' }
    ];

 	$scope.cfDatasetSize = $scope.datasetSize[$scope.datasetSize.length-1];
    $scope.itemDatasetSize = $scope.datasetSize[$scope.datasetSize.length-1];
    $scope.selectedAlgorithm =  $scope.algorithms[2];

 	function onError(data) {
 		console.error('Error: ' + JSON.stringify(data));
    }

    function onSuccess(data) {
    	//console.debug(JSON.stringify(data));
    	var rmseData = [];
    	var maeData = [];
    	var trainingData = [];
    	var precisionData = [];
    	var recallData = [];
    	var recommendationData = [];

    	for(var i = 0 ; i < data.length ; i++){
    		var name = data[i].name;
    		if(name === 'Collaborative Recommender'){
				$scope.cfCount = data[i].size;
    		} else if(name === 'Item Recommender'){
                $scope.itemCount = data[i].size;
            }
    		rmseData[i] = [name, data[i].rmse];
    		maeData[i] = [name, data[i].mae];
    		precisionData[i] = [name, data[i].precision];
    		recallData[i] = [name, data[i].recall];
    		trainingData[i] = [name, data[i].trainingTime];
    		recommendationData[i] = [name, data[i].recommendationTime];
    	}
    	rmse.data.columns = rmseData;
    	$scope.rmse = c3.generate(rmse);

    	mae.data.columns = maeData;
    	$scope.mae = c3.generate(mae);

    	precision.data.columns = precisionData;
    	$scope.precision = c3.generate(precision);

    	recall.data.columns = recallData;
    	$scope.recall = c3.generate(recall);

    	training.data.columns = trainingData;
    	$scope.training = c3.generate(training);

    	recommendation.data.columns = recommendationData;
    	$scope.recommendation = c3.generate(recommendation);
    }

    $scope.loadData = function(){
    	EvaluationService.getStatistics(onSuccess, onError);
    };

    function onSuccessR(){
    	$scope.loadData();
    }

    $scope.updateCF = function(cfDatasetSize){
        if(angular.isDefined(stopCF)) return;
    	$scope.cfDatasetSize = cfDatasetSize;
        progressCF();
    	ConfigurationService.updateRecommender({name: 'Collaborative Recommender', size: cfDatasetSize.value, correlation: $scope.selectedAlgorithm.name}, onSuccessR, onError);
    };

    $scope.updateItem = function(itemDatasetSize, selectedAlgorithm){
        if(angular.isDefined(stopItem)) return;
        $scope.itemDatasetSize = itemDatasetSize;
        $scope.selectedAlgorithm = selectedAlgorithm;
        progressItem();
        ConfigurationService.updateRecommender({name: 'Item Recommender', size: itemDatasetSize.value, correlation: selectedAlgorithm.name}, onSuccessR, onError);
    };

    function onSuccessProgressItem(data){
        $scope.itemProgress = data.trainingProgress;
    }

    function progressItem(){
        stopItem = $interval(function(){
            ConfigurationService.getTrainingProgress({name: 'Item Recommender'}, onSuccessProgressItem, onError);
            if($scope.itemProgress  === 100){
                cancelProgressItem();
            }
        }, 1000);
    }

    function cancelProgressItem(){
        $interval.cancel(stopItem);
        stopItem = undefined;
    }

    function onSuccessProgressCF(data){
        $scope.cfProgress = data.trainingProgress;
    }

    function progressCF(){
        stopCF = $interval(function(){
            ConfigurationService.getTrainingProgress({name: 'Collaborative Recommender'}, onSuccessProgressCF, onError);
            if($scope.cfProgress  === 100){
                cancelProgressCF();
            }
        }, 1000);
    }

    function cancelProgressCF(){
        $interval.cancel(stopCF);
        stopCF = undefined;
    }
});