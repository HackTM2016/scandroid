(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('ScanDeleteController',ScanDeleteController);

    ScanDeleteController.$inject = ['$uibModalInstance', 'entity', 'Scan'];

    function ScanDeleteController($uibModalInstance, entity, Scan) {
        var vm = this;
        vm.scan = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Scan.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
