angular.module('gdisApp').controller('ExportController', function($scope, $http) {
    $scope.message = 'Hier können Daten exportiert werden.';
    $scope.selAll = ['all'];
    $scope.selStoryname = [];
    $scope.selTestname = [];
    $scope.selEntitytype = [];
    $scope.exportWays = [{
        'way': 'Alle Tests im ganzen System anzeigen.',
        'url': 'http://localhost:8082/exporter/e/storyTest/all',
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
        'url': 'Hier können alle Daten nach bestimmten kriterien gefiltert und gemerged werden.',
        'ident': 'custom'
    }]

    $scope.getList = function(type){
        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
        if(type == 'all')
            return $scope.selAll;
        if($scope.selStory != undefined){
            return $scope.selStory;
        }

        //get all Story Names
        if(type == 'story' && $scope.selStory == undefined){
            $http.get('http://localhost:40042/stories/list').then(function successCallback(response) {
                $scope.selStory = response.data;
                return response.data;
            })
        } else {
            return $scope.selStory;
        }

        //get all Testnames
        if(type == 'test' && $scope.selTestnames == undefined){
            $http.get('http://localhost:40042/stories/testnames').then(function successCallback(response) {
                $scope.selTestnames = response.data;
                return response.data;
            })
        } else {
            return $scope.selTestnames;
        }

        //get all Entities
        if(type == 'entity' && $scope.selEntitytype == undefined){
            $http.get('http://localhost:40042/entity/get/all').then(function successCallback(response) {
                let res = []
                response.data.forEach(function(ent){
                    res.push(ent.entityName)
                })
                $scope.selEntitytype = res;
                return res;
            })
        } else {
            return $scope.selEntitytype.length;
        }
    }

    $scope.customExport = function(type){
        console.log('bla');
    }

});