(function() {
    'use strict';
    angular
        .module('scandroidApp')
        .factory('Scan', Scan);

    Scan.$inject = ['$resource', 'DateUtils'];

    function Scan ($resource, DateUtils) {
        var resourceUrl =  'api/scans/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.updated = DateUtils.convertLocalDateFromServer(data.updated);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.updated = DateUtils.convertLocalDateToServer(data.updated);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.updated = DateUtils.convertLocalDateToServer(data.updated);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
