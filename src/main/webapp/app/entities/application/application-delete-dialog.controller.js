(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('ApplicationDeleteController',ApplicationDeleteController);

    ApplicationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Application'];

    function ApplicationDeleteController($uibModalInstance, entity, Application) {
        var vm = this;
        vm.application = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Application.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
