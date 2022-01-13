import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BasicTest {

    //@Test
    public void test1() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.google.com");
        Thread.sleep(3000);
    }
    @Test
    public void test2(){
        System.out.println("Hello, Module");
        Assertions.assertTrue(true);
    }
}
