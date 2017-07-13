var gdisApp = angular.module('gdisApp', ['ngRoute', 'ngMaterial', 'ngAnimate', 'ngMessages']);

// routing
gdisApp.config(function($routeProvider) {
    $routeProvider

        // main page
        .when('/', {
            templateUrl : 'pages/home.html',
            controller  : 'mainController'
        })

        .when('/import', {
            templateUrl : 'pages/import.html',
            controller  : 'importController'
        })

        .when('/export', {
            templateUrl : 'pages/export.html',
            controller  : 'exportController'
        })

        .when('/entitymanagement', {
            templateUrl : 'pages/entitymanagement.html',
            controller  : 'entitymanagementController'
        })

});


gdisApp.controller('mainController', function($scope, $mdSidenav, $location) {
    $scope.message = 'Bitte wählen Sie einen Menüpunkt aus der Leiste aus!';
    $scope.imagePath = 'imgs/generali.jpg';
    $scope.smallNav = true;

    //init
    $(document).ready(function() { $mdSidenav('small').toggle(); });
    $('#navbar_container').animate({'width':$('#md_sidebar_small').width() + 'px'}, 0);

    $scope.toggle = function() {
        $mdSidenav('small').toggle();
        $mdSidenav('big').toggle();
        $scope.smallNav = !$scope.smallNav;
        setNavSize();
    }

    $( window ).resize(function() {
        setNavSize();
    });

    function setNavSize(){
        let curNav = $scope.smallNav ? '#md_sidebar_small' : '#md_sidebar_big';
        $('#navbar_container').animate({'width':$(curNav).width() + 'px'}, 200);
    }

    $scope.openUrl = function(url, toggle) {
        if(toggle){
            $scope.toggle()
        }
        $location.path(url);
    }

    $scope.navs = [{
        'name': 'Home',
        'descr': 'Langingpage, Startpunkt in die Anwendung.',
        'icon': 'home',
        'url': '/'
    }, {
        'name': 'Import', 
        'descr': 'Importieren von Daten und Storys.',
        'icon': 'note_add',
        'url': '/import'
    }, {
        'name': 'Export', 
        'descr': 'Exportieren von fertigen Testcases.',
        'icon': 'import_export',
        'url': '/export'
    }, {
        'name': 'Manage Entities',
        'descr': 'Anlegen und verändern von Entities.',
        'icon': 'assignment',
        'url': '/entitymanagement'
    }];

})

