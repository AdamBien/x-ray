satellite.factory('Rest', function ($http) {
    return{
        get: function (uri, callback) {
            $http.get(uri).
                    success(
                            function (data) {
                                callback(data);
                            }
                    );

        },
        post: function (data) {
            $http.post(host, data);

        }
    };

});