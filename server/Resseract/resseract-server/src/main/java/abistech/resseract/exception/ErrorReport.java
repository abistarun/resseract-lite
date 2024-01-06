package abistech.resseract.exception;

import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorReport {
    private String message;
    private ErrorType type;

    public ErrorReport(final ErrorReport errorReport) {
        this.message = errorReport.message;
        this.type = errorReport.type;
    }
}
