package selenium.drivers;/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import co.verisoft.fw.selenium.drivers.VerisoftDriver;
import co.verisoft.fw.selenium.junit.extensions.SeleniumLogExtesion;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// TODO: add another test class for grid and remte private String url = "http://localhost:4444/wd/hub";
// TODO: Change asserts to soft asserts so the driver.close() will always run, even if test fails
// TODO: add conditional tests - only run if there is appium server installed

/**
 * Note! Each test creates a new instance of VerisoftDriver since WebDriver does not support multi-thread and this
 * is a way to run the tests in parallel.
 */
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(SeleniumLogExtesion.class)
public class VerisoftDriverAPITest {

    private static String pageTestUrl = "file://" +
            new File(System.getProperty("user.dir") +
                    "/src/test/resources/DelegateDriverTestForm.html").getAbsolutePath();


    private ChromeOptions capabilities = new ChromeOptions();
    {
        capabilities.addArguments("--headless");
    }


    @Test
    public void shouldClickOnCheckbox() {
        // Set up
        VerisoftDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);
        WebElement element = driver.findElement(By.id("checkbox"));
        assertFalse(element.isSelected());
        element.click();
        assertTrue(element.isSelected(), "checkbox should be selected");

        driver.quit();

    }

    @Test
    public void shouldRetrieveUrlUsingGet() {

        // Set up
        WebDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);

        //Assert
        assertTrue(driver.getCurrentUrl().contains("DelegateDriverTestForm"), "Page url should contain DelegateDriverTestForm");

        // Clean up
        driver.quit();
    }

    @Test
    public void shouldFindElementUsingAllStrategiesTest() {

        // Set up
        VerisoftDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);

        List<WebElement> findByClassName = driver.findElements(By.className("fielda"));
        assertFalse(findByClassName.isEmpty(), "findByClassName list should not be empty");

        List<WebElement> findBycssSelector = driver.findElements(By.cssSelector("input[id='texta']"));
        assertFalse(findBycssSelector.isEmpty(), "findBycssSelector list should not be empty");

        List<WebElement> findById = driver.findElements(By.id("texta"));
        assertFalse(findById.isEmpty(), "findById list should not be empty");

        List<WebElement> findByLinkText = driver.findElements(By.linkText("new tab"));
        assertFalse(findByLinkText.isEmpty(), "findByLinkText list should not be empty");

        List<WebElement> findByName = driver.findElements(By.name("checkboxInput"));
        assertFalse(findByName.isEmpty(), "findByName list should not be empty");

        List<WebElement> findByPartialLinkText = driver.findElements(By.partialLinkText("window"));
        assertFalse(findByPartialLinkText.isEmpty(), "findByPartialLinkText list should not be empty");

        List<WebElement> findByTagName = driver.findElements(By.tagName("select"));
        assertFalse(findByTagName.isEmpty(), "findByTagName list should not be empty");

        List<WebElement> findByXPath = driver.findElements(By.xpath("//input[@id='texta']"));
        assertFalse(findByXPath.isEmpty(), "findByXPath list should not be empty");

        // Clean up
        driver.quit();

    }

    @Test
    public void shouldRetrievePageSource() {

        // Set up
        VerisoftDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);
        String pageSource = driver.getPageSource();

        // Assert
        assertTrue(pageSource.contains("html"), "Expected page source. Got " + pageSource);

        // Clean up
        driver.quit();
    }

    @Test
    public void shouldGetScreenshotAndStoreAsFile() throws IOException, InterruptedException {

        // Set up
        WebDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);
        File destFile = new File("./target/ScreenShot/img.png");
        FileUtils.copyFile(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE), destFile);

        // Assert
        assertTrue(destFile.exists(), "File does not exist");

        // Clean up
        driver.quit();
    }

    @Test
    public void shouldRetrieveTitleUsingGetTitle() {

        // Set up
        VerisoftDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);
        String title = driver.getTitle();

        // Assert
        assertEquals("DelegateDriverTestForm", title, "expected title to be 'DelegateDriverTestForm', received " + title);

        // Clean up
        driver.quit();
    }

    @Test
    public void shouldGetWindowHandle() {

        // Set up
        VerisoftDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);
        String windowId = driver.getWindowHandle();

        // Assert
        assertFalse(windowId.isEmpty(), "exptected to have window handle ");

        // Clean up
        driver.quit();
    }

    @Test
    public void shouldGetMultipleWindowHandles() {
        // Set up
        VerisoftDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);
        Set<String> allWindows = driver.getWindowHandles();
        assertEquals(allWindows.size(), 1, "Should contain 1 window handle");

        // Open a second tab
        driver.executeScript("window.open()");
        allWindows = driver.getWindowHandles();

        driver.switchTo().window((String) allWindows.toArray()[1]);
        driver.get(pageTestUrl);
        allWindows = driver.getWindowHandles();
        assertEquals(allWindows.size(), 2);

        // Clean up
        driver.quit();
    }

    @Test
    public void shouldBeAbleToSwitchWindows() {
        // Set up
        VerisoftDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);
        Set<String> allWindows = driver.getWindowHandles();
        assertEquals(allWindows.size(), 1, "Should contain 1 window handle");

        // Open a second tab
        driver.executeScript("window.open()");
        allWindows = driver.getWindowHandles();

        driver.switchTo().window((String) allWindows.toArray()[1]);
        driver.get(pageTestUrl);

        // Check the checkbox
        WebElement element = driver.findElement(By.id("checkbox"));
        element.click();
        assertTrue(element.isSelected(), "checkbox should be selected");

        // Switch back to original window
        driver.switchTo().window((String) allWindows.toArray()[0]);
        element = driver.findElement(By.id("checkbox"));
        assertFalse(element.isSelected(), "checkbox in original window should not be selected");

        // Clean up
        driver.quit();

    }

    @Test
    public void shouldRunAsyncScript() {
        // Set up
        VerisoftDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);
        long time = System.currentTimeMillis();
        driver.executeAsyncScript("window.setTimeout(arguments[arguments.length - 1], 5000)");
        assertTrue(System.currentTimeMillis() - time > 4500, "Should wait 5 seconds");

        // Clean up
        driver.quit();

    }


    @Test
    public void shouldSupportNavigateOperations() {

        // Set up
        VerisoftDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);
        assertEquals("DelegateDriverTestForm", driver.getTitle());

        driver.navigate().back();
        assertEquals("", driver.getTitle());
        driver.navigate().forward();
        assertEquals("DelegateDriverTestForm", driver.getTitle());

        driver.navigate().refresh();
        assertEquals("DelegateDriverTestForm", driver.getTitle());

        driver.navigate().to(pageTestUrl);
        assertEquals("DelegateDriverTestForm", driver.getTitle());

        // Clean up
        driver.quit();
    }


    //@Test
    public void mouseHover() {
        // Set up
        VerisoftDriver driver = new VerisoftDriver(capabilities);

        // Test
        driver.get(pageTestUrl);
        //WebElement x = Waits.elementToBeClickable(driver, 30, By.id("tab"));
        Actions ac = new Actions(driver);
        //ac.moveToElement(x).click().build().perform();

        // Assert
        Set<String> handles = driver.getWindowHandles();
        assertEquals(2, handles.size(), "Should have 2 windows after action");

        // Clean up
        driver.quit();

    }


}