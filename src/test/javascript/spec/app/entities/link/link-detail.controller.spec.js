'use strict';

describe('Controller Tests', function() {

    describe('Link Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLink, MockScan;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLink = jasmine.createSpy('MockLink');
            MockScan = jasmine.createSpy('MockScan');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Link': MockLink,
                'Scan': MockScan
            };
            createController = function() {
                $injector.get('$controller')("LinkDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'scandroidApp:linkUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
