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
    }).factory('ProfileService',function(Profile){
        return {
            currentUser : Profile.get()
        };
    });

angular.module("milestogo", ["milestogo.services"]).
    config(function ($routeProvider) {
        $routeProvider
            .when('/', {templateUrl: 'views/activity/list.html', controller: ActivityListController})
            .when('/activity/new', {templateUrl: 'views/activity/create.html', controller: ActivityCreateController})
            .when('/activity/progress', {templateUrl: 'views/activity/progress.html', controller: ActivityProgressController})
            .when('/activity/:activityId', {templateUrl: 'views/activity/detail.html', controller: ActivityDetailController})
            .when('/activity/edit/:activityId', {templateUrl: 'views/activity/create.html', controller: ActivityDetailController})
            .otherwise({
                redirectTo: '/'
            });
    });

function ActivityListController($scope, Activity, ProfileService) {
    $scope.providers = ProfileService.currentUser.providers;
    $scope.activities = Activity.query();

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


function ActivityDetailController($scope, $routeParams, $location, Activity) {
    var activityId = $routeParams.activityId;

    $scope.activity = Activity.get({activityId: activityId});

    $scope.save = function () {

        console.log($scope.activity);

        $scope.activity.$save(function (activity, headers) {
            toastr.success("Updated new activity");
            $location.path('/');
        });
    };
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
        })

}