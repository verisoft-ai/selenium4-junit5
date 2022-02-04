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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Listener class for window events
 *
 * @since 0.0.3 (Feb 2022)
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a>
 */
@ToString
@NoArgsConstructor
@Slf4j
public final class WindowListener implements WebDriverListener {

    private ActionTime actionTime;


    @Override
    public void beforeAnyWindowCall(WebDriver.Window window, Method method, Object[] args) {
        actionTime = ActionTime.getMeasureTime();
        actionTime.captureStartTime();
        log.trace("Window event fired " +
                " Window object " + window.toString() +
                "  Method is: " + method.getName() +
                " Args are: " + Arrays.toString(args));
    }

    @Override
    public void afterAnyWindowCall(WebDriver.Window window, Method method, Object[] args, Object result) {
        actionTime = ActionTime.getMeasureTime();
        actionTime.captureEndTime();
        log.trace("Window event done " +
                " Window object " + window.toString() +
                " Method was: " + method.getName() +
                " Args were: " + Arrays.toString(args) +
                " Result is " + result +
                " Action time is " + actionTime.getDelta());
    }

    @Override
    public void beforeGetSize(WebDriver.Window window) {
        //No-Op
    }

    @Override
    public void afterGetSize(WebDriver.Window window, Dimension result) {
        actionTime.captureEndTime();
        log.debug("After set size " +
                " Window object " + window.toString() +
                " Dimensions object " + result +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeSetSize(WebDriver.Window window, Dimension size) {
        //No-Op
    }

    @Override
    public void afterSetSize(WebDriver.Window window, Dimension size) {
        actionTime.captureEndTime();
        log.debug("After set size " +
                " Window object " + window.toString() +
                " Dimensions object " + size.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeGetPosition(WebDriver.Window window) {
        //No-Op
    }

    @Override
    public void afterGetPosition(WebDriver.Window window, Point result) {
        actionTime.captureEndTime();
        log.debug("After get position " +
                " Window object " + window.toString() +
                " Point object " + result +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeSetPosition(WebDriver.Window window, Point position) {
        //No-Op
    }

    @Override
    public void afterSetPosition(WebDriver.Window window, Point position) {
        actionTime.captureEndTime();
        log.debug("After set position " +
                " Window object " + window.toString() +
                " Point object " + position.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeMaximize(WebDriver.Window window) {
        //No-Op
    }

    @Override
    public void afterMaximize(WebDriver.Window window) {
        actionTime.captureEndTime();
        log.debug("After maximize " +
                " Window object " + window.toString() +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeFullscreen(WebDriver.Window window) {
        //No-Op
    }

    @Override
    public void afterFullscreen(WebDriver.Window window) {
        actionTime.captureEndTime();
        log.debug("After full screen " +
                " Window object " + window.toString() +
                " Action time: " + actionTime.getDelta());
    }
}
