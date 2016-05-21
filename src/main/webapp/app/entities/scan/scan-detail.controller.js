(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('ScanDetailController', ScanDetailController);

    ScanDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Scan', 'Link', 'Application'];

    function ScanDetailController($scope, $rootScope, $stateParams, entity, Scan, Link, Application) {
        var vm = this;
        vm.scan = entity;
        
        var unsubscribe = $rootScope.$on('scandroidApp:scanUpdate', function(event, result) {
            vm.scan = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
