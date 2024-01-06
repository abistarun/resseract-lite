package abistech.resseract.exception;

import lombok.Getter;
@Getter
public class ResseractException extends Exception {

    private final ErrorReport errorReport;

    public ResseractException(ErrorReport errorReport, Object... params) {
        super(String.format(errorReport.getMessage(), params));
        errorReport = new ErrorReport(errorReport);
        errorReport.setMessage(String.format(errorReport.getMessage(), params));
        this.errorReport = errorReport;
    }

    public ResseractException(ErrorReport errorReport, Throwable causedBy, Object... params) {
        super(String.format(errorReport.getMessage(), params), causedBy);
        errorReport = new ErrorReport(errorReport);
        errorReport.setMessage(String.format(errorReport.getMessage(), params));
        this.errorReport = errorReport;
    }
}
