package co.verisoft.fw.objectrepository;

import co.verisoft.fw.report.observer.Report;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

@Slf4j
public class ObjectReporsitoryFactory {

    private ObjectReporsitoryFactory() {
    }

    //TODO: add support to List<WebElement>
    public static void initObjects(WebDriver driver, Object page) {
        @Nullable String pageName = getPageName();
        
        Field[] allFields = page.getClass().getDeclaredFields();
        for (Field field : allFields) {
            field.setAccessible(true);
            if (field.getType().equals(WebElement.class) &&
                    field.getAnnotation(ObjectRepositoryItem.class) != null) {
                try {
                    field.set(page, (WebElement) Proxy.newProxyInstance(
                            WebElement.class.getClassLoader(),
                            new Class[]{WebElement.class},
                            new DynamicWebElement(driver, field.getAnnotation(ObjectRepositoryItem.class).id())));
                } catch (Exception e) {
                    Report.error("Could not proxy object from object repository. Message is " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static String getPageName(Object page) {
        if (page.getClass().getAnnotation(PageName.class))
    }
}
