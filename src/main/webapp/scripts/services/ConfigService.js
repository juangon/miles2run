'use strict';

angular.module('milestogo')
    .service('ConfigService', function ConfigService($location) {
        return {
            getBaseUrl: function () {
                if ($location.port() === 9000) {
                    return "http://localhost:8080/milestogo/api/v2/";
                } else if ($location.port() === 8080) {
                    return "/milestogo/api/v2/";
                } else {
                    return "/api/v2/";
                }
            }
        };
    });
