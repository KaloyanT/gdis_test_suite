(function() {
  'use strict';
  
  angular
    .module('gdisApp')
    .controller('CustomExportCtrl', CustomExportCtrl);
    
  CustomExportCtrl.$inject = ['$scope', '$uibModalInstance', 'NgTableParams', '$http', 'data', '$compile'];
  
  function CustomExportCtrl($scope, $modalInstance, NgTableParams, $http, data, $compile) {
    angular.element(document).ready(function () {
        $('[data-toggle="popover"]').popover({ html : true })
            .click(function(ev) {
             //this is workaround needed in order to make ng-click work inside of popover
             $compile($('.popover.in').contents())($scope);
        });
    });
    $scope.chosenMode = '';
    $scope.data = data;
    $scope.selectedEntities = [];
    $scope.curStep = 0;
    $scope.steps = [
        { number: 1, name: 'Export Art', ident: 'init'},
        { number: 2, ident: 'filterAttributes', nextText: 'Rekombination und Bearbeitung', name: 'Attribut Filter'},
        { number: 3, name: 'Rekombination und Bearbeitung', nextText: 'Schließen' },
    ];
    $scope.currentStep = angular.copy($scope.steps[$scope.curStep]);

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };
    
    $scope.nextStep = function() {
        $scope.curStep++;
        
        if($scope.curStep == 2)
            if($scope.exportMode == 'test')
                $scope.steps[2].ident = 'recombinationTest';
            else
                $scope.steps[2].ident = 'recombinationEnt';
            $scope.recombination();
        if($scope.curStep == 3)
            $scope.finish();
        
        $scope.currentStep = angular.copy($scope.steps[$scope.curStep]);

        if(!$scope.$$phase)
            $scope.$digest();

    };

    $scope.finish = function(){
        $modalInstance.close({data: []});
    }

    $scope.recombination = function(){
        console.log('recombination');
        $scope.filter = []
        $scope.attributesOnly = [];

        $scope.attributes.forEach(function(a){
            if($scope.exportMode == 'entity')
                $scope.attributesOnly.push(a.attrName);
            if(a.filter){
                if(a.filterType == 'string' && a.filterRegexVal != undefined && a.filterRegexVal != '')
                    $scope.filter.push({'type': 'string', 'col': a.attrName, 'exp': a.filterRegexVal});
                if(a.filterType == 'number' && a.filterRangeValLower != undefined && a.filterRangeValUpper != undefined && a.filterRangeValLower != '' && a.filterRangeValUpper != '')
                    $scope.filter.push({'type': 'number', 'col': a.attrName, 'min': a.filterRangeValLower, 'max': a.filterRangeValUpper})
                if(a.filterType == 'location'  && a.range != undefined && a.range != '')
                    $scope.filter.push({'type': 'location', 'col': a.attrName, 'city': 'bla', 'range': a.filterRangeVal}) 
            }
        });

        if($scope.exportMode == 'test'){
            var testName = $scope.chosenTest;
        } else {
            var testName = JSON.stringify([]);
        }

        $scope.filter = JSON.stringify($scope.filter);
        $scope.attributesOnly = JSON.stringify($scope.attributesOnly);
        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
        console.log(`http://localhost:40042/entity-data/get/filter/by-entities/${$scope.exportMode}/${$scope.attributesOnly}/${$scope.filter}/json/${testName}`);
        $http.get(`http://localhost:40042/entity-data/get/filter/by-entities/${$scope.exportMode}/${$scope.attributesOnly}/${$scope.filter}/json/${testName}`).then(function successCallback(response) {
            $scope.availEntities.forEach(function(ent){
                response.data.forEach(function(d){
                    if(d.name == ent.name){
                        ent.count = d.count;
                        ent.data = d.data; 
                        ent.attrs = d.attributes;  
                    }
                })
                ent._mergedData = []
                ent.data.forEach(function(row){
                    let _curObject = {};
                    let idx = 0;
                    row.forEach(function(entry){
                        _curObject[ent.attrs[idx]] = entry;
                        idx++;
                    })
                    ent._mergedData.push(_curObject);
                })

                ent.cols = [];
                ent.attrs.forEach(function(a){
                    ent.cols.push({title: a.split('.')[1], field: a, visible: true})
                })

                ent.table = new NgTableParams({
                    page: 1,
                    count: ent._mergedData.length
                }, { 
                    counts: [],
                    dataset: ent._mergedData 
                });
                 
            });

            if($scope.exportMode == 'test'){
                let _combinedData = [];
                let _combinedCols = [];
                let _combinedAttributes = [];
                let _count = $scope.availEntities[0].count;
                let _combinedNames = [];
                $scope.availEntities.forEach(function(ent){
                    _combinedNames.push(ent.name)
                    _combinedCols = _combinedCols.concat(ent.cols)
                    _combinedAttributes = _combinedAttributes.concat(ent.attrs);
                    if(_combinedData.length < 1){
                        _combinedData = _combinedData.concat(ent._mergedData);
                        console.log(_combinedData);
                    } else {
                        let i = 0;
                        _combinedData.forEach(function(entry){
                            let _d = Object.assign(entry, ent._mergedData[i]);
                            _d = Object.assign(_d, {popover: generateHtmlPopover(i, _d)})
                            _combinedData[i] = _d;
                            i++;
                        })
                    }
                });

                _combinedCols.unshift({title: 'Edit', field: 'popover', visible: true});
                $scope.cEnt = {
                    name: $scope.chosenTest + ' - Beteiligte Entitys: ' + _combinedNames, 
                    cols: _combinedCols,
                    attrs: _combinedAttributes,
                    count: _count,
                    table: new NgTableParams({
                        page: 1,
                        count: _combinedData.length
                    }, { 
                        counts: [],
                        dataset: _combinedData
                    }),
                }

            } else {
                $scope.availEntities.forEach(function(ent){
                    ent.cols.unshift({title: '', visible: true});
                })
            }

        });  
     
    }

    function generateHtmlPopover(idx, object){
        let keys = Object.keys(object);
        let html_start = `<form id="${idx}Form"><div class="row">`

        let html_inputs = '';

        keys.forEach(function(key){
            html_inputs = html_inputs.concat(`
                <div class="form-group col-xs-6">
                    <label for="${key}">${key}</label>
                    <input type="text" class="form-control" value="${object[key]}" id="${key}">
                </div>
            `)
        });

        let html_end = `</div><div class="row"><div class="form-group col-xs-6"><button type="button" class="btn btn-raised btn-danger">Abbruch</button></div><div class="form-group col-xs-6"><button type="button" class="btn btn-raised btn-primary" ng-click='updateTable(${idx}, ${JSON.stringify(object)})'>Speichern</button></div></div></form> `

        return html_start.concat(html_inputs).concat(html_end);
    }

    $scope.updateTable = function(idx, obj){
        let _keys = Object.keys(obj);
        let $inputs = $(`#${idx}Form :input`);

        let new_obj = {};
        let i = 0;
        $inputs.each(function(inp) {
            new_obj[_keys[i]] = i;
            console.log($(inp).val());
            i++;
        });
        console.log(JSON.stringify(new_obj));
     }

    $scope.getAttributes = function(fromTest, testName=''){
        $scope.attributes = [];
        $scope.availEntities = [];
        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';

        let raw_attrs = [];
        let raw_ents = [];

        if(fromTest){
            $scope.chosenTest = testName;
            $scope.exportMode = 'test'
        } else {
            $scope.exportMode = 'entity'
        }


        if(fromTest){
            var promise = new Promise(function(ok, err){
                $http.get(`http://localhost:40042/entity/get/by-test/${$scope.chosenTest}`).then(function successCallback(response) {
                    if(response.data){
                        response.data.forEach(function(attr){
                            raw_attrs.push(attr);
                            let ent = attr.split('.')[0];
                            if(raw_ents.indexOf(ent) < 0){
                                raw_ents.push(ent);
                            }
                        });
                        ok();
                    } else {
                        err();
                    }
                 
                });
            });

        } else {
            var promise = new Promise(function(ok, err){
                $http.get('http://localhost:40042/entity/get/all').then(function successCallback(response) {
                    if(response.data){
                        response.data.forEach(function(ent){
                            if($scope.selectedEntities.indexOf(ent.entityName) > -1){
                                raw_ents.push(ent.entityName);
                                ent.testEntityAttributes.forEach(function(attr){
                                    raw_attrs.push(`${ent.entityName}.${attr}`);
                                });
                            }
                        });
                        ok();
                    } else {
                        err();
                    }
                });
            });
        }

        promise.then(function(){
            raw_ents.forEach(function(ent){
                $scope.availEntities.push({name: ent, count: 0, data: [], attrs: []})
            });
            raw_attrs.forEach(function(attr){
                $scope.attributes.push({attrName: attr, filter: false})
            });

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
                console.log('Done with All!');
                console.log($scope.attributes);
                console.log($scope.availEntities);

                $scope.nextStep();
            });
        });

    }



    $scope.fromEntities = function(fromTest){
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


    $scope.fromTest = function(fromTest, testName=''){
        if(fromTest){
            $scope.chosenTest = testName;    
        }
        
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

    $scope.edit = function(idx){
        console.log(idx);
    }

    $scope.toggle = function(id){
        $(`#${id}Short`).toggle(300);
        $(`#${id}Long`).toggle(300);
    }
    $scope.toggleManipulation = function(id){
        $('[data-toggle="popover"]').popover({ html : true })
            .click(function(ev) {
             //this is workaround needed in order to make ng-click work inside of popover
             $compile($('.popover.in').contents())($scope);
        });

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