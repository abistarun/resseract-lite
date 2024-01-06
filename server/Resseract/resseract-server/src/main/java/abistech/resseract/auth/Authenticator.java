package abistech.resseract.auth;

import abistech.resseract.exception.ResseractException;

import java.util.Properties;

public interface Authenticator {

    void initialize(Properties properties) throws ResseractException;

    Properties authenticate(String uid) throws ResseractException;

    void stop();
}
