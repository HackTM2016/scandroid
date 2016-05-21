(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .controller('LinkController', LinkController);

    LinkController.$inject = ['$scope', '$state', 'Link'];

    function LinkController ($scope, $state, Link) {
        var vm = this;
        vm.links = [];
        vm.loadAll = function() {
            Link.query(function(result) {
                vm.links = result;
            });
        };

        vm.loadAll();
        
    }
})();