gdisApp.controller('entitymanagementController', function($scope, $mdDialog, $http) {
    $scope.message = 'Hier können Entities verwaltet werden.';
    $scope.entities = ['bla', 'blub'];
    //Utils
    function showAlert(title, text, aria) {
        $mdDialog.show(
            $mdDialog.alert()
            .parent(angular.element(document.querySelector('#popupContainer')))
            .clickOutsideToClose(true)
            .title(title)
            .textContent(text)
            .ariaLabel(aria)
            .ok('Okidoki!')
        );
    };

    //Auflisten
    $scope.getAllEntities = (function (ev) {
        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';

        $http.get('http://localhost:40042/entities/object').then(function successCallback(response) {
            ent_data = response.data; 
            $scope.availEntities = [];
            let entNames = [];
            let realEnts = [];

            for (let i = 0; i < ent_data.length; i++) {
                if(entNames.indexOf({'Name': ent_data[i].entityName, 'Id': i}) < 0){
                    realEnts.push({'Name': ent_data[i].entityName, 'Id': i, 'Attributes': ent_data[i].testEntityAttributes});
                    entNames.push({'Name': ent_data[i].entityName, 'Id': i, 'Attributes': ent_data[i].testEntityAttributes.join(', '), 'Instances': 0});  
                }
            }

            $scope.availEntities = entNames;
            $scope.realEntities = realEnts;

            }, function errorCallback(response) {
                showAlert('Entity Fehler', 'Konnte Entitäten nicht aus Backend laden', 'Entity Fehler');
                return [];
        });

    });
    $scope.getAllEntities();

    //Hinzufügen
    function DialogController($scope, $mdDialog, ex_entity) {
        $scope.update = ex_entity.Name != '' ? true : false;
        $scope.pas_entity = {
            'name': ex_entity.Name,
            'attributes': ex_entity.Attributes           
        } 
        $scope.entity = {
            'name': ex_entity.Name,
            'attributes': ex_entity.Attributes
        };

        $scope.getAttrSize = function(ev) {
            var items = [];

            for (var i = 0; i < $scope.entity.attributes.length + 1; i++) { 
                let value = $scope.entity.attributes[i];
                if(value && value != ''){
                    items.push($scope.entity.attributes[i]);
                } else if (value == '') {
                    $scope.entity.attributes.splice(i, 1);
                }
            } 
            $scope.entity.attributes = items;
            items.push('');

            return items;
        }        

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        $scope.submit = function() {
            let old_name = $scope.pas_entity.name;
            let old_attributes = $scope.pas_entity.attributes;

            let name = $scope.entity.name;
            let attributes = $scope.entity.attributes.filter(function(attr){return (attr && attr != '');});
            $mdDialog.hide({'old_name': old_name, 'old_attributes': old_attributes, 'new_name': name, 'new_attributes': attributes, 'updated': $scope.update});
        };


    }

    $scope.entityCreation = function (entity) {
        let data = null;
        if(entity){
            data = entity;
        } else {
            data = {
                Name: '',
                Attributes: []
            };
        }

        $mdDialog.show({
            controller: DialogController,
            templateUrl: 'entityCreationDialog.tmpl.html',
            parent: angular.element(document.body),
            locals: {
                ex_entity: data
            },
            fullscreen: $scope.customFullscreen
        })
        .then(function(updated_ent) {
            $scope.createEntity(updated_ent);
        }, function() {
            showAlert('Creation Fehler', 'Abgebrochen durch Nutzer.', 'Fehler beim Anlegen von Entity!');
        });
    };


    $scope.createEntity = function(updated_ent) {
        let update = updated_ent.old_name != '';
        let req_url = 'http://localhost:40042/entity';
        if(update){
            let old_data_attr = updated_ent.old_attributes.toString();
            let new_data_attr = updated_ent.new_attributes.toString();
            req_url = req_url + `?old_entity_name=${updated_ent.old_name}&old_attributes=${old_data_attr}&new_entity_name=${updated_ent.new_name}&new_attributes=${new_data_attr}&update=${update}`;
        } else {
            let data_attr = updated_ent.new_attributes.toString();
            req_url = req_url + `?entity_name=${updated_ent.new_name}&attributes=${data_attr}&update=${update}`;
        }
        console.log(req_url);
        let data = null;

        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
        $http.defaults.headers.post['dataType'] = 'text/*';

        $http.post(req_url, data).
            success(function(data, status, headers, config) {
                showAlert('Creation Erfolgreich', 'Entität erfolgreich erstellt!', 'Entität erfolgreich erstellt!');
                $scope.getAllEntities();
              }).
              error(function(data, status, headers, config) {
                console.log(status);
                if(status == 409){
                    return showAlert('Creation Fehler', 'Entity mit selben Namen existier bereits!', 'Fehler bei Erstellung!');
                }
                return showAlert('Creation Fehler', 'Fehler beim Upload! (' + status + ')', 'Fehler beim Upload!');
                
            });

    }

    //Bearbeiten
    $scope.edit = function(idx){
        let to_edit = $scope.realEntities[idx];
        console.log(to_edit);
        $scope.entityCreation(to_edit);
    }
    

});

