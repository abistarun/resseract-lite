import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-progress-indicator',
  templateUrl: './progress-indicator.component.html',
  styleUrls: ['./progress-indicator.component.css']
})
export class ProgressIndicatorComponent implements OnInit {

  @Input() valueBased: boolean;
  progress = 0;

  constructor() { }

  ngOnInit() {
  }

  updateProgress(progress) {
    this.progress = progress;
  }
}
