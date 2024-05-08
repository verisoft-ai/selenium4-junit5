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

package co.verisoft.fw.selenium.drivers;

import co.verisoft.fw.async.AsyncListenerImp;
import co.verisoft.fw.selenium.listeners.*;
import co.verisoft.fw.utils.Property;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.decorators.WebDriverDecorator;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * VeriSoft driver. A top level object which handles all types of WebDriver interfaces - <br>
 * 1. Local and Remote<br>
 * 2. All Apppium and Selenium based drivers (Web and mobile are currently supported)<br>
 *
 * <p>
 * VeriSoft driver is a concrete class which implements a variety of interfaces (which makes it easier not having to
 * perform castings on many operations). <br>
 * I supports local creation of local WebDriver objects using WebDriverManager package (see more about WebDriverManager
 * at <a href="https://github.com/bonigarcia/webdrivermanager">WebDriverManager Github Repository </a>)<br>
 * It also supports creation of remote WebDriver objects using the built-in Selenium mechanism. VeriSoftDriver follows
 * the RemoteWebDriver.java architecture, and actually stores an instance of RemoteWebDriver in it.
 * <br>
 * <p>
 * VerisoftDriver implements the WebDriver behavior, and in addition, it also adds some functionality. The main additional functionalities which
 * are currently supported:<br>
 * 1. Async operations. See async operation in the "See Also" section <br>
 * 2. Extended logging<br>
 * 3. All available events included within the Selenium framework are registered. See events in the "See Also"
 * section<br>
 * 4. WebDriver is wrapped with EventFiringDecorator. See in the "See Also" section <br>
 * <br><br>
 * The driver is instanciated by specifying the relevant DesiredCapabilities, and if the driver is a remote driver,
 * specifying remote url. All of VeriSoft's ctors expectes at least
 * a DesiredCapabilities object.
 * <br>
 * All other inner WebDriver class are also implemented as delegate classes.<br>
 * <p>
 * TODO Add support to client-server drives e.g WinAppDriver and Winium
 * TODO Add additional drivers support such as dockers (see Selenium-Jupiter for a list of supported drivers)
 * TODO Find ways to add unit tests to this class (it is heavily dependened on OS, external SW etc.). Maybe dockers?
 * <br>
 * <b>Example 1 - Creating a local VerisoftDriver with firefox as driver:</b><br>
 * <pre>
 *     {@code
 *     DesiredCapabilities capabilities = new DesiredCapabilities();
 *     capabilities.setCapability("browserName", "firefox");
 *
 *     WebDriver driver = new VerisoftDriver(capabilities);
 *
 *     driver.get("http://www.google.com");
 *     String title = driver.getTitle();
 *     if (title == "Google")
 *         System.out.println("We are in Google homepage");
 *
 *     driver.close();
 *     }
 * </pre><br>
 * <b>Example 2 - Creating a remote VerisoftDriver with safari as driver:</b><br>
 * <pre>
 *     {@code
 *     DesiredCapabilities capabilities = new DesiredCapabilities();
 *     capabilities.setCapability("browserName", "safari");
 *
 *     WebDriver driver = new VerisoftDriver("http://remote-url-address:port", capabilities);
 *
 *     driver.get("http://www.google.com");
 *     String title = driver.getTitle();
 *     if (title == "Google")
 *         System.out.println("We are in Google homepage");
 *
 *     driver.close();
 *     }
 * </pre><br>
 *
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a> @ <a href="http://www.verisoft.co">www.VeriSoft.co</a>
 * @see org.openqa.selenium.remote.RemoteWebDriver
 * @see co.verisoft.fw.async.AsyncTask
 * @since 1.9.6
 */

