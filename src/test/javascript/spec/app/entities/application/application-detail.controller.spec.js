'use strict';

describe('Controller Tests', function() {

    describe('Application Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockApplication, MockScan;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockApplication = jasmine.createSpy('MockApplication');
            MockScan = jasmine.createSpy('MockScan');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Application': MockApplication,
                'Scan': MockScan
            };
            createController = function() {
                $injector.get('$controller')("ApplicationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'scandroidApp:applicationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
