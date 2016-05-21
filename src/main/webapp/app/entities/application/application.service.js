(function() {
    'use strict';
    angular
        .module('scandroidApp')
        .factory('Application', Application);

    Application.$inject = ['$resource'];

    function Application ($resource) {
        var resourceUrl =  'api/applications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
