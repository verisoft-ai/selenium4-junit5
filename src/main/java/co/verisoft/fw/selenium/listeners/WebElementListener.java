package co.verisoft.fw.selenium.listeners;

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

import co.verisoft.fw.utils.ActionTime;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Listener for all WebElement related actions
 * Currently - logs only
 *
 * @author Nir Gallner
 */
@ToString
@NoArgsConstructor
@Log4j2
/**
 * Listener class for WebElement events
 *
 * @since 0.0.3 (Feb 2022)
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a>
 */
public final class WebElementListener implements WebDriverListener {

    private ActionTime actionTime;

    @Override
    public void beforeAnyWebElementCall(WebElement element, Method method, Object[] args) {
        actionTime = ActionTime.getMeasureTime();
        log.trace("WebElement " + element.toString() +
                " event is fired. Method is: " + method.getName() +
                "Args are: " + Arrays.toString(args));
    }

    @Override
    public void afterAnyWebElementCall(WebElement element, Method method, Object[] args, Object result) {
        log.trace("WebElement " + element.toString() +
                " event is done. Method was: " + method.getName() +
                "Args were: " + Arrays.toString(args) +
                "Result is: " + result);
    }


    @Override
    public void beforeClick(WebElement element) {
        //No-Op
    }

    @Override
    public void afterClick(WebElement element) {
        actionTime.captureEndTime();
        log.debug("Clicked on element " + element.toString() + " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeSubmit(WebElement element) {
        // No-Op
    }

    @Override
    public void afterSubmit(WebElement element) {
        actionTime.captureEndTime();
        log.debug("Submitted (clicked) on element " + element.toString() + " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        // No-Op
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        actionTime.captureEndTime();
        log.debug("Sent keys for element " + element.toString() +
                " Keys were: " + keysToSend +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeClear(WebElement element) {
        // No-Op
    }

    @Override
    public void afterClear(WebElement element) {
        actionTime.captureEndTime();
        log.debug("Cleared element " + element.toString() +
                " Action time " + actionTime.getDelta());
    }

    @Override
    public void beforeGetTagName(WebElement element) {
        // No-Op
    }

    @Override
    public void afterGetTagName(WebElement element, String result) {
        actionTime.captureEndTime();
        log.debug("After Get Tag Name. Element " +
                element.toString() +
                " Tag - " + result +
                " Action time " + actionTime.getDelta());
    }

    @Override
    public void beforeGetAttribute(WebElement element, String name) {
        // No-Op
    }

    @Override
    public void afterGetAttribute(WebElement element, String name, String result) {
        actionTime.captureEndTime();
        log.debug("After get attribute. Element " +
                element.toString() +
                ", attribute: " + result +
                " Action time " + actionTime.getDelta());
    }

    @Override
    public void beforeIsSelected(WebElement element) {
        // No-Op
    }

    @Override
    public void afterIsSelected(WebElement element, boolean result) {
        actionTime.captureEndTime();
        log.debug("After is selected. Element " +
                element.toString() +
                ", isSelected?: " + result +
                " Action time " + actionTime.getDelta());
    }

    @Override
    public void beforeIsEnabled(WebElement element) {
        // No-Op
    }

    @Override
    public void afterIsEnabled(WebElement element, boolean result) {
        actionTime.captureEndTime();
        log.debug("After is enabled. Element " + element.toString() +
                ", isEnabled?: " + result +
                " Action time " + actionTime.getDelta());
    }

    @Override
    public void beforeGetText(WebElement element) {
        // No-Op
    }

    @Override
    public void afterGetText(WebElement element, String result) {
        actionTime.captureEndTime();
        log.debug("After get text. Element " +
                element.toString() +
                ", text: " + result +
                " Action time " + actionTime.getDelta());
    }

    @Override
    public void beforeFindElement(WebElement element, By locator) {
        // No-Op
    }

    @Override
    public void afterFindElement(WebElement element, By locator, WebElement result) {
        actionTime.captureEndTime();
        log.debug("After find Element. Element " + element.toString() +
                " Locator " + locator.toString() +
                ", result Element: " + result +
                " Action time " + actionTime.getDelta());
    }

    @Override
    public void beforeFindElements(WebElement element, By locator) {
        // No-Op
    }

    @Override
    public void afterFindElements(WebElement element, By locator, List<WebElement> result) {
        actionTime.captureEndTime();
        log.debug("After find Elements. Found " + result.size() + "Elements " +
                " using locator" + locator.toString() +
                " Action time " + actionTime.getDelta() +
                " Elements list: " + Arrays.toString(result.toArray()));

    }

    @Override
    public void beforeIsDisplayed(WebElement element) {
        // No-Op
    }

    @Override
    public void afterIsDisplayed(WebElement element, boolean result) {
        actionTime.captureEndTime();
        log.debug("After is displayed. Element " + element.toString() +
                ", isDisplayed?: " + result +
                " Action time " + actionTime.getDelta());
    }

    @Override
    public void beforeGetLocation(WebElement element) {
        // No-Op
    }

    @Override
    public void afterGetLocation(WebElement element, Point result) {
        actionTime.captureEndTime();
        log.debug("After get location. Element " + element.toString() +
                ", location: " + result +
                " Action time " + actionTime.getDelta());
    }

    @Override
    public void beforeGetSize(WebElement element) {
        // No-Op
    }

    @Override
    public void afterGetSize(WebElement element, Dimension result) {
        actionTime.captureEndTime();
        log.debug("After get size. Element " + element.toString() +
                ", size: " + result +
                " Action time " + actionTime.getDelta());
    }

    @Override
    public void beforeGetCssValue(WebElement element, String propertyName) {
        // No-Op
    }

    @Override
    public void afterGetCssValue(WebElement element, String propertyName, String result) {
        actionTime.captureEndTime();
        log.debug("After get Css value. Element " + element.toString() +
                " Property name " + propertyName +
                ", value: " + result +
                " Action time " + actionTime.getDelta());
    }

}
