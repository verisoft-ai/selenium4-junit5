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

import co.verisoft.fw.selenium.drivers.VerisoftDriver;
import co.verisoft.fw.selenium.drivers.VerisoftMobileDriver;
import co.verisoft.fw.selenium.drivers.factory.DriverCapabilities;
import co.verisoft.fw.selenium.drivers.factory.DriverUrl;
import co.verisoft.fw.selenium.junit.extensions.DriverInjectionExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@Disabled
@ExtendWith(DriverInjectionExtension.class)
public class DriverInjectionTest {

    private static final String pageTestUrl = "file://" +
            new File(System.getProperty("user.dir") +
                    "/src/test/resources/DelegateDriverTestForm.html").getAbsolutePath();

    @DriverCapabilities
    DesiredCapabilities capabilities = new DesiredCapabilities();

    {
        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("appPackage", "com.android.chrome");
        capabilities.setCapability("appActivity", "com.google.android.apps.chrome.Main");
        capabilities.setCapability("platformVersion", "11");
        capabilities.setCapability("automationName", "uiAutomator2");

    }

    @DriverUrl
    private final URL url = new URL("http://localhost:4723/");

    public DriverInjectionTest() throws MalformedURLException {
    }


    @Test
    @Tag("smoke")
    public void shouldWebCreateWebDriver(VerisoftDriver driver) {
        driver.get(pageTestUrl);
        WebElement element = driver.findElement(By.id("checkbox"));
    }

    @Test
    @Tag("smoke")
    public void shouldWebCreateMobileDriver(VerisoftMobileDriver driver) {
        driver.get(pageTestUrl);
        WebElement element = driver.findElement(By.id("checkbox"));
    }

}
