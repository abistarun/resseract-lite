import { Component, NgZone } from '@angular/core';
import { CoreEventEmitterService } from './services/core-event-emiter/core-event-emitter.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  isMobileScreen: boolean = false;

  constructor(private emitterService: CoreEventEmitterService,
    public snackBar: MatSnackBar,
    private router: Router,
    private zone: NgZone) {
      Highcharts.setOptions({ lang: { thousandsSep: ',' } });
  }

  ngOnInit() {
    if (window.screen.width < 768) {
      this.isMobileScreen = true;
      alert("For best results, please view on a larger screen.");
    }
    this.emitterService.listenMessageEvent((message) => this.showMessage(message));
    var parser = document.createElement('a');
    parser.href = window.location.href;
    if (parser.pathname !== '/showcase') {
      this.router.navigate(["/canvas"]);
    }
  }

  showMessage(message) {
    console.log(message);
    this.zone.run(() => {
      this.snackBar.open(message, undefined, {
        duration: 6000
      });
    });
  }
}
