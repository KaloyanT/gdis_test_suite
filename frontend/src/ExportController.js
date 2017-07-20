angular.module('gdisApp').controller('ExportController', function($scope, $http) {
    $scope.message = 'Hier können Daten exportiert werden.';
    $scope.exportWays = [{
        'way': 'Alle Tests im ganzen System anzeigen.',
        'url': 'http://localhost:8082/exporter/e/storyTest/all'
    }, {
        'way': 'Alle Tests mit <Storynamen> (hier "autoversicherungAbschliessen") anzeigen.',
        'url': 'http://localhost:8082/exporter/e/storyTest/by-story-name/autoversicherungAbschliessen'
    },{
        'way': 'Alle Tests mit <Testnamen> (hier "autoTest") anzeigen.',
        'url': 'http://localhost:8082/exporter/e/storyTest/by-test-name/autoTest'
    }]

    var init = function () {
        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';

        $http.get('http://localhost:40042/record/json').then(function successCallback(response) {
            test_data = response.data; 
            
            var storyNames = [];

            for (var i = 0; i < test_data.length; i++) {

                storyNames.push(test_data[i].storyName);

            }

            $scope.storyNames = storyNames;

        }, function errorCallback(response) {
            alert('Fehler beim Datenholen aufgetreten.');
        });
    };
    init();

});