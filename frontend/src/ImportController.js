angular.module('gdisApp').controller('ImportController', function($scope, $http, $mdDialog) {
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
            templateUrl: '../dialogSrc/dataNamingDialog.tmpl.html',
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
            templateUrl: '../dialogSrc/dataMappingDialog.tmpl.html',
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