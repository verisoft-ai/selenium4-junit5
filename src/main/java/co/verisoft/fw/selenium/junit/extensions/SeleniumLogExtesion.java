package co.verisoft.fw.selenium.junit.extensions;

import co.verisoft.fw.utils.Property;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;

@Slf4j
/**
 * If activated, it creates a file for WebDriver logs, sets it in the ./target/logs directory, and
 * set the log level (verbose) accourding to property file.
 *
 * @since 0.0.3 (Feb 2022)
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a> @ <a href="http://www.verisoft.co">www.VeriSoft.co</a>
 */
public class SeleniumLogExtesion implements BeforeAllCallback {

    private static boolean executed = false;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (!executed) {
            createWebDriverLogFile();
            executed = true;
        }

    }


    private void createWebDriverLogFile() {

        // Create a directory if does not exists
        String directoryName = System.getProperty("user.dir") + "/target/logs/";
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Create the file
        String logFileName = directoryName + "WebDriverLog.log";
        File logFile = new File(logFileName);


        // Declare the file
        System.setProperty("webdriver.chrome.logfile", logFileName);
        String verbose = new Property("root.config.properties").getProperty("selenium.logs.verbose");
        System.setProperty("webdriver.chrome.verboseLogging", verbose);
    }
}
