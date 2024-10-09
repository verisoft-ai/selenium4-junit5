package co.verisoft.fw.asserts;

import co.verisoft.fw.report.observer.Report;
import co.verisoft.fw.selenium.drivers.VerisoftDriver;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class extends the SoftAsserts class and provides functionality to capture screenshots
 * when assertions fail. It is responsible for saving screenshots in a specified folder and
 * attaching them to the report.
 */
public class SoftAssertsScreenShot extends SoftAsserts {

    static String SCREENSHOT_FOLDER = "target/screenshots/";
    @Setter
    private VerisoftDriver driver;


    public SoftAssertsScreenShot(VerisoftDriver driver) {
        this.driver = driver;
    }

    /**
     * Captures a screenshot and attaches it to the report.
     * This method takes a screenshot using the provided driver, saves it in the designated
     * folder with a unique name based on the current date and time, and attaches it to the
     * report. If an error occurs during the process, an error message is logged to the report.
     */
    public void takeScreenshotAndAttachedToReport() {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dir = new File(SCREENSHOT_FOLDER);
            LocalDateTime now = LocalDateTime.now();

            if (!dir.exists()) {
                Files.createDirectories(dir.toPath());
            }
            DateTimeFormatter fileNameFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
            String fileName = String.format("%s_%s.png",
                    "softAssert_screenshots",
                    fileNameFormatter.format(now));
            File file = new File(dir, fileName);
            FileUtils.deleteQuietly(file);
            FileUtils.moveFile(screenshot, file);
            Report.info("Screenshot of softAssertion failure: ", file);
        } catch (Exception e) {
            Report.info("Failed to take a screenshot: " + e.getMessage());
        }
    }

    /**
     * Handles assertion failures by capturing a screenshot and attaching it to the report.
     *
     * @param message The message to include with the screenshot in the report.
     * @param e       The AssertionError that occurred.
     */
    @Override
    protected void handleFailure(String message, AssertionError e) {
        super.handleFailure(message, e);
        takeScreenshotAndAttachedToReport();
    }
}
