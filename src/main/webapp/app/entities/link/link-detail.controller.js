(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('LinkDetailController', LinkDetailController);

    LinkDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Link', 'Scan'];

    function LinkDetailController($scope, $rootScope, $stateParams, entity, Link, Scan) {
        var vm = this;
        vm.link = entity;
        
        var unsubscribe = $rootScope.$on('scandroidApp:linkUpdate', function(event, result) {
            vm.link = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
