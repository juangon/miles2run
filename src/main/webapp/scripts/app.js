'use strict';

angular.module('milestogo', [
        'ngCookies',
        'ngResource',
        'ngSanitize',
        'ngRoute',
        'ui.bootstrap'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when('/activity/post', {
                templateUrl: 'views/postactivity.html',
                controller: 'PostActivityCtrl'
            })
            .when('/progress', {
                templateUrl: 'views/progress.html',
                controller: 'ProgressCtrl'
            })
            .when('/notifications', {
                templateUrl: 'views/notifications.html',
                controller: 'NotificationsCtrl'
            })
            .when('/friends', {
                templateUrl: 'views/friends.html',
                controller: 'FriendsCtrl'
            })
            .when('/activity/edit/:activityId', {
                templateUrl: 'views/EditActivity.html',
                controller: 'EditActivityCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    }).run(function ($rootScope, ProfileService, $route, $location) {
        ProfileService.me().success(function (data, status, headers, config) {
            console.log("User cached..");
            localStorage['user'] = JSON.stringify(data);
        }).error(function (data, status, headers, config) {
            toastr.error("Looks like you are not signed in. Please signin using twitter");
        });
    });

function HeaderCtrl($scope, $location) {

    $scope.isActive = function (viewLocation) {
        return viewLocation === $location.path();
    };

}