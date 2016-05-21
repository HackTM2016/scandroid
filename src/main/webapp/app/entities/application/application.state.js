(function() {
    'use strict';

    angular
        .module('scandroidApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('application', {
            parent: 'entity',
            url: '/application',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Applications'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application/applications.html',
                    controller: 'ApplicationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('application-detail', {
            parent: 'entity',
            url: '/application/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Application'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application/application-detail.html',
                    controller: 'ApplicationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Application', function($stateParams, Application) {
                    return Application.get({id : $stateParams.id});
                }]
            }
        })
        .state('application.new', {
            parent: 'application',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application/application-dialog.html',
                    controller: 'ApplicationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                packageName: null,
                                url: null,
                                icon: null,
                                iconContentType: null,
                                apk: null,
                                apkContentType: null,
                                version: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('application', null, { reload: true });
                }, function() {
                    $state.go('application');
                });
            }]
        })
        .state('application.edit', {
            parent: 'application',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application/application-dialog.html',
                    controller: 'ApplicationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Application', function(Application) {
                            return Application.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('application', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application.delete', {
            parent: 'application',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application/application-delete-dialog.html',
                    controller: 'ApplicationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Application', function(Application) {
                            return Application.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('application', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
