package abistech.resseract.auth.impl;

import abistech.resseract.auth.Authenticator;
import abistech.resseract.auth.Feature;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.Constants;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class DummyAuthenticator implements Authenticator {
    @Override
    public void initialize(Properties properties) throws ResseractException {
    }

    @Override
    public Properties authenticate(String idToken) {
        Properties properties = new Properties();
        if (idToken != null)
            properties.put(Constants.EMAIL_ID, idToken);
        properties.put(Constants.ACCESSIBLE_FEATURES, Arrays.stream(Feature.values()).collect(Collectors.toList()));
        return properties;
    }

    @Override
    public void stop() {

    }
}
