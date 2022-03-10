/*
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
package co.verisoft.fw.pages;


import co.verisoft.fw.selenium.drivers.VerisoftMobileDriver;
import co.verisoft.fw.utils.Property;
import co.verisoft.fw.utils.Waits;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.Arrays;


/**
 * Represent base page for WebDriver interface (Web, mobile, client - server)
 * This class contains the basic common functionality shared by all pages
 *
 * @author David Yehezkel
 * 30 Mar 2020
 */
@ToString
@Slf4j
public abstract class BasePage {

    protected WebDriver driver;
    protected final int timeOut;
    protected final int pollingInterval;


    /**
     * C-tor. Initializes generic properties such as timeOut and pollingInterval
     *
     * @param driver a WebDriver object to store and use
     */
    public BasePage(WebDriver driver) {
        Property prop = new Property();
        timeOut = prop.getIntProperty("selenium.wait.timeout");
        pollingInterval = prop.getIntProperty("polling.interval");
        this.driver = driver;
        if (driver instanceof VerisoftMobileDriver)
            PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        else
            PageFactory.initElements(driver, this);

    }


    /**
     * This is a default implementation of isOnPage.
     * It receives one or more WebElements and checks if they are present
     *
     * @param elements One or more WebElements to check for presense
     * @return true- all elements specified were present, false - otherwise
     */
    public boolean isOnPage(WebElement... elements) {
        try {
            Waits.presenceOfAllElements(driver, timeOut, elements);
            log.info("elements " + Arrays.toString(elements) + "was present on page");
            return true;
        } catch (Exception e) {
            log.info("elements " + Arrays.toString(elements) + "wasn't present on page");
            return false;
        }
    }


    /**
     * check if the main page url contains the text
     *
     * @param fraction part of url to be search for
     * @return true if text contains false other wise
     */
    public boolean urlContains(String fraction) {
        try {
            Waits.urlContains(driver, timeOut / 10, fraction);
            return true;
        } catch (Exception e) {
            log.debug("url: " + driver.getCurrentUrl() + " not contains " + fraction);
            return false;
        }
    }
}
