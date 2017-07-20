angular.module('gdisApp').controller('EntitymanagementController', function($scope, $mdDialog, $http) {
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
            templateUrl: '../dialogSrc/entityCreationDialog.tmpl.html',
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