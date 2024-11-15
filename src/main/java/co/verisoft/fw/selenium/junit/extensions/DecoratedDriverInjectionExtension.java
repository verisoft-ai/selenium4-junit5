package co.verisoft.fw.selenium.junit.extensions;

import co.verisoft.fw.selenium.drivers.*;
import co.verisoft.fw.selenium.drivers.decorators.LoggingDecorator;
import co.verisoft.fw.selenium.drivers.factory.AnnotationsReader;
import co.verisoft.fw.selenium.drivers.factory.SingleSession;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.Optional;

@Slf4j
public class DecoratedDriverInjectionExtension implements ParameterResolver, AfterEachCallback, AfterAllCallback {

    AnnotationsReader annotationsReader;

    public DecoratedDriverInjectionExtension() {
        annotationsReader = new AnnotationsReader();
    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

        // Extract the parameter and it's type
        Parameter parameter = parameterContext.getParameter();
        Class<?> type = parameter.getType();

        // Is the type assignable from WebDriver && test is NOT a test template?
        return (WebDriver.class.isAssignableFrom(type) && !isTestTemplate(extensionContext));
    }


    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(extensionContext);

        // First, get the type
        Parameter parameter = parameterContext.getParameter();
        int index = parameterContext.getIndex();
        Optional<Object> testInstance = extensionContext.getTestInstance();

        log.trace("Resolving parameter " + parameter + ", index " + index);

        Class<?> type = parameter.getType();

        // Mobile Driver
        if (AppiumDriver.class.isAssignableFrom(type) || VerisoftMobileDriver.class.isAssignableFrom(type))
            return resolveMobileDriver(applicationContext,extensionContext, testInstance, parameter);

            // Web Driver
        else if (WebDriver.class.isAssignableFrom(type))
            return resolveWebDriver(applicationContext,extensionContext, parameter, testInstance, type);
        else
            throw new RuntimeException("Could not resolve parameter. " + type + " is neither assignable from " +
                    "WebDriver nor VerisoftDriver / VerisoftMobileDriver");
    }


    /**
     * Get the capabilities and create new VerisoftDriver insatnce
     *
     * @param extensionContext Junit 5 context object
     * @param parameter        paameter extracted from the test to be initizlized with WebDriver based object
     * @param testInstance     Junit 5 instance object
     * @param type             Type definition of the object to be initialized
     * @return a VerisoftDriver object.
     */
    private Object resolveWebDriver(ApplicationContext applicationContext,ExtensionContext extensionContext, Parameter parameter, Optional<Object> testInstance, Class<?> type) {
        Optional<Capabilities> capabilities= annotationsReader.getCapabilities(applicationContext,parameter,
                extensionContext.getTestInstance());
        DecoratedDriver driver =  new DecoratedDriver(capabilities.orElse(null));
        driver.decorateDriver(LoggingDecorator.class);
        return driver;
    }


    /**
     * Get the capabilities and create new VerisoftobileDriver insatnce
     *
     * @param extensionContext Junit 5 context object
     * @param testInstance     Junit 5 instance object
     * @param parameter        paameter extracted from the test to be initizlized with WebDriver based object
     * @return a VerisoftMobileDriver object.
     */
    private Object resolveMobileDriver(ApplicationContext context,ExtensionContext extensionContext, Optional<Object> testInstance, Parameter parameter) {
        Optional<Capabilities> capabilities =annotationsReader.getCapabilities(context,parameter,
                extensionContext.getTestInstance());
        Optional<URL> url = annotationsReader.getUrl(context,parameter, testInstance);
        DecoratedMobileDriver driver =  new DecoratedMobileDriver(capabilities.orElse(null));
        driver.decorateDriver(LoggingDecorator.class);
        return driver;
    }


    /**
     * Is the test method marked as Junit 5 TestTemplate?
     *
     * @param extensionContext Junit 5 context object
     * @return true if TestTemplate, false otherwise
     */
    private boolean isTestTemplate(ExtensionContext extensionContext) {
        Optional<Method> testMethod = extensionContext.getTestMethod();
        return testMethod.isPresent()
                && testMethod.get().isAnnotationPresent(TestTemplate.class);
    }


    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {

        // Close the driver, unless test class is marked as @SingleSession, which will has 1 driver for class
        if (!isSingleSession(extensionContext))
            VerisoftDriverManager.getDriver().quit();
    }


    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {

        // Closes the driver
        if (isSingleSession(extensionContext))
            VerisoftDriverManager.getDriver().quit();
    }


    /**
     * Is test class is marked as @SingleSession, which will has 1 driver for class?
     *
     * @param extensionContext Junit 5 context object
     * @return True if the class is a single session class, false otherwise
     */
    private boolean isSingleSession(ExtensionContext extensionContext) {
        boolean singleSession = false;
        Optional<Class<?>> testClass = extensionContext.getTestClass();
        if (testClass.isPresent()) {
            singleSession = testClass.get()
                    .isAnnotationPresent(SingleSession.class);
        }
        log.trace("Single session " + singleSession);
        return singleSession;
    }
}
