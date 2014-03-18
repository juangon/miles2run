'use strict';

angular.module('milestogo')
    .directive('calHeatmap', function () {
        function link(scope, el) {
            var config = scope.config;
            var element = el[0];
            var cal = new CalHeatMap();
            cal.init({
                    itemSelector: element,
                    domain: config.domain ? config.domain : "month",
                    subDomain: config.subDomain ? config.subDomain : "day",
                    subDomainTextFormat: config.subDomainTextFormat ? config.subDomainTextFormat : "%d",
                    data: config.data ? config.data : "",
                    start: config.start ? config.start : new Date(),
                    cellSize: config.cellSize ? config.cellSize : 25,
                    range: config.range ? config.range : 3,
                    domainGutter: config.domainGutter ? config.domainGutter : 10,
                    legend: config.legend ? config.legend : [2, 5, 8, 10],
                    itemName: config.itemName ? config.itemName : "item"
                }

            )
            ;
        }

        return {
            template: '<div id="cal-heatmap" align="center" config="config"></div>',
            restrict: 'E',
            link: link,
            scope: { config: '=' }
        };
    })
;
