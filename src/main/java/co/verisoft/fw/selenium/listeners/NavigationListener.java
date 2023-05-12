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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;

/**
 * Listener for all WebElement related actions
 * Currently - logs only
 *
 * @author Nir Gallner
 */
@ToString
@NoArgsConstructor
@Slf4j
/**
 * Listener class for navigation events
 *
 * @since 0.0.3 (Feb 2022)
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a>
 */
public final class NavigationListener implements WebDriverListener {

    private ActionTime actionTime;


    @Override
    public void beforeAnyNavigationCall(WebDriver.Navigation navigation, Method method, Object[] args) {
        actionTime = ActionTime.getMeasureTime();
        log.trace("Navigation event fired " +
                " Navigation object " + navigation.toString() +
                "  Method is: " + method.getName() +
                "Args are: " + Arrays.toString(args));
    }

    @Override
    public void afterAnyNavigationCall(WebDriver.Navigation navigation, Method method, Object[] args, Object result) {
        actionTime = ActionTime.getMeasureTime();
        log.trace("Navigation event done " +
                " Navigation object " + navigation.toString() +
                " Method was: " + method.getName() +
                " Args were: " + Arrays.toString(args) +
                " Result is " + result +
                " Action time is " + actionTime.getDelta());
    }

    @Override
    public void beforeTo(WebDriver.Navigation navigation, String url) {
        //No-Op
    }

    @Override
    public void afterTo(WebDriver.Navigation navigation, String url) {
        actionTime.captureEndTime();
        log.debug("Navigate to " +
                " URL " + url +
                " navigation object " + navigation.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeTo(WebDriver.Navigation navigation, URL url) {
        //No-Op
    }

    @Override
    public void afterTo(WebDriver.Navigation navigation, URL url) {
        actionTime.captureEndTime();
        log.debug("Navigate to " +
                " URL " + url.toString() +
                " navigation object " + navigation.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeBack(WebDriver.Navigation navigation) {
        //No-Op
    }

    @Override
    public void afterBack(WebDriver.Navigation navigation) {
        actionTime.captureEndTime();
        log.debug("Navigated back " +
                " navigation object " + navigation.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeForward(WebDriver.Navigation navigation) {
        //No-Op
    }

    @Override
    public void afterForward(WebDriver.Navigation navigation) {
        actionTime.captureEndTime();
        log.debug("Navigated forward " +
                " navigation object " + navigation.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeRefresh(WebDriver.Navigation navigation) {
        //No-Op
    }

    @Override
    public void afterRefresh(WebDriver.Navigation navigation) {
        actionTime.captureEndTime();
        log.debug("Refreshed. " +
                " navigation object " + navigation.toString() +
                " Action time: " + actionTime.getDelta());
    }
}
