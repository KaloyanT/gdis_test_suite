angular.module('gdisApp').controller('ExportController', function($scope, $http, $uibModal) {
    $scope.message = 'Hier können Daten exportiert werden.';
    $scope.selEntities = [];
    $scope.selStories = [];
    $scope.selTest = [];
    $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
    $scope.selStory = function(){
        $http.get('http://localhost:40042/stories/list').then(function successCallback(response) {
            response.data.forEach(function(ent){
                $scope.selStories.push(ent);
            })
        });
    }
    $scope.selTestnames = function(){
        $http.get('http://localhost:40042/stories/testnames').then(function successCallback(response) {
            console.log('res', response);
            response.data.forEach(function(ent){
                $scope.selTest.push(ent);
                console.log(ent);
            })
        });
    }
    $scope.selEntitytype = function(){
        $http.get('http://localhost:40042/entity/get/all').then(function successCallback(response) {
            response.data.forEach(function(ent){
                $scope.selEntities.push(ent.entityName);
            })
        });
    }   
    Promise.all([$scope.selEntitytype(), $scope.selTestnames(), $scope.selStory()]).then(function() {
        console.log('Done with promises!');
        $scope.$apply();
        console.log($scope.selTest);
    })
    $scope.exportWays = [{
        'way': 'Alle Tests im ganzen System anzeigen.',
        'url': 'http://localhost:8082/exporter/e/storyTest/all',
        'ident': 'Alles',
        'select': ['all']
    }, {
        'way': 'Alle Tests mit <Storynamen> anzeigen.',
        'url': 'http://localhost:8082/exporter/e/storyTest/by-story-name/',
        'ident': 'Storyname',
        'select': $scope.selStories
    },{
        'way': 'Alle Tests mit <Testnamen> anzeigen.',
        'url': 'http://localhost:8082/exporter/e/storyTest/by-test-name/',
        'ident': 'Testname',
        'select': $scope.selTest
    },{
        'way': 'Alle Entities mit <Entitytype> anzeigen.',
        'url': 'http://localhost:8082/exporter/e/objects/by-entity-type/',
        'ident': 'Entitytype',
        'select': $scope.selEntities
    },{
        'way': 'Mergen u. Bearbeiten von Daten nach bestimmten Richtlinien.',
        'url': 'Hier können alle Daten nach bestimmten kriterien gefiltert und gemerged werden.',
        'ident': 'custom'
    }]

    $scope.customExport = function(type){
        console.log('bla');
    }


    //custom export modal opener
    $scope.openModal = function(data) {
        data.availTests = $scope.selTest;
        var modalInstance = $uibModal.open({
            templateUrl: '../dialogSrc/exportModal.html',
            controller: 'CustomExportCtrl',
            size: '',
            resolve: {
                data: function() {
                    return data === null ? {} : data;
                }
            }
        });

        modalInstance.result.then(function(result) {
            console.log(result)
            if (result) {
              console.log(result);
            }
        }, function(result) {
            console.log(result);
            //alert(result);
            console.log('Modal dismissed at: ' + new Date());
        });
    };

});