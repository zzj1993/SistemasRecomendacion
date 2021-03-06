'use strict';

/**
 * @ngdoc function
 * @name yelpRecommenderWebApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the yelpRecommenderWebApp
 */
 angular.module('yelpRecommenderWebApp').controller('RecommendationCtrl', function ($scope, $modal, NeighborhoodService, UserService, RecommendationService, UserReviewsService, DeleteReviewsService, AddReviewsService, SearchService) {

 	var today = new Date();

 	$scope.days = [{name: 'Sunday', value: 1}, {name: 'Monday', value: 2}, {name: 'Tuesday', value: 2}, {name: 'Wednesday', value: 3}, 
 	{name: 'Thursday', value: 3}, {name: 'Friday', value: 3}, {name: 'Saturday', value: 1}];
 	$scope.selectedDay = $scope.days[today.getDay()];

 	$scope.time = [{name: '12 AM', value: 0}, {name: '1 AM', value: 0}, {name: '2 AM', value: 0}, {name: '3 AM', value: 0},
 	{name: '4 AM', value: 0}, {name: '5 AM', value: 0}, {name: '6 AM', value: 6}, {name: '7 AM', value: 6},
 	{name: '8 AM', value: 6}, {name: '9 AM', value: 6}, {name: '10 AM', value: 10}, {name: '11 AM', value: 10},
 	{name: '12 PM', value: 10}, {name: '1 PM', value: 10}, {name: '2 PM', value: 10}, {name: '3 PM', value: 10},
 	{name: '4 PM', value: 16}, {name: '5 PM', value: 16}, {name: '6 PM', value: 16}, {name: '7 PM', value: 16},
 	{name: '8 PM', value: 20}, {name: '9 PM', value: 20}, {name: '10 PM', value: 20}, {name: '11 PM', value: 20}];
 	$scope.selectedTime = $scope.time[today.getHours()];

 	$scope.collaborativeRecommendations = [];
 	$scope.itemRecommendations = [];
 	$scope.neighborhoodRecommendations = [];
 	$scope.dayTimeRecommendations = [];
 	$scope.hybridRecommendations = [];
 	$scope.textRecommendations = [];
	$scope.contentRecommendations = [];
 	$scope.userReviews = [];
 	$scope.searchQuery = '';

 	$scope.dataRecommendationExists = function(){
 		return $scope.collaborativeRecommendations.length > 0 || 
 		$scope.itemRecommendations.length > 0 || 
 		$scope.neighborhoodRecommendations.length > 0 ||
 		$scope.dayTimeRecommendations.length > 0 || 
 		$scope.textRecommendations.length > 0 ||
 		$scope.hybridRecommendations.length > 0||$scope.contentRecommendations.length >0;
 	};

 	function onError(data) {
 		console.error('Error: ' + JSON.stringify(data));
 	}

 	function onSuccessNeighborhoods(data){
 		$scope.neighborhoods = data;
 		$scope.selectedNeighborhood = $scope.neighborhoods[Math.floor(Math.random()*$scope.neighborhoods.length)];
 	}

 	function onSuccessUsers(data){
 		$scope.users = data;
 		$scope.selectedUser = $scope.users[Math.floor(Math.random()*$scope.users.length)];
 	}

 	function onSuccessCollaborative(data){
 		$scope.collaborativeRecommendations = data;
 	}

 	function onSuccessItem(data){
 		$scope.itemRecommendations = data;
 	}

 	function onSuccessNeighborhood(data){
 		$scope.neighborhoodRecommendations = data;
 	}

 	function onSuccessDayTime(data){
 		$scope.dayTimeRecommendations = data;
 	}

 	function onSuccessHybrid(data){
 		$scope.hybridRecommendations = data;
 	}

 	function onSuccessText(data){
 		$scope.textRecommendations = data;
 	}
	
	function onSuccessContent(data){
		$scope.contentRecommendations = data;
	}

 	function onSuccessUser(data){
		$scope.userReviews = data;
 	}

 	$scope.loadData = function(){
 		NeighborhoodService.getNeighborhoods(onSuccessNeighborhoods, onError);
 		UserService.getUsers(onSuccessUsers, onError);
 	};

 	$scope.getRecommendations = function(selectedUser, selectedNeighborhood, selectedDay, selectedTime, searchQuery){
 		$scope.selectedUser = selectedUser;
 		$scope.selectedNeighborhood = selectedNeighborhood;
 		$scope.selectedDay = selectedDay;
 		$scope.selectedTime = selectedTime;
 		$scope.searchQuery = searchQuery;
 		var param = {
 			name: 'Collaborative Recommender', 
 			userId: selectedUser.id, 
 			neighborhood: selectedNeighborhood.name, 
 			day: selectedDay.value, 
 			time: selectedTime.value, 
 			text: searchQuery
 		};
 		RecommendationService.getRecommendations(param, onSuccessCollaborative, onError);
 		param.name = 'Item Recommender';
 		RecommendationService.getRecommendations(param, onSuccessItem, onError);
 		param.name = 'Neighborhood Recommender';
 		RecommendationService.getRecommendations(param, onSuccessNeighborhood, onError);
 		param.name = 'Day Time Recommender';
 		RecommendationService.getRecommendations(param, onSuccessDayTime, onError);
 		param.name = 'Hybrid Recommender';
 		RecommendationService.getRecommendations(param, onSuccessHybrid, onError);
 		param.name = 'Text Recommender';
 		RecommendationService.getRecommendations(param, onSuccessText, onError);
		param.name = 'Content Recommender';
 		RecommendationService.getRecommendations(param, onSuccessContent, onError);
 		UserReviewsService.getUserReviews({userId: selectedUser.id}, onSuccessUser, onError);
 	};

 	$scope.open = function (recommendation) {
 		var modalInstance = $modal.open({
 			templateUrl: 'myModalContent.html',
 			controller: 'ModalInstanceCtrl',
 			resolve: {
 				recommendation: function () {
 					return recommendation;
 				}
 			}
 		});
 	};

 	function onSuccessDelete(){
 		UserReviewsService.getUserReviews({userId: $scope.selectedUser.id}, onSuccessUser, onError);
 	}

 	$scope.delete = function(review){
 		DeleteReviewsService.deleteReview({userId: $scope.selectedUser.id, businessId: review.businessId, businessName: review.businessName, 
 			stars: review.stars, itemStars: review.itemStars, collaborativeStars: review.collaborativeStars}, onSuccessDelete, onError);
 	};

 	function onSuccessAddReview(){
 		var param = {
 			name: 'Collaborative Recommender', 
 			userId: $scope.selectedUser.id, 
 			neighborhood: $scope.selectedNeighborhood.name, 
 			day: $scope.selectedDay.value, 
 			time: $scope.selectedTime.value, 
 			text: ''
 		};
 		RecommendationService.getRecommendations(param, onSuccessCollaborative, onError);
 		param.name = 'Item Recommender';
 		RecommendationService.getRecommendations(param, onSuccessItem, onError);
 		param.name = 'Neighborhood Recommender';
 		RecommendationService.getRecommendations(param, onSuccessNeighborhood, onError);
 		param.name = 'Day Time Recommender';
 		RecommendationService.getRecommendations(param, onSuccessDayTime, onError);
 		UserReviewsService.getUserReviews({userId: $scope.selectedUser.id}, onSuccessUser, onError);
 	}

 	$scope.addReview = function(recommendation){
 		AddReviewsService.addReview({businessId: recommendation.businessId, userId: recommendation.userId, 
 			stars: recommendation.stars, computedStars: 0, itemStars: 0}, onSuccessAddReview, onError);
 	};

 	$scope.search = function(searchQuery){
 		$scope.searchQuery = searchQuery;
 		SearchService.search({text: searchQuery});
 	};

 }).controller('ModalInstanceCtrl', function ($scope, $modalInstance, recommendation) {
 	$scope.recommendation = recommendation;
 	$scope.ok = function () {
 		$modalInstance.close();
 	};
 });