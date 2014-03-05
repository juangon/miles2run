angular.module("milestogo.services", ["ngResource"]).
    factory('Status', function ($resource) {
        var Status = $resource('app/api/status/:statusId', {statusId: '@id'});
        Status.prototype.isNew = function(){
            return (typeof(this.id) === 'undefined');
        };
        return Status;
    });

angular.module("milestogo", ["milestogo.services"]).
    config(function ($routeProvider) {
        $routeProvider
            .when('/', {templateUrl: 'views/status/list.html', controller: StatusListController})
            .when('/status/new', {templateUrl: 'views/status/create.html', controller: StatusCreateController})
            .when('/status/:statusId', {templateUrl: 'views/status/detail.html', controller: StatusDetailController})
            .when('/status/edit/:statusId', {templateUrl: 'views/status/create.html', controller: StatusDetailController})
            .otherwise({
                redirectTo:'/'
            });
    });

function StatusListController($scope, Status) {
    $scope.statuses = Status.query();

}

function StatusCreateController($scope, $routeParams, $location, $http, Status) {

    $scope.status = new Status();
    $scope.save = function () {
        console.log($scope.status);
        $scope.status.$save(function (status, headers) {
            toastr.success("Posted new status");
            $location.path('/');
        });
    };
}


function StatusDetailController($scope, $routeParams, $location, Status) {
    var statusId = $routeParams.statusId;

    $scope.status = Status.get({statusId: statusId});

    $scope.save = function () {
        console.log($scope.status);
        $scope.status.$save(function (status, headers) {
            toastr.success("Updated new status");
            $location.path('/');
        });
    };
}
