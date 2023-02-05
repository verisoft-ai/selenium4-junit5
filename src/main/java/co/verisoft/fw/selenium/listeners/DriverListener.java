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

import co.verisoft.fw.store.StoreManager;
import co.verisoft.fw.store.StoreType;
import co.verisoft.fw.utils.ActionTime;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.Method;
import java.util.*;

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
 * Listener class for WebDriver events
 *
 * @since 0.0.3 (Feb 2022)
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a>
 */
public final class DriverListener implements WebDriverListener {

    private ActionTime actionTime;


    @Override
    public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args) {
        actionTime = ActionTime.getMeasureTime();
        log.trace("WebDriver " + driver.toString() +
                " event is fired. Method is: " + method.getName() +
                "Args are: " + Arrays.toString(args));
    }

    @Override
    public void afterAnyWebDriverCall(WebDriver driver, Method method, Object[] args, Object result) {
        log.trace("WebDriver " + driver.toString() +
                " event is done. Method was: " + method.getName() +
                "Args were: " + Arrays.toString(args) +
                "Result is: " + result);
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        //No-Op
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        actionTime.captureEndTime();
        log.debug("After Get Operation " +
                " URL " + url +
                " driver " + driver.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeGetCurrentUrl(WebDriver driver) {
        //No-Op
    }

    @Override
    public void afterGetCurrentUrl(String result, WebDriver driver) {
        actionTime.captureEndTime();
        log.debug("After Get current URL Operation " +
                " result " + result +
                " driver " + driver.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeGetTitle(WebDriver driver) {
        //No-Op
    }

    @Override
    public void afterGetTitle(WebDriver driver, String result) {
        actionTime.captureEndTime();
        log.debug("After get title Operation " +
                " title " + result +
                " driver " + driver.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        //No-Op
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        actionTime.captureEndTime();
        log.debug("After find Element. Driver " + driver.toString() +
                " Locator " + locator.toString() +
                ", result Element: " + result +
                " Action time " + actionTime.getDelta());
    }

    @Override
    public void beforeFindElements(WebDriver driver, By locator) {
        //No-Op
    }

    @Override
    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
        actionTime.captureEndTime();
        log.debug("After find Elements. Found " + result.size() + " Elements " +
                " using locator" + locator.toString() +
                " Action time " + actionTime.getDelta() +
                " Elements list: " + Arrays.toString(result.toArray()) +
                " With driver " + driver.toString());
    }

    @Override
    public void beforeGetPageSource(WebDriver driver) {
        //No-Op
    }

    @Override
    public void afterGetPageSource(WebDriver driver, String result) {
        actionTime.captureEndTime();
        log.debug("After get page source " +
                " driver " + driver.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeClose(WebDriver driver) {
        //No-Op
    }

    @Override
    public void afterClose(WebDriver driver) {
        actionTime.captureEndTime();
        log.debug("After close " +
                " driver " + driver.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeQuit(WebDriver driver) {
        //No-Op
    }

    @Override
    public void afterQuit(WebDriver driver) {
        actionTime.captureEndTime();
        log.debug("After quit " +
                " driver " + driver.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeGetWindowHandles(WebDriver driver) {
        //No-Op
    }

    @Override
    public void afterGetWindowHandles(WebDriver driver, Set<String> result) {
        actionTime.captureEndTime();
        log.debug("After get window handles. Found " + result.size() + " windows " +
                " Action time " + actionTime.getDelta() +
                " result list: " + Arrays.toString(result.toArray()) +
                " With driver " + driver.toString());
    }

    @Override
    public void beforeGetWindowHandle(WebDriver driver) {
        //No-Op
    }

    @Override
    public void afterGetWindowHandle(WebDriver driver, String result) {
        actionTime.captureEndTime();
        log.debug("After get window handles. " +
                " Action time " + actionTime.getDelta() +
                " handle: " + result +
                " With driver " + driver.toString());
    }

    @Override
    public void beforeExecuteScript(WebDriver driver, String script, Object[] args) {
        //No-Op
    }

    @Override
    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
        actionTime.captureEndTime();
        log.debug("After execute script. " +
                " result: " + result +
                " Action time " + actionTime.getDelta() +
                " script " + script +
                " args " + Arrays.toString(args) +
                " With driver " + driver.toString());
    }

    @Override
    public void beforeExecuteAsyncScript(WebDriver driver, String script, Object[] args) {
        //No-Op
    }

    @Override
    public void afterExecuteAsyncScript(WebDriver driver, String script, Object[] args, Object result) {
        actionTime.captureEndTime();
        log.debug("After execute async script. " +
                " result: " + result +
                " Action time " + actionTime.getDelta() +
                " script " + script +
                " args " + Arrays.toString(args) +
                " With driver " + driver.toString());
    }

    @Override
    public void beforePerform(WebDriver driver, Collection<Sequence> actions) {
        //No-Op
    }

    @Override
    public void afterPerform(WebDriver driver, Collection<Sequence> actions) {
        actionTime.captureEndTime();
        log.debug("After action perform. " +
                " actions: " + Arrays.toString(actions.toArray()) +
                " Action time " + actionTime.getDelta() +
                " With driver " + driver.toString());
    }

    @Override
    public void beforeResetInputState(WebDriver driver) {
        //No-Op
    }

    @Override
    public void afterResetInputState(WebDriver driver) {
        actionTime.captureEndTime();
        log.debug("After reset input state. " +
                " Action time " + actionTime.getDelta() +
                " With driver " + driver.toString());
    }
}
