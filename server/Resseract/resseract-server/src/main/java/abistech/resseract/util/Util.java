package abistech.resseract.util;

import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author abisTarun
 */
public class Util {
    private static final Logger logger = LogManager.getLogger(Util.class.getName());

    private Util() {
    }

    public static boolean isValidString(String str) {
        return str != null && !str.equalsIgnoreCase(Constants.NULL) && !str.isEmpty();
    }

    public static String format(double inp) {
        return String.format("%.2f", inp);
    }

    public static String getTempFolderLocation() {
        String dirPath = String.join(File.separator, System.getProperty(Constants.USER_HOME), Constants.RESSERACT_TEMP);
        boolean isWindows = System.getProperty(Constants.OS_NAME).toLowerCase().startsWith("windows");
        if (isWindows) {
            dirPath = dirPath.replace("\\", "/");
        }
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory())
            if (!dir.mkdirs())
                return null;
        return dirPath;
    }

    private static InputStream getResource(String resourcePath) {
        return Objects.requireNonNull(Util.class.getResourceAsStream(resourcePath));
    }

    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
                // Ignore
            }
        }
    }

    public static Boolean parseBoolean(Object string) {
        if (string == null)
            return null;
        return string.toString().equalsIgnoreCase(Constants.ONE) ||
                string.toString().equalsIgnoreCase(Constants.ONE_D) ||
                string.toString().equalsIgnoreCase(Constants.TRUE);
    }

    public static Object getNullValue(DataType dataType) {
        return switch (dataType) {
            case DATE -> new Date(0);
            case NUMERICAL -> -1.0d;
            case CATEGORICAL -> Constants.EMPTY;
            case BOOLEAN -> false;
        };
    }

    public static Properties loadProperties(String profile) throws ResseractException {
        try (InputStream resource = getResource("/application-" + profile + "-properties.json")) {
            logger.debug("Reading properties from " + resource);
            Scanner scanner = new Scanner(resource);
            StringBuilder jsonStr = new StringBuilder();
            while (scanner.hasNext()) {
                jsonStr.append(scanner.nextLine());
            }
            Properties properties = JSONHandler.deserialize(jsonStr.toString(), Properties.class);
            if (properties != null) {
                for (String propName : properties.stringPropertyNames()) {
                    String propertyValue = properties.getProperty(propName);
                    propertyValue = propertyValue.replaceAll("\\{user\\.home}", Objects.requireNonNull(Util.getTempFolderLocation()));
                    properties.put(propName, propertyValue);
                }
            }
            return properties;
        } catch (IOException e) {
            throw new ResseractException(CustomErrorReports.PROPERTIES_FILE_NOT_FOUND, e);
        }
    }
}
