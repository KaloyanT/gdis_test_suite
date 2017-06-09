import { Component, OnInit, NgModule } from '@angular/core';
import {Story} from '../story.model';

@Component({
  selector: 'app-story-list',
  templateUrl: './story-list.component.html',
  styleUrls: ['./story-list.component.css']
})
export class StoryListComponent implements OnInit {
  selectedAll = false;
  stories: Story[] = [
    new Story('Story 1', '1', '../tabelle.html', false),
    new Story('Story 2', '2', '../tabelle.html', false),
    new Story('Story 3', '3', '../tabelle.html', false),
  ];
  inputs: Story[] = [
    new Story('Story 1', '1', '../tabelle.html', false),
    new Story('Story 2', '2', '../tabelle.html', false),
    new Story('Story 3', '3', '../tabelle.html', false),
  ];

  storyStatus: boolean[] = [];

  constructor() {
  }

  ngOnInit() {
  }

  checkAll() {
    this.selectedAll = true ? false : true;
    // for (const st of this.inputs) {
    //   st.selected = true;
    // }
  }

  displayIndex(i) {
    console.log(i);
    for (let j = 0; j < this.storyStatus.length; j++) {
      this.storyStatus[j] = false;
    }
    //   if (this.storyStatus[i] === true) {
    //     this.storyStatus[i] = false;
    //   } else {
    //     this.storyStatus[i] = true;
    //   }
    // }

    this.storyStatus[i] = true;
  }

  // this.storyStatus[i] === null ? this.storyStatus[i] = true;
  // this.storyStatus[i] === true ? this.storyStatus[i] = false : this.storyStatus[i] = true;
//   var myApp = angular.module('myApp', []);
//
//   myApp.directive('fileModel', ['$parse', function ($parse) {
//   return {
//     restrict: 'A',
//     link: function(scope, element, attrs) {
//       var model = $parse(attrs.fileModel);
//       var modelSetter = model.assign;
//
//       element.bind('change', function(){
//         scope.$apply(function(){
//           modelSetter(scope, element[0].files[0]);
//         });
//       });
//     }
//   };
// }]);
//
//   myApp.service('fileUpload', ['$https:', function ($https:) {
//   this.uploadFileToUrl = function(file, uploadUrl){
//     var fd = new FormData();
//     fd.append('file', file);
//
//     $https:.post(uploadUrl, fd, {
//       transformRequest: angular.identity,
//       headers: {'Content-Type': undefined}
//     })
//
//       .success(function(){
//       })
//
//       .error(function(){
//       });
//   }
// }]);
//
//   myApp.controller('myCtrl', ['$scope', 'fileUpload', function($scope, fileUpload){
//   $scope.uploadFile = function(){
//     var file = $scope.myFile;
//
//     console.log('file is ' );
//     console.dir(file);
//
//     var uploadUrl = "/fileUpload";
//     fileUpload.uploadFileToUrl(file, uploadUrl);
//   };
// }]);
}

