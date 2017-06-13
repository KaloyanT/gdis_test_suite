var gdisApp = angular.module('gdisApp', ['ngRoute']);

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
});

gdisApp.controller('importController', function($scope, $http) {
    $scope.message = 'Hier können Daten importiert werden.';
    
    $scope.upload = function() {
        var f = document.getElementById('file').files[0],
            r = new FileReader();

        if(f) {
            console.log(f);

            r.onloadend = function(e) {
                var data = e.target.result;
                console.log(e);
                $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
                $http.defaults.headers.post['dataType'] = 'text/csv'
                console.log(data);
                $http.post('http://localhost:40042/record', data);
            }

            r.readAsText(f);            
        } else {
            alert('Keine Dateien ausgesucht!');
        }

    }

    $scope.add = $(function() {


        $(document).on('change', ':file', function() {
            var input = $(this),
                numFiles = input.get(0).files ? input.get(0).files.length : 1,
                label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
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
          
    });
});

gdisApp.controller('exportController', function($scope) {
    $scope.message = 'Hier können Daten exportiert werden.';
});