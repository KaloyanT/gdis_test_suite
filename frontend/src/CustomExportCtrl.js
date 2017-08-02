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
                    } else {
                        let i = 0;
                        _combinedData.forEach(function(entry){
                            let _d = Object.assign(entry, ent._mergedData[i], {count: i});
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
                    data: _combinedData,
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
                let j = 0;
                $scope.availEntities.forEach(function(ent){
                    ent.cols.unshift({title: 'Edit', field: 'popover', visible: true});
                    let i = 0;
                    ent._mergedData.forEach(function(entry){
                        let _d = Object.assign(entry, {count: i, entNo: j});
                        _d = Object.assign(_d, {popover: generateHtmlPopover(i, _d, j)});
                        ent._mergedData[i] = _d;
                        i++;
                    })

                    ent.table = new NgTableParams({
                        page: 1,
                        count: ent._mergedData.length
                    }, { 
                        counts: [],
                        dataset: ent._mergedData 
                    });
                    j++;

                });

            }

        });  
     
    }

    function generateHtmlPopover(idx, object, entNo=''){
        let keys = Object.keys(object);
        let html_start = `<form id="${idx}${entNo}Form"><div class="row">`

        let html_inputs = '';

        keys.forEach(function(key){
            if(key != 'count' && key != 'entNo'){
                html_inputs = html_inputs.concat(`
                    <div class="form-group col-xs-6">
                        <label for="${key}">${key}</label>
                        <input type="text" class="form-control" value="${object[key]}" id="${key}">
                    </div>
                `);
            }
        });

        let html_end = `</div><div class="row"><div class="form-group col-xs-6"><button type="button" class="btn btn-raised btn-danger" onclick="$(&quot;#${idx}${entNo}Popover&quot;).popover(&quot;hide&quot;);">Abbruch</button></div><div class="form-group col-xs-6"><button type="button" class="btn btn-raised btn-primary" ng-click='updateTable(${idx}, ${JSON.stringify(object)}, "${entNo}")'>Speichern</button></div></div></form> `

        return html_start.concat(html_inputs).concat(html_end);
    }

    $scope.updateTable = function(idx, obj, entNo=''){

        let _keys = Object.keys(obj);
        let idxCount = _keys.indexOf('count');
        _keys.splice(idxCount, 1);

        let _i = 0;
        let _new_obj = {count: idx};

        if($scope.cEnt == undefined){
            _new_obj['entNo'] = entNo;
            let idxEntNo = _keys.indexOf('entNo');
            _keys.splice(idxEntNo, 1);
            
            
            $(`#${idx}${entNo}Form :text`).each(function(i){
                _new_obj[_keys[_i]] = $(this).val();
                _i++;
            });     

            _new_obj['popover'] = generateHtmlPopover(idx, _new_obj, entNo);



            sleep(50).then(() => {
                $scope.availEntities[entNo]._mergedData[idx] = _new_obj;
                $scope.availEntities[entNo].table.data[idx] = _new_obj;
                $scope.availEntities[entNo].table.reload();                

            });

        } else {
            $(`#${idx}${entNo}Form :text`).each(function(i){
                _new_obj[_keys[_i]] = $(this).val();
                _i++;
            });

            _new_obj = Object.assign(_new_obj, {popover: generateHtmlPopover(idx, _new_obj, entNo)});

            sleep(50).then(() => {
                $scope.cEnt.data[idx] = _new_obj;
                $scope.cEnt.table.data[idx] = _new_obj;
                $scope.cEnt.table.reload();   
            })
        }

        $(`#${idx}${entNo}Popover`).popover('hide');
        sleep(100).then(() => {
            $('[data-toggle="popover"]').popover({ html : true })
                .click(function(ev) {
                 //this is workaround needed in order to make ng-click work inside of popover
                 $compile($('.popover.in').contents())($scope);
            });
        });
    }

    function sleep (time) {
      return new Promise((resolve) => setTimeout(resolve, time));
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

                $scope.nextStep();
            });
        });

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
            $scope.selectedEntities.splice(idx, 1);
        }
        $(`#${ent}`).toggleClass('md-primary');  

    }

    function cleanData(dataArray){
        let _res = [];
        dataArray.forEach(function(entry){
            if(entry.popover != undefined)
                delete entry.popover;
            if(entry.count != undefined)
                delete entry.count;
            if(entry.entNo != undefined)
                delete entry.entNo;
            if(entry.$$hashKey != undefined)
                delete entry.$$hashKey;

            _res.push(entry);
        });
        return _res;
    }

    $scope.download = function(source, exportMode){
        console.log(source, exportMode);
        if(exportMode == 'recombine'){
            var _exportMode = 'true';
        } else {
            var _exportMode = 'false';
        }

        if(source == 'fromTest'){
            let _copiedData = $scope.cEnt.data.slice();
            var _data = [cleanData(_copiedData)];
            var url = `http://localhost:40042/entity/download/${_exportMode}`;
        } else {
            var _data = [];
            let entCopy = $scope.availEntities.slice();
            entCopy.forEach(function(ent){
                _data.push(cleanData(ent._mergedData));
            });
            var url = `http://localhost:40042/entity/download/${_exportMode}`;
        }

        var data = JSON.stringify(_data);
        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
        $http.defaults.headers.post['dataType'] = 'application/json';

        $http.post(url, data).
            success(function(data, status, headers, config) {
                var blob = new Blob([data], {type: "text/csv"});
                var a = document.createElement('a');
                a.href = window.URL.createObjectURL(blob);
                a.download = 'testCase.csv';
                a.style.display = 'none';
                document.body.appendChild(a);
                a.click();

                if($scope.cEnt == undefined){

                    let j = 0;
                    $scope.availEntities.forEach(function(ent){
                        let i = 0;
                        ent._mergedData.forEach(function(entry){
                            let _d = Object.assign(entry, {count: i, entNo: j});
                            _d = Object.assign(_d, {popover: generateHtmlPopover(i, _d, j)});
                            ent._mergedData[i] = _d;
                            i++;
                        })

                        ent.table.data = ent._mergedData;
                        ent.table.reload();
                        j++;
                    });

                } else {
                    let _combinedData = $scope.cEnt.data.slice();
                    let i = 0;
                    _combinedData.forEach(function(entry){
                        let _d = Object.assign(entry, {count: i});
                        _d = Object.assign(_d, {popover: generateHtmlPopover(i, _d)})
                        _combinedData[i] = _d;
                        i++;
                    })
                    $scope.cEnt.table.data = _combinedData;
                    $scope.cEnt.table.reload();
                }

            }).
            error(function(data, status, headers, config) {
                console.log(data, status, headers, config);
            });  

    }

  }

})();