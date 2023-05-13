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
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * Listener for alerts activation.
 * Currently - logs only
 *
 * @author Nir Gallner
 */
@ToString
@NoArgsConstructor
@Slf4j
/**
 * Listener class for Alert events
 *
 * @since 0.0.3 (Feb 2022)
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a>
 */
public final class AlertListener implements WebDriverListener {

    private ActionTime actionTime;


    @Override
    public void beforeAnyAlertCall(Alert alert, Method method, Object[] args) {
        if (Objects.isNull(alert))
            return;
        actionTime = ActionTime.getMeasureTime();
        log.trace("Alert " + alert.getText() +
                " is fired. Method is: " + method.getName() +
                "Args are: " + Arrays.toString(args));

    }

    @Override
    public void afterAnyAlertCall(Alert alert, Method method, Object[] args, Object result) {
        if (Objects.isNull(alert))
            return;
        log.trace("Alert " + alert.getText() +
                " is done. Method was: " + method.getName() +
                "Args were: " + Arrays.toString(args) +
                " Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeAccept(Alert alert) {
        // No-OP
    }

    @Override
    public void afterAccept(Alert alert) {
        if (Objects.isNull(alert))
            return;
        actionTime.captureEndTime();
        log.debug("Alert " + alert.getText() + " accepted. Action time: " + actionTime.getDelta());
    }

    @Override
    public void beforeDismiss(Alert alert) {
        // No-OP
    }

    @Override
    public void afterDismiss(Alert alert) {
        if (Objects.isNull(alert))
            return;
        actionTime.captureEndTime();
        log.debug("Alert " + alert.getText() + " dismissed. Action time: " + actionTime.getDelta());
    }


    @Override
    public void beforeGetText(Alert alert) {
        // No - OP
    }

    @Override
    public void afterGetText(Alert alert, String result) {
        if (Objects.isNull(alert))
            return;
        actionTime.captureEndTime();
        log.debug("Alert " + alert.getText() + "  - get text is done. Text retrieved is " + result +
                "Action time: " + actionTime.getDelta());
    }


    @Override
    public void beforeSendKeys(Alert alert, String keys) {
        // No- OP
    }

    @Override
    public void afterSendKeys(Alert alert, String keys) {
        if (Objects.isNull(alert))
            return;
        actionTime.captureEndTime();
        log.debug("Alert " + alert.getText() + " send keys. String was " + keys + ". Action time: " + actionTime.getDelta());
    }

}
