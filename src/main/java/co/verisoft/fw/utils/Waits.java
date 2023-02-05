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
package co.verisoft.fw.utils;


import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * Handle all kinds of waits for web ,ios ,android elements to appear on the DOM before continue
 *
 * @author David Yehezkel
 * @since 1.9.6
 */
@Log4j2
@ToString
public final class Waits {
    private Waits() {
    }

    /**
     * wait by millisecond
     *
     * @param milliseconds
     */
    public static void milliseconds(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }


    /**
     * wait for alert to be on page
     *
     * @param driver
     * @param timeOut
     * @param by
     * @return Alert
     */
    public static Alert alertIsPresent(WebDriver driver, int timeOut, By by) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.alertIsPresent());
    }

    /**
     * An expectation for checking WebElement with given locator has attribute which contains specific
     * value
     *
     * @param locator   used to define WebElement to check its parameters
     * @param attribute used to define css or html attribute
     * @param value     used as expected attribute value
     * @return Boolean true when element has css or html attribute which contains the value
     */
    public static boolean attributeContains(WebDriver driver, int timeOut, By locator,
                                            String attribute, String value) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.attributeContains(locator, attribute, value));
    }

    /**
     * An expectation for checking WebElement with given locator has attribute which contains specific
     * value
     *
     * @param element   used to check its parameters
     * @param attribute used to define css or html attribute
     * @param value     used as expected attribute value
     * @return Boolean true when element has css or html attribute which contains the value
     */
    public static boolean attributeContains(WebDriver driver, int timeOut, WebElement element,
                                            String attribute, String value) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    /**
     * An expectation for checking WebElement with given locator has attribute with a specific value
     *
     * @param locator   used to find the element
     * @param attribute used to define css or html attribute
     * @param value     used as expected attribute value
     * @return Boolean true when element has css or html attribute with the value
     */
    public static boolean attributeToBe(WebDriver driver, int timeOut, By locator, String attribute, String value) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    /**
     * An expectation for checking given WebElement has attribute with a specific value
     *
     * @param element   used to check its parameters
     * @param attribute used to define css or html attribute
     * @param value     used as expected attribute value
     * @return Boolean true when element has css or html attribute with the value
     */
    public static boolean attributeToBe(WebDriver driver, int timeOut, WebElement element,
                                        String attribute, String value) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    /**
     * An expectation for checking WebElement any non empty value for given attribute
     *
     * @param element   used to check its parameters
     * @param attribute used to define css or html attribute
     * @return Boolean true when element has css or html attribute with non empty value
     */
    public static boolean attributeToBeNotEmpty(WebDriver driver, int timeOut, WebElement element, String attribute) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.attributeToBeNotEmpty(element, attribute));
    }

    /**
     * Determine whether or not this element is selected or not.
     * This operation only applies to inputelements such as checkboxes,
     * options in a select and radio buttons.
     *
     * @param driver
     * @param timeOut
     * @param by
     * @param selected
     * @return true if element was selected
     * false other wise
     */
    public static boolean elementSelectionStateToBe(WebDriver driver, int timeOut, By by, boolean selected) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.elementSelectionStateToBe(by, selected));
    }

    /**
     * Determine whether or not this element is selected or not.
     * This operation only applies to inputelements such as checkboxes,
     * options in a select and radio buttons.
     *
     * @param driver
     * @param timeOut
     * @param element
     * @param selected
     * @return true if element was selected
     * false other wise
     */
    public static boolean elementSelectionStateToBe(WebDriver driver, int timeOut,
                                                    WebElement element, boolean selected) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.elementSelectionStateToBe(element, selected));
    }

    /**
     * An expectation for checking an element is visible and enabled such that you can click it.
     *
     * @param locator used to find the element
     * @return the WebElement once it is located and clickable (visible and enabled)
     */
    public static WebElement elementToBeClickable(WebDriver driver, int timeOut, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * An expectation for checking an element is visible and enabled such that you can click it.
     *
     * @param element the WebElement
     * @return the (same) WebElement once it is clickable (visible and enabled)
     */
    public static WebElement elementToBeClickable(WebDriver driver, int timeOut, WebElement element) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * An expectation for checking if the given element is selected.
     *
     * @param locator By to be selected
     * @return true once the element is selected
     */
    public static boolean elementToBeSelected(WebDriver driver, int timeOut, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.elementToBeSelected(locator));
    }

    /**
     * An expectation for checking if the given element is selected.
     *
     * @param element WebElement to be selected
     * @return true once the element is selected
     */
    public static boolean elementToBeSelected(WebDriver driver, int timeOut, WebElement element) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.elementToBeSelected(element));
    }

    /**
     * An expectation for checking whether the given frame is available to switch to. <p> If the frame
     * is available it switches the given driver to the specified frame.
     *
     * @param locator used to find the frame
     * @return WebDriver instance after frame has been switched
     */
    public static WebDriver frameToBeAvailableAndSwitchToIt(WebDriver driver, int timeOut, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    /**
     * An expectation for checking whether the given frame is available to switch to. <p> If the frame
     * is available it switches the given driver to the specified frameIndex.
     *
     * @param frameLocator used to find the frame (index)
     * @return WebDriver instance after frame has been switched
     */
    public static WebDriver frameToBeAvailableAndSwitchToIt(WebDriver driver, int timeOut, int frameLocator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }

    /**
     * An expectation for checking whether the given frame is available to switch to. <p> If the frame
     * is available it switches the given driver to the specified frame.
     *
     * @param frameLocator used to find the frame (id or name)
     * @return WebDriver instance after frame has been switched
     */
    public static WebDriver frameToBeAvailableAndSwitchToIt(WebDriver driver, int timeOut, String frameLocator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }


    /**
     * An expectation for checking whether the given frame is available to switch to. <p> If the frame
     * is available it switches the given driver to the specified webelement.
     *
     * @param frameLocator used to find the frame (webelement)
     * @return WebDriver instance after frame has been switched
     */
    public static WebDriver frameToBeAvailableAndSwitchToIt(WebDriver driver, int timeOut, WebElement frameLocator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }

    /**
     * An expectation for checking the element to be invisible
     *
     * @param element used to check its invisibility
     * @return Boolean true when elements is not visible anymore
     */
    public static boolean invisibilityOf(WebDriver driver, int timeOut, WebElement element) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * An expectation for checking all elements from given list to be invisible
     *
     * @param elements used to check their invisibility
     * @return Boolean true when all elements are not visible anymore
     */
    public static boolean invisibilityOfAllElements(WebDriver driver, int timeOut, List<WebElement> elements) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.invisibilityOfAllElements(elements));
    }

    /**
     * An expectation for checking all elements from given list to be invisible
     *
     * @param elements used to check their invisibility
     * @return Boolean true when all elements are not visible anymore
     */
    public static boolean invisibilityOfAllElements(WebDriver driver, int timeOut, WebElement... elements) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.invisibilityOfAllElements(elements));
    }

    /**
     * An expectation for checking that an element is either invisible or not present on the DOM.
     *
     * @param locator used to find the element
     * @return true if the element is not displayed or the element doesn't exist or stale element
     */
    public static boolean invisibilityOfElementLocated(WebDriver driver, int timeOut, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * An expectation for checking that an element with text is either invisible or not present on the
     * DOM.
     *
     * @param locator used to find the element
     * @param text    of the element
     * @return true if no such element, stale element or displayed text not equal that provided
     */
    public static boolean invisibilityOfElementWithText(WebDriver driver, int timeOut, By locator, String text) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.invisibilityOfElementWithText(locator, text));
    }

    /**
     * An expectation for checking number of WebElements with given locator
     *
     * @param locator used to find the element
     * @param number  used to define number of elements
     * @return Boolean true when size of elements list is equal to defined
     */
    public static List<WebElement> numberOfElementsToBe(WebDriver driver, int timeOut, By locator, int number) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.numberOfElementsToBe(locator, number));
    }

    /**
     * An expectation for checking number of WebElements with given locator being less than defined
     * number
     *
     * @param locator used to find the element
     * @param number  used to define maximum number of elements
     * @return Boolean true when size of elements list is less than defined
     */
    public static List<WebElement> numberOfElementsToBeLessThan(WebDriver driver, int timeOut, By locator, int number) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.numberOfElementsToBeLessThan(locator, number));
    }

    /**
     * An expectation for checking number of WebElements with given locator being more than defined number
     *
     * @param locator used to find the element
     * @param number  used to define minimum number of elements
     * @return list of found WebElement by locator
     */
    public static List<WebElement> numberOfElementsToBeMoreThan(WebDriver driver, int timeOut,
                                                                By locator, int number) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, number));
    }

    /**
     * An expectation for checking number of window with given by expectedNumberOfWindows
     *
     * @param driver
     * @param timeOut
     * @param expectedNumberOfWindows
     * @return true if the number of window with expectedNumberOfWindows is correct
     */
    public static boolean numberOfWindowsToBe(WebDriver driver, int timeOut, int expectedNumberOfWindows) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows));
    }

    /**
     * An expectation for checking that there is at least one element present on a web page.
     *
     * @param locator used to find the element
     * @return the list of WebElements once they are located
     */
    public static List<WebElement> presenceOfAllElementsLocatedBy(WebDriver driver, int timeOut, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * check for presence Of All Elements element not have to be visible on page only present
     *
     * @param driver
     * @param timeOut
     * @param elements
     * @return the list of WebElements once they are located on page
     * @author David Yehezkel
     */
    public static List<WebElement> presenceOfAllElements(WebDriver driver, int timeOut, WebElement... elements) {
        return (new WebDriverWait(driver, Duration.ofSeconds(timeOut)))
                .until(new ExpectedCondition<List<WebElement>>() {
                    @Override
                    public @Nullable List<WebElement> apply(WebDriver d) {
                        for (WebElement webElement : elements) {
                            if (webElement.getLocation() == null)
                                return null;
                        }
                        return elements.length > 0 ? Arrays.asList(elements) : null;
                    }
                });
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page. This does not
     * necessarily mean that the element is visible.
     *
     * @param locator used to find the element
     * @return the WebElement once it is located
     */
    public static WebElement presenceOfElementLocated(WebDriver driver, int timeOut, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait until an element is no longer attached to the DOM.
     *
     * @param element The element to wait for.
     * @return false if the element is still attached to the DOM, true otherwise.
     */
    public static boolean stalenessOf(WebDriver driver, int timeOut, WebElement element) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.stalenessOf(element));
    }

    /**
     * An expectation for checking WebElement with given locator has text with a value as a part of
     * it
     *
     * @param locator used to find the element
     * @param pattern used as expected text matcher pattern
     * @return Boolean true when element has text value containing @value
     */
    public static boolean textMatches(WebDriver driver, int timeOut, By locator, Pattern pattern) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.textMatches(locator, pattern));
    }

    /**
     * An expectation for checking WebElement with given locator has specific text
     *
     * @param locator used to find the element
     * @param value   used as expected text
     * @return Boolean true when element has text value equal to @value
     */
    public static boolean textToBe(WebDriver driver, int timeOut, By locator, String value) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.textToBe(locator, value));
    }

    /**
     * An expectation for checking if the given text is present in the specified element.
     *
     * @param element the WebElement
     * @param text    to be present in the element
     * @return true once the element contains the given text
     */
    public static boolean textToBePresentInElement(WebDriver driver, int timeOut,
                                                   WebElement element, String text) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    /**
     * An expectation for checking if the given text is present in the element that matches the given
     * locator.
     *
     * @param locator used to find the element
     * @param text    to be present in the element found by the locator
     * @return true once the first element located by locator contains the given text
     */
    public static boolean textToBePresentInElementLocated(WebDriver driver, int timeOut,
                                                          By locator, String text) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * An expectation for checking if the given text is present in the specified elements value
     * attribute.
     *
     * @param locator used to find the element
     * @param text    to be present in the value attribute of the element found by the locator
     * @return true once the value attribute of the first element located by locator contains the
     * given text
     */
    public static boolean textToBePresentInElementValue(WebDriver driver, int timeOut,
                                                        By locator, String text) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.textToBePresentInElementValue(locator, text));
    }

    /**
     * An expectation for checking if the given text is present in the specified elements value
     * attribute.
     *
     * @param element the WebElement
     * @param text    to be present in the element's value attribute
     * @return true once the element's value attribute contains the given text
     */
    public static boolean textToBePresentInElementValue(WebDriver driver, int timeOut, WebElement element,
                                                        String text) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.textToBePresentInElementValue(element, text));
    }

    /**
     * An expectation for checking that the title contains a case-sensitive substring
     *
     * @param title the fragment of title expected
     * @return true when the title matches, false otherwise
     */
    public static boolean titleContains(WebDriver driver, int timeOut, String title) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.titleContains(title));
    }

    /**
     * An expectation for checking the title of a page.
     *
     * @param title the expected title, which must be an exact match
     * @return true when the title matches, false otherwise
     */
    public static boolean titleIs(WebDriver driver, int timeOut, String title) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.titleIs(title));
    }


    /**
     * An expectation for the URL of the current page to contain specific text.
     *
     * @param fraction the fraction of the url that the page should be on
     * @return <code>true</code> when the URL contains the text
     */
    public static boolean urlContains(WebDriver driver, int timeOut, String fraction) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.urlContains(fraction));
    }

    /**
     * Expectation for the URL to match a specific regular expression
     *
     * @param regex the regular expression that the URL should match
     * @return <code>true</code> if the URL matches the specified regular expression
     */
    public static boolean urlMatches(WebDriver driver, int timeOut, String regex) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.urlMatches(regex));
    }


    /**
     * An expectation for the URL of the current page to be a specific url.
     *
     * @param url the url that the page should be on
     * @return <code>true</code> when the URL is what it should be
     */
    public static boolean urlToBe(WebDriver driver, int timeOut, String url) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.urlToBe(url));
    }

    /**
     * An expectation for checking that an element, known to be present on the DOM of a page, is
     * visible. Visibility means that the element is not only displayed but also has a height and
     * width that is greater than 0.
     *
     * @param element the WebElement
     * @return the (same) WebElement once it is visible
     */
    public static WebElement visibilityOf(WebDriver driver, int timeOut, WebElement element) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * An expectation for checking that all elements present on the web page that match the locator
     * are visible. Visibility means that the elements are not only displayed but also have a height
     * and width that is greater than 0.
     *
     * @param elements list of WebElements
     * @return the list of WebElements once they are located
     */
    public static List<WebElement> visibilityOfAllElements(WebDriver driver, int timeOut, List<WebElement> elements) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    /**
     * An expectation for checking that all elements present on the web page that match the locator
     * are visible. Visibility means that the elements are not only displayed but also have a height
     * and width that is greater than 0.
     *
     * @param elements list of WebElements
     * @return the list of WebElements once they are located
     */
    public static List<WebElement> visibilityOfAllElements(WebDriver driver, int timeOut, WebElement... elements) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    /**
     * An expectation for checking that all elements present on the web page that match the locator
     * are visible. Visibility means that the elements are not only displayed but also have a height
     * and width that is greater than 0.
     *
     * @param locator used to find the element
     * @return the list of WebElements once they are located
     */
    public static List<WebElement> visibilityOfAllElementsLocatedBy(WebDriver driver, int timeOut, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page and visible.
     * Visibility means that the element is not only displayed but also has a height and width that is
     * greater than 0.
     *
     * @param locator used to find the element
     * @return the WebElement once it is located and visible
     */
    public static WebElement visibilityOfElementLocated(WebDriver driver, int timeOut, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    /**
     * @param driver
     * @param timeOut
     * @param by
     * @param textToBeContains
     * @return
     */
    public static boolean containsText(WebDriver driver, int timeOut, By by, String textToBeContains) {
        (new WebDriverWait(driver, Duration.ofSeconds(timeOut)))
                .until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver d) {
                        if (d.findElement(by).getText().contains(textToBeContains))
                            return true;
                        else
                            return false;
                    }
                });
        return true;
    }

    public static void pageToFullyLoad(WebDriver driver, int timeOut) {
        (new WebDriverWait(driver, Duration.ofSeconds(timeOut))).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState").equals("complete");
            }
        });
    }

    /**
     * waits for number of window to be user expected number of windows and switch to user window number
     *
     * @param driver
     * @param timeOut
     * @param expectedNumberOfWindows
     * @param switchToWindowNumber
     */
    public static void numberOfWindowsToBeAndSwitchTo(WebDriver driver, int timeOut, int expectedNumberOfWindows,
                                                      int switchToWindowNumber) {
        numberOfWindowsToBe(driver, timeOut, expectedNumberOfWindows);
        Set<String> windows = driver.getWindowHandles();
        driver.switchTo().window(windows.toArray()[switchToWindowNumber].toString());
    }
}
