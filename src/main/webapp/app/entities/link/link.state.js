(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('link', {
            parent: 'entity',
            url: '/link',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Links'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/link/links.html',
                    controller: 'LinkController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('link-detail', {
            parent: 'entity',
            url: '/link/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Link'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/link/link-detail.html',
                    controller: 'LinkDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Link', function($stateParams, Link) {
                    return Link.get({id : $stateParams.id});
                }]
            }
        })
        .state('link.new', {
            parent: 'link',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/link/link-dialog.html',
                    controller: 'LinkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                url: null,
                                postData: null,
                                suspect: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('link', null, { reload: true });
                }, function() {
                    $state.go('link');
                });
            }]
        })
        .state('link.edit', {
            parent: 'link',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/link/link-dialog.html',
                    controller: 'LinkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Link', function(Link) {
                            return Link.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('link', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('link.delete', {
            parent: 'link',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/link/link-delete-dialog.html',
                    controller: 'LinkDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Link', function(Link) {
                            return Link.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('link', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
