package co.verisoft.fw.objectrepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
public class DynamicWebElements implements InvocationHandler {

    private WebDriver driver;
    private String elementObjectId;
    private @Nullable String pageName;

    public DynamicWebElements(WebDriver driver, String elementObjectId, @Nullable String pageName) {
        this.driver = driver;
        this.elementObjectId = elementObjectId;
        this.pageName = pageName;
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
//List<WebElement> resolveElementsFromRepository(Object proxy) throws IOException {
//    ObjectRepository repository = retrieveObjectRepository();
//    List<Locator> sortedLocatorsList = getSortedLocatorsList(repository, this.elementObjectId, pageName);
//
//    List<WebElement> foundElements = new ArrayList<>();
//
//    for (Locator locator : sortedLocatorsList) {
//        By by = resolveLocator(locator);
//        try {
//            List<WebElement> elements = this.driver.findElements(by);
//            for (WebElement element : elements) {
//                if (!foundElements.contains(element)) {
//                    // Element not in the list, add it
//                    foundElements.add(element);
//                    log.debug("Found element using locator: {}", by);
//                }
//            }
//        } catch (Exception e) {
//            // Log exception (if needed) and continue to the next locator
//            log.debug("Exception while finding elements with locator: {}. Exception: {}", by, e.getMessage());
//        }
//    }
//
//    if (foundElements.isEmpty()) {
//        log.debug("Could not perform findElements for element {}", this.elementObjectId);
//    }
//
//    return foundElements;
//}
    private static List<Locator> getSortedLocatorsList(ObjectRepository repository, String elementObjectId, @Nullable String pageName) {
        List<LocatorObject> locatorObjects = repository.getObjectsRepository().stream()
                .filter(locator -> (locator.getObjectId()
                        .equalsIgnoreCase(elementObjectId))).collect(Collectors.toList());

        LocatorObject uniqueLocatorObject;
        if (Objects.nonNull(pageName))
            uniqueLocatorObject = locatorObjects.stream()
                    .filter(locator -> (locator.getPageName()
                            .equalsIgnoreCase(pageName))).findFirst().orElse(null);
        else if (locatorObjects.isEmpty())
            uniqueLocatorObject=null;
        else
            uniqueLocatorObject = locatorObjects.get(0);


        if (Objects.isNull(uniqueLocatorObject))
            throw new IllegalArgumentException("Unable to find a unique locator in object repository during page object initialization," +
                    "Do you have a locator with page: " + pageName + " and ID: " + elementObjectId + " in the object repository?");

        List<Locator> locators = uniqueLocatorObject.getLocators();
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
