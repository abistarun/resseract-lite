package abistech.resseract.exception;

/**
 * @author abisTarun
 */
public class CustomErrorReports {

    public static final ErrorReport DATA_READ_ERROR = new ErrorReport("Error occurred while reading Data", ErrorType.JAVA_INTERNAL);
    public static final ErrorReport DATA_WRITE_ERROR = new ErrorReport("Error occurred while writing Data", ErrorType.JAVA_INTERNAL);
    public static final ErrorReport DATA_NOT_FOUND = new ErrorReport("Data not present", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport DATA_DUPLICATE = new ErrorReport("Data for given key already present.", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport INVALID_PARAMETERS = new ErrorReport("Invalid parameters.", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport FILE_UPLOAD_ERROR = new ErrorReport("Error while uploading file to server", ErrorType.JAVA_INTERNAL);
    public static final ErrorReport INVALID_NUMBER = new ErrorReport("Invalid number detected for value [%s] in column [%s]", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport RESOURCE_NOT_FOUND = new ErrorReport("Error while loading resource", ErrorType.JAVA_INTERNAL);
    public static final ErrorReport UNKNOWN_ERROR = new ErrorReport("Congrats you found a bug!! Report now!", ErrorType.UNKNOWN_ERROR);
    public static final ErrorReport EXPR_DATA_TYPE_NOT_SUPPORTED = new ErrorReport("Data type not supported by expression evaluator", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport EXPRESSION_EVALUATION_ERROR = new ErrorReport("Exception while evaluation expression", ErrorType.JAVA_INTERNAL);
    public static final ErrorReport INVALID_SORT_COLUMN = new ErrorReport("Invalid sort column. Available sort columns are %s", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport FEATURE_NOT_ACCESSIBLE = new ErrorReport("Feature not accessible for specified role.", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport ANALYSIS_NOT_ACCESSIBLE = new ErrorReport("User is not authorised to access specified analysis.", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport DASHBOARD_NOT_FOUND = new ErrorReport("Dashboard not present", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport PROPERTIES_FILE_NOT_FOUND = new ErrorReport("Properties file not found", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport INVALID_DATA_TYPE = new ErrorReport("Invalid Data Type", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport DB_DRIVER_NOT_FOUND = new ErrorReport("Database driver not found", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport DB_SQL_ERROR = new ErrorReport("SQL Exception : [%s]", ErrorType.JAVA_INTERNAL);
    public static final ErrorReport INVALID_DATA_TYPE_FOR_MAX_FUNCTION = new ErrorReport("Invalid Data Type for Max function", ErrorType.VALIDATION_EXCEPTION);
    public static final ErrorReport INVALID_DATE_FORMAT = new ErrorReport("Invalid Date Format for [%s]", ErrorType.VALIDATION_EXCEPTION);

    private CustomErrorReports() {
    }

}
