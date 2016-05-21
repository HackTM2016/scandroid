(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('LinkDialogController', LinkDialogController);

    LinkDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Link', 'Scan'];

    function LinkDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Link, Scan) {
        var vm = this;
        vm.link = entity;
        vm.scans = Scan.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('scandroidApp:linkUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.link.id !== null) {
                Link.update(vm.link, onSaveSuccess, onSaveError);
            } else {
                Link.save(vm.link, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
