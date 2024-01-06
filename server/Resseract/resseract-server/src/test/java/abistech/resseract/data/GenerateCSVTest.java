package abistech.resseract.data;

import abistech.resseract.util.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateCSVTest {

    @Test
    public void testGenerateCSVBasic() throws ParseException {
        String d1 = "2018-02-01 12:12:13";
        String d2 = "2018-02-02 12:12:13";
        List<Object> index = Arrays.asList(Constants.INDEX_FORMATTER.parse(d1), Constants.INDEX_FORMATTER.parse(d2));

        List<String> columnNames = Arrays.asList("C1", "C2");

        List<List<Object>> columns = new ArrayList<>();
        columns.add(Arrays.asList(1.0d, 2.0d));
        columns.add(Arrays.asList(3.0d, 4.0d));

        String csvString = DataService.generateCSVString(index, columnNames, columns, true, null);
        String expectedString = "\"" + Constants.INDEX + "\"" + Constants.CSV_SEPARATOR + "\"C1\"" + Constants.CSV_SEPARATOR + "\"C2\"" + Constants.NEW_LINE +
                "\"" + d1 + "\"" + Constants.CSV_SEPARATOR + "\"1.0\"" + Constants.CSV_SEPARATOR + "\"3.0\"" + Constants.NEW_LINE +
                "\"" + d2 + "\"" + Constants.CSV_SEPARATOR + "\"2.0\"" + Constants.CSV_SEPARATOR + "\"4.0\"" + Constants.NEW_LINE;

        Assert.assertEquals(expectedString, csvString);
    }

    @Test
    public void testGenerateCSVWithoutHeader() throws ParseException {
        String d1 = "2018-02-01 12:12:13";
        String d2 = "2018-02-02 12:12:13";
        List<Object> index = Arrays.asList(Constants.INDEX_FORMATTER.parse(d1), Constants.INDEX_FORMATTER.parse(d2));

        List<String> columnNames = Arrays.asList("C1", "C2");

        List<List<Object>> columns = new ArrayList<>();
        columns.add(Arrays.asList(1.0d, 2.0d));
        columns.add(Arrays.asList(3.0d, 4.0d));

        String csvString = DataService.generateCSVString(index, columnNames, columns, false, null);
        String expectedString = "\"" + d1 + "\"" + Constants.CSV_SEPARATOR + "\"1.0\"" + Constants.CSV_SEPARATOR + "\"3.0\"" + Constants.NEW_LINE +
                "\"" + d2 + "\"" + Constants.CSV_SEPARATOR + "\"2.0\"" + Constants.CSV_SEPARATOR + "\"4.0\"" + Constants.NEW_LINE;

        Assert.assertEquals(expectedString, csvString);
    }

    @Test
    public void testGenerateCSVWithIdentifier() throws ParseException {
        String d1 = "2018-02-01 12:12:13";
        String d2 = "2018-02-02 12:12:13";
        List<Object> index = Arrays.asList(Constants.INDEX_FORMATTER.parse(d1), Constants.INDEX_FORMATTER.parse(d2));

        List<String> columnNames = Arrays.asList("C1", "C2");

        List<List<Object>> columns = new ArrayList<>();
        columns.add(Arrays.asList(1.0d, 2.0d));
        columns.add(Arrays.asList(3.0d, 4.0d));

        String csvString = DataService.generateCSVString(index, columnNames, columns, true, "Identifier 1");
        String expectedString = "\"" + Constants.INDEX + "\"" + Constants.CSV_SEPARATOR + "\"Identifier\"" + Constants.CSV_SEPARATOR + "\"C1\"" + Constants.CSV_SEPARATOR + "\"C2\"" + Constants.NEW_LINE +
                "\"" + d1 + "\"" + Constants.CSV_SEPARATOR + "\"Identifier 1\"" + Constants.CSV_SEPARATOR + "\"1.0\"" + Constants.CSV_SEPARATOR + "\"3.0\"" + Constants.NEW_LINE +
                "\"" + d2 + "\"" + Constants.CSV_SEPARATOR + "\"Identifier 1\"" + Constants.CSV_SEPARATOR + "\"2.0\"" + Constants.CSV_SEPARATOR + "\"4.0\"" + Constants.NEW_LINE;

        Assert.assertEquals(expectedString, csvString);
    }
}
