package co.verisoft.fw.objectrepository;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;


@Slf4j
@ToString
public class DynamicWebElements extends AbstractDynamicElement{

public DynamicWebElements(WebDriver driver, String elementObjectId,String pageName)
{
    super(driver,elementObjectId,pageName);
}
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> elements = resolveElementsFromRepository(proxy);
        return method.invoke(elements, args);
    }

    List<WebElement> resolveElementsFromRepository(Object proxy) throws IOException {
        ObjectRepository repository = retrieveObjectRepository();
        List<Locator> sortedLocatorsList = getSortedLocatorsList(repository, this.elementObjectId, pageName);

        for (Locator locator : sortedLocatorsList) {
            By by = resolveLocator(locator);
            try {
                List<WebElement> elements = this.driver.findElements(by);
                if (!elements.isEmpty()) {
                    // Elements found, return immediately
                    log.debug("Found elements using locator: {}", by);
                    return elements;
                }
            } catch (Exception e) {
                // NO-OP
            }
        }

        log.debug("Could not perform findElements to element " + this.elementObjectId);
        return Collections.emptyList();
    }
}
