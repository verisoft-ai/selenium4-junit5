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
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobilePlatform;
import io.github.bonigarcia.wdm.WebDriverManager;
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
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.remote.Augmentable;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.decorators.Decorated;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.virtualauthenticator.HasVirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticatorOptions;

import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
@SuppressWarnings("deprecation")
@ToString
@Augmentable
@Slf4j
//@Deprecated
public class VerisoftDriver implements
        WebDriver,
        JavascriptExecutor,
        HasCapabilities,
        HasVirtualAuthenticator,
        Interactive,
        PrintsPage,
        TakesScreenshot,
        WrapsDriver {

    protected WebDriver driver;
    private AsyncListenerImp asyncListener;
    private List<WebDriverListener> webDriverlisteners;


    private void initListeners() {
        if (webDriverlisteners != null)
            return;

        this.webDriverlisteners = new ArrayList<>();

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

    }

    public void addListener(@NotNull WebDriverListener listener) {
        if (webDriverlisteners == null)
            initListeners();

        webDriverlisteners.add(listener);

        if (Objects.nonNull(this.driver)){
            WebDriver original = ((WebDriver) ((Decorated) this.driver).getOriginal());
            initDriver(original);
        }

    }

    public VerisoftDriver(Capabilities capabilities){
        setupDriver(null, capabilities);
    }
    /**
     * C-tor for local and remote drivers
     *
     * @param remoteAddress address of the remote Selenium server
     * @param capabilities  capabilities object
     */
    public VerisoftDriver(@Nullable URL remoteAddress, Capabilities capabilities) {
        setupDriver(remoteAddress, capabilities);
    }

    private void setupDriver(@Nullable URL remoteAddress, Capabilities capabilities){
        initListeners();
        try {
            createRemoteDriver(remoteAddress, capabilities);
        } catch (Throwable t) {
            log.error("Error instanciate local VerisoftDriver", t);
            throw new RuntimeException(t);
        }
    }

    public VerisoftDriver(HttpCommandExecutor commandExecutor, Capabilities capabilities) {
        initListeners();
        try {
            createRemoteDriver(commandExecutor, capabilities);
        } catch (Throwable t) {
            log.error("Error instanciate local VerisoftDriver", t);
            throw new RuntimeException(t);
        }

    }


    @Override
    public void get(String url) {
        log.debug("Driver activity log: get URL -> " + url);
        driver.get(url);
    }

    @Override
    public String getCurrentUrl() {
        String currentUrl = driver.getCurrentUrl();
        log.debug("Driver activity log: get Current Url -> " + currentUrl);
        return currentUrl;
    }

    @Override
    public String getTitle() {
        String title = driver.getTitle();
        log.debug("Driver activity log: get title -> " + title);
        return title;
    }

    @Override
    public List<WebElement> findElements(By by) {
        log.debug("Driver activity log: Going to find multiple elements using locator -> " + by.toString());
        return driver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        log.debug("Driver activity log: Going to find a single element using locator-> " + by.toString());
        return driver.findElement(by);
    }

    @Override
    public String getPageSource() {
        log.debug("Driver activity log: Going to get page source");
        return driver.getPageSource();
    }

    @Override
    public void close() {
        log.debug("Driver activity log: Going to Close");
        driver.close();
    }

    @Override
    public void quit() {
        log.debug("Driver activity log: quit -> " + driver.toString());
        driver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        Set<String> windowHandles = driver.getWindowHandles();
        log.debug("Driver activity log: get window handles - Size -> " + windowHandles.size() + " , "
                + Arrays.toString(windowHandles.toArray()));
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        String windowHandle = driver.getWindowHandle();
        log.debug("Driver activity log: get window handle -> " + windowHandle);
        return windowHandle;
    }

    @Override
    public TargetLocator switchTo() {
        return new VerisoftTargetLocator();
    }

    @Override
    public Navigation navigate() {
        return new VerisoftNavigation();
    }

    @Override
    public Options manage() {
        return new VerisoftDriverOptions();
    }


    @Override
    public Object executeScript(String script, Object... args) {
        log.debug("Driver activity log: execute script -> " + script + " , args -> " + Arrays.toString(args));
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        log.debug("Driver activity log: execute async script -> " + script + " , args -> " + Arrays.toString(args));
        return ((JavascriptExecutor) driver).executeAsyncScript(script, args);
    }

    @Override
    public ScriptKey pin(String script) {
        log.debug("Driver activity log: execute pin script -> " + script);
        return ((JavascriptExecutor) driver).pin(script);
    }

    @Override
    public void unpin(ScriptKey key) {
        log.debug("Driver activity log: execute unpin script with key-> " + key.toString());
        ((JavascriptExecutor) driver).unpin(key);
    }

    @Override
    public Set<ScriptKey> getPinnedScripts() {
        Set<ScriptKey> keys = ((JavascriptExecutor) driver).getPinnedScripts();
        log.debug("Driver activity log: retrieve script keys ->" + Arrays.toString(keys.toArray()));
        return keys;
    }

    @Override
    public Object executeScript(ScriptKey key, Object... args) {
        log.debug("Driver activity log: execute script -> " + key.toString() + " , args -> " +
                Arrays.toString(args));
        return ((JavascriptExecutor) driver).executeScript(key, args);
    }


    @Override
    public Capabilities getCapabilities() {
        log.debug("Driver activity log: retrieve capabilities");
        return ((HasCapabilities) driver).getCapabilities();
    }

    @Override
    public VirtualAuthenticator addVirtualAuthenticator(VirtualAuthenticatorOptions options) {
        log.debug("Driver activity log: add virtual authenticator with options: " + options.toString());
        return ((RemoteWebDriver) driver).addVirtualAuthenticator(options);
    }

    @Override
    public void removeVirtualAuthenticator(VirtualAuthenticator authenticator) {
        log.debug("Driver activity log: remove virtual authenticator : " + authenticator.toString());
        ((RemoteWebDriver) driver).removeVirtualAuthenticator(authenticator);
    }

    @Override
    public void perform(Collection<Sequence> actions) {
        log.debug("Driver activity log: perform -> " + Arrays.toString(actions.toArray()));
        ((Interactive) driver).perform(actions);
    }

    @Override
    public void resetInputState() {
        log.debug("Driver activity log: reset input state");
        ((Interactive) driver).resetInputState();
    }

    @Override
    public Pdf print(PrintOptions printOptions) throws WebDriverException {
        log.debug("Driver activity log: print pdf with options " + printOptions.toString());
        return ((PrintsPage) driver).print(printOptions);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        log.debug("Driver activity log: using: get screenshot as for target " + target.toString());
        return ((TakesScreenshot) driver).getScreenshotAs(target);
    }

    @Override
    public WebDriver getWrappedDriver() {
        log.debug("Driver activity log: Retrieving wrapped driver");
        return this.driver;
    }


    private class VerisoftAlert implements Alert {
        public VerisoftAlert() {
        }

        public void dismiss() {
            log.debug("Driver activity log: switchTo -> alert -> dismiss");
            driver.switchTo().alert().dismiss();
        }

        public void accept() {
            log.debug("Driver activity log: switchTo -> alert -> accept");
            driver.switchTo().alert().accept();
        }

        public String getText() {
            String text = driver.switchTo().alert().getText();
            log.debug("Driver activity log: switchTo -> alert -> getText. Text is: " + text);
            return text;
        }

        public void sendKeys(String keysToSend) {
            log.debug("Driver activity log: switchTo -> alert -> sendKeys : " + keysToSend);
            if (keysToSend == null) {
                throw new IllegalArgumentException("Keys to send should be a not null CharSequence");
            }
            driver.switchTo().alert().sendKeys(keysToSend);
        }
    }

    protected class VerisoftTargetLocator implements TargetLocator {
        protected VerisoftTargetLocator() {
        }

        public WebDriver frame(int frameIndex) {
            log.debug("Driver activity log: switchTo -> frame -> frameIndex : " + frameIndex);
            return driver.switchTo().frame(frameIndex);
        }

        public WebDriver frame(String frameName) {
            log.debug("Driver activity log: switchTo -> frame -> frameName : " + frameName);
            return driver.switchTo().frame(frameName);
        }

        public WebDriver frame(WebElement frameElement) {
            log.debug("Driver activity log: switchTo -> frame -> frameElement : " + frameElement.toString());
            return driver.switchTo().frame(frameElement);
        }

        public WebDriver parentFrame() {
            log.debug("Driver activity log: switchTo -> parentFrame");
            return driver.switchTo().parentFrame();
        }

        public WebDriver window(String windowHandleOrName) {
            log.debug("Driver activity log: switchTo -> window " + windowHandleOrName);
            return driver.switchTo().window(windowHandleOrName);
        }

        public WebDriver newWindow(WindowType typeHint) {
            log.debug("Driver activity log: switchTo -> newWindow " + typeHint.toString());
            return driver.switchTo().newWindow(typeHint);
        }

        public WebDriver defaultContent() {
            log.debug("Driver activity log: switchTo -> defaultContent");
            return driver.switchTo().defaultContent();
        }

        public WebElement activeElement() {
            WebElement element = driver.switchTo().activeElement();
            log.debug("Driver activity log: switchTo -> activeElement. Active element is: " + element.toString());
            return element;
        }

        public Alert alert() {
            return new VerisoftAlert();
        }
    }

    private class VerisoftNavigation implements Navigation {
        private VerisoftNavigation() {
        }

        public void back() {

            String back = "Driver activity log: Navigation -> back From : " + getCurrentUrl() + " ";
            driver.navigate().back();
            back += "To : " + getCurrentUrl();
            log.debug(back);
        }

        public void forward() {

            String forward = "Driver activity log: Navigation -> forward From : " + getCurrentUrl() + " ";
            driver.navigate().forward();
            forward += "To : " + getCurrentUrl();
            log.debug(forward);
        }

        public void to(String url) {

            log.debug("Driver activity log: Navigation -> to url -> " + url);
            get(url);
        }

        public void to(URL url) {

            log.debug("Driver activity log: Navigation -> to -> " + url);
            get(String.valueOf(url));
        }

        public void refresh() {

            log.debug("Driver activity log: Navigation -> refresh URL: " + getCurrentUrl());
            driver.navigate().refresh();
        }
    }


    protected class VerisoftDriverOptions implements Options {
        protected VerisoftDriverOptions() {
        }

        @Beta
        public Logs logs() {
            log.debug("Driver activity log: Retrieve logs");
            return driver.manage().logs();
        }

        public void addCookie(Cookie cookie) {
            log.debug("Driver activity log: options -> add cookie -> " + cookie.toString());
            driver.manage().addCookie(cookie);

        }

        public void deleteCookieNamed(String name) {
            log.debug("Driver activity log: options -> delete cookie named -> " + name);
            driver.manage().deleteCookieNamed(name);
        }

        public void deleteCookie(Cookie cookie) {
            log.debug("Driver activity log: options -> delete cookie -> " + cookie.toString());
            driver.manage().deleteCookie(cookie);
        }

        public void deleteAllCookies() {
            log.debug("Driver activity log: options -> delete all cookie");
            driver.manage().deleteAllCookies();
        }

        public Set<Cookie> getCookies() {
            Set<Cookie> cookies = driver.manage().getCookies();
            log.debug("Driver activity log: options -> get cookies " + Arrays.toString(cookies.toArray()));
            return cookies;

        }

        public Cookie getCookieNamed(String name) {
            String msg = "Driver activity log: options -> get Cookie Named -> " + name;
            Cookie cookie = driver.manage().getCookieNamed(name);
            msg += " Cookie retrieved: " + cookie.toString();
            log.debug(msg);
            return cookie;
        }

        public Timeouts timeouts() {
            return new VerisoftDriver.VerisoftDriverOptions.VerisoftTimeouts();
        }


        @Beta
        public Window window() {
            return new VerisoftDriver.VerisoftDriverOptions.VerisoftWindow();
        }

        @Beta
        protected class VerisoftWindow implements Window {

            protected VerisoftWindow() {
            }

            public Dimension getSize() {
                Dimension dimension = driver.manage().window().getSize();
                log.debug("Driver activity log: manage -> window -> getSize : " + dimension.toString());
                return dimension;
            }

            public void setSize(Dimension targetSize) {
                log.debug("Driver activity log: manage -> window -> setSize : " + targetSize.toString());
                driver.manage().window().setSize(targetSize);
            }

            public Point getPosition() {
                Point point = driver.manage().window().getPosition();
                log.debug("Driver activity log: manage -> window -> getPosition : " + point.toString());
                return point;
            }

            public void setPosition(Point targetPosition) {
                log.debug("Driver activity log: manage -> window -> setPosition : " + targetPosition.toString());
                driver.manage().window().setPosition(targetPosition);
            }

            public void maximize() {

                log.debug("Driver activity log: manage -> window -> maximize ");
                driver.manage().window().maximize();
            }

            public void minimize() {

                log.debug("Driver activity log: manage -> window -> minimize ");
                driver.manage().window().minimize();
            }

            public void fullscreen() {

                log.debug("Driver activity log: manage -> window -> fullscreen ");
                driver.manage().window().fullscreen();
            }
        }

        protected class VerisoftTimeouts implements Timeouts {
            protected VerisoftTimeouts() {
            }

            /**
             * @deprecated
             */
            @Deprecated
            public Timeouts implicitlyWait(long time, TimeUnit unit) {
                log.debug("Driver activity log: manage -> timeouts -> implicitlyWait : [time = " +
                        time + ", " + "unit = "
                        + unit.toString() + "]");
                return driver.manage().timeouts().implicitlyWait(time, unit);
            }

            public Timeouts implicitlyWait(Duration duration) {
                log.debug("Driver activity log: manage -> timeouts -> implicitlyWait : [time = '" +
                        duration.toString() + "']");
                return driver.manage().timeouts().implicitlyWait(duration);
            }

            public Duration getImplicitWaitTimeout() {
                Duration duration = driver.manage().timeouts().getImplicitWaitTimeout();
                log.debug("Driver activity log: manage -> timeouts -> get implicit wait timeout. Time " +
                        duration.toString());
                return duration;
            }

            /**
             * @deprecated
             */
            @Deprecated
            public Timeouts setScriptTimeout(long time, TimeUnit unit) {
                log.debug("Driver activity log: manage -> timeouts -> setScriptTimeout : [time = " +
                        time + ", " + "unit = "
                        + unit.toString() + "]");
                return driver.manage().timeouts().setScriptTimeout(time, unit);
            }

            public Timeouts setScriptTimeout(Duration duration) {

                log.debug("Driver activity log: manage -> timeouts -> setScriptTimeout : [time = '" +
                        duration.toString() + "']");
                return driver.manage().timeouts().setScriptTimeout(duration);
            }

            public Timeouts scriptTimeout(Duration duration) {
                log.debug("Driver activity log: manage -> timeouts -> setScriptTimeout : [time = '" +
                        duration.toString() + "']");
                return driver.manage().timeouts().scriptTimeout(duration);
            }

            public Duration getScriptTimeout() {
                Duration duration = driver.manage().timeouts().getScriptTimeout();
                log.debug("Driver activity log: manage -> timeouts -> getScriptTimeout. Time " +
                        duration.toString());
                return duration;
            }

            /**
             * @deprecated
             */
            @Deprecated
            public Timeouts pageLoadTimeout(long time, TimeUnit unit) {
                log.debug("Driver activity log: manage -> timeouts -> pageLoadTimeout : [time = " +
                        time + ", " + "unit = "
                        + unit.toString() + "]");
                return driver.manage().timeouts().pageLoadTimeout(time, unit);
            }

            public Timeouts pageLoadTimeout(Duration duration) {
                log.debug("Driver activity log: manage -> timeouts -> pageLoadTimeout : [time = '" +
                        duration.toString() + "']");
                return driver.manage().timeouts().pageLoadTimeout(duration);
            }

            public Duration getPageLoadTimeout() {
                Duration duration = driver.manage().timeouts().getPageLoadTimeout();
                log.debug("Driver activity log: manage -> timeouts -> getPageLoadTimeout. Time " +
                        duration.toString());
                return duration;
            }
        }
    }

    /**
     * Retrieves the Async listener. If it is not initialized yet, initializes the listener
     *
     * @return AsyncListener object
     */
    public AsyncListenerImp async() {
        if (asyncListener == null)
            asyncListener = new AsyncListenerImp();
        return asyncListener;
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
        WebDriver tempDriver = null;

        String browserName = capabilities.getBrowserName();
        String platformName = (capabilities.getCapability("platformName") == null ?
                "" : capabilities.getCapability("platformName").toString().trim());

        // Mobile Driver section
        if (this instanceof VerisoftMobileDriver) {
            if (platformName.equalsIgnoreCase(MobilePlatform.ANDROID))
                tempDriver = new AndroidDriver(remoteAddress, capabilities);
            else if (platformName.equalsIgnoreCase(MobilePlatform.IOS))
                tempDriver = new IOSDriver(remoteAddress, capabilities);
        }

        // Web Driver section
        else {
            // local drivers such as chrome , firefox
            if (remoteAddress == null) {
                tempDriver = instanciateLocalDriver(capabilities);
            } else {
                tempDriver = new RemoteWebDriver(remoteAddress, capabilities);
            }
        }

        initDriver(tempDriver);
    }



    /**
     * Private method to create a proper WebDriver object. If the remoteAddress is null, it will create a
     * local instance of WebDriver. If the remoteAddress is not null, it will create a RemoteWebDriver object,
     * which can hold either remote of local adresses (http://localhost)
     *
     * @param commandExecutor HttpCommandExecutor object
     * @param capabilities  a capabilities object.
     */
    private void createRemoteDriver(HttpCommandExecutor commandExecutor, Capabilities capabilities) {
        WebDriver tempDriver = null;

        String browserName = capabilities.getBrowserName();
        String platformName = (capabilities.getCapability("platformName") == null ?
                "" : capabilities.getCapability("platformName").toString().trim());

        // Mobile Driver section
        if (this instanceof VerisoftMobileDriver) {
            if (platformName.equalsIgnoreCase(MobilePlatform.ANDROID))
                tempDriver = new AndroidDriver(commandExecutor, capabilities);
            else if (platformName.equalsIgnoreCase(MobilePlatform.IOS))
                tempDriver = new IOSDriver(commandExecutor, capabilities);
        }

        // Web Driver section
        else {
            tempDriver = new RemoteWebDriver(commandExecutor, capabilities);
        }

        initDriver(tempDriver);
    }


    /**
     * Start listeners for driver
     *
     * @param driver the driver to add the listeners to
     */
    private void initDriver(WebDriver driver) {

        WebDriverListener[] listenersArr = new WebDriverListener[webDriverlisteners.size()];
        listenersArr = webDriverlisteners.toArray(listenersArr);

        this.driver = new EventFiringDecorator(listenersArr).decorate(driver);

        VerisoftDriverManager.addDriverToMap(driver);
    }


    public VerisoftDriver(WebDriver otherDriver){
        initListeners();
        initDriver(otherDriver);
    }

    /**
     * Cretes a local driver instance
     *
     * @param capabilities capabilities object
     * @return a new WebDriver object
     */
    private WebDriver instanciateLocalDriver(Capabilities capabilities) {


        boolean isHeadless;
        String browserName = capabilities.getBrowserName().toLowerCase();

        switch (browserName) {

            case "chrome":
                if (capabilities.getCapability("driverVersion") == null)
                    WebDriverManager.chromedriver().setup();
                else
                    WebDriverManager.chromedriver().driverVersion(capabilities.getBrowserVersion()).setup();

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.merge(capabilities);
                return new ChromeDriver(chromeOptions);

            case "firefox":

                if (capabilities.getCapability("browserVersion") == null)
                    WebDriverManager.firefoxdriver().setup();
                else
                    WebDriverManager.firefoxdriver().driverVersion(capabilities.getBrowserVersion()).setup();


                FirefoxOptions firefoxOptions = new FirefoxOptions();
                capabilities.merge(firefoxOptions);
                return new FirefoxDriver(firefoxOptions);

            case "ie":
                if (capabilities.getCapability("browserVersion") == null)
                    WebDriverManager.iedriver().setup();
                else
                    WebDriverManager.iedriver().driverVersion(capabilities.getBrowserVersion()).setup();

                InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
                internetExplorerOptions.merge(capabilities);
                return new InternetExplorerDriver(internetExplorerOptions);

            case "microsoftedge":
                if (capabilities.getCapability("browserVersion") == null)
                    WebDriverManager.edgedriver().setup();
                else
                    WebDriverManager.edgedriver().driverVersion(capabilities.getBrowserVersion()).setup();


                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.merge(capabilities);
                return new EdgeDriver(edgeOptions);

            case "safari":
                if (capabilities.getCapability("browserVersion") == null)
                    WebDriverManager.safaridriver().setup();
                else
                    WebDriverManager.safaridriver().driverVersion(capabilities.getBrowserVersion()).setup();


                SafariOptions safariOptions = new SafariOptions();
                safariOptions.merge(capabilities);
                return new SafariDriver(safariOptions);

            default:
                break;
        }
        throw new IllegalStateException("Illegal browser name");
    }
}
