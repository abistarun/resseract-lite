import { Component, OnInit } from '@angular/core';
import { CoreEventEmitterService } from '../services/core-event-emiter/core-event-emitter.service';
import { ActivatedRoute } from '@angular/router';
import { AuthorisationService } from '../services/authorisation-service/authorisation-service.service';
import { AnalysisService } from '../services/analysis-service/analysis-service.service';

@Component({
  selector: 'app-showcase',
  templateUrl: './showcase.component.html',
  styleUrls: ['./showcase.component.css']
})
export class ShowcaseComponent implements OnInit {

  dashboardName: string;

  constructor(private emitterService: CoreEventEmitterService, private route: ActivatedRoute,
    authService: AuthorisationService,
    private analysisService: AnalysisService) {
    this.route.queryParams.subscribe(params => {
      this.dashboardName = params['dashboardName'];
    });
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    (async () => {
      await this.analysisService.updateDataInfos();
      this.emitterService.emitLoadDashboardEvent(this.dashboardName);
    })();
  }
}
