(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('ApplicationDialogController', ApplicationDialogController);

    ApplicationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Application', 'Scan'];

    function ApplicationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Application, Scan) {
        var vm = this;
        vm.application = entity;
        vm.scans = Scan.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('scandroidApp:applicationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.application.id !== null) {
                Application.update(vm.application, onSaveSuccess, onSaveError);
            } else {
                Application.save(vm.application, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.setIcon = function ($file, application) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        application.icon = base64Data;
                        application.iconContentType = $file.type;
                    });
                });
            }
        };

        vm.setApk = function ($file, application) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        application.apk = base64Data;
                        application.apkContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
    }
})();
