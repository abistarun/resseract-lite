import { Component, OnInit, Input } from '@angular/core';
import { ConfigKey } from '../specs/config-key';
import { HttpResponse } from '@angular/common/http';
import { CoreFetcherService } from '../services/core-fetcher/core-fetcher.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  @Input() configKey: ConfigKey;
  isLoaded: boolean = true;
  label = "Click here to upload file"

  constructor(private fetcherService: CoreFetcherService) { }

  ngOnInit() {
  }

  selectFile(event) {
    let selectedFile = event.target.files.item(0);
    this.upload(selectedFile)
  }

  upload(currFile) {
    this.isLoaded = false;
    this.configKey.currValue = null;
    this.fetcherService.pushFileToStorage(currFile).subscribe(httpResponse => {
      if (httpResponse instanceof HttpResponse && httpResponse.status == 200) {
        this.configKey.currValue = httpResponse.body;
        this.label = currFile.name;
        this.isLoaded = true;
      }
    });
    this.label = "Click here to upload file"
  }
}
