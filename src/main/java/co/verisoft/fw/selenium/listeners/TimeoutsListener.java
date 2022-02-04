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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;

/**
 * Listener class for timeouts events
 *
 * @since 0.0.3 (Feb 2022)
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a>
 */
@ToString
@NoArgsConstructor
@Slf4j
public final class TimeoutsListener implements WebDriverListener {

    private ActionTime actionTime;


    @Override
    public void beforeAnyTimeoutsCall(WebDriver.Timeouts timeouts, Method method, Object[] args)  {
        actionTime = ActionTime.getMeasureTime();
        actionTime.captureStartTime();
        log.trace("Timeout event fired " +
                " Timeout object " + timeouts.toString() +
                "  Method is: " + method.getName() +
                " Args are: " + Arrays.toString(args));
    }

    @Override
    public void afterAnyTimeoutsCall(WebDriver.Timeouts timeouts, Method method, Object[] args, Object result)  {
        actionTime = ActionTime.getMeasureTime();
        actionTime.captureEndTime();
        log.trace("Timeouts event done " +
                " Timeouts object " + timeouts.toString() +
                " Method was: " + method.getName() +
                " Args were: " + Arrays.toString(args) +
                " Result is " + result +
                " Action time is " + actionTime.getDelta());
    }

    @Override
    public void beforeImplicitlyWait(WebDriver.Timeouts timeouts, Duration duration) {
        //No-Op
    }

    @Override
    public void afterImplicitlyWait(WebDriver.Timeouts timeouts, Duration duration) {
        actionTime.captureEndTime();
        log.debug("After implicit wait " +
                " Timeouts object " + timeouts.toString() +
                " Duration object " + duration.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeSetScriptTimeout(WebDriver.Timeouts timeouts, Duration duration) {
        //No-Op
    }

    @Override
    public void afterSetScriptTimeout(WebDriver.Timeouts timeouts, Duration duration) {
        actionTime.captureEndTime();
        log.debug("After set script timeout " +
                " Timeouts object " + timeouts.toString() +
                " Duration object " + duration.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforePageLoadTimeout(WebDriver.Timeouts timeouts, Duration duration) {
        //No-Op
    }

    @Override
    public void afterPageLoadTimeout(WebDriver.Timeouts timeouts, Duration duration) {
        actionTime.captureEndTime();
        log.debug("After page load timeout " +
                " Timeouts object " + timeouts.toString() +
                " Duration object " + duration.toString() +
                " Action time: " + actionTime.getDelta());
    }
}
