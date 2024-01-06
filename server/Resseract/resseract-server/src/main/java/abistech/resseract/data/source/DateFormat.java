package abistech.resseract.data.source;

import abistech.resseract.data.frame.DataPeriod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class DateFormat {

    private static final Map<String, DateFormat> allFormats = new LinkedHashMap<>();

    static {
        allFormats.put("dd/MMM/yyyy", new DateFormat("dd/MMM/yyyy", DataPeriod.DAILY));
        allFormats.put("dd/MM/yyyy", new DateFormat("dd/MM/yyyy", DataPeriod.DAILY));
        allFormats.put("dd/MMM/yy", new DateFormat("dd/MMM/yy", DataPeriod.DAILY));
        allFormats.put("dd/MM/yy", new DateFormat("dd/MM/yy", DataPeriod.DAILY));
        allFormats.put("MMM/dd/yyyy", new DateFormat("MMM/dd/yyyy", DataPeriod.DAILY));
        allFormats.put("MM/dd/yyyy", new DateFormat("MM/dd/yyyy", DataPeriod.DAILY));
        allFormats.put("MM/dd/yyyy HH:mm", new DateFormat("MM/dd/yyyy HH:mm", DataPeriod.MINUTES));
        allFormats.put("MMM/dd/yy", new DateFormat("MMM/dd/yy", DataPeriod.DAILY));
        allFormats.put("MM/dd/yy", new DateFormat("MM/dd/yy", DataPeriod.DAILY));
        allFormats.put("yyyy/MM/dd", new DateFormat("yyyy/MM/dd", DataPeriod.DAILY));
        allFormats.put("dd-MMM-yyyy", new DateFormat("dd-MMM-yyyy", DataPeriod.DAILY));
        allFormats.put("dd-MM-yyyy", new DateFormat("dd-MM-yyyy", DataPeriod.DAILY));
        allFormats.put("dd-MMM-yy", new DateFormat("dd-MMM-yy", DataPeriod.DAILY));
        allFormats.put("dd-MM-yy", new DateFormat("dd-MM-yy", DataPeriod.DAILY));
        allFormats.put("MMM-dd-yyyy", new DateFormat("MMM-dd-yyyy", DataPeriod.DAILY));
        allFormats.put("MM-dd-yyyy", new DateFormat("MM-dd-yyyy", DataPeriod.DAILY));
        allFormats.put("MMM-dd-yy", new DateFormat("MMM-dd-yy", DataPeriod.DAILY));
        allFormats.put("MM-dd-yy", new DateFormat("MM-dd-yy", DataPeriod.DAILY));
        allFormats.put("MMM/yyyy", new DateFormat("MMM/yyyy", DataPeriod.MONTHLY));
        allFormats.put("MMM/yy", new DateFormat("MMM/yy", DataPeriod.MONTHLY));
        allFormats.put("yyyy-MM", new DateFormat("yyyy-MM", DataPeriod.MONTHLY));
        allFormats.put("yy-MMM", new DateFormat("yy-MMM", DataPeriod.MONTHLY));
        allFormats.put("MMM-yyyy", new DateFormat("MMM-yyyy", DataPeriod.MONTHLY));
        allFormats.put("MMM-yy", new DateFormat("MMM-yy", DataPeriod.MONTHLY));
        allFormats.put("yyyy-MM-dd", new DateFormat("yyyy-MM-dd", DataPeriod.DAILY));
        allFormats.put("yy", new DateFormat("yy", DataPeriod.YEARLY));
        allFormats.put("yyyy", new DateFormat("yyyy", DataPeriod.YEARLY));
    }

    public static String identifyFormat(String dateStr) {
        for (Map.Entry<String, DateFormat> formatEntry : allFormats.entrySet()) {
            try {
                formatEntry.getValue().getSdf().parse(dateStr);
                return formatEntry.getValue().format;
            } catch (ParseException e) {
                // Ignore
            }
        }

        return null;
    }

    public static String[] getAllFormats() {
        return allFormats.keySet().toArray(new String[0]);
    }

    public static DateFormat getDateFormat(String format) {
        if (format == null)
            return null;
        return allFormats.get(format);
    }

    private final String format;
    private final SimpleDateFormat sdf;
    private final DataPeriod period;

    private DateFormat(String format, DataPeriod period) {
        this.format = format;
        this.sdf = new SimpleDateFormat(format);
        this.sdf.setLenient(false);
        this.period = period;
    }

    public String getFormat() {
        return format;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public DataPeriod getPeriod() {
        return period;
    }

}
