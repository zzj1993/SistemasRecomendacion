'use strict';

/**
 * @ngdoc function
 * @name yelpRecommenderWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the yelpRecommenderWebApp
 */
 angular.module('yelpRecommenderWebApp').controller('EvaluationCtrl', function ($scope, $window, EvaluationService, ConfigurationService) {
 	
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

 	$scope.cfCount = 0;

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

 	$scope.cfDatasetSize = $scope.datasetSize[$scope.datasetSize.length-1];

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

    function onSuccessCF(data){
    	$scope.loadData();
    }

    $scope.updateCF = function(cfDatasetSize){
    	$scope.cfDatasetSize = cfDatasetSize;
    	ConfigurationService.updateRecommender({name: 'Collaborative Recommender', size: cfDatasetSize.value}, onSuccessCF, onError);
    };
});