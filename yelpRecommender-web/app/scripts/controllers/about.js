'use strict';

/**
 * @ngdoc function
 * @name yelpRecommenderWebApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the yelpRecommenderWebApp
 */
angular.module('yelpRecommenderWebApp')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
