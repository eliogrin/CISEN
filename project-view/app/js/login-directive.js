(function() {
    'use strict';

    var app = angular.module('cisenLoginDirective', []);

    app.directive('loginDirective', function() {
        return {
            templateUrl: 'views/tpl/login.tpl.html'
        };
    });
})();