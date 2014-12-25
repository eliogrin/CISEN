(function () {
    'use strict';
    var globalUser = {};
//    var globalUserId = "";
    var login;
    var app = angular.module('cisenControllers', []);

    app.controller('LoginController',
        [ 'SERVER_URL', '$http', '$scope', '$location', '$rootScope',
            function (SERVER_URL, $http, $scope, $location, $rootScope) {
                login = $scope.login = function (user) {
                    globalUser = user;

                    var $promise = $scope.getUsers(user);

                    $promise.success(function (data) {
                        if (data != null && data.name === user.name) {
                            $rootScope.jobs = data.jobs;
                            globalUser = data;
//                            var jobs = data.jobs;
//                            globalUser.jobs = [];
//                            for (var i = 0; i < jobs.length; i++) {
//                                globalUser.jobs[i] = jobs[i];
//                            }
//                            globalUserId = data._id;
                            $location.path('/home');
                        } else {
                            var newUser = {
                                "name": user.name,
                                "jobs": []
                            };
                            $http({
                                method: 'POST',
                                url: SERVER_URL + '/services/users',
                                data: JSON.stringify(newUser)
                            }).success(function (data, status, headers, config) {
                                $scope.login(user);
                            });

                        }
                    }).error(function (data) {
                        $location.path('/login');
                    });
                };

                $scope.getUsers = function (user) {
                    return $http({
                        method: 'GET',
                        url: SERVER_URL + '/services/users',
                        params: {
//                         query: {
                            name: user.name
//                         }
                        }
                    });
                };

                $scope.resetForm = function () {
                    $scope.user = {};
                };

                $scope.isResetForm = function (user) {
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
            function (SERVER_URL, $http, $scope, $location, $rootScope) {
                var jobs = $rootScope.jobs;
                //
                $scope.jobsMessage = jobs.length > 0;

                $scope.getCi = function (job, counter) {
                    var localCounter = counter;
                    $http.get(SERVER_URL + '/services/config/ci/' + job.ci).success(
                        function (data, status, headers, config) {
                            $scope.jobs[localCounter].ci = data;
                        });
                };

                $scope.getMessenger = function (job, counter) {
                    var localCounter = counter;
                    $http.get(SERVER_URL + '/services/config/messenger/' + job.messenger)
                        .success(function (data, status, headers, config) {
                            $scope.jobs[localCounter].messenger = data;
                        });
                };

                $scope.getProcessor = function (job, counter) {
                    var localCounter = counter;
                    $http.get(SERVER_URL + '/services/config/processor/' + job.processor)
                        .success(function (data, status, headers, config) {
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
                $scope.addNewJob = function () {
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
            function (SERVER_URL, $http, $scope, $location, $rootScope) {
//                $scope.job_ci = {};
//                $scope.ciSelectedKeys = {};
//                $scope.job_processor = {};
//                $scope.processorSelectedKeys = {};
//                $scope.job_messenger = {};
//                $scope.messengerSelectedKeys = {};


                $scope.ciChanged = function (selectedCI) {
                    var changed = $scope.getChanged(selectedCI);
                    $scope.ciSelectedKeys = changed[0];
                    $scope.job_ci = changed[1];
                };

                $scope.processorChanged = function (selectedProcessor) {
                    var changed = $scope.getChanged(selectedProcessor);
                    $scope.processorSelectedKeys = changed[0];
                    $scope.job_processor = changed[1];
                };

                $scope.messengerChanged = function (selectedMessenger) {
                    var changed = $scope.getChanged(selectedMessenger);
                    $scope.messengerSelectedKeys = changed[0];
                    $scope.job_messenger = changed[1];
                };

                $scope.getChanged = function (selected) {
                    if(selected === []){
                        selected = {};
                    }
                    var selectedKeys = Object.keys(selected);

                    selectedKeys.splice(selectedKeys.indexOf('_id'), 1);
                    selectedKeys.splice(selectedKeys.indexOf('type'), 1);
                    selectedKeys.splice(selectedKeys.indexOf('description'), 1);
                    selectedKeys.splice(selectedKeys.indexOf('baseType'), 1);

                    var obj = angular.copy(selected);
                    for (var ci in obj) {
                        obj[ci] = "";
                    }
                    return [selectedKeys, obj];
                };

                $scope.fillTemplateConfigs = function () {
                    $http.get(SERVER_URL + '/services/plugins/cis/').success(
                        function (data, status, headers, config) {
                            $scope.ciTemplates = data;
                            if(data.length > 0) {
                                $scope.ciSelected = data[0];
                                $scope.ciChanged($scope.ciSelected);
                            }
                        });
                    $http.get(SERVER_URL + '/services/plugins/messengers/').success(
                        function (data, status, headers, config) {
                            $scope.messengerTemplates = data;
                            if(data.length > 0) {
                                $scope.messengerSelected = data[0];
                                $scope.messengerChanged($scope.messengerSelected);
                            }
                        });
                    $http.get(SERVER_URL + '/services/plugins/processors/').success(
                        function (data, status, headers, config) {
                            $scope.processorsTemplates = data;
                            if(data.length > 0) {
                                $scope.processorsSelected = data[0];
                                $scope.processorChanged($scope.processorsSelected);
                            }
                        });
                };

                $scope.saveCiTemplateConfigs = function (data) {
                    $http.post(SERVER_URL + '/services/plugins/cis/', data);
                };
                $scope.saveProcessorTemplateConfigs = function (data) {
                    $http.post(SERVER_URL + '/services/plugins/processors/', data);
                };
                $scope.saveMessengerTemplateConfigs = function (data) {
                    $http.post(SERVER_URL + '/services/plugins/messengers/', data);
                };

                $scope.fillTemplateConfigs();
                $scope.tab = 1;

                $scope.setTab = function (newValue) {
                    this.tab = newValue;
                };

                $scope.isSet = function (tabName) {
                    return this.tab === tabName;
                };

                $scope.saveConfig = function () {
                    globalUser.jobs.push({
                        "name": "Test job" + globalUser.jobs.length,
                        "ci": "547abc4f1b89357b41d94682",
                        "messenger": "547abc4e1b89357b41d9467c",
                        "processor": "547ac53b686fc984090db7e0"
                    });

                    $http({
                        method: 'PUT',
                        url: SERVER_URL + '/services/users',
                        data: JSON.stringify(globalUser)
                    }).success(function (data, status, headers, config) {
                        login(globalUser);
                    });

//                    $http.put(SERVER_URL + '/services/users/' + globalUserId, globalUser).success(
//                        function (data, status, headers, config) {
//
//                            login(globalUser);
//                        });
                };
            } ]);

})();
