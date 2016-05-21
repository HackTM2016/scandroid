'use strict';

describe('Controller Tests', function() {

    describe('Scan Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockScan, MockLink, MockApplication;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockScan = jasmine.createSpy('MockScan');
            MockLink = jasmine.createSpy('MockLink');
            MockApplication = jasmine.createSpy('MockApplication');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Scan': MockScan,
                'Link': MockLink,
                'Application': MockApplication
            };
            createController = function() {
                $injector.get('$controller')("ScanDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'scandroidApp:scanUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