@ToString
@Slf4j
@Getter
public class DecoratedDriver implements
        WebDriver,
        JavascriptExecutor,
        HasCapabilities,
        TakesScreenshot,
        WrapsDriver{

    private WebDriver originalDriver;
    protected WebDriver decoratedDriver;

    // TODO: change this to async decorator
    private AsyncListenerImp asyncListener;
    private Capabilities capabilities;


    /**
     * C-tor for local drivers only
     *
     * @param capabilities cpabilities object
     */
    public DecoratedDriver(@Nullable Capabilities capabilities) {
        try {
            createRemoteDriver(null, capabilities);
        } catch (Throwable t) {
            log.error("Error instanciate local VerisoftDriver", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * C-tor for local and remote drivers
     *
     * @param remoteAddress address of the remote Selenium server
     * @param capabilities  capabilities object
     */
    public DecoratedDriver(URL remoteAddress, Capabilities capabilities) {
        try {
            createRemoteDriver(remoteAddress, capabilities);
        } catch (Throwable t) {
            log.error("Error instanciate local VerisoftDriver", t);
            throw new RuntimeException(t);
        }
    }

    private void decorateDriver() {

        List<WebDriverListener> webDriverlisteners = new ArrayList<>();

        webDriverlisteners.add(new AlertListener());
        webDriverlisteners.add(new NavigationListener());
        webDriverlisteners.add(new OptionsListener());
        webDriverlisteners.add(new TimeoutsListener());
        webDriverlisteners.add(new DriverListener());
        webDriverlisteners.add(new WebElementListener());
        webDriverlisteners.add(new WindowListener());

        // Create asyncListener object to be activated here
        if (asyncListener == null) {
            asyncListener = new AsyncListenerImp();
            webDriverlisteners.add(asyncListener);
        }

        decorateDriver(webDriverlisteners);
    }

    public void decorateDriver(@NotNull List<WebDriverListener> listeners) {

        // Validations
        if (originalDriver == null)
            throw new IllegalStateException("Cannot decorate a null driver");
        else if (decoratedDriver == null)
            decoratedDriver = originalDriver;

        WebDriverListener[] listenersArr = new WebDriverListener[listeners.size()];
        listenersArr = listeners.toArray(listenersArr);

        decoratedDriver = new EventFiringDecorator(listenersArr).decorate(decoratedDriver);
        VerisoftDriverManager.addDriverToMap(decoratedDriver);
    }


    public void decorateDriver(Class clazz) {
        if (!WebDriverDecorator.class.isAssignableFrom(clazz))
            throw new IllegalArgumentException("public void decorateDriver(Class clazz) method can only receive " +
                    "calsses which implementes the Decorated interface");

        try {
            this.decoratedDriver = ((WebDriverDecorator<WebDriver>) clazz.getDeclaredConstructor().newInstance())
                    .decorate(this.decoratedDriver);
        } catch (Throwable t) {
            log.error("Cannot instanciate decorator ", t);
            throw new IllegalStateException(t);
        }

        VerisoftDriverManager.addDriverToMap(this.decoratedDriver);
    }


    /**
     * Private method to create a proper WebDriver object. If the remoteAddress is null, it will create a
     * local instance of WebDriver. If the remoteAddress is not null, it will create a RemoteWebDriver object,
     * which can hold either remote of local adresses (http://localhost)
     *
     * @param remoteAddress Address of the driver. Use either null for local instances or a url to
     *                      Selenium Grid or Url to external supplier.
     * @param capabilities  a capabilities object.
     */
    private void createRemoteDriver(@Nullable URL remoteAddress, Capabilities capabilities) {

        String browserName = capabilities.getBrowserName();
        String platformName = (capabilities.getCapability("platformName") == null ?
                "" : capabilities.getCapability("platformName").toString().trim());

        // Mobile Driver section
        if (this instanceof DecoratedMobileDriver) {

            // Create a new driver object - android
            if (platformName.equalsIgnoreCase("android")) {
                originalDriver = remoteAddress == null ?
                        new AndroidDriver(capabilities) :
                        new AndroidDriver(remoteAddress, capabilities);
            }


            // Create a new driver object - ios
            else if (platformName.equalsIgnoreCase("ios")) {
                originalDriver = remoteAddress == null ?
                        new IOSDriver(capabilities) :
                        new IOSDriver(remoteAddress, capabilities);
            }

            // Appium generic
            else {
                originalDriver = remoteAddress == null ?
                        new AppiumDriver(capabilities) :
                        new AppiumDriver(remoteAddress, capabilities);
            }
        }

        // Web Driver section
        else {
            // local drivers such as chrome , firefox
            if (remoteAddress == null) {
                originalDriver = instanciateLocalDriver(capabilities);
            } else {
                originalDriver = new RemoteWebDriver(remoteAddress, capabilities);
            }
        }

        this.decorateDriver();
    }


    /**
     * Cretes a local driver instance
     *
     * @param capabilities capabilities object
     * @return a new WebDriver object
     */
    private WebDriver instanciateLocalDriver(Capabilities capabilities) {
        Property prop = new Property("webdrivermanager.properties");

        boolean isHeadless;
        String browserName = capabilities.getBrowserName().toLowerCase();

        switch (browserName) {

            case "chrome":
                String version = prop.getProperty("chromeDriverVersion");
                if (!version.isEmpty())
                    WebDriverManager.chromedriver().clearDriverCache().driverVersion(version).setup();
                else
                    WebDriverManager.chromedriver().clearDriverCache().setup();

                ChromeOptions chromeOptions = new ChromeOptions();
                isHeadless = capabilities.is("headless");
                if (isHeadless)
                    chromeOptions.addArguments("--headless");

                chromeOptions.merge(capabilities);
                return new ChromeDriver(chromeOptions);

            case "firefox":

                try {
                    WebDriverManager.firefoxdriver().setup();
                } catch (Throwable t) {
                    version = prop.getProperty("geckoDriverVersion");
                    WebDriverManager.firefoxdriver().driverVersion(version).setup();
                }

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                isHeadless = capabilities.is("headless");
                if (isHeadless)
                    firefoxOptions.addArguments("--headless");

                firefoxOptions.merge(capabilities);
                return new FirefoxDriver(firefoxOptions);

            case "ie":
                try {
                    WebDriverManager.iedriver().setup();
                } catch (Throwable t) {
                    version = prop.getProperty("internetExplorerVersion");
                    WebDriverManager.iedriver().driverVersion(version).setup();
                }

                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.merge(capabilities);
                return new InternetExplorerDriver(ieOptions);

            case "edge":
                try {
                    WebDriverManager.edgedriver().setup();
                } catch (Throwable t) {
                    version = prop.getProperty("edgeDriverVersion");
                    WebDriverManager.edgedriver().driverVersion(version).setup();
                }

                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.merge(capabilities);
                return new EdgeDriver(edgeOptions);

            case "safari":
                try {
                    WebDriverManager.safaridriver().setup();
                } catch (Throwable t) {
                    version = prop.getProperty("safariDriverVersion");
                    WebDriverManager.safaridriver().driverVersion(version).setup();
                }

                SafariOptions safariOptions = new SafariOptions();
                safariOptions.merge(capabilities);
                return new SafariDriver(safariOptions);

            default:
                break;
        }

        throw new IllegalStateException("Illegal browser name");
    }

    @Override
    public void get(String url) {
        this.decoratedDriver.get(url);
    }

    @Override
    public String getCurrentUrl() {
        return decoratedDriver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return decoratedDriver.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return decoratedDriver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return decoratedDriver.findElement(by);
    }

    @Override
    public String getPageSource() {
        return decoratedDriver.getPageSource();
    }

    @Override
    public void close() {
        decoratedDriver.close();
    }

    @Override
    public void quit() {
        decoratedDriver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return decoratedDriver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return decoratedDriver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return decoratedDriver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return decoratedDriver.navigate();
    }

    @Override
    public Options manage() {
        return decoratedDriver.manage();
    }

    @Override
    public Capabilities getCapabilities() {
        return this.capabilities;
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) decoratedDriver).executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) decoratedDriver).executeAsyncScript(script, args);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) decoratedDriver).getScreenshotAs(target);
    }

    @Override
    public WebDriver getWrappedDriver() {
        return decoratedDriver;
    }

}
