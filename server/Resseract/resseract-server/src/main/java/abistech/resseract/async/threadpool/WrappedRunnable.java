package abistech.resseract.async.threadpool;

import abistech.resseract.auth.AuthenticationService;

import java.util.Properties;

public class WrappedRunnable implements Runnable {

    private final Properties properties;
    private final Runnable runnable;

    public WrappedRunnable(Runnable runnable) {
        this.runnable = runnable;
        properties = AuthenticationService.getProperties();
    }

    @Override
    public void run() {
        AuthenticationService.updateProperties(properties);
        runnable.run();
    }
}
