package selenium.drivers;

import co.verisoft.fw.extentreport.Description;
import co.verisoft.fw.extentreport.ExtentReport;
import co.verisoft.fw.report.observer.Report;
import co.verisoft.fw.selenium.drivers.VerisoftDriver;
import co.verisoft.fw.selenium.drivers.factory.DriverCapabilities;
import co.verisoft.fw.selenium.drivers.factory.DriverUrl;
import co.verisoft.fw.selenium.junit.extensions.DriverInjectionExtension;
import co.verisoft.fw.selenium.junit.extensions.ScreenShotExtension;
import co.verisoft.fw.selenium.junit.extensions.SeleniumLogExtesion;
import co.verisoft.fw.utils.Asserts;
import io.cloudbeat.junit.CbJunitExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

@ExtentReport
@ExtendWith({DriverInjectionExtension.class, SeleniumLogExtesion.class, ScreenShotExtension.class, CbJunitExtension.class})
public class CloudBeatIntegrationTest{

    @DriverCapabilities
    private DesiredCapabilities capabilities = new DesiredCapabilities();
    {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        capabilities.setBrowserName("chrome");
        options.merge(capabilities);
    }

    
    public CloudBeatIntegrationTest() throws MalformedURLException {
    }

    @Test
    @DisplayName("Search Wikipedia test")
    @Description("This is my description")
    public void searchWikipedia(VerisoftDriver driver) throws InterruptedException {
        driver.get("https://www.wikipedia.org/");
        driver.findElement(By.id("searchInput")).sendKeys("Test Automation");
        new Actions(driver).sendKeys(Keys.ENTER).build().perform();

        String phraseToAssert = "Test automation";

        // Note!! Verisoft Assert
        Asserts.assertTrue(driver.getTitle().contains(phraseToAssert), "Page should contain the pharase " + phraseToAssert);

        Report.info("Got to this point - We are on the right page");
    }
}
