'use strict';

angular.module('milestogo')
    .controller('MainCtrl', function ($scope, ActivityService) {
        $scope.currentUser = JSON.parse(localStorage['user']);

        ActivityService.timeline($scope.currentUser.username).success(function (data, status, headers, config) {
            $scope.activities = data;
        }).error(function (data, status, headers, config) {
            toastr.error("Unable to fetch timeline. Please try after sometime.");
        });

        $scope.delete = function (idx) {
            var activityToDelete = $scope.activities[idx];
            ActivityService.deleteActivity($scope.currentUser.username, activityToDelete.id).success(function (data, status) {
                toastr.success("Deleted activity");
                $scope.activities.splice(idx, 1);
            }).error(function () {
                toastr.error("Unable to delete activity. Please try later.");
            });
        };

    });
