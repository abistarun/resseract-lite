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
        List<Double> data = Arrays.asList(3.5d, 3d, 3.2d, 3.1d, 3.6d, 3.9d, 3.4d, 3.4d, 2.9d, 3.1d, 3.7d, 3.4d, 3d, 3d,
                4d, 4.4d, 3.9d, 3.5d, 3.8d, 3.8d, 3.4d, 3.7d, 3.6d, 3.3d, 3.4d, 3d, 3.4d, 3.5d, 3.4d,
                3.2d, 3.1d, 3.4d, 4.1d, 4.2d, 3.1d, 3.2d, 3.5d, 3.6d, 3d, 3.4d, 3.5d, 2.3d, 3.2d, 3.5d,
                3.8d, 3d, 3.8d, 3.2d, 3.7d, 3.3d, 3.2d, 3.2d, 3.1d, 2.3d, 2.8d, 2.8d, 3.3d, 2.4d, 2.9d,
                2.7d, 2d, 3d, 2.2d, 2.9d, 2.9d, 3.1d, 3d, 2.7d, 2.2d, 2.5d, 3.2d, 2.8d, 2.5d, 2.8d, 2.9d,
                3d, 2.8d, 3d, 2.9d, 2.6d, 2.4d, 2.4d, 2.7d, 2.7d, 3d, 3.4d, 3.1d, 2.3d, 3d, 2.5d, 2.6d,
                3d, 2.6d, 2.3d, 2.7d, 3d, 2.9d, 2.9d, 2.5d, 2.8d, 3.3d, 2.7d, 3d, 2.9d, 3d, 3d, 2.5d, 2.9d,
                2.5d, 3.6d, 3.2d, 2.7d, 3d, 2.5d, 2.8d, 3.2d, 3d, 3.8d, 2.6d, 2.2d, 3.2d, 2.8d, 2.8d, 2.7d,
                3.3d, 3.2d, 2.8d, 3d, 2.8d, 3d, 2.8d, 3.8d, 2.8d, 2.8d, 2.6d, 3d, 3.4d, 3.1d, 3d, 3.1d,
                3.1d, 3.1d, 2.7d, 3.2d, 3.3d, 3d, 2.5d, 3d, 3.4d, 3d);
        Map<Double, Integer> expectedHistogram = new HashMap<>();
        expectedHistogram.put(2.0, 4);
        expectedHistogram.put(2.22, 7);
        expectedHistogram.put(2.44, 13);
        expectedHistogram.put(2.65, 23);
        expectedHistogram.put(2.87, 36);
        expectedHistogram.put(3.09, 30);
        expectedHistogram.put(3.31, 18);
        expectedHistogram.put(3.53, 7);
        expectedHistogram.put(3.75, 8);
        expectedHistogram.put(3.96, 2);
        expectedHistogram.put(4.18, 1);
        expectedHistogram.put(4.4, 1);

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
        expectedHistogram.put(1.5, 0);
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
        expectedHistogram.put(new Date(994055400000L), 0); // Mon Jul 02 12:00:00 IST 2001
        expectedHistogram.put(new Date(1009823400000L), 20); // Tue Jan 01 00:00:00 IST 2002

        // Test
        Map<Date, Integer> histogram = HistogramUtil.createHistogramDate(data);

        // Verify
        Assert.assertEquals(expectedHistogram, histogram);
    }
}