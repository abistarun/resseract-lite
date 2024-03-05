package abistech.resseract.util;

import java.util.*;
import java.util.stream.Collectors;

public class HistogramUtil {

    private static final int NUMBER_OF_BINS = 20;

    public static Map<Double, Integer> createHistogramDouble(List<Double> input) {
        List<Double> data = input.stream().filter(Objects::nonNull).toList();
        long uniqueCount = data.stream().distinct().count();
        if (uniqueCount < NUMBER_OF_BINS * 2) {
            Map<Double, Integer> valueCount = new HashMap<>();
            for (Double d : data) {
                if (!valueCount.containsKey(d)) {
                    valueCount.put(d, 0);
                }
                valueCount.put(d, valueCount.get(d) + 1);
            }
            return valueCount;
        }
        double min = Collections.min(data);
        double max = Collections.max(data);

        // Calculate bin width
        double binWidth = (max - min) / NUMBER_OF_BINS;

        // Initialize histogram
        int numberOfBins = NUMBER_OF_BINS + 2;
        double[] binsStarts = new double[numberOfBins];
        int[] bins = new int[numberOfBins];
        // Initialize bins
        double startBin = min;
        for (int i = 0; i < numberOfBins; i++) {
            binsStarts[i] = startBin;
            startBin += binWidth;
        }

        // Populate histogram
        for (double value : data) {
            for (int i = 0; i < numberOfBins; i++) {
                if (value >= binsStarts[i] && value < binsStarts[i + 1]) {
                    bins[i]++;
                    break;
                }
            }
        }
        Map<Double, Integer> histogram = new HashMap<>();
        for (int i = 0; i < numberOfBins; i++) {
            histogram.put(Util.round(binsStarts[i]), bins[i]);
        }
        return histogram;
    }

    public static Map<Date, Integer> createHistogramDate(List<Date> dates) {
        // Convert Date To Long
        List<Long> data = dates.stream().filter(Objects::nonNull).map(Date::getTime).toList();
        long uniqueCount = data.stream().distinct().count();
        if (uniqueCount < NUMBER_OF_BINS * 2) {
            Map<Date, Integer> valueCount = new HashMap<>();
            for (Long d : data) {
                Date key = new Date(d);
                if (!valueCount.containsKey(key)) {
                    valueCount.put(key, 0);
                }
                valueCount.put(key, valueCount.get(key) + 1);
            }
            return valueCount;
        }
        long min = Collections.min(data);
        long max = Collections.max(data);

        // Calculate bin width
        long binWidth = (max - min) / NUMBER_OF_BINS;

        // Initialize histogram
        int numberOfBins = NUMBER_OF_BINS + 2;
        long[] binsStarts = new long[numberOfBins];
        int[] bins = new int[numberOfBins];
        // Initialize bins
        long startBin = min;
        for (int i = 0; i < numberOfBins; i++) {
            binsStarts[i] = startBin;
            startBin += binWidth;
        }

        // Populate histogram
        for (double value : data) {
            for (int i = 0; i < numberOfBins; i++) {
                if (value >= binsStarts[i] && value < binsStarts[i + 1]) {
                    bins[i]++;
                    break;
                }
            }
        }
        Map<Date, Integer> histogram = new HashMap<>();
        for (int i = 0; i < numberOfBins; i++) {
            histogram.put(new Date(binsStarts[i]), bins[i]);
        }
        return histogram;
    }
}