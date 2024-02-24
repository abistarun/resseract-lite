package abistech.resseract.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    public static final String RESSERACT_TEMP = ".ress";
    public static final String VALUE = "Value";
    public static final String QUOTE = "\"";
    public static final String DB_DRIVER = "hibernate.connection.driver_class";
    public static final String DB_URL = "hibernate.connection.url";
    public static final String DB_USER = "hibernate.connection.username";
    public static final String DB_PASSWORD = "hibernate.connection.password";
    public static final String DB_CONTEXT_CLASS = "hibernate.current_session_context_class";
    public static final String DB_PLATFORM = "hibernate.dialect";
    public static final String THREAD_POOL_SIZE = "THREAD_POOL_SIZE";
    public static final String INDEX_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat INDEX_FORMATTER = new SimpleDateFormat(INDEX_FORMAT, Locale.ENGLISH);
    public static final String CSV_SEPARATOR = ",";
    public static final String CSV = "\\.csv";
    public static final String INDEX = "Index";
    public static final String NEW_LINE = "\n";
    public static final String NULL = "null";
    public static final int RESSERACT_HTTP_ERROR_CODE = 499;
    public static final String USER_HOME = "user.home";
    public static final String UNDERSCORE = "_";
    public static final String EMPTY = "";
    public static final long DAEMON_THREAD_INTERVAL_MS = 10000;
    public static final String IDENTIFIER = "Identifier";
    public static final String DATA_TYPE = "dataType";
    public static final String DATA_TYPE_POSTFIX = " Data Type";
    public static final String FORMAT_POSTFIX = " Format";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String EMAIL_ID = "email-id";
    public static final String ACCESSIBLE_FEATURES = "accessible-features";
    public static final String AUTHENTICATOR = "AUTHENTICATOR_CLASS";
    public static final String EXPRESSION = "EXPRESSION";
    public static final String OS_NAME = "os.name";
    public static final int CACHE_INTERVAL_SEC = 5;
    static final String ONE = "1";
    static final String ONE_D = "1.0";

    private Constants() {
    }

}