gdisApp.controller('importController', function($scope, $http, $mdDialog) {
    $scope.message = 'Hier können .csv und .story Dateien importiert werden.';
    $scope.importCsv = false;
    $scope.importStory = false;
    $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
    $http.defaults.headers.post['dataType'] = 'text/*';

    function showAlert(title, text, aria) {
        $mdDialog.show(
            $mdDialog.alert()
            .parent(angular.element(document.querySelector('#popupContainer')))
            .clickOutsideToClose(true)
            .title(title)
            .textContent(text)
            .ariaLabel(aria)
            .ok('Okidoki!')
        );
    };

    $scope.removeRow = function(idx) { 
        $scope.uploads.splice(idx, 1);

        var input = $(':file'),
            numFiles = $scope.uploads ? $scope.uploads.length : 1,
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
        
        if (numFiles != 0) {
            input.trigger('fileselect', [numFiles, label]);    
        } else {
            input.parents('.input-group').find(':text').val('No file selected!');
        }
        
    }

    $scope.clear = function() {
        $scope.uploads = [];
    }

    function showStoryNamingPrompt(c) {
        var confirm = $mdDialog.prompt()
            .title('Wie soll die Story heißen?')
            .textContent('Storynamen müssen unique sein. Bereits vorhanden: ' + $scope.storyNames.join(', '))
            .placeholder('Story Name')
            .ariaLabel('Story Name')
            .ok('Speichern')
        $mdDialog.show(confirm).then(function(result) {
            c(result);
        }, function(reason) {
            c(false);
        });
    };

    function DialogDataNamingController($scope, $mdDialog, story_names) {
        $scope.storyNames = story_names;
        $scope.test = {
            name: '',
            story: story_names[0]
        }


        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        $scope.submit = function() {
            $mdDialog.hide({'testName': $scope.test.name, 'storyName': $scope.test.story});
        };
    }

    function DialogMappingController($scope, $mdDialog, keys, result, availMappings) {
        $scope.keys = keys;
        $scope.mapping = [];
        console.log(keys);
        for (key of $scope.keys) {
            console.log(key);
            $scope.mapping.push({
                'key': key,
                'mapTo': null
            });
        }
        console.log($scope.mapping);
        $scope.availMappings = availMappings;
        $scope.result = {
            'testName': result.testName,
            'storyName': result.storyName
        }

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        $scope.submit = function() {
            $mdDialog.hide({'result': $scope.result, 'mapping': $scope.mapping});
        };
    }

    function showDataNamingPrompt(c) {
        $mdDialog.show({
            controller: DialogDataNamingController,
            templateUrl: 'dataNamingDialog.tmpl.html',
            parent: angular.element(document.body),
            fullscreen: $scope.customFullscreen,
            locals: {
                story_names: $scope.storyNames
            }
        })
        .then(function(result) {
            doDataMappings(c, {'testName': result.testName, 'storyName': result.storyName});
        }, function() {
            c(false);
        });
    }

    function showDataMappingPrompt(c, result, keys) {
        $mdDialog.show({
            controller: DialogMappingController,
            templateUrl: 'dataMappingDialog.tmpl.html',
            parent: angular.element(document.body),
            fullscreen: $scope.customFullscreen,
            locals: {
                keys: keys,
                availMappings: $scope.entityNames,
                result: result
            }
        })
        .then(function(result) {
            c(result);
        }, function() {
            c(false);
        });
    }

    function doDataMappings(c, result){

        r = new FileReader();

        r.onloadend = function(e) {
            var data = e.target.result;
            var custom_headers = {};
            var naming = null;
            let req_url = 'http://localhost:40042/record/discovery'
            $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
            $http.defaults.headers.post['dataType'] = 'text/*';

            $http.post(req_url, data).
                success(function(data, status, headers, config) {
                    showDataMappingPrompt(c, result, data);
                }).
                error(function(data, status, headers, config) {
                    console.log(status);
                });     
            
        }

        r.readAsText($scope.uploads[0]); 

    }

    $scope.upload = function() {
        $scope.existingStories = $http.get('http://localhost:40042/stories/list').then(function successCallback(response) {
            $scope.storyNames = response.data;
            $http.get('http://localhost:40042/entities/mapping').then(function successCallback(response) {
                $scope.entityNames = response.data;
                $scope.uploadInit();
            }, function errorCallback(response) {
                alert('Fehler beim Entity holen aufgetreten.');
            });
            
        }, function errorCallback(response) {
            alert('Fehler beim Storynamen holen aufgetreten.');
        });
    }

    $scope.uploadInit = function() {
        var req_url = 'http://localhost:40042';
        var f = $scope.uploads;
        var j = 0, k = f.length;

        if(f) {


            for (var i = 0; i < k; i++) {
                (function(cntr) {

                    r = new FileReader();

                    r.onloadend = function(e) {
                        var data = e.target.result;
                        var custom_headers = {};
                        var naming = null;

                        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
                        $http.defaults.headers.post['dataType'] = 'text/*';

                        if($scope.importStory){
                            showStoryNamingPrompt(function(result){
                                if(result){
                                    req_url = req_url + '/story?scenarios=default&story_name=' + result;
                                    $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
                                    $http.defaults.headers.post['dataType'] = 'text/*';
                                    $http.post(req_url, data).
                                        success(function(data, status, headers, config) {
                                            $scope.uploads[cntr]['curUpStatus'] = 'Uploaded (' + status + ')'; 
                                        }).
                                        error(function(data, status, headers, config) {
                                            if(status==409){
                                                showAlert('Story Upload Fehler', 'Story mit selbem Namen existiert bereits!', 'Upload Fehler');
                                            }
                                            $scope.uploads[cntr]['curUpStatus'] = 'Failed to Upload (' + status + ')'; 
                                            console.log(status);
                                        });     
                                } else {
                                    showAlert('Story Upload Fehler', 'Falscher User Input', 'Upload Fehler');
                                }
                            });
                            
                        } else if ($scope.importCsv){
                            showDataNamingPrompt(function(result){
                                console.log(result);
                                var mapping_str = ''
                                for (pair of result.mapping) {
                                    console.log(pair);
                                    mapping_str = mapping_str + pair.key + ',' + pair.mapTo + ';'
                                }
                                console.log(mapping_str);
                                req_url = req_url + `/record?story_name=${result.result.storyName}&test_name=${result.result.testName}&mapping=${mapping_str}`; 
                                $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
                                $http.defaults.headers.post['dataType'] = 'text/*';
                                $http.post(req_url, data).
                                    success(function(data, status, headers, config) {
                                        $scope.uploads[cntr]['curUpStatus'] = 'Uploaded (' + status + ')'; 
                                    }).
                                    error(function(data, status, headers, config) {
                                        $scope.uploads[cntr]['curUpStatus'] = 'Failed to Upload (' + status + ')'; 
                                        console.log(status);
                                    });     
                            })
                            
                        }

                        
                    }

                    r.readAsText(f[cntr]); 

                })(i);
            }

        } else {
            alert('Keine Dateien zum Upload ausgewählt!');
        }
    }

    $scope.add = $(function() {

        $(document).on('change', ':file', function() {
            var input = $(this),
                numFiles = input.get(0).files ? input.get(0).files.length : 1,
                label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
                fileExtension = input.get(0).files[0].name.split('.').pop();
                indexOfExtension = ['csv', 'story'].indexOf(fileExtension.toLowerCase());

                if (indexOfExtension == 0){
                    $scope.importCsv = true;
                    $scope.importStory = false;
                } else {
                    $scope.importCsv = false;
                    $scope.importStory = true;
                }
                

            setTimeout(function () {
                $scope.$apply(function () {
                    var files = input.get(0).files;
                    var fileBuffer=[];
                    Array.prototype.push.apply( fileBuffer, files );

                    var j = 0, k = fileBuffer.length;
                    for (var i = 0; i < k; i++) {
                        fileBuffer[i]['curUpStatus'] = 'Queued';
                    }

                    $scope.uploads = fileBuffer
                }), 
            10});

            input.trigger('fileselect', [numFiles, label]);

        });

        $(document).ready( function() {
            $(':file').on('fileselect', function(event, numFiles, label) {
                var input = $(this).parents('.input-group').find(':text'),
                    log = numFiles > 1 ? numFiles + ' files selected' : label;
                if( input.length ) {
                    input.val(log);
                } else {
                    if( log ) alert(log);
                }
                
            });
        });

        $("#reset").on("click",function() {
            var input = $(':file').parents('.input-group').find(':text');
                if( input.length ) {
                    input.val('No file selected!');
                }
        });
          
    });
});

gdisApp.controller('exportController', function($scope, $http) {
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