(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('ScanController', ScanController);

    ScanController.$inject = ['$scope', '$state', 'Scan'];

    function ScanController ($scope, $state, Scan) {
        var vm = this;
        vm.scans = [];
        vm.loadAll = function() {
            Scan.query(function(result) {
                vm.scans = result;
            });
        };

        vm.loadAll();
        
    }
})();
