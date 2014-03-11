angular.module("milestogo.services", ["ngResource"]).
    factory('Activity', function ($resource) {
        var Activity = $resource('api/v1/activities/:activityId', {activityId: '@id'});
        Activity.prototype.isNew = function () {
            return (typeof(this.id) === 'undefined');
        };
        return Activity;
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

function ActivityListController($scope, Activity) {
    $scope.activities = Activity.query();

}

function ActivityCreateController($scope, $routeParams, $location, $http, Activity) {

    $scope.activity = new Activity();

    $scope.save = function () {
        console.log($scope.activity);
        $scope.activity.$save(function (activity, headers) {
            toastr.success("Posted new activity");
            $location.path('/');
        });
    };
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