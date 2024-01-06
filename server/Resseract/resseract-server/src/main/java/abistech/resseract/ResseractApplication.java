package abistech.resseract;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@SpringBootApplication
public class ResseractApplication {
    private static final Logger logger = LogManager.getLogger(ResseractApplication.class.getName());

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "spring");
        SpringApplication.run(ResseractApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowserAfterStartup() {
        try {
            launchAppInBrowser();
        } catch (IOException e) {
            logger.error("Unable to launch app in browser. Please go to http://localhost:8242 to access resseract");
        }
    }

    private static void launchAppInBrowser() throws IOException {
        Runtime rt = Runtime.getRuntime();
        String url = "http://localhost:8242";

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else if (os.contains("mac")) {
            rt.exec("open " + url);
        } else if (os.contains("nix") || os.contains("nux")) {
            String[] browsers = { "google-chrome", "firefox", "mozilla", "epiphany", "konqueror",
                    "netscape", "opera", "links", "lynx" };

            StringBuilder cmd = new StringBuilder();
            for (int i = 0; i < browsers.length; i++)
                if(i == 0)
                    cmd.append(String.format(    "%s \"%s\"", browsers[i], url));
                else
                    cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
            rt.exec(new String[] { "sh", "-c", cmd.toString() });
        }
    }
}