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
package co.verisoft.fw.utils;

import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;


/**
 * Retry class, inspired by Yujun Liang & Alex Collins book "Selenium WebDriver - From Foundations To Framework"
 * <br> Link to the book on amazon can be found
 * <a href="https://www.amazon.com/Selenium-WebDriver-Foundations-Yujun-Liang-ebook/dp/B01N9D0HMG">here</a>.
 *
 * <br>Original code can be found
 * <a href="https://github.com/selenium-webdriver-book/source/blob/8e45f00195a9c0965f3cbd1be33295102d4679b1/src/test/java/swb/framework/robust/Retry.java">here</a>.
 *
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a> @ <a href="http://www.verisoft.co">www.VeriSoft.co</a>
 * @since 1.9.6
 */
public class Retry {
    private final long interval;
    private final TimeUnit unit;
    private long count;
    @SuppressWarnings("unused")
    private WebDriver driver;


    public Retry(WebDriver driver, int count, int interval, TimeUnit unit) {
        this.count = count;
        this.interval = interval;
        this.unit = unit;
        this.driver = driver;
    }


    public void attempt(Attemptable attemptable) {
        for (int i = 0; i < count; i++) {
            try {
                attemptable.attempt();
                return;
            } catch (Throwable e) {
                if (i == count - 1) {
                    throw new IllegalStateException(e);
                }
                attemptable.onAttemptFail();
            }
            try {
                unit.sleep(interval);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}

