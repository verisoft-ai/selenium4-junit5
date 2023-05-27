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
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

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
 * Listener class for options events
 *
 * @since 0.0.3 (Feb 2022)
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a>
 */
public final class OptionsListener implements WebDriverListener {

    private ActionTime actionTime;


    @Override
    public void beforeAnyOptionsCall(WebDriver.Options options, Method method, Object[] args) {
        actionTime = ActionTime.getMeasureTime();
        actionTime.captureStartTime();
        log.trace("Option event fired " +
                " Options object " + options.toString() +
                "  Method is: " + method.getName() +
                " Args are: " + Arrays.toString(args));
    }


    @Override
    public void afterAnyOptionsCall(WebDriver.Options options, Method method, Object[] args, Object result) {
        actionTime = ActionTime.getMeasureTime();
        actionTime.captureEndTime();
        log.trace("Options event done " +
                " Options object " + options.toString() +
                " Method was: " + method.getName() +
                " Args were: " + Arrays.toString(args) +
                " Result is " + result +
                " Action time is " + actionTime.getDelta());
    }


    @Override
    public void beforeAddCookie(WebDriver.Options options, Cookie cookie) {
        //No-Op
    }


    @Override
    public void afterAddCookie(WebDriver.Options options, Cookie cookie) {
        actionTime.captureEndTime();
        log.debug("After add cookie " +
                " Options object " + options.toString() +
                " Cookies object " + cookie.toString() +
                " Action time: " + actionTime.getDelta());
    }


    @Override
    public void beforeDeleteCookieNamed(WebDriver.Options options, String name) {
        //No-Op
    }


    @Override
    public void afterDeleteCookieNamed(WebDriver.Options options, String name) {
        actionTime.captureEndTime();
        log.debug("After delete cookie named " + name +
                " Options object " + options.toString() +
                " Action time: " + actionTime.getDelta());
    }


    @Override
    public void beforeDeleteCookie(WebDriver.Options options, Cookie cookie) {
        //No-Op
    }


    @Override
    public void afterDeleteCookie(WebDriver.Options options, Cookie cookie) {
        actionTime.captureEndTime();
        log.debug("After delete cookie  " +
                " Cookie object " + cookie.toString() +
                " Options object " + options.toString() +
                " Action time: " + actionTime.getDelta());
    }


    @Override
    public void beforeDeleteAllCookies(WebDriver.Options options) {
        //No-Op
    }


    @Override
    public void afterDeleteAllCookies(WebDriver.Options options) {
        actionTime.captureEndTime();
        log.debug("After delete all cookies  " +
                " Options object " + options.toString() +
                " Action time: " + actionTime.getDelta());
    }


    @Override
    public void beforeGetCookies(WebDriver.Options options) {
        //No-Op
    }


    @Override
    public void afterGetCookies(WebDriver.Options options, Set<Cookie> result) {
        actionTime.captureEndTime();
        log.debug("After get cookies  " +
                " Cookies " + Arrays.toString(result.toArray()) +
                " Options object " + options.toString() +
                " Action time: " + actionTime.getDelta());
    }


    @Override
    public void beforeGetCookieNamed(WebDriver.Options options, String name) {
        //No-Op
    }


    @Override
    public void afterGetCookieNamed(WebDriver.Options options, String name, Cookie result) {
        actionTime.captureEndTime();
        log.debug("After delete cookie  named " + name +
                " Cookie result " + result +
                " Options object " + options.toString() +
                " Action time: " + actionTime.getDelta());
    }
}
