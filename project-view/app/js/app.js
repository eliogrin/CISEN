(function () {
    'use strict';

    // Application Module
    var app = angular.module('cisen', [
        'ngRoute',
        'cisenControllers',
        //'cisenServices'
    ]).constant('SERVER_URL', 'http://epuadniw0048.kyiv.epam.com:3000/cisen');

    app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/login', {
            templateUrl: 'views/login.html',
            controller: 'LoginController'
        })
        .when('/home', {
            templateUrl: 'views/home.html',
            controller: 'HomeController'
        })
        .when('/config', {
            templateUrl: 'views/config.html',
            controller: 'ConfigController'
        })
        .otherwise({
            redirectTo: '/login'
        });
    }]);

})();