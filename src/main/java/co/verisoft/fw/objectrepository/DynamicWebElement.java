package co.verisoft.fw.objectrepository;

import co.verisoft.fw.report.observer.Report;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class DynamicWebElement implements InvocationHandler {

    private WebDriver driver;
    private String elementObjectId;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        WebElement e = resolveElementFromRepository(proxy);
        return method.invoke(e, args);
    }


    private WebElement resolveElementFromRepository(Object proxy) throws IOException {

        ObjectRepository repository = retrieveObjectRepository();
        List<Locator> sortedLocatorsList = getSortedLocatorsList(repository, this.elementObjectId);

        for (Locator locator : sortedLocatorsList) {
            By by = resolveLocator(locator);
            try {
                return this.driver.findElement(by);
            } catch (Exception e) {
                // NO-OP
            }
        }

        Report.warn("Could not perform findElement to element " + this.elementObjectId);
        return null;
    }


    private static List<Locator> getSortedLocatorsList(ObjectRepository repository, String elementObjectId) {
        LocatorObject locatorObject = repository.getObjectsRepository().stream()
                .filter(locator -> (locator.getObjectId()
                        .equalsIgnoreCase(elementObjectId))).findAny().orElse(null);

        assert locatorObject != null;
        List<Locator> locators = locatorObject.getLocators();
        Collections.sort(locators);
        return locators;
    }


    private static ObjectRepository retrieveObjectRepository() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/test/resources/objectsRepository.json"); // TODO: refactor hard coded file name
        return objectMapper.readValue(file, ObjectRepository.class);
    }


    private static By resolveLocator(Locator locator) {
        switch (locator.getType()) {
            case "id":
                return By.id(locator.getValue());
            case "xpath":
                return By.xpath(locator.getValue());
            case "cssSelector":
                return By.cssSelector(locator.getValue());
            case "name":
                return By.name(locator.getValue());
            case "className":
                return By.className(locator.getValue());
            case "linkText":
                return By.linkText(locator.getValue());
            case "partialLinkText":
                return By.partialLinkText(locator.getValue());
            case "tagName":
                return By.tagName(locator.getValue());
            default:
                return null;
        }
    }
}
