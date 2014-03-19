'use strict';

angular.module('milestogo')
    .controller('ProgressCtrl', function ($scope, ProgressService, ConfigService) {
        $scope.currentUser = JSON.parse(localStorage['user']);
        $scope.error = null;
        $scope.data = {};

        ProgressService.progress($scope.currentUser.username).success(function (data, status, headers, config) {
            $scope.error = null;
            $scope.status = status;
            $scope.data = data;
            $scope.style = "width:" + data.percentage + "%";
        }).
            error(function (data, status, headers, config) {
                toastr.error("Unable to fetch your progress. Please try later.");
                $scope.error = {message: "Unable to fetch your progress. Please try later."};
                console.log(data);
                console.log(status);
            });

        function startDate() {
            var d = new Date();
            d.setMonth(d.getMonth() - 2);
            return d;
        }

        $scope.config = {
            start: startDate(),
            data: ConfigService.getBaseUrl() + "profiles/" + $scope.currentUser.username + "/progress/timeline",
            itemName: $scope.currentUser.goalUnit.toString().toLowerCase().replace("s", "")
        };


    });