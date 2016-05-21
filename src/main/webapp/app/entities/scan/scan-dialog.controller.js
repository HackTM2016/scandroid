(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('ScanDialogController', ScanDialogController);

    ScanDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Scan', 'Link', 'Application'];

    function ScanDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Scan, Link, Application) {
        var vm = this;
        vm.scan = entity;
        vm.links = Link.query();
        vm.applications = Application.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('scandroidApp:scanUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.scan.id !== null) {
                Scan.update(vm.scan, onSaveSuccess, onSaveError);
            } else {
                Scan.save(vm.scan, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.updated = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
