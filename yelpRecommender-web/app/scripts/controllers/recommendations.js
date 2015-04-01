'use strict';

/**
 * @ngdoc function
 * @name yelpRecommenderWebApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the yelpRecommenderWebApp
 */
angular.module('yelpRecommenderWebApp').controller('RecommendationCtrl', function ($scope) {

	$scope.users = [{name: 'Juan', id: 'asdasdasdasdasd'}, {name: 'Pedro', id: 'asdasdasdasdasd'}]
	$scope.selectedUser;

	$scope.neighborhoods = [{name: 'barrio 1'}, {name: 'barrio 2'}];
	$scope.selectedNeighborhood;

	$scope.days = [{name: 'Monday', value: 2}, {name: 'Tuesday', value: 2}, {name: 'Wednesday', value: 3}, 
		{name: 'Thursday', value: 3}, {name: 'Friday', value: 3}, {name: 'Saturday', value: 1}, {name: 'Sunday', value: 1}];
	$scope.selectedDay;

	$scope.time = [{name: '12 AM', value: 0}, {name: '1 AM', value: 0}, {name: '2 AM', value: 0}, {name: '3 AM', value: 0},
		{name: '4 AM', value: 0}, {name: '5 AM', value: 0}, {name: '6 AM', value: 6}, {name: '7 AM', value: 6},
		{name: '8 AM', value: 6}, {name: '9 AM', value: 6}, {name: '10 AM', value: 10}, {name: '11 AM', value: 10},
		{name: '12 PM', value: 10}, {name: '1 PM', value: 10}, {name: '2 PM', value: 10}, {name: '3 PM', value: 10},
		{name: '4 PM', value: 16}, {name: '5 PM', value: 16}, {name: '6 PM', value: 16}, {name: '7 PM', value: 16},
		{name: '8 PM', value: 20}, {name: '9 PM', value: 20}, {name: '10 PM', value: 20}, {name: '11 PM', value: 20}];
	$scope.selectedTime;

	

});