import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpClient, HttpEvent, HttpHeaders, HttpRequest } from '@angular/common/http';

import { AnalysisResult } from 'src/app/specs/analysis-result';
import { Configration } from '../../specs/analysis-configration';
import { AnalysisSpecification } from '../../specs/analysis-specification';
import { ConfigKey } from '../../specs/config-key';
import { DataInfo } from '../../specs/data-info';
import { DataKey } from '../../specs/data-key';
import {
  accessibleFeaturesUrl, allSourceUrl, customColumnUrl, dashboardsUrl, dashboardUrl, dataInfoURL,
  dataUrl, exportAnalysisUrl, fileUploadUrl, getDataConfigurationUrl, requiredAnalysisUrl, runAnalysisUrl, 
  uploadDataProgressUrl, uploadDataUrl
} from './http.constants';

@Injectable()
export class CoreFetcherService {
  private token: string = "";

  constructor(private http: HttpClient) { }

  private getDefaultOptions() {
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Id-Token': this.token
      })
    };
  }

  setUser(token: string) {
    this.token = token;
  }

  getAccessibleFeatures() {
    return this.http.get<string[]>(accessibleFeaturesUrl, this.getDefaultOptions());
  }

  getAllSources(): Observable<{ [source: string]: ConfigKey[] }> {
    return this.http.get<{ [source: string]: ConfigKey[] }>(allSourceUrl, this.getDefaultOptions());
  }

  getDataConfigurations(source: string, sourceConfigration: Configration) {
    let url = getDataConfigurationUrl + "/" + source;
    return this.http.post<ConfigKey[]>(url, sourceConfigration, this.getDefaultOptions());
  }

  getDataInfo(): Observable<DataInfo[]> {
    return this.http.get<DataInfo[]>(dataInfoURL, this.getDefaultOptions());
  }

  uploadData(source: string, configration: Configration) {
    let url = uploadDataUrl + "/" + source;
    return this.http.post(url, configration, this.getDefaultOptions())
  }

  uploadDataProgress(dataKey: DataKey) {
    let url = uploadDataProgressUrl;
    return this.http.post<number>(url, dataKey, this.getDefaultOptions())
  }

  addCustomColumn(dataKey: DataKey, columnName: string, expression: string) {
    let data = {
      dataKey: dataKey.key,
      columnName: columnName,
      expression: expression
    };
    return this.http.post(customColumnUrl, data, this.getDefaultOptions())
  }

  saveDashboard(name: string, data: any) {
    let url = dashboardUrl + "/" + name;
    return this.http.post(url, data, this.getDefaultOptions())
  }

  getAllDashboards() {
    return this.http.get<string[]>(dashboardsUrl, this.getDefaultOptions())
  }

  getDashoardData(name) {
    let url = dashboardUrl + "/" + name;
    return this.http.get<any>(url, this.getDefaultOptions())
  }

  getRequiredConfigurations(analysisTypes: string[]): Observable<{ [analysisType: string]: ConfigKey[] }> {
    return this.http.post<{ [analysisType: string]: ConfigKey[] }>(requiredAnalysisUrl, analysisTypes, this.getDefaultOptions());
  }

  deleteData(dataKey: DataKey) {
    let url = dataUrl + "/" + dataKey.key;
    return this.http.delete(url, this.getDefaultOptions());
  }

  deleteDashboard(name: string) {
    let url = dashboardUrl + "/" + name;
    return this.http.delete(url, this.getDefaultOptions());
  }

  pushFileToStorage(file: File): Observable<HttpEvent<{}>> {
    let formdata: FormData = new FormData();
    formdata.append('file', file);
    let req = new HttpRequest('POST', fileUploadUrl, formdata, {
      responseType: 'text',
      headers: new HttpHeaders({ 'Id-Token': this.token })
    });
    return this.http.request(req);
  }

  runAnalysis(analysisSpecifications: AnalysisSpecification[]): Observable<AnalysisResult[]> {
    return this.http.post<AnalysisResult[]>(runAnalysisUrl, analysisSpecifications, this.getDefaultOptions());
  }

  exportAsCSV(specs: AnalysisSpecification[]) {
    this.http.post<string[]>(exportAnalysisUrl, specs, this.getDefaultOptions()).subscribe(
      csvData => {
        var a = document.createElement('a');
        document.body.appendChild(a);
        csvData.forEach((element, index) => {
          let nameOfFileToDownload = specs[index].dataKey.key + "-" + specs[index].analysisType + ".csv"
          var blob = new Blob([element], {
            type: "text/csv"
          });
          a.href = window.URL.createObjectURL(blob);
          a.download = nameOfFileToDownload;
          a.click();
        });
        document.body.removeChild(a);
      }
    );
  }
}
