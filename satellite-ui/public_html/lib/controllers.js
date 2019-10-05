satellite.controller("gridsController",
        function ($scope, Rest) {
            Rest.get(host, function (data) {
                $scope.grids = data;
            });
            $scope.cacheSelected = function (gridName) {
                console.log(gridName);
                Rest.get(host + 'maps/' + gridName, function (data) {
                    $scope.entries = data;
                });
            };
        }
);
