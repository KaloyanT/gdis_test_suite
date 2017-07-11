var gdisApp = angular.module('gdisApp', ['ngRoute', 'ngMaterial']);

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

        .when('/entitymapping', {
            templateUrl : 'pages/entitymapping.html',
            controller  : 'entitymappingController'
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

gdisApp.controller('entitycreationController', function($scope) {
    $scope.message = 'Hier können neue Entities erstells werden.';
    $scope.entity = {
        Name: '',
        Attributes: []
    }

    $scope.updateAttributeInputs = function(idx) {
        var value = $('input[id="' + idx + '"]').val();

        $('label[for="' + idx + '"]').text('Attribut (schon hinzugefügt!)');

        if (value && value != '' && $scope.entity.Attributes.indexOf(value) < 0){
            $scope.entity.Attributes.push(value);
        } else {
            alert('Entity Attribut existiert bereits oder anderer Fehler!');
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

gdisApp.controller('entitymappingController', function($scope) {
    $scope.message = 'Hier können bereits importierte Daten auf bereits erstellte Entities gemappt werden.';
});

gdisApp.controller('importController', function($scope, $http) {
    $scope.message = 'Hier können Daten importiert werden.';

    $scope.removeRow = function(idx) { 
        $scope.uploads.splice(idx, 1);

        var input = $(':file'),
            numFiles = $scope.uploads ? $scope.uploads.length : 1,
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
        
        if (numFiles != 0) {
            input.trigger('fileselect', [numFiles, label]);    
        } else {
            input.parents('.input-group').find(':text').val('No files selected!');
        }
        
    }

    $scope.clear = function() {
        $scope.uploads = [];
    }

    $scope.upload = function() {
        var f = $scope.uploads;
        var j = 0, k = f.length;

        if(f) {
            for (var i = 0; i < k; i++) {
                (function(cntr) {

                    r = new FileReader();

                    r.onloadend = function(e) {
                        var data = e.target.result;
                        $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
                        $http.defaults.headers.post['dataType'] = 'text/csv'

                        $http.post('http://localhost:40042/record', data).
                            success(function(data, status, headers, config) {
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

    $scope.add = $(function() {

        $(document).on('change', ':file', function() {
            var input = $(this),
                numFiles = input.get(0).files ? input.get(0).files.length : 1,
                label = input.val().replace(/\\/g, '/').replace(/.*\//, '');

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
                    }), 10});

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
                    input.val('No files selected!');
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