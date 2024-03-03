package abistech.resseract.util;

import java.util.*;
import java.util.stream.Collectors;

public class HistogramUtil {

    private static final int NUMBER_OF_BINS = 20;

    public static Map<Double, Integer> createHistogramDouble(List<Double> input) {
        List<Double> data = input.stream().filter(Objects::nonNull).collect(Collectors.toList());
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
        // Calculate number of bins dynamically using Freedman-Diaconis rule
        double min = Collections.min(data);
        double max = Collections.max(data);
        // Calculate Inter Quartile Range
        Collections.sort(data);
        int n = data.size();
        int q1Index = (int) Math.ceil(0.25 * (n + 1)) - 1;
        int q3Index = (int) Math.ceil(0.75 * (n + 1)) - 1;
        double q1 = data.get(q1Index);
        double q3 = data.get(q3Index);
        double interQuartileRange = q3 - q1;

        // Calculate Number of Bins
        int numBins = (int) Math.ceil((max - min) / (2 * interQuartileRange * Math.pow(data.size(), -1.0 / 3.0)));
        if (numBins > NUMBER_OF_BINS) {
            numBins = NUMBER_OF_BINS;
        }

        // Calculate bin width
        double binWidth = (max - min) / numBins;

        // Initialize histogram
        Map<Double, Integer> histogram = new TreeMap<>();

        // Initialize bins
        double startBin = min;
        for (int i = 0; i < numBins + 2; i++) {
            histogram.put(Util.round(startBin, 2), 0);
            startBin += binWidth;
        }

        // Populate histogram
        for (double value : data) {
            double bin = Util.round(min + binWidth * (int) ((value - min) / binWidth), 2);
            histogram.put(bin, histogram.get(bin) + 1);
        }

        return histogram;
    }

    public static Map<Date, Integer> createHistogramDate(List<Date> dates) {
        // Convert Date To Long
        List<Long> data = dates.stream().filter(Objects::nonNull).map(Date::getTime).collect(Collectors.toList());
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
        // Calculate number of bins dynamically using Freedman-Diaconis rule
        long min = Collections.min(data);
        long max = Collections.max(data);

        // Calculate Inter Quartile Range
        Collections.sort(data);
        int n = data.size();
        int q1Index = (int) Math.ceil(0.25 * (n + 1)) - 1;
        int q3Index = (int) Math.ceil(0.75 * (n + 1)) - 1;
        long q1 = data.get(q1Index);
        long q3 = data.get(q3Index);
        long interQuartileRange = q3 - q1;

        // Calculate Number of Bins
        int numBins = (int) Math.ceil((max - min) / (2 * interQuartileRange * Math.pow(data.size(), -1.0 / 3.0)));
        if (numBins > NUMBER_OF_BINS) {
            numBins = NUMBER_OF_BINS;
        }

        // Calculate bin width
        long binWidth = (max - min) / numBins;

        // Initialize histogram
        Map<Date, Integer> histogram = new TreeMap<>();

        // Initialize bins
        long startBin = min;
        for (int i = 0; i < numBins + 2; i++) {
            histogram.put(new Date(startBin), 0);
            startBin += binWidth;
        }

        // Populate histogram
        for (double value : data) {
            long bin = min + binWidth * (int) ((value - min) / binWidth);
            histogram.put(new Date(bin), histogram.get(new Date(bin)) + 1);
        }

        return histogram;
    }
}