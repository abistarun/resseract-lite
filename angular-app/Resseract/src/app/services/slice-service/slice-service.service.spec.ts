import { TestBed, inject } from '@angular/core/testing';

import { SliceService } from './slice-service.service';

describe('SliceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SliceService]
    });
  });

  it('should be created', inject([SliceService], (service: SliceService) => {
    expect(service).toBeTruthy();
  }));
});
