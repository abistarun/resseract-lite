package abistech.resseract.async;

import abistech.resseract.auth.AuthenticationService;
import abistech.resseract.util.Constants;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class DaemonContext extends TimerTask {

    private static Timer timer;
    private final Properties properties;

    private DaemonContext(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void run() {
        AuthenticationService.updateProperties(properties);
    }

    public static void initialize() {
        DaemonContext daemonContext = new DaemonContext(AuthenticationService.getProperties());
        timer = new Timer();
        timer.scheduleAtFixedRate(daemonContext, 0, Constants.DAEMON_THREAD_INTERVAL_MS);
    }

    public static void stop() {
        timer.cancel();
    }
}
