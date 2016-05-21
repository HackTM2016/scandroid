(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('ApplicationController', ApplicationController);

    ApplicationController.$inject = ['$scope', '$state', 'DataUtils', 'Application'];

    function ApplicationController ($scope, $state, DataUtils, Application) {
        var vm = this;
        vm.applications = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.loadAll = function() {
            Application.query(function(result) {
                vm.applications = result;
            });
        };

        vm.loadAll();
        
    }
})();
