'use strict';

angular.module('recomendadorPeliculasWebApp').factory('HttpHeadersIntercetor', ['localStorageService', function(localStorageService, $rootScope, $q, $window) {
    return {
        request: function (config) {
            config.headers = config.headers || {};
            var tokenValue = localStorageService.get('Token');
            if (tokenValue) {
                config.headers.Authorization = tokenValue;
            }
            return config;
        }
    };
}]);