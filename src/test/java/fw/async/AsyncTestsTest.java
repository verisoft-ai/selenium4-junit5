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
package fw.async;


import co.verisoft.fw.async.AsyncListenerImp;
import co.verisoft.fw.async.AsyncTask;
import co.verisoft.fw.async.Observer;
import co.verisoft.fw.extensions.jupiter.JunitLogExtension;
import co.verisoft.fw.selenium.drivers.VerisoftDriver;
import co.verisoft.fw.selenium.drivers.factory.DriverCapabilities;
import co.verisoft.fw.selenium.junit.extensions.DriverInjectionExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith({JunitLogExtension.class, DriverInjectionExtension.class})
public class AsyncTestsTest {

    private static final String pageTestUrl = "file://" +
            new File(System.getProperty("user.dir") +
                    "/src/test/resources/DelegateDriverTestForm.html").getAbsolutePath();


    @DriverCapabilities
    private final DesiredCapabilities capabilities = new DesiredCapabilities();
    {
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("headless", true);
    }


    @Test
    public void shouldInvokeAsyncOperationOnceAfterFindBy(VerisoftDriver driver) throws InterruptedException {
        driver.get(pageTestUrl);

        // Set the async task
        Observer o = new AsyncTask(driver, () -> {
            driver.findElement(By.id("texta")).sendKeys("test1");
            return true;
        });
        driver.async().register(o);

        // Default dispatch is 5 second
        Thread.sleep(5500);
        driver.findElement(By.id("texta"));
        String text = (String) driver.executeScript("return document.getElementById('texta').value");
        assertEquals("test1", text, "text should have been " + "test1");
    }

    @Test
    public void shouldNotBeInvokedTwice(VerisoftDriver driver) throws InterruptedException {

        driver.get(pageTestUrl);

        // Set the async task
        Observer o = new AsyncTask(driver, () -> {
            driver.findElement(By.id("texta")).sendKeys("test1");
            return true;
        });
        driver.async().register(o);

        // First invocation - default dispatch is 5 second
        Thread.sleep(4500);
        driver.findElement(By.id("texta"));
        String text = (String) driver.executeScript("return document.getElementById('texta').value");
        assertEquals("test1", text, "text should have been " + "test1");

        // Change value
        driver.executeScript("document.getElementById('texta').value='delete'");

        // Wait and try again
        Thread.sleep(5500);
        driver.findElement(By.id("texta"));
        text = (String) driver.executeScript("return document.getElementById('texta').value");
        assertNotEquals(text, "test1", "text should have been " + "test1");
    }

    @Test
    public void shouldBeInvokedTwice(VerisoftDriver driver) throws InterruptedException {
        driver.get(pageTestUrl);


        // Set async task
        Observer o = new AsyncTask(driver, () -> {
            driver.findElement(By.id("texta")).sendKeys("test1");
            return false;
        });
        driver.async().register(o);

        // First invocation - default dispatch is 5 second
        Thread.sleep(4500);
        driver.findElement(By.id("texta"));
        String text = (String) driver.executeScript("return document.getElementById('texta').value");
        assertEquals("test1", text, "text should have been " + "test1");

        // Change value
        driver.executeScript("document.getElementById('texta').value=''");

        // Wait and try again
        Thread.sleep(5500);
        driver.findElement(By.id("texta"));
        text = (String) driver.executeScript("return document.getElementById('texta').value");
        assertEquals("test1", text, "text should have been " + "test1");
    }

    @Test
    public void shouldChangeDispatchValues(VerisoftDriver driver)  {
        Duration interval = driver.async().getDispatchInterval();

        // Test default values
        assertEquals(Duration.ofSeconds(5), interval, "default value for interval is 5 seconds");

        // Change values
        driver.async().setDispatchInterval(Duration.ofDays(3));
        interval = driver.async().getDispatchInterval();

        assertEquals(Duration.ofDays(3), interval, "Value was changed to 3 days");
    }

    @Test
    public void shouNotAllowDispatchValuesToBeLessThanOneSecond(VerisoftDriver driver)  {
        Duration interval = driver.async().getDispatchInterval();

        // Test default values
        assertEquals(Duration.ofSeconds(5), interval, "default value for interval is 5 seconds");

        // Change values
        driver.async().setDispatchInterval(Duration.ofSeconds(3));
        interval = driver.async().getDispatchInterval();

        assertEquals(Duration.ofSeconds(5), interval, "Minimum value of interval is 5");
    }

    @Test
    public void DoesNotCrashWhenUnregisterNoObservers(VerisoftDriver driver)  {
        Observer o = new Observer() {
            @Override
            public void update() {
                // No-Op
            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        };

        assertDoesNotThrow(() -> driver.async().unregister(o), "Should not throw an exception");
    }

    @Test
    public void shouldBeInvokedAfterDispatchChange(VerisoftDriver driver) throws InterruptedException {
        driver.get(pageTestUrl);
        driver.async().setDispatchInterval(Duration.ofSeconds(5));
        Observer o = new AsyncTask(driver, () -> {
            driver.findElement(By.id("texta")).sendKeys("test1");
            return false;
        });
        driver.async().register(o);

        // Wait insufficient time - should not be invoked
        Thread.sleep(2000);
        driver.findElement(By.id("texta"));
        String text = (String) driver.executeScript("return document.getElementById('texta').value");
        assertEquals("", text, "text should have been ''");

        // Wait and try again - should  be invoked
        Thread.sleep(3500);
        driver.findElement(By.id("texta"));
        text = (String) driver.executeScript("return document.getElementById('texta').value");
        assertEquals("test1", text, "text should have been test1");
    }

    @Test
    public void shouldNotAllowToCreateTaskWithLessThanOneSecond(VerisoftDriver driver)  {

        // The default
        AsyncListenerImp listener = new AsyncListenerImp(Duration.ofSeconds(1));
        assertEquals(Duration.ofSeconds(5), listener.getDispatchInterval(), "Value should be 5 seconds");

        // more than 1
        listener = new AsyncListenerImp(Duration.ofSeconds(10));
        assertEquals(Duration.ofSeconds(10), listener.getDispatchInterval(), "Value should be 10 seconds");

        // less than 5 - resolve to 5
        listener = new AsyncListenerImp(Duration.ofSeconds(3));
        assertEquals(Duration.ofSeconds(5), listener.getDispatchInterval(), "Value should be 5 seconds");
    }

}
