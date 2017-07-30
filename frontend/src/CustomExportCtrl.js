(function() {
  'use strict';
  
  angular
    .module('gdisApp')
    .controller('CustomExportCtrl', CustomExportCtrl);
    
  CustomExportCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'data'];
  
  function CustomExportCtrl($scope, $modalInstance, $http, data) {
    $scope.chosenMode = '';
    $scope.data = data;
    $scope.selectedEntities = [];

    $scope.steps = [
      { number: 1, name: 'Export Art', ident: 'init'},
      { number: 2, ident: 'fromTest', nextText: 'Rekombination', next: 'recombination' },
      { number: 3, name: 'Attribut Eingrenzung', ident: 'fromEntities', next: 'recombination', nextText: 'Rekombination' },
      { number: 4, name: 'Rekombination und Abschluß', ident: 'recombination', next: 'close', nextText: 'Schließen' }
      ];

    $scope.currentStep = angular.copy($scope.steps[0]);
    
    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };
    
    var nextNumber = 0;
    $scope.nextStep = function(step) {
        if(step == 'fromTest')
            nextNumber = 1;
            $scope.steps[1].name = `Attribut Eingrenzung ${$scope.chosenTest}`
            $scope.currentStep = angular.copy($scope.steps[nextNumber]);
        if(step == 'fromEntities')
            nextNumber = 1;
            $scope.currentStep = angular.copy($scope.steps[nextNumber]);
        if(step == 'recombination')
            nextNumber = 3;
            $scope.recombination();
            $scope.currentStep = angular.copy($scope.steps[nextNumber]);
        if(step == 'close')
            $scope.finish();
      
    };

    $scope.finish = function(){
        
    }

    $scope.recombination = function(){
        $scope.filter = []
        $scope.attributes.forEach(function(a){
            if(a.filter){
                if(a.filterType == 'string')
                    $scope.filter.push({'type': 'string', 'col': a.attrName, 'exp': a.filterRexgexVal});
                if(a.filterType == 'number')
                    $scope.filter.push({'type': 'number', 'col': a.attrName, 'min': a.filterRangeValLower, 'max': a.filterRangeValUpper})
                if(a.filterType == 'location')
                    $scope.filter.push({'type': 'location', 'col': a.attrName, 'city': 'bla', 'range': a.filterRangeVal}) 
            }
        });

        $scope.filter = JSON.stringify($scope.filter);
        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';

        if($scope.chosenTest.length > 0){
            $http.get(`http://localhost:40042/entity-data/get/filter/by-test/${$scope.chosenTest}/${$scope.filter}/true`).then(function successCallback(response) {
                console.log(response);
                $scope.availEntities.forEach(function(ent){
                    ent.count = response.data
                });
                $scope.downloadLink = `http://localhost:40042/entity-data/get/filter/by-test/${$scope.chosenTest}/${$scope.filter}/false`
          
            });            
        } else {
            let entsToCollect = JSON.stringify($scope.selectedEntities);
            $http.get(`http://localhost:40042/entity-data/get/filter/by-entities/${entsToCollect}/${$scope.filter}/true`).then(function successCallback(response) {
                console.log(response);
                $scope.availEntities.forEach(function(ent){
                    ent.count = response.data[ent.name];
                });
                $scope.downloadLink = `http://localhost:40042/entity-data/get/filter/by-entities/${entsToCollect}/${$scope.filter}/false`
          
            });           
            //todo
          
        }

        
    }



    $scope.fromEntities = function(){
        $scope.attributes = [];
        $scope.availEntities = [];
        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';

        $scope.selectedEntities.forEach(function(ent){
            $scope.availEntities.push({name: ent, count: 0, data: []})
        });

        let promisesAttributeCollection = [];
        $scope.selectedEntities.forEach(function(ent){
            promisesAttributeCollection.push($http.get(`http://localhost:40042/entity/a/${ent}`)
                .then(function(res) {
                    if(res){
                        res.data.forEach(function(attr){
                            $scope.attributes.push({attrName: attr, filter: false})
                        });
                    }
                })
            );
        });

        Promise.all(promisesAttributeCollection).then(function() {
            console.log('Done with Attribute Collection!');
            console.log($scope.attributes);

            let promisesMetaTypes = [];
            $scope.attributes.forEach(function(attr){
                promisesMetaTypes.push($http.get(`http://localhost:40042/entity/m/${attr.attrName}`)
                    .then(function(res) {
                        if(res){
                            attr.filterType = res.data;
                            if(res.data == 'number'){
                                attr.filterName = 'Spanne (von bis)';
                            }
                            if(res.data == 'string'){
                                attr.filterName = 'Regex';
                            }
                            if(res.data == 'location'){
                                attr.filterName = 'Umkreis (km)';
                            }
                        }
                    })
                );
            });        

            Promise.all(promisesMetaTypes).then(function() {
                console.log('Done with Meta Type Collection!');
                console.log($scope.attributes);
                $scope.nextStep('fromTest');
                $scope.nextStep('fromTest');
            })

        })

    }


    $scope.fromTest = function(test){
        $scope.chosenTest = test;
        $scope.attributes = [];
        $scope.availEntities = [];
        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
        $http.get(`http://localhost:40042/entity/get/by-test/${$scope.chosenTest}`).then(function successCallback(response) {
            let _ents = []
            response.data.forEach(function(attr){
                let curEnt = attr.split('.')[0];
                if(_ents.indexOf(curEnt) < 0){
                    _ents.push(curEnt);
                }
                $scope.attributes.push({attrName: attr, filter: false});   
            })

            _ents.forEach(function(ent){
                $scope.availEntities.push({name: ent, count: 0, data: []})
            });

            //todo get /json from api to display and modify in frontend
            $scope.availEntities.forEach(function(ent){
                $scope.attributes.forEach(function(attr){
                    let curEnt = attr.attrName.split('.')[0];
                    if(curEnt == ent.name){
                        ent.data.push(attr.attrName);
                    }
                });
            });


            let promises = [];
            $scope.attributes.forEach(function(attr){
                promises.push($http.get(`http://localhost:40042/entity/m/${attr.attrName}`)
                    .then(function(res) {
                        if(res){
                            attr.filterType = res.data;
                            if(res.data == 'number'){
                                attr.filterName = 'Spanne (von bis)';
                            }
                            if(res.data == 'string'){
                                attr.filterName = 'Regex';
                            }
                            if(res.data == 'location'){
                                attr.filterName = 'Umkreis (km)';
                            }
                        }
                    })
                );
            });
        
            Promise.all(promises).then(function() {
                console.log('Done with promises!');
                console.log($scope.attributes);
                $scope.nextStep('fromTest');
            })

             
        });
      
    }

    $scope.toggle = function(id){
        $(`#${id}Short`).toggle(300);
        $(`#${id}Long`).toggle(300);
    }
    $scope.toggleManipulation = function(id){
        $(`#${id}`).toggle(300);
    }
    $scope.selEntity = function(ent){
        if($scope.selectedEntities.indexOf(ent) < 0){
            $scope.selectedEntities.push(ent);
        } else {
            let idx = $scope.selectedEntities.indexOf(ent);
            console.log('idx', idx);
            $scope.selectedEntities.splice(idx, 1);
        }
        $(`#${ent}`).toggleClass('md-primary');  

    }
  }

})();