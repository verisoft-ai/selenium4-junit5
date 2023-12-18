package co.verisoft.fw.objectrepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ToString

public class DynamicWebElement extends AbstractDynamicElement{//} implements InvocationHandler {

//    private WebDriver driver;
//    private String elementObjectId;
//    private @Nullable String pageName;

    public DynamicWebElement(WebDriver driver, String elementObjectId,@Nullable String pageName)
    {
        super(driver,elementObjectId,pageName);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        WebElement e = resolveElementFromRepository(proxy);
        return method.invoke(e, args);
    }


    private WebElement resolveElementFromRepository(Object proxy) throws IOException {

        ObjectRepository repository = retrieveObjectRepository();
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


//    private static List<Locator> getSortedLocatorsList(ObjectRepository repository, String elementObjectId, @Nullable String pageName) {
//        List<LocatorObject> locatorObjects = repository.getObjectsRepository().stream()
//                .filter(locator -> (locator.getObjectId()
//                        .equalsIgnoreCase(elementObjectId))).collect(Collectors.toList());
//
//        LocatorObject uniqueLocatorObject;
//        if (Objects.nonNull(pageName))
//            uniqueLocatorObject = locatorObjects.stream()
//                    .filter(locator -> (locator.getPageName()
//                            .equalsIgnoreCase(pageName))).findFirst().orElse(null);
//        else if (locatorObjects.isEmpty())
//            uniqueLocatorObject=null;
//        else
//            uniqueLocatorObject = locatorObjects.get(0);
//
//
//        if (Objects.isNull(uniqueLocatorObject))
//            throw new IllegalArgumentException("Unable to find a unique locator in object repository during page object initialization," +
//                    "Do you have a locator with page: " + pageName + " and ID: " + elementObjectId + " in the object repository?");
//
//        List<Locator> locators = uniqueLocatorObject.getLocators();
//        Collections.sort(locators);
//        return locators;
//    }
//
//
//    private static ObjectRepository retrieveObjectRepository() throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        File file = new File("src/test/resources/objectsRepository.json"); // TODO: refactor hard coded file name
//        return objectMapper.readValue(file, ObjectRepository.class);
//    }
//
//
//    private static By resolveLocator(Locator locator) {
//        switch (locator.getType()) {
//            case "id":
//                return By.id(locator.getValue());
//            case "xpath":
//                return By.xpath(locator.getValue());
//            case "cssSelector":
//                return By.cssSelector(locator.getValue());
//            case "name":
//                return By.name(locator.getValue());
//            case "className":
//                return By.className(locator.getValue());
//            case "linkText":
//                return By.linkText(locator.getValue());
//            case "partialLinkText":
//                return By.partialLinkText(locator.getValue());
//            case "tagName":
//                return By.tagName(locator.getValue());
//            default:
//                return null;
//        }
//    }
}
