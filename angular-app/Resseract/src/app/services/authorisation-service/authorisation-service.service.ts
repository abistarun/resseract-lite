import { Injectable } from '@angular/core';
import { CoreFetcherService } from '../core-fetcher/core-fetcher.service';

@Injectable({
  providedIn: 'root'
})
export class AuthorisationService {
  token: string;
  accessibleFeatures: string[];

  constructor(private fetcherService: CoreFetcherService) {
  }

  setToken(token: any) {
    this.token = token;
    this.fetcherService.setUser(this.token);
  }

  async loadAccessibleFeatures() {
    this.accessibleFeatures = await this.fetcherService.getAccessibleFeatures().toPromise();
  }
  
  getAccessibleFeatures() {
    return this.accessibleFeatures;
  }

  isUserLoggedIn() {
      return true;
  }

  hasFeature(feature: string) {
    return this.accessibleFeatures && this.accessibleFeatures.includes(feature);
  }
}
