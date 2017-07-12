var gdisApp = angular.module('gdisApp', ['ngRoute', 'ngMaterial', 'ngAnimate']);

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

        .when('/entitycreation', {
            templateUrl : 'pages/entitycreation.html',
            controller  : 'entitycreationController'
        })

});

gdisApp.controller('navBarController', function($scope) {     
    $scope.category = 'home';
    $scope.sortCategory = function (category) {
        $scope.category = category;
    }
  
    $scope.isActive = function (category) {
        return $scope.category === category;
    }
});

gdisApp.controller('mainController', function($scope) {
    $scope.message = 'Bitte wählen Sie einen Menüpunkt aus der Leiste aus!';
    $scope.imagePath = 'imgs/generali.jpg';
    $scope.actionsFirstRow = ['Import', 'Export'];
    $scope.actionsSecondRow = ['Entity Creation']

})

gdisApp.controller('entitycreationController', function($scope, $mdDialog) {
    $scope.message = 'Hier können neue Entities erstells werden.';
    $scope.entity = {
        Name: '',
        Attributes: []
    }

    function showAlert(ev, title, text, aria) {
        $mdDialog.show(
            $mdDialog.alert()
            .parent(angular.element(document.querySelector('#popupContainer')))
            .clickOutsideToClose(true)
            .title(title)
            .textContent(text)
            .ariaLabel(aria)
            .ok('Okidoki!')
            .targetEvent(ev)
        );
    };

    $scope.updateAttributeInputs = function(idx) {
        var value = $('input[id="' + idx + '"]').val();

        $('label[for="' + idx + '"]').text('Attribut (schon hinzugefügt!)');

        if (value && value != '' && $scope.entity.Attributes.indexOf(value.toLowerCase()) < 0){
            $scope.entity.Attributes.push(value.toLowerCase());
        } else {
            showAlert(1, 'Attribut Fehler', 'Eine Entität muss unique Attribute haben. Case Insensitive.', 'Attribut Fehler');
        }
    }

    $scope.getSize = function() {
        var items = []; 

        for (var i = 0; i < $scope.entity.Attributes.length + 1; i++) { 
            items.push(i); 
        } 
        console.log(items);
        return items;
    }

});

gdisApp.controller('importController', function($scope, $http, $mdDialog) {
    $scope.message = 'Hier können .csv und .story Dateien importiert werden.';
    $scope.importCsv = false;
    $scope.importStory = false;

    function showAlert(ev, title, text, aria) {
        $mdDialog.show(
            $mdDialog.alert()
            .parent(angular.element(document.querySelector('#popupContainer')))
            .clickOutsideToClose(true)
            .title(title)
            .textContent(text)
            .ariaLabel(aria)
            .ok('Okidoki!')
            .targetEvent(ev)
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

    $scope.upload = function() {
        var f = $scope.uploads;
        var j = 0, k = f.length;

        if(f) {
/*            if($scope.importCsv){

            } else if ($scope.importStory) {

            } else {
                showAlert(1, 'Upload Error', 'Sie haben weder story noch csv ausgewählt!', 'Upload Fehler');
            }*/

            for (var i = 0; i < k; i++) {
                (function(cntr) {

                    r = new FileReader();

                    r.onloadend = function(e) {
                        var data = e.target.result;
                        var custom_headers = {};
                        var req_url = 'http://localhost:40042';

                        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
                        $http.defaults.headers.post['dataType'] = 'text/*';

                        console.log($scope.importStory);
                        console.log($scope.importCsv);
                        if($scope.importStory){
                            req_url = req_url + '/story?scenarios=default&story_name=' + $scope.storyName;
                        } else if ($scope.importCsv){
                            req_url = req_url + '/record';
                        }
                        console.log(req_url);
                        $http.post(req_url, data).
                            success(function(data, status, headers, config) {
                                console.log(data);
                                $scope.uploads[cntr]['curUpStatus'] = 'Uploaded (' + status + ')'; 
                                console.log(status);
                              }).
                              error(function(data, status, headers, config) {
                                $scope.uploads[cntr]['curUpStatus'] = 'Failed to Upload (' + status + ')'; 
                                console.log(status);
                              });
                    }

                    r.readAsText(f[cntr]); 

                })(i);
            }

        } else {
            alert('Keine Dateien zum Upload ausgewählt!');
        }
    }

/*    $scope.openStoryToEntityMapping = (function (ev) {
        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';

        $http.get('http://localhost:40042/entities/object').then(function successCallback(response) {
            ent_data = response.data; 
            var entNames = [];

            for (var i = 0; i < ent_data.length; i++) {

                entNames.push({'Name': ent_data[i].entityName, 'Id': i});
            }

            $scope.availEntities = entNames;
            console.log(entNames);
            $mdDialog.show({
                templateUrl: 'dialog-template.html',
                scope: $scope,
                preserveScope: true,
                targetEvent: ev
            });

            }, function errorCallback(response) {
                console.log(response);
                alert('Fehler beim Datenholen aufgetreten.');
                return [];
        });
    });*/

    $scope.showStoryNamingPrompt = function(ev) {
        var confirm = $mdDialog.prompt()
            .title('Wie soll die Story heißen?')
            .textContent('Storynamen müssen unique sein.')
            .placeholder('Story Name')
            .ariaLabel('Story Name')
            .ok('Speichern')
            .cancel('bla');

        $mdDialog.show(confirm).then(function(result) {
            console.log(result);
            $scope.storyName = result;    
            console.log($scope.storyName);
        }, function(reason) {
            console.log(reason);
        });
    };

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
                    $scope.showStoryNamingPrompt(1);
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

    var init = function () {
        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';

        $http.get('http://localhost:40042/record/json').then(function successCallback(response) {
            test_data = response.data; 
            
            var storyNames = [];

            for (var i = 0; i < test_data.length; i++) {

                storyNames.push(test_data[i].storyName);

            }

            console.log(storyNames);
            $scope.storyNames = storyNames;

        }, function errorCallback(response) {
            alert('Fehler beim Datenholen aufgetreten.');
        });
    };
    init();

});