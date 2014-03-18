'use strict';

angular.module('milestogo')
    .controller('EditActivityCtrl', function ($scope, $routeParams, ActivityService, $location) {
        $scope.currentUser = JSON.parse(localStorage['user']);

        var activityId = $routeParams.activityId;
        ActivityService.get($scope.currentUser.username, activityId).success(function (data) {
            $scope.activityDetails = data;
        }).error(function (data) {
            toastr.error("Unable to fetch activity with id: " + activityId);
            $location.path('/');

        });

        $scope.update = function () {
            var activity = {
                id: $scope.activityDetails.id,
                status: $scope.activityDetails.status,
                goalUnit: $scope.activityDetails.goalUnit,
                distanceCovered: $scope.activityDetails.distanceCovered,
                share: $scope.activityDetails.share,
                activityDate: $scope.activityDetails.activityDate
            };

            ActivityService.updateActivity($scope.currentUser.username, activityId, activity).success(function (data, status, headers, config) {
                toastr.success("Updated new activity");
                $location.path('/');
            }).error(function (data, status, headers, config) {
                console.log("Error handler for update activity. Status code " + status);
                toastr.error("Unable to update activity. Please try later.");
                $location.path('/');
            });
        };

        $scope.today = function () {
            $scope.dt = new Date();
        };
        $scope.today();

        $scope.showWeeks = true;
        $scope.toggleWeeks = function () {
            $scope.showWeeks = !$scope.showWeeks;
        };

        $scope.clear = function () {
            $scope.dt = null;
        };

        // Disable weekend selection
        $scope.disabled = function (date, mode) {
            return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
        };

        $scope.toggleMin = function () {
            $scope.minDate = ( $scope.minDate ) ? null : new Date();
        };
        $scope.toggleMin();

        $scope.open = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $scope.opened = true;
        };

        $scope.dateOptions = {
            'year-format': "'yy'",
            'starting-day': 1
        };

        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'shortDate'];
        $scope.format = $scope.formats[0];
    });
