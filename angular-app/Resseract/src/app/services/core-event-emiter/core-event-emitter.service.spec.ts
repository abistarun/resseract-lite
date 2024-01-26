import { TestBed, inject } from '@angular/core/testing';

import { CoreEventEmitterService } from './core-event-emitter.service';

describe('CoreEventEmitterService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CoreEventEmitterService]
    });
  });

  it('should be created', inject([CoreEventEmitterService], (service: CoreEventEmitterService) => {
    expect(service).toBeTruthy();
  }));
});
