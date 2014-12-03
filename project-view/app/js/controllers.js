(function() {
    'use strict';
    var globalUser = {};
    var globalUserId = "";
    var login;
    var app = angular.module('cisenControllers', []);

    app.controller('LoginController',
        [ 'SERVER_URL', '$http', '$scope', '$location', '$rootScope',
         function(SERVER_URL, $http, $scope, $location, $rootScope) {
             login = $scope.login = function(user) {
                 globalUser = user;

                 var $promise = $scope.getUsers(user);

                 $promise.success(function(data) {
                     if (data.length > 0 && data[0].name === user.name) {
                         $rootScope.jobs = data[0].jobs;
                         var jobs = data[0].jobs;
                         globalUser.jobs = [];
                         for (var i = 0; i < jobs.length; i++) {
                             globalUser.jobs[i] = jobs[i];
                         }
                         globalUserId = data[0]._id;
                         $location.path('/home');
                     } else {
                         var newUser = {
                             "name": user.name,
                             "jobs": []
                         };
                         $http({
                             method: 'POST',
                             url: SERVER_URL + '/users',
                             data: JSON.stringify(newUser)
                         }).success(function(data, status, headers, config) {
                             $scope.login(user);
                         });

                     }
                 }).error(function(data) {
                     $location.path('/login');
                 });
             };

             $scope.getUsers = function(user) {
                 return $http({
                     method: 'GET',
                     url: SERVER_URL + '/users',
                     params: {
                         query: {
                             name: user.name
                         }
                     }
                 });
             };

             $scope.resetForm = function() {
                 $scope.user = {};
             };

             $scope.isResetForm = function(user) {
                 return !user.email;
             };
         } ]);

    app.controller('HomeController',
        [
         'SERVER_URL',
         '$http',
         '$scope',
         '$location',
         '$rootScope',
         function(SERVER_URL, $http, $scope, $location, $rootScope) {
             var jobs = $rootScope.jobs;
             //
             $scope.jobsMessage = jobs.length > 0;

             $scope.getCi = function(job, counter) {
                 var localCounter = counter;
                 $http.get(SERVER_URL + '/configs_ci/' + job.ci).success(
                     function(data, status, headers, config) {
                         $scope.jobs[localCounter].ci = data;
                     });
             };

             $scope.getMessenger = function(job, counter) {
                 var localCounter = counter;
                 $http.get(SERVER_URL + '/configs_messenger/' + job.messenger)
                     .success(function(data, status, headers, config) {
                         $scope.jobs[localCounter].messenger = data;
                     });
             };

             $scope.getProcessor = function(job, counter) {
                 var localCounter = counter;
                 $http.get(SERVER_URL + '/configs_processor/' + job.processor)
                     .success(function(data, status, headers, config) {
                         $scope.jobs[localCounter].processor = data;
                     });
             };

             for (var i = 0; i < jobs.length; i++) {
                 var job = {};
                 job = jobs[i];
                 $scope.jobs[i] = {
                     name: job.name,
                     ci: {},
                     messenger: {},
                     processor: {}
                 };
                 $scope.getCi(job, i);
                 $scope.getMessenger(job, i);
                 $scope.getProcessor(job, i);
             }
             $scope.addNewJob = function() {
                 $location.path('/config');
             };
         } ]);

    app.controller('ConfigController',
        [
         'SERVER_URL',
         '$http',
         '$scope',
         '$location',
         '$rootScope',
         function(SERVER_URL, $http, $scope, $location, $rootScope) {
             $scope.fillTemplateConfigs = function() {
                 $http.get(SERVER_URL + '/plugins_ci/').success(
                     function(data, status, headers, config) {
                         $scope.ciTemplates = data;
                         $scope.ciSelected = data[0];
                     });
                 $http.get(SERVER_URL + '/plugins_messengers/').success(
                     function(data, status, headers, config) {
                         $scope.messengerTemplates = data;
                         $scope.messengerSelected = data[0];
                     });
                 $http.get(SERVER_URL + '/plugins_processors/').success(
                     function(data, status, headers, config) {
                         $scope.processorsTemplates = data;
                         $scope.processorsSelected = data[0];
                     });
             };

             $scope.saveCiTemplateConfigs = function(data) {
                 $http.post(SERVER_URL + '/plugins_ci/', data);
             };
             $scope.saveProcessorTemplateConfigs = function(data) {
                 $http.post(SERVER_URL + '/plugins_processors/', data);
             };
             $scope.saveMessengerTemplateConfigs = function(data) {
                 $http.post(SERVER_URL + '/plugins_messengers/', data);
             };

             $scope.fillTemplateConfigs();
             $scope.tab = 1;

             $scope.setTab = function(newValue) {
                 this.tab = newValue;
             };

             $scope.isSet = function(tabName) {
                 return this.tab === tabName;
             };

             $scope.saveConfig = function() {
                 globalUser.jobs.push({
                     "name": "Test job" + globalUser.jobs.length,
                     "ci": "547abc4f1b89357b41d94682",
                     "messenger": "547abc4e1b89357b41d9467c",
                     "processor": "547ac53b686fc984090db7e0"
                 });
                 $http.put(SERVER_URL + '/users/' + globalUserId, globalUser).success(
                     function(data, status, headers, config) {

                         login(globalUser);
                     });
             };
         } ]);

})();
