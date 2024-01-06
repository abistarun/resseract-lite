package abistech.resseract.auth;

import abistech.resseract.analysis.Analysis;
import abistech.resseract.analysis.AnalysisFactory;
import abistech.resseract.analysis.AnalysisType;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.Constants;
import abistech.resseract.data.source.Source;
import abistech.resseract.data.source.SourceFactory;
import abistech.resseract.data.source.SourceType;

import java.util.*;

public class AuthenticationService {

    private static final ThreadLocal<Properties> threadLocal = new ThreadLocal<>();

    private static Authenticator authenticator;

    private static final Map<Feature, Set<SourceType>> featureVsSourceTypes = new HashMap<>();
    private static final Map<Feature, Set<AnalysisType>> featureVsAnalysisTypes = new HashMap<>();

    public static void initialize(Properties properties) throws ResseractException {
        ClassLoader classLoader = AuthenticationService.class.getClassLoader();
        try {
            authenticator = (Authenticator) classLoader.loadClass(properties.getProperty(Constants.AUTHENTICATOR)).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ResseractException(CustomErrorReports.PROPERTIES_FILE_NOT_FOUND);
        }
        authenticator.initialize(properties);
        initializeFeatureVsSourceTypes();
        initializeFeatureVsAnalysisTypes();
    }

    private static void initializeFeatureVsSourceTypes() {
        for (SourceType curr : SourceType.values()) {
            Source source = SourceFactory.getSource(curr);
            if (source == null)
                continue;
            Feature feature = source.getAuthorizedFeature();
            featureVsSourceTypes.computeIfAbsent(feature, k -> new HashSet<>());
            featureVsSourceTypes.get(feature).add(curr);
        }
    }

    private static void initializeFeatureVsAnalysisTypes() {
        for (AnalysisType curr : AnalysisType.values()) {
            Analysis analysis = AnalysisFactory.getAnalysis(curr);
            if (analysis == null)
                continue;
            Feature feature = analysis.getAuthorizedFeature();
            featureVsAnalysisTypes.computeIfAbsent(feature, k -> new HashSet<>());
            featureVsAnalysisTypes.get(feature).add(curr);
        }
    }

    public static void authenticate(String idToken) throws ResseractException {
        Properties properties = authenticator.authenticate(idToken);
        threadLocal.set(properties);
    }

    @SuppressWarnings("unchecked")
    public static void validateFeature(Feature feature) throws ResseractException {
        Collection<Feature> featureSet = (Collection<Feature>) threadLocal.get().get(Constants.ACCESSIBLE_FEATURES);
        if (!featureSet.contains(feature))
            throw new ResseractException(CustomErrorReports.FEATURE_NOT_ACCESSIBLE);
    }

    public static String getUserIdentifier() {
        return threadLocal.get().getProperty(Constants.EMAIL_ID);
    }

    public static Properties getProperties() {
        return threadLocal.get();
    }

    public static void updateProperties(Properties properties) {
        threadLocal.set(properties);
    }

    public static void shutdown() {
        authenticator.stop();
    }

    @SuppressWarnings("unchecked")
    public static Collection<Feature> getAccessibleFeatures() {
        return (Collection<Feature>) threadLocal.get().get(Constants.ACCESSIBLE_FEATURES);
    }

    public static Set<SourceType> getAccessibleSourceTypes() {
        Set<SourceType> result = new HashSet<>();
        for (Feature feature : getAccessibleFeatures()) {
            if (feature == null || !featureVsSourceTypes.containsKey(feature))
                continue;
            result.addAll(featureVsSourceTypes.get(feature));
        }
        return result;
    }

    private static Set<AnalysisType> getAccessibleAnalysisTypes() {
        Set<AnalysisType> result = new HashSet<>();
        for (Feature feature : getAccessibleFeatures()) {
            if (feature == null || !featureVsAnalysisTypes.containsKey(feature))
                continue;
            result.addAll(featureVsAnalysisTypes.get(feature));
        }
        return result;
    }

    public static void validateAnalysisType(AnalysisType analysisType) throws ResseractException {
        if (!getAccessibleAnalysisTypes().contains(analysisType))
            throw new ResseractException(CustomErrorReports.ANALYSIS_NOT_ACCESSIBLE);
    }
}
