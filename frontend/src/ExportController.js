angular.module('gdisApp').controller('ExportController', function($scope, $http) {
    $scope.message = 'Hier können Daten exportiert werden.';
    $scope.selAll = ['all'];
    $scope.selStoryname = [];
    $scope.selTestname = [];
    $scope.selEntitytype = [];
    $scope.exportWays = [{
        'way': 'Alle Tests im ganzen System anzeigen.',
        'url': 'http://localhost:8082/exporter/e/storyTest/',
        'ident': 'Alles',
        'select': 'all'
    }, {
        'way': 'Alle Tests mit <Storynamen> anzeigen.',
        'url': 'http://localhost:8082/exporter/e/storyTest/by-story-name/',
        'ident': 'Storyname',
        'select': 'story'
    },{
        'way': 'Alle Tests mit <Testnamen> anzeigen.',
        'url': 'http://localhost:8082/exporter/e/storyTest/by-test-name/',
        'ident': 'Testname',
        'select': 'test'
    },{
        'way': 'Alle Entities mit <Entitytype> anzeigen.',
        'url': 'http://localhost:8082/exporter/e/objects/by-entity-type/',
        'ident': 'Entitytype',
        'select': 'entity'
    },{
        'way': 'Mergen u. Bearbeiten von Daten nach bestimmten Richtlinien.',
        'url': 'Hier können alle Daten nach bestimmten kriterien gefiltert und gemerged werden. Zusätzlich ist verarbeitung von Hand möglich, falls erwünscht.',
        'ident': 'custom'
    }]

    $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';

    let promises = [];

    //get all Story Names
    promises.push($http.get('http://localhost:40042/stories/list').then(function successCallback(response) {
        $scope.selStoryname = response.data;
    }));
    //get all Entities
    promises.push($http.get('http://localhost:40042/entity/get/all').then(function successCallback(response) {
        let res = []
        response.data.forEach(function(ent){
            res.push(ent.entityName)
        })
        $scope.selEntitytype = res;
    }));
    //get all Testnames
    promises.push($http.get('http://localhost:40042/stories/testnames').then(function successCallback(response) {
        $scope.selTestname = response.data;
    }));
    
    Promise.all(promises).then(function() {
        $scope.$digest();
    });

    $scope.getList = function(type){
        if(type == 'all')
            return $scope.selAll;
        if(type == 'story')
            return $scope.selStoryname;
        if(type == 'test')
            return $scope.selTestname;
        if(type == 'entity')
            return $scope.selEntitytype;

    };

    $scope.customExport = function(type){
        console.log('bla');
    }

});