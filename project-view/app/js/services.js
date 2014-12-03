(function() {
    'use strict';

    var app = angular.module('cisenServices', ['ngResource']);

    //app.factory('Users', function($http) {
    //    return {
    //        login: function(user) {
    //            // Send data to user.json
    //            var $promise = $http.get('data/user.json', user);
    //
    //            $promise.then(function(msg) {
    //                if (msg.data === 'success') {
    //                    console.log('success login');
    //                } else {
    //                    console.log('error login');
    //                }
    //            });
    //        }
    //    };
    //});

    //app.factory('User', ['SERVER_URL', '$resource', function($resource) {
    //    return $resource(SERVER_URL + '/users', {}, {
    //        query: {
    //            method:'GET',
    //            params: {
    //                name: user.name
    //            },
    //            isArray: true
    //         }
    //    });
    //}]);

    //factory("User", function($resource) {
    //    return $resource("users/:userId.json", {}, {
    //        query: {method: "GET", params: {userId: "users"}, isArray: true}
    //    });
    //});

    //$http({
    //    method: 'GET',
    //    url: SERVER_URL + '/users',
    //    params: {
    //        query: {
    //            name: user.name
    //        }
    //    }
    //});
    app.factory('Jobs', function(data) {
        var jobs = {};

        return {
            set: function(data) {
                jobs = data;
            },
            get: function() {
                return jobs;
            }
        };
    });
})();
