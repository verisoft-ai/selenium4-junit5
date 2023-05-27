package selenium.drivers;

import co.verisoft.fw.selenium.drivers.factory.DriverCapabilities;
import co.verisoft.fw.selenium.drivers.factory.DriverUrl;
import co.verisoft.fw.selenium.junit.extensions.DecoratedDriverInjectionExtension;
import co.verisoft.fw.selenium.junit.extensions.SeleniumLogExtesion;
import co.verisoft.fw.utils.Slf4jObserver;
import config.BaseTest;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith({SeleniumLogExtesion.class, DecoratedDriverInjectionExtension.class})
@Disabled
public class NewVerisoftDriverTest extends BaseTest {

    @BeforeAll
    public static void beforeAll() {
        Slf4jObserver slf4jObserver = new Slf4jObserver();

    }
    @DriverCapabilities
    private DesiredCapabilities capabilities = new DesiredCapabilities();
    {
        capabilities.setCapability("browserName", "chrome");
//        capabilities.setCapability("headless", true);
    }

    @DriverUrl
    @Autowired
    @Nullable URL url;


    @Test
    public void test1(WebDriver driver){
        driver.get("https://www.google.com");
    }
}
