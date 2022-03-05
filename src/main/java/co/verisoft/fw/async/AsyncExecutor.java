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
package co.verisoft.fw.async;

import org.openqa.selenium.support.events.WebDriverListener;

import java.time.Duration;

/**
 * Indicates that a driver can execute asynchronous tasks, providing access to the mechanism to do so.
 *
 * <p>
 * Since Selenium is a single thread application, paralel execution is not possible. Still, some operations should
 * be run asynchronously (e.g watiting for a pop up to appear and dismiss it, doing some generic operation and continue
 * the script). AsyncExecutor is the mechanism to do this type of actions, while still keeps the single thread application
 * model
 * </p>
 *
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a> @ <a href="http://www.verisoft.co">www.VeriSoft.co</a>
 * @since 2.0.1
 */
public interface AsyncExecutor {

    /**
     * Gets the AsyncListener interface
     *
     * @return An AsyncListener interface
     * @see org.openqa.selenium.WebDriver.Options
     */
    AsyncListener async();


    /**
     * Interface to define the AsyncListener. One have to implement Subject, SearchingEventListener and methods
     * inline in order to satisfy the interface
     */
    interface AsyncListener extends Subject, WebDriverListener {

        /**
         * Setter for the dispatch interval and the time unit. The defailt valie of the
         * dispatcher is 1 second and it is the minimum dispatcher possible. If tried to
         * set less than 1 second, setter will not update the values
         *
         * @param interval new interval for invocation
         */
        void setDispatchInterval(Duration interval);
    }
}
