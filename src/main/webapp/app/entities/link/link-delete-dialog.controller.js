(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('LinkDeleteController',LinkDeleteController);

    LinkDeleteController.$inject = ['$uibModalInstance', 'entity', 'Link'];

    function LinkDeleteController($uibModalInstance, entity, Link) {
        var vm = this;
        vm.link = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Link.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
