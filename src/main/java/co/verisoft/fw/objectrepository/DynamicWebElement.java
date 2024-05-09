package co.verisoft.fw.objectrepository;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@ToString
public class DynamicWebElement extends AbstractDynamicElement {

    public DynamicWebElement(WebDriver driver, ObjectRepository repository,
                             String elementObjectId, @Nullable String pageName) {
        super(driver, repository, elementObjectId, pageName);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        WebElement e = resolveElementFromRepository(proxy);
        return method.invoke(e, args);
    }

    private WebElement resolveElementFromRepository(Object proxy) throws IOException {

        List<Locator> sortedLocatorsList = getSortedLocatorsList(repository, this.elementObjectId, pageName);

        for (Locator locator : sortedLocatorsList) {
            By by = resolveLocator(locator);
            try {
                return this.driver.findElement(by);
            } catch (Exception e) {
                // NO-OP
            }
        }

        log.debug("Could not perform findElement to element " + this.elementObjectId);
        return new NonInteractableWebElement();
    }
}
