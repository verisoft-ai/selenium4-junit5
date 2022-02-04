import co.verisoft.fw.selenium.drivers.VerisoftDriver;
import co.verisoft.fw.selenium.drivers.VerisoftDriverManager;
import co.verisoft.fw.selenium.listeners.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.events.internal.EventFiringKeyboard;

import java.util.ArrayList;
import java.util.List;

@Disabled
public class BasicTest {

    @Test
    public void test1() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");

        List<WebDriverListener> listeners = new ArrayList<>();

        listeners.add(new AlertListener());
        listeners.add(new NavigationListener());
        listeners.add(new OptionsListener());
        listeners.add(new TimeoutsListener());
        listeners.add(new DriverListener());
        listeners.add(new WebElementListener());
        listeners.add(new WindowListener());

        WebDriverListener[] listenersArr = new WebDriverListener[listeners.size()];
        listenersArr = listeners.toArray(listenersArr);


        WebDriver driver2 = new VerisoftDriver(capabilities);
        WebDriver driver = new EventFiringDecorator(listenersArr).decorate(driver2);
        driver.get("http://www.google.com");
        WebElement button = driver.findElement(By.xpath(".//a[text()='Sign in']"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", button);
    }

    @Test
    public void test2() throws InterruptedException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");

        WebDriver driver = new VerisoftDriver(capabilities);

        WebDriver driver2 = VerisoftDriverManager.getDriver();
        driver2.get("http://www.google.com");
        WebElement button = driver2.findElement(By.xpath(".//a[text()='Sign in']"));
        JavascriptExecutor js = (JavascriptExecutor) driver2;
        js.executeScript("arguments[0].click();", button);
        driver2.quit();
    }
}
