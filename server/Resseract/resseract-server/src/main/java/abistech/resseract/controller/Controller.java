package abistech.resseract.controller;

import abistech.resseract.CoreServer;
import abistech.resseract.analysis.AnalysisResult;
import abistech.resseract.analysis.AnalysisType;
import abistech.resseract.auth.Feature;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.DataInfo;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.Constants;
import abistech.resseract.analysis.AnalysisSpecification;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.source.SourceType;
import jakarta.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author abisTarun
 */
@RestController
class Controller {

    @Autowired
    private FileService fileService;

    private static final Logger logger = LogManager.getLogger(Controller.class.getName());

    static {
        String profile = System.getenv("RESSERACT_PROFILE");
        CoreServer.initialize(profile);
        logger.debug("Resseract initialized !!!");
    }


    @GetMapping("/health")
    public String health() {
        logger.info("Health Check");
        return "UP";
    }

    @GetMapping("/accessiblefeatures")
    public Collection<Feature> getAccessibleFeatures() {
        return CoreServer.getAccessibleFeatures();
    }

    @GetMapping("/sources")
    public Map<SourceType, List<ConfigKey>> getAllSources() {
        return CoreServer.getAllSources();
    }

    @PostMapping("/generatedataconfiguration/{source}")
    public List<ConfigKey> getDataConfigurations(@PathVariable SourceType source, @RequestBody Config sourceConfig) throws ResseractException {
        return CoreServer.getDataConfigurations(source, sourceConfig);
    }

    @PostMapping("/uploaddata/{source}")
    public ResponseEntity uploadData(@PathVariable SourceType source, @RequestBody Config configurations) throws ResseractException {
        CoreServer.uploadData(source, configurations, true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/customcolumn")
    public ResponseEntity addCustomColumn(@RequestBody Map<String, String> configurations) throws ResseractException {
        DataKey dataKey = new DataKey(configurations.get("dataKey"));
        String columnName = configurations.get("columnName");
        String expression = configurations.get("expression");
        CoreServer.addCustomColumn(dataKey, columnName, expression);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/uploaddataprogress")
    public Double uploadProgress(@RequestBody DataKey dataKey) throws ResseractException {
        return CoreServer.getDataUploadProgress(dataKey);
    }

    @PostMapping("/runanalysis")
    public List<AnalysisResult> runAnalysis(@RequestBody List<AnalysisSpecification> configurations) throws ResseractException {
        return CoreServer.runAnalysis(configurations);
    }

    @DeleteMapping("/data/{dataKey}")
    public ResponseEntity deleteData(@PathVariable String dataKey) throws ResseractException {
        CoreServer.deleteData(new DataKey(dataKey));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/dashboard/{dashboardName}")
    public ResponseEntity deleteDashboardName(@PathVariable String dashboardName) {
        CoreServer.deleteDashboard(dashboardName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/requiredconfigrations")
    public Map<AnalysisType, List<ConfigKey>> getRequiredConfigurations(@RequestBody List<AnalysisType> analysisTypes) {
        return CoreServer.getRequiredConfigurations(analysisTypes);
    }

    @GetMapping("/datainfo")
    public List<DataInfo> getDataInfo() throws ResseractException {
        return CoreServer.getAllDataInfo();
    }

    @PostMapping(value = "/uploadfile", consumes = "multipart/form-data")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws ResseractException {
        return fileService.store(file);
    }

    @PostMapping("/exportanalysis")
    public List<String> exportAnalysis(@RequestBody List<AnalysisSpecification> specifications) throws ResseractException {
        return CoreServer.exportAnalysis(specifications);
    }

    @GetMapping(value = "/dashboards")
    public List<String> getAllDashboards() {
        return CoreServer.getAllDashboards();
    }

    @GetMapping(value = "/dashboard/{dashboardName}")
    public String getDashboard(@PathVariable String dashboardName) throws ResseractException {
        return CoreServer.getDashboardData(dashboardName);
    }

    @PostMapping(value = "/dashboard/{dashboardName}")
    public ResponseEntity saveDashboard(@PathVariable String dashboardName, @RequestBody String data) {
        CoreServer.saveDashboard(dashboardName, data);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResseractException> handleException(Exception ex) {
        ResseractException resseractException;
        if (ex instanceof ResseractException)
            resseractException = (ResseractException) ex;
        else
            resseractException = new ResseractException(CustomErrorReports.UNKNOWN_ERROR, ex);
        logger.error(resseractException.getMessage(), resseractException);
        return ResponseEntity.status(Constants.RESSERACT_HTTP_ERROR_CODE).body(resseractException);
    }

    @PreDestroy
    public void shutdown() {
        CoreServer.shutdown();
    }
}
