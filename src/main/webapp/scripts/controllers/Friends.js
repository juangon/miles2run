'use strict';

angular.module('milestogo')
    .controller('FriendsCtrl', function ($scope, $http, $window) {
        $scope.selected = undefined;

        // Any function returning a promise object can be used to load values asynchronously
        $scope.getProfiles = function (val) {
            return $http.get('api/v2/profiles', {
                params: {
                    name: val,
                    sensor: false
                }
            }).then(function (res) {
                var profiles = res.data;
                return profiles;
            });
        };

        $scope.fetchProfile = function () {
            console.log($scope.profile);
            $window.location.href = "profiles/" + $scope.profile.username;
        }

    });
