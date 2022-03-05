/*
 * (C) Copyright 2022 VeriSoft (http://www.verisoft.co)
 *
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
package utils.locators;

import co.verisoft.fw.selenium.drivers.VerisoftDriver;
import co.verisoft.fw.selenium.drivers.factory.DriverCapabilities;
import co.verisoft.fw.selenium.junit.extensions.DriverInjectionExtension;
import co.verisoft.fw.selenium.junit.extensions.SeleniumLogExtesion;
import co.verisoft.fw.utils.locators.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith({SeleniumLogExtesion.class, DriverInjectionExtension.class})
public class LocatorsTest {

    private static final String pageTestUrl = "file://" +
            new File(System.getProperty("user.dir") +
                    "/src/test/resources/framework.html").getAbsolutePath();


    @DriverCapabilities
    private final DesiredCapabilities capabilities = new DesiredCapabilities();

    {
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("headless", true);
    }


    @Test
    public void shouldFindLocatorFromSeveralLocators(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AnyBy.any(By.id("will-not-find"),
                By.cssSelector("#option1")));
        assertEquals(1, elements.size(), "Should find 1 elements");
    }

    @Test
    public void shouldNotFindLocatorFromSeveralLocators(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AnyBy.any(By.id("will-not-find"),
                By.cssSelector("will-not-filnd2")));
        assertEquals(0, elements.size(), "Should find 0 elements");
    }

    @Test
    public void shouldNotFindLocatorFromDifferentObjects(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AllBy.all(By.xpath("//*[@id='option1']"),
                By.id("option2")));
        assertEquals(0, elements.size(), "Should find 0 elements");
    }

    @Test
    public void shouldFindLocatorFromSameObject(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AllBy.all(By.xpath("//*[@id='option1']"),
                By.name("options")));
        assertEquals(1, elements.size(), "Should find 1 elements");
    }

    @Test
    public void shouldNotFindOption1Object(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(NotBy.not(By.id("option1")));

        boolean foundObject = false;
        for (WebElement e : elements)
            if (e.getAttribute("id").equals("option1")) {
                foundObject = true;
                break;
            }

        assertFalse(foundObject, "Should not find WebElement");
    }

    @Test
    public void shouldFindOption2Object(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(NotBy.not(By.id("option1")));

        boolean foundObject = false;
        for (WebElement e : elements)
            if (e.getAttribute("id").equals("option2")) {
                foundObject = true;
                break;
            }

        assertTrue(foundObject, "Should find WebElement");
    }

    @Test
    public void allByOneLocatorOneFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AllBy.all(By.id("option1")));
        assertEquals(1, elements.size(), "Expected one element to be found");
        assertEquals("option1", elements.get(0).getAttribute("id"));
    }

    @Test
    public void allByOneLocatorNumerousFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AllBy.all(By.name("options")));
        assertTrue(1 < elements.size(), "Expected more than one element to be found");
    }

    @Test
    public void allByOneLocatorNoneFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AllBy.all(By.name("option")));
        assertEquals(0, elements.size(), "Expected no element to be found");
    }

    @Test
    public void allByNumerousLocatorsOneFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AllBy.all(By.name("options"), By.id("option1")));
        assertEquals(1, elements.size(), "Expected one element to be found");
        assertEquals("option1", elements.get(0).getAttribute("id"));
    }

    @Test
    public void allByNumerousLocatorsNoneFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AllBy.all(By.name("option"), By.id("option1")));
        assertEquals(0, elements.size(), "Expected no element to be found");
    }

    @Test
    public void allByNumerousLocatorsNumerousFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AllBy.all(By.name("options"), By.xpath("//*[@extra='yes']")));
        assertTrue(1 < elements.size(), "Expected more than one element to be found");
    }

    @Test
    public void anyByOneLocatorOneFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AnyBy.any(By.id("option1")));
        assertEquals(1, elements.size(), "Expected one element to be found");
        assertEquals("option1", elements.get(0).getAttribute("id"));
    }

    @Test
    public void anyByOneLocatorNumerousFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AllBy.all(By.name("options")));
        assertTrue(1 < elements.size(), "Expected more than one element to be found");
    }

    @Test
    public void anyByOneLocatorNoneFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AllBy.all(By.name("option")));
        assertEquals(0, elements.size(), "Expected no element to be found");
    }

    @Test
    public void anyByNumerousLocatorsOneFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AnyBy.any(By.name("option"), By.id("option1")));
        assertEquals(1, elements.size(), "Expected one element to be found");
        assertEquals("option1", elements.get(0).getAttribute("id"));
    }

    @Test
    public void anyByNumerousLocatorsNoneFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AnyBy.any(By.name("option"), By.id("option3")));
        assertEquals(0, elements.size(), "Expected no element to be found");
    }

    @Test
    public void anyByNumerousLocatorsNumerousFound(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AnyBy.any(By.name("options"), By.id("option3")));
        assertTrue(1 < elements.size(), "Expected more than one element to be found");
    }

    @Test
    public void anyByNumerousLocatorsCollection(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(AnyBy.any(By.name("options"), By.id("test1")));
        assertTrue(2 < elements.size(), "Expected more than one element to be found");
    }

    @Test
    void elementByFoundResults(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(ElementBy.partialText("checkbox"));
        assertTrue(1 <= elements.size(), "Expected more than one element to be found");
    }

    @Test
    void elementByNoResults(VerisoftDriver driver) {
        //although "yes" exists in the document, it is not a value do will not be found
        List<WebElement> elements = driver.findElements(ElementBy.partialText("yes"));
        assertEquals(0, elements.size(), "Expected to found 0 elements");
    }

    @Test
    void inputByOneResult(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        List<WebElement> elements = driver.findElements(InputBy.label("label"));
        assertEquals(1, elements.size(), "Expected to found 1 element");
        assertEquals("texta", elements.get(0).getAttribute("id"), "Expected to find 'texta' ");
    }

    @Test
    void notByNumeroursResultsExpectsPass(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        // This will find all elements, which does not have id="table"
        List<WebElement> elements = driver.findElements(NotBy.not(By.id("table")));

        assertTrue(1 < elements.size(), "Expected to found more than 1 element");
        for (WebElement e : elements) {
            if (e.getAttribute("id").equals("table"))
                fail("Found an element with id: table");
        }

    }

    @Test
    void notByNumeroursResultsExpectsFail(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        boolean found = false;
        // This will find all elements, which does not have id="table"
        List<WebElement> elements = driver.findElements(NotBy.not(By.id("table")));

        assertTrue(1 < elements.size(), "Expected to found more than 1 element");
        for (WebElement e : elements) {
            if (e.getAttribute("id").equals("options"))
                found = true;
        }

        assertTrue(found, "should have found the value 'options' in the DOM");

    }

    @Test
    void tdBySpecificValueCellCol(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        WebElement element = driver.findElement(TdBy.cellLocation(1, 0));
        assertEquals("January", element.getText(), "Expected to found and element with value 'January'");

    }

    @Test
    void tdBySpecificHeaderByCol(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        WebElement element = driver.findElement(TdBy.tableHeader(0));
        assertEquals("Month", element.getText(), "Expected to found and element with value 'January'");

    }
}
