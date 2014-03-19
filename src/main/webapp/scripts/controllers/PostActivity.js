'use strict';

function PostActivityCtrl($scope, ActivityService, $location, ProfileService) {

    $scope.currentUser = JSON.parse(localStorage['user']);
    $scope.activity = {
        goalUnit: $scope.currentUser.goalUnit,
        share: {}
    };

    $scope.postActivity = function () {
        console.log($scope.activity);
        ActivityService.postActivity($scope.currentUser.username, $scope.activity).success(function (data, status, headers, config) {
            toastr.success("Posted new activity");
            $location.path('/');
        }).error(function (data, status, headers, config) {
            console.log("Error handler for PostActivity. Status code " + status);
            toastr.error("Unable to post activity. Please try later.");
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
}
angular.module('milestogo')
    .controller('PostActivityCtrl', PostActivityCtrl);