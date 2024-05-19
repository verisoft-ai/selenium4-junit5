package co.verisoft.fw.selenium.junit.extensions;

import co.verisoft.fw.selenium.drivers.VerisoftDriverManager;
import co.verisoft.fw.store.StoreManager;
import co.verisoft.fw.store.StoreType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
/**
 * If activated, extension creates a screenshot when a test failed, and pushes it's name into the
 * local thread store
 *
 * @since 0.0.4 (Apr 2022)
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a> @ <a href="http://www.verisoft.co">www.VeriSoft.co</a>
 */
public class ScreenShotExtension implements TestExecutionExceptionHandler {
    /**
     * If test has failed, take a screenshot and put it in the store
     *
     * @param extensionContext
     * @param throwable
     * @throws Throwable
     */
    @Override
    public void handleTestExecutionException(ExtensionContext extensionContext, Throwable throwable) throws Throwable {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime now = LocalDateTime.now();

        Map<String, WebDriver> drivers = VerisoftDriverManager.getDrivers();

        if (drivers == null || drivers.isEmpty()) {
            log.error("No drivers available. No screenshots can be captured.");
            throw throwable;
        }

        for (Map.Entry<String, WebDriver> entry : drivers.entrySet()) {
            String driverName = entry.getKey();
            WebDriver driver = entry.getValue();

            if (driver == null) {
                log.error("Cannot retrieve driver - driver is null for " + driverName);
                continue;
            }

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dir = new File("target/screenshots/");

            if (!dir.exists()) {
                Files.createDirectories(dir.toPath());
            }

            Method method = extensionContext.getTestMethod().get();
            DateTimeFormatter fileNameFormatter = DateTimeFormatter.ofPattern("HHmmss");
            File file = new File(dir, String.format("%s_%s_%s_%s.png",
                    method.getDeclaringClass().getName(),
                    method.getName(),
                    driverName,
                    fileNameFormatter.format(now)));

            FileUtils.deleteQuietly(file);
            FileUtils.moveFile(screenshot, file);

            // Get the screenshot list from the store and put the value
            Map<String, List<String>> screenShots = StoreManager.getStore(StoreType.LOCAL_THREAD)
                    .getValueFromStore("screenshots");

            List<String> paths = Objects.isNull(screenShots.get(extensionContext.getDisplayName())) ?
                    new ArrayList<>() :
                    screenShots.get(extensionContext.getDisplayName());

            paths.add(file.getName());
            screenShots.put(extensionContext.getDisplayName(), paths);
        }

        throw throwable;
    }
}

