import { Component, OnInit } from '@angular/core';
import {Story} from '../story.model';

@Component({
  selector: 'app-story-list',
  templateUrl: './story-list.component.html',
  styleUrls: ['./story-list.component.css']
})
export class StoryListComponent implements OnInit {
  stories: Story[] = [
    new Story('Story 1', '1', '../tabelle.html'),
    new Story('Story 2', '2', '../tabelle.html'),
    new Story('Story 3', '3',  '../tabelle.html'),
  ];

  storyStatus: boolean[] = [];
  constructor() { }

  ngOnInit() {
  }

  displayIndex(i) {
      console.log(i);
         if (this.storyStatus[i] === true) {
          this.storyStatus[i] = false;
        } else {
          this.storyStatus[i] = true;
        }
      // this.storyStatus[i] === null ? this.storyStatus[i] = true;
      // this.storyStatus[i] === true ? this.storyStatus[i] = false : this.storyStatus[i] = true;
  }

}
