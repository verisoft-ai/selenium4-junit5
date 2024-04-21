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


import co.verisoft.fw.utils.Waits;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

/**
 * Represent expected functionality of any web base page
 *
 * @author David Yehezkel
 * 29 Mar 2020
 */
@ToString
@Slf4j
public abstract class WebBasePage extends BasePage implements WebBasePageJS {


    protected WebDriver driver;

    public WebBasePage(WebDriver driver) {
        this(driver,true);
    }
    public WebBasePage(WebDriver driver, boolean waitForPageToLoad) {
        super(driver);
        this.driver = driver;

        if (waitForPageToLoad) {
            // Wait for the page to be fully loaded before continuing
            Waits.pageToFullyLoad(driver, timeOut);
        }

        log.debug("Created new page object instance: " + this.getClass());
    }

    public WebBasePage(WebDriver driver, boolean waitForPageToLoad, String objectRepositoryFilePath) {
        super(driver, objectRepositoryFilePath);
        this.driver = driver;

        if (waitForPageToLoad) {
            // Wait for the page to be fully loaded before continuing
            Waits.pageToFullyLoad(driver, timeOut);
        }

        log.debug("Created new page object instance: " + this.getClass());
    }

    /**
     * mouse hover using Selenium Actions
     *
     * @param element
     */
    protected void mouseHover(WebElement element) {
        log.info("preform mouse hover on element" + element.toString());
        new Actions(driver).moveToElement(element).build().perform();
    }

    public String checkListText(List<WebElement> webText, String... listText) {

        String result = "";
        Waits.visibilityOfAllElements(driver, timeOut, webText);

        if (webText.size() != listText.length) {
            return "size of params didn't match to list size";
        }

        for (int i = 0; i < listText.length; i++) {
            if (!webText.get(i).getText().equals(listText[i]))
                result += "<br>" + "expected : " + listText[i] + "actual : " + webText.get(i).getText();
        }

        return result;
    }

    public void pressKey(Keys key) {
        Actions action = new Actions(driver);
        action.sendKeys(key).build().perform();
    }

    /**
     * check if the main page url contains the text
     *
     * @param fraction
     * @return true if text contains false other wise
     */
    public boolean checkUrlContainsText(String fraction) {
        try {
            Waits.urlContains(driver, timeOut / 10, fraction);
            return true;
        } catch (Exception e) {
            log.debug("url: " + driver.getCurrentUrl() + " not contains " + fraction);
            return false;
        }
    }
}
