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
import co.verisoft.fw.utils.Asserts;
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
        capabilities.setCapability("appPackage", "com.leumi.leumiwallet");
        capabilities.setCapability("appActivity", "com.ngsoft.app.ui.LMTestSettingActivity");
        capabilities.setCapability("deviceName", "RFCTA2182SF");
        capabilities.setCapability("securityToken", "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2YTY0NDgyYi00YzA1LTQ1OTQtOWNhNS1hODViMDczNmU1NzkifQ.eyJpYXQiOjE2NzMxNzg3NjksImp0aSI6IjY5MmIyMzU3LTk3MzMtNDlkMi05ZDQxLTdhYmZjMWFiODI0YyIsImlzcyI6Imh0dHBzOi8vYXV0aDcucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL2xldW1pcWEtcGVyZmVjdG9tb2JpbGUtY29tIiwiYXVkIjoiaHR0cHM6Ly9hdXRoNy5wZXJmZWN0b21vYmlsZS5jb20vYXV0aC9yZWFsbXMvbGV1bWlxYS1wZXJmZWN0b21vYmlsZS1jb20iLCJzdWIiOiJjZjllYjA4MC02M2FkLTRmZWEtOGQwZi0xZTY3NjA4MzI2NWEiLCJ0eXAiOiJPZmZsaW5lIiwiYXpwIjoib2ZmbGluZS10b2tlbi1nZW5lcmF0b3IiLCJub25jZSI6ImYxMzM4MjA2LWMyN2ItNGVkMy04MTYzLWE1NDhlNDRjNTYxZiIsInNlc3Npb25fc3RhdGUiOiI3YWUyM2Y2Ni1kNDdmLTQ5YjEtYjQyNi03MDExY2E1N2ZjZjgiLCJzY29wZSI6Im9wZW5pZCBvZmZsaW5lX2FjY2VzcyBwcm9maWxlIGVtYWlsIn0.FwyDgRDzg3VOw3ZrC_WRnyAPcimVHcFKoE2yjbY8XgE");

    }
//    DesiredCapabilities capabilities = new DesiredCapabilities();
//
//    {
//        capabilities.setCapability("platformName", "android");
//        capabilities.setCapability("deviceName", "emulator-5554");
//        capabilities.setCapability("appPackage", "com.android.chrome");
//        capabilities.setCapability("appActivity", "com.google.android.apps.chrome.Main");
//        capabilities.setCapability("automationName", "uiAutomator2");
//        //capabilities.setCapability("fullReset", "true");
//
//    }

    @DriverUrl
    private final URL url = new URL("https://leumiqa.perfectomobile.com/nexperience/perfectomobile/wd/hub");
//    private final URL url = new URL("http://localhost:4723/wd/hub");

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
    public void shouldWebCreateMobileDriver(VerisoftMobileDriver driver) throws InterruptedException {
        WebElement e = driver.findElement(By.id("com.android.chrome:id/send_report_checkbox"));
        Asserts.assertTrue(e.getAttribute("checked").equalsIgnoreCase("true"), "Should be selected");
        e.click();
        Asserts.assertTrue(e.getAttribute("checked").equalsIgnoreCase("false"), "Should NOT be selected");
    }

}
