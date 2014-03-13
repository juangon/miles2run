angular.module("milestogo.services", ["ngResource", "ui.bootstrap"]).
    factory('Activity',function ($resource) {
        var Activity = $resource('api/v1/activities/:activityId', {activityId: '@id'});
        Activity.prototype.isNew = function () {
            return (typeof(this.id) === 'undefined');
        };
        return Activity;
    }).factory('Profile',function ($resource) {
        var Profile = $resource('api/v1/profiles/me');
        Profile.prototype.isNew = function () {
            return (typeof(this.id) === 'undefined');
        };
        return Profile;
    }).factory('ProfileService', function (Profile) {
        return {
            currentUser: Profile.get()
        };
    });

angular.module("milestogo", ["milestogo.services"]).
    config(function ($routeProvider) {
        $routeProvider
            .when('/', {templateUrl: 'views/activity/list.html', controller: ActivityListController})
            .when('/activity/new', {templateUrl: 'views/activity/create.html', controller: ActivityCreateController})
            .when('/activity/progress', {templateUrl: 'views/activity/progress.html', controller: ActivityProgressController})
            .when('/notifications', {templateUrl: 'views/notifications/notifications.html', controller: NotificationController})
            .when('/profiles/friends', {templateUrl: 'views/profiles/friends.html', controller: FriendsController})
            .when('/activity/:activityId', {templateUrl: 'views/activity/detail.html', controller: ActivityDetailController})
            .when('/activity/edit/:activityId', {templateUrl: 'views/activity/edit.html', controller: ActivityDetailController})
            .otherwise({
                redirectTo: '/'
            });
    });

function ActivityListController($scope, Activity, ProfileService, $http) {
    $scope.providers = ProfileService.currentUser.providers;
    $scope.activities = Activity.query();

    $scope.delete = function (idx) {
        var activityToDelete = $scope.activities[idx];
        console.log("Activity to delete: " + activityToDelete);
        $http({method: 'DELETE', url: 'api/v1/activities/' + activityToDelete.id}).success(function (data, status) {
            toastr.success("Deleted activity");
            $scope.activities.splice(idx, 1);
        }).
            error(function (data, status) {
                console.log(data);
                console.log(status);
            })
    }

}

function ActivityCreateController($scope, $routeParams, $location, $http, Activity, ProfileService) {
    $scope.activity = new Activity();
    $scope.activity.share = {};
    $scope.todayDate = new Date();
    $scope.activity.goalUnit = "KMS";
    $scope.providers = ProfileService.currentUser.providers;
    $scope.save = function () {
        console.log($scope.activity);
        $scope.activity.$save(function (activity, headers) {
            toastr.success("Posted new activity");
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


function ActivityDetailController($scope, $routeParams, $location, Activity, $http) {
    var activityId = $routeParams.activityId;
    $scope.activityDetails = Activity.get({activityId: activityId});

    $scope.save = function () {

        console.log($scope.activityDetails);
        var activity = {
            id: $scope.activityDetails.id,
            status: $scope.activityDetails.status,
            goalUnit: $scope.activityDetails.goalUnit,
            distanceCovered: $scope.activityDetails.distanceCovered,
            share: $scope.activityDetails.share
        }

        $http({method: 'PUT', data: activity, url: 'api/v1/activities/' + $scope.activityDetails.id}).success(function (data, status) {
            toastr.success("Updated new activity");
            $location.path('/');
        }).
            error(function (data, status) {
                console.log(data);
                console.log(status);
            })

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


function ActivityProgressController($scope, $http) {

    $http({method: 'GET', url: 'api/v1/progress'}).success(function (data, status) {
        $scope.status = status;
        $scope.data = data;
        $scope.style = "width:" + data.percentage + "%";
    }).
        error(function (data, status) {
            console.log(data);
            console.log(status);
        });


        $("#cal-heatmap").empty();
        var cal = new CalHeatMap();
        cal.init({
            domain: "month",
            subDomain:"day",
            subDomainTextFormat: "%d",
            itemName:"km",
            data: "/api/v1/activities/list",
            start: new Date(2014,0),
            cellSize: 9,
            range: 3,
            cellSize: 25,
            domainGutter: 10,
            legend: [2, 4, 6, 10]
        });


}

function NotificationController($scope, $http) {
    $scope.notifications = null;
}

function FriendsController($scope, $http) {
    $scope.friends = null;
}