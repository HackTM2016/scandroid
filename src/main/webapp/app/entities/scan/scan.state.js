(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('scan', {
            parent: 'entity',
            url: '/scan',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Scans'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/scan/scans.html',
                    controller: 'ScanController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('scan-detail', {
            parent: 'entity',
            url: '/scan/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Scan'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/scan/scan-detail.html',
                    controller: 'ScanDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Scan', function($stateParams, Scan) {
                    return Scan.get({id : $stateParams.id});
                }]
            }
        })
        .state('scan.new', {
            parent: 'scan',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/scan/scan-dialog.html',
                    controller: 'ScanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                updated: null,
                                success: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('scan', null, { reload: true });
                }, function() {
                    $state.go('scan');
                });
            }]
        })
        .state('scan.edit', {
            parent: 'scan',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/scan/scan-dialog.html',
                    controller: 'ScanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Scan', function(Scan) {
                            return Scan.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('scan', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('scan.delete', {
            parent: 'scan',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/scan/scan-delete-dialog.html',
                    controller: 'ScanDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Scan', function(Scan) {
                            return Scan.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('scan', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
