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


import co.verisoft.fw.selenium.drivers.VerisoftDriverManager;
import co.verisoft.fw.utils.Property;
import co.verisoft.fw.utils.Waits;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.Objects;
import java.util.Set;


/**
 * Default interface the main goal is to concentrate all mainly Java Script functions
 *
 * @author David Yehezkel
 * 29 Mar 2020
 */
public interface WebBasePageJS {

    int timeout = new Property().getIntProperty("selenium_wait_timeout");

    /**
     * click on element by using Java Script
     *
     * @param element Webelement element
     */
    default void clickOnElementByJS(WebElement element) {
        ((JavascriptExecutor) Objects.requireNonNull(VerisoftDriverManager.getDriver()))
                .executeScript("arguments[0].click();", element);
    }


    /**
     * Perform mouse hover on WebElement by Java Script
     *
     * @param element Webelement element
     */
    default void mouseHoverByJS(WebElement element) {
        String mouseOverScript =
                "if(document.createEvent){" +
                        "var evObj = document.createEvent('MouseEvents');" +
                        "evObj.initEvent('mouseover',true, false); " +
                        "arguments[0].dispatchEvent(evObj);} " +
                        "else if(document.createEventObject) { " +
                        "arguments[0].fireEvent('onmouseover');}";

        ((JavascriptExecutor) Objects.requireNonNull(VerisoftDriverManager.getDriver()))
                .executeScript(mouseOverScript, element);
    }


    /**
     * Get RGB color of Pseudo Code CSS from WebElemnt
     *
     * @param element Webelement element
     * @return RGB(Red, Green, Blue)
     */
    default String getBeforePseudoCode(WebElement element) {
        return ((JavascriptExecutor) Objects.requireNonNull(VerisoftDriverManager.getDriver()))
                .executeScript("return window.getComputedStyle(arguments[0], ':before')" +
                                ".getPropertyValue('background-color');"
                        , element).toString();
    }


    /**
     * open new tab
     */
    default void openNewTab() {

        ((JavascriptExecutor) Objects.requireNonNull(VerisoftDriverManager.getDriver()))
                .executeScript("window.open()");
    }


    /**
     * @param rootElement Root shadow element
     * @return return Shadow root element
     */
    default WebElement getShadowRoot(WebElement rootElement) {
        return (WebElement) ((JavascriptExecutor) Objects.requireNonNull(VerisoftDriverManager.getDriver()))
                .executeScript("return arguments[0].shadowRoot", rootElement);
    }


    /**
     * get css of ::before attribute
     *
     * @param element Webelement element
     * @return RGB(red, green, blue, blur)
     */
    default String getColorOfBeforeCssAtter(WebElement element) {
        return ((JavascriptExecutor) Objects.requireNonNull(VerisoftDriverManager.getDriver()))
                .executeScript("return window.getComputedStyle(arguments[0], ':before')" +
                                ".getPropertyValue('background-color');",
                        element).toString();
    }


    /**
     * open new window (tab depend on driver version)
     */
    default void openNewTabAndSwitchToIt() {
        Set<String> windows = VerisoftDriverManager.getDriver().getWindowHandles();
        ((JavascriptExecutor) VerisoftDriverManager.getDriver()).executeScript("window.open();");
        Waits.numberOfWindowsToBeAndSwitchTo(VerisoftDriverManager.getDriver(), timeout,
                windows.size() + 1, windows.size());
    }
}
