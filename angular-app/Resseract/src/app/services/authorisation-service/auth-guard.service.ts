import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { AuthorisationService } from './authorisation-service.service';

@Injectable()
export class AuthGuardService implements CanActivate {

    constructor(private authService: AuthorisationService) { }

    canActivate(): boolean {
        return this.authService.isUserLoggedIn();
    }
}