package co.verisoft.fw.objectrepository;

import co.verisoft.fw.report.observer.Report;
import co.verisoft.fw.utils.Property;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ObjectReporsitoryFactory {
    protected static ObjectRepository repository;

    static ObjectRepository retrieveObjectRepository() {
        ObjectMapper objectMapper = new ObjectMapper();
        String repositoryFilePath = new Property().getProperty("object.repository.path");
        File file = new File(repositoryFilePath);
        try {
            return objectMapper.readValue(file, ObjectRepository.class);
        } catch (IOException e) {
            throw new RuntimeException("Object repository file not found" + e);
        }
    }

    public static void initObjects(WebDriver driver, Object page) {
        repository = retrieveObjectRepository();
        @Nullable String pageName = getPageName(page);

        Field[] allFields = page.getClass().getDeclaredFields();
        for (Field field : allFields) {
            field.setAccessible(true);

            if (field.getType().equals(WebElement.class) &&
                    field.getAnnotation(ObjectRepositoryItem.class) != null) {
                try {
                    field.set(page, createWebElementProxy(driver, field.getAnnotation(ObjectRepositoryItem.class).id(), pageName));
                } catch (Exception e) {
                    Report.error("Could not proxy object from object repository. Message is " + e.getMessage());
                    throw new RuntimeException(e);
                }
            } else if (isListOfWebElements(field) &&
                    field.getAnnotation(ObjectRepositoryItem.class) != null) {
                try {
                    field.set(page, createListWebElementProxy(driver, field.getAnnotation(ObjectRepositoryItem.class).id(), pageName));
                } catch (Exception e) {
                    Report.error("Could not proxy list of objects from object repository. Message is " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static boolean isListOfWebElements(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            if (typeArguments.length == 1 && typeArguments[0] == WebElement.class) {
                return field.getType() == List.class;
            }
        }
        return false;
    }

    private static Object createWebElementProxy(WebDriver driver, String elementObjectId, String pageName) {
        return Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class[]{WebElement.class},
                new DynamicWebElement(driver, repository, elementObjectId, pageName));
    }

    private static Object createListWebElementProxy(WebDriver driver, String elementObjectId, String pageName) {
        return Proxy.newProxyInstance(
                List.class.getClassLoader(),
                new Class[]{List.class},
                (proxy, method, args) -> {
                    List<WebElement> elements = new DynamicWebElements(driver, repository, elementObjectId, pageName).resolveElementsFromRepository(proxy);
                    return method.invoke(elements, args);
                });
    }

    private static @Nullable String getPageName(Object page) {
        if (Objects.nonNull(page.getClass().getAnnotation(PageObjectName.class)))
            return page.getClass().getSimpleName();
        else
            return null;
    }
}


