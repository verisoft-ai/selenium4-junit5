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
package co.verisoft.fw.pages;


import co.verisoft.fw.selenium.drivers.VerisoftMobileDriver;
import co.verisoft.fw.utils.Waits;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

/**
 * Basic Mobile POM functionalities.
 *
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a> @ <a href="http://www.verisoft.co">www.VeriSoft.co</a>
 * @since 2.0.1
 */
@Slf4j
@ToString
public abstract class MobileBasePage extends BasePage {

    protected VerisoftMobileDriver driver;

    protected MobileBasePage(WebDriver driver) {
        super(driver);
    }

    public MobileBasePage(VerisoftMobileDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(new AppiumFieldDecorator(driver), this);

        log.debug("Created new page object instance: " + this.getClass());
    }


    /**
     * swap with x , y params
     *
     * @param percentHeightStart - start from the window Dimension height size
     * @param percentHeightEnd   - end at the window Dimension height size
     * @param xStart             - x start  coordinate
     * @param xEnd               - x end coordinate
     * @param secDuration        how much to wait between press and move actions
     * @author david
     */
    @SuppressWarnings("rawtypes")
    public void swipeScreenByTouchAction(double percentHeightStart, double percentHeightEnd, int xStart,
                                         int xEnd, int secDuration) {
        Dimension size = driver.manage().window().getSize();
        double scrollHeightStart = size.getHeight() * percentHeightStart; //0.5
        int scrollStart = (int) scrollHeightStart;
        double scrollHeightEnd = size.getHeight() * percentHeightEnd; //0.2
        int scrollEnd = (int) scrollHeightEnd;
        new TouchAction((PerformsTouchActions) driver.getWrappedDriver())
                .press(PointOption.point(xStart, scrollStart))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(secDuration)))
                .moveTo(PointOption.point(xEnd, scrollEnd))
                .release().perform();
    }


    public void swipeByTouchActionInsideContainerElement(By containerBy, double percentHeightStart,
                                                         double percentHeightEnd, int secDuration) {
        int xStart = Waits.presenceOfElementLocated(driver, timeOut, containerBy).getLocation().getX();
        int xEnd = Waits.presenceOfElementLocated(driver, timeOut, containerBy).getLocation().getX();
        swipeScreenByTouchAction(percentHeightStart, percentHeightEnd, xStart, xEnd, secDuration);
    }

    public void swipeByTouchActionInsideContainerElement(WebElement containerElement,
                                                         double percentHeightStart,
                                                         double percentHeightEnd, int secDuration) {
        int xStart = Waits.presenceOfAllElements(driver, secDuration, containerElement)
                .get(0).getLocation().getX();

        int xEnd = Waits.presenceOfAllElements(driver, secDuration, containerElement)
                .get(0).getLocation().getX();

        swipeScreenByTouchAction(percentHeightStart, percentHeightEnd, xStart, xEnd, secDuration);
    }


    /**
     * scroll into view to element on screen
     *
     * @param by              by to the element needed to find
     * @param scrollingRounds how much round to try find
     * @return element if found other wise throw ElementNotVisibleException
     */
    public WebElement scrollInToView(By by, int scrollingRounds) {
        int rounds = 0;
        while (rounds < scrollingRounds) {
            try {
                WebElement element = Waits.visibilityOfElementLocated(driver, timeOut / 6, by);
                return element;
            } catch (Exception e) {
                rounds++;
                swipeScreenByTouchAction(0.8, 0.2, 30,
                        30, 3);
            }
        }
        throw new ElementNotVisibleException("element : " + by.toString());
    }


    /**
     * scroll into view inside container element
     *
     * @param containerElement   - container element
     * @param by                 - by to the element needed to find
     * @param scrollingRounds    - how much round to try find
     * @param percentHeightStart
     * @param percentHeightEnd
     * @param secDuration
     * @return element if found other wise throw ElementNotVisibleException
     */
    public WebElement scrollInToViewElement(WebElement containerElement, By by, int scrollingRounds,
                                            double percentHeightStart, double percentHeightEnd, int secDuration) {
        int rounds = 0;
        while (rounds < scrollingRounds) {
            try {
                WebElement element = Waits.visibilityOfElementLocated(driver, timeOut / 6, by);
                return element;
            } catch (Exception e) {
                rounds++;
                swipeByTouchActionInsideContainerElement(containerElement, percentHeightStart,
                        percentHeightEnd, secDuration);
            }
        }
        throw new ElementNotVisibleException("Container Element : " + containerElement.toString() + " By : " +
                by.toString());
    }
}
