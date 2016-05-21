(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('ApplicationDetailController', ApplicationDetailController);

    ApplicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Application', 'Scan'];

    function ApplicationDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Application, Scan) {
        var vm = this;
        vm.application = entity;
        
        var unsubscribe = $rootScope.$on('scandroidApp:applicationUpdate', function(event, result) {
            vm.application = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
