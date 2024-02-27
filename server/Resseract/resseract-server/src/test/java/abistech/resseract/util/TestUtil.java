package abistech.resseract.util;

import abistech.resseract.CoreServer;
import abistech.resseract.auth.AuthenticationService;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.DataService;
import abistech.resseract.data.source.SourceType;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class TestUtil {

    public static String getResource(String resourcePath) throws ResseractException {
        try {
            return Objects.requireNonNull(TestUtil.class.getResource(resourcePath)).toURI().getPath();
        } catch (URISyntaxException e) {
            throw new ResseractException(CustomErrorReports.RESOURCE_NOT_FOUND);
        }
    }

    public static void initialize() throws ResseractException {
        CoreServer.initialize("test");
        AuthenticationService.authenticate("dummy");
        seedData();
        seedDashboards();
    }

    private static void seedDashboards() {
        DataService.saveDashboard("T1", "D1");
        DataService.saveDashboard("T2", "D2");
        DataService.saveDashboard("T3", "D3");
    }

    private static void seedData() throws ResseractException {
        String directory = getResource("/DataDirectory/Data");
        for (File file : Objects.requireNonNull(new File(directory).listFiles())) {
            if (file.isDirectory()) {
                Config configKeys = getConfig(file);
                configKeys.put(ConfigKey.DATA_KEY, file.getName());
                configKeys.put(ConfigKey.CSV_FILE, file.getPath() + "/Data.csv");
                List<ConfigKey> dataConfigurations = DataService.getDataConfigurations(SourceType.CSV, configKeys);
                for (ConfigKey dataConfiguration : dataConfigurations) {
                    if (configKeys.getProperties().containsKey(dataConfiguration.getKey()))
                        continue;
                    configKeys.put(dataConfiguration, dataConfiguration.getDefaultValue().toString());
                }
                DataService.uploadData(SourceType.CSV, configKeys, false);
            }
        }
    }

    private static Config getConfig(File file) {
        try {
            String propertiesFileName = file.getPath() + "/properties.json";
            InputStream inputStream = new FileInputStream(new File(propertiesFileName));
            Scanner scanner = new Scanner(inputStream);
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNext()) {
                builder.append(scanner.nextLine());
            }
            return JSONHandler.deserialize(builder.toString(), Config.class);
        } catch (Exception e) {
            return new Config();
        }
    }

    private static void clearDatabase() {
        try {
            FileUtils.delete(new File("resseract-test.db"));
        } catch (IOException ignored) {
        }
    }

    public static void closeServer() {
        CoreServer.shutdown();
        clearDatabase();
    }

    public static String readJSONFromAnalysisAssert(String analysisAssertFileName) throws ResseractException {
        String assertJSONFile = TestUtil.getResource("/AnalysisAsserts/" + analysisAssertFileName);
        StringBuilder builder = new StringBuilder();
        try (FileReader fileReader = new FileReader(assertJSONFile);
             BufferedReader br = new BufferedReader(fileReader)) {
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line.trim());
            }
        } catch (Exception e) {
            throw new ResseractException(CustomErrorReports.DATA_READ_ERROR, e);
        }
        return builder.toString();
    }
}
