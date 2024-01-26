import { TestBed, inject } from '@angular/core/testing';

import { CoreFetcherService } from './core-fetcher.service';

describe('CoreFetcherService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CoreFetcherService]
    });
  });

  it('should be created', inject([CoreFetcherService], (service: CoreFetcherService) => {
    expect(service).toBeTruthy();
  }));
});
