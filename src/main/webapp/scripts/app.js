'use strict';

var app = angular.module('milestogo', [
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
            .when('/activity/share/:activityId', {
                templateUrl: 'views/share.html',
                controller: 'ShareActivityCtrl'
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
    });

app.config(['$provide', function ($provide) {
    var profile = angular.copy(window.activeUserProfile);
    $provide.constant('activeProfile', profile);
}]);

function HeaderCtrl($scope, $location) {

    $scope.isActive = function (viewLocation) {
        return viewLocation === $location.path();
    };

}