package abistech.resseract.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HistogramUtilTest {

    @Test
    public void testHistogramRandomDouble() {
        // Setup
        List<Double> data = Arrays.asList(4.9, 7.2, 2.8, 0.6, 3.5, 1.7, 8.4, 6.1, 9.0, 5.3, 1.2, 4.7, 8.2,
                0.4, 2.3, 3.1, 6.7, 7.6, 9.3, 5.9, 8.0, 1.6, 2.5, 0.3, 4.2, 9.8, 6.6, 3.4, 7.9, 5.7, 4.3,
                1.8, 9.2, 0.7, 2.6, 8.7, 3.7, 6.2, 5.1, 1.5, 7.4, 0.2, 9.5, 4.1, 6.8, 2.0, 3.8, 8.5, 5.6
        );
        Map<Double, Integer> expectedHistogram = new HashMap<>();
        expectedHistogram.put(0.2, 14);
        expectedHistogram.put(2.6, 11);
        expectedHistogram.put(5.0, 12);
        expectedHistogram.put(7.4, 11);
        expectedHistogram.put(9.8, 1);
        expectedHistogram.put(12.2, 0);

        // Test
        Map<Double, Integer> histogram = HistogramUtil.createHistogramDouble(data);

        // Verify
        Assert.assertEquals(expectedHistogram, histogram);
    }

    @Test
    public void testHistogramBinary() {
        // Setup
        List<Double> data = Arrays.asList(1d, 2d, 1d, 1d, 2d, 2d, 1d, 2d, 1d,
                2d, 1d, 2d, 1d, 1d, 2d, 2d, 1d, 2d, 1d, 2d, 1d, 2d, 1d, 1d, 2d,
                2d, 1d, 2d, 1d, 2d, 1d, 2d, 1d, 1d, 2d, 2d, 1d, 2d, 1d, 2d, 1d);
        Map<Double, Integer> expectedHistogram = new HashMap<>();
        expectedHistogram.put(1.0, 21);
        expectedHistogram.put(2.0, 20);

        // Test
        Map<Double, Integer> histogram = HistogramUtil.createHistogramDouble(data);

        // Verify
        Assert.assertEquals(expectedHistogram, histogram);
    }

    @Test
    public void testHistogramDate() throws ParseException {
        // Setup
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        List<Date> data = Arrays.asList(sdf.parse("2001"), sdf.parse("2002"), sdf.parse("2001"),
                sdf.parse("2001"), sdf.parse("2002"), sdf.parse("2002"), sdf.parse("2001"),
                sdf.parse("2002"), sdf.parse("2001"), sdf.parse("2002"), sdf.parse("2001"),
                sdf.parse("2002"), sdf.parse("2001"), sdf.parse("2001"), sdf.parse("2002"),
                sdf.parse("2002"), sdf.parse("2001"), sdf.parse("2002"), sdf.parse("2001"),
                sdf.parse("2002"), sdf.parse("2001"), sdf.parse("2002"), sdf.parse("2001"),
                sdf.parse("2001"), sdf.parse("2002"), sdf.parse("2002"), sdf.parse("2001"),
                sdf.parse("2002"), sdf.parse("2001"), sdf.parse("2002"), sdf.parse("2001"),
                sdf.parse("2002"), sdf.parse("2001"), sdf.parse("2001"), sdf.parse("2002"),
                sdf.parse("2002"), sdf.parse("2001"), sdf.parse("2002"), sdf.parse("2001"),
                sdf.parse("2002"), sdf.parse("2001"));
        Map<Date, Integer> expectedHistogram = new HashMap<>();
        expectedHistogram.put(new Date(978287400000L), 21); // Mon Jan 01 00:00:00 IST 2001
        expectedHistogram.put(new Date(1009823400000L), 20); // Tue Jan 01 00:00:00 IST 2002

        // Test
        Map<Date, Integer> histogram = HistogramUtil.createHistogramDate(data);

        // Verify
        Assert.assertEquals(expectedHistogram, histogram);
    }
}