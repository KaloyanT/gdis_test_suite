var gdisApp = angular.module('gdisApp', ['ngRoute', 'ngMaterial', 'ngAnimate', 'ngMessages']);


// routing
gdisApp.config(function($routeProvider) {
    $routeProvider

        // main page
        .when('/', {
            templateUrl : 'pages/home.html',
            controller  : 'MainController'
        })

        .when('/import', {
            templateUrl : 'pages/import.html',
            controller  : 'ImportController'
        })

        .when('/export', {
            templateUrl : 'pages/export.html',
            controller  : 'ExportController'
        })

        .when('/entitymanagement', {
            templateUrl : 'pages/entitymanagement.html',
            controller  : 'EntitymanagementController'
        })
        .otherwise({
            redirectTo: '/'
        });

});