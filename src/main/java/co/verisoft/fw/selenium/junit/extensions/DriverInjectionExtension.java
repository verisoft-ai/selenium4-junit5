/*
 * (C) Copyright 2022 VeriSoft (http://www.verisoft.co)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.verisoft.fw.selenium.junit.extensions;

import co.verisoft.fw.selenium.drivers.VerisoftDriver;
import co.verisoft.fw.selenium.drivers.VerisoftDriverManager;
import co.verisoft.fw.selenium.drivers.VerisoftMobileDriver;
import co.verisoft.fw.selenium.drivers.factory.AnnotationsReader;
import co.verisoft.fw.selenium.drivers.factory.SingleSession;
import io.appium.java_client.AppiumDriver;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.HttpCommandExecutor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.Optional;

/**
 * Extension to inject VerisoftDriver and VerisoftMobileDriver directly into the test instances<br>
 * This class is inspired by Boni Garcia code Selenium-Jupiter. Boni Garcia's code can be found
 * <a href="https://github.com/bonigarcia/selenium-jupiter/blob/master/src/main/java/io/github/bonigarcia/seljup/SeleniumJupiter.java">here</a>.
 *
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a> @ <a href="http://www.verisoft.co">www.VeriSoft.co</a>
 * @since 0.0.. (March 2022)
 */
@ToString
@Slf4j
public class DriverInjectionExtension implements ParameterResolver, AfterEachCallback, AfterAllCallback {

    AnnotationsReader annotationsReader;

    public DriverInjectionExtension() {
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

        // First, get the type
        Parameter parameter = parameterContext.getParameter();
        int index = parameterContext.getIndex();
        Optional<Object> testInstance = extensionContext.getTestInstance();

        log.trace("Resolving parameter " + parameter + ", index " + index);

        Class<?> type = parameter.getType();

        // Mobile Driver
        if (AppiumDriver.class.isAssignableFrom(type) || VerisoftMobileDriver.class.isAssignableFrom(type))
            return resolveMobileDriver(extensionContext, testInstance, parameter);

        // Web Driver
        else if (WebDriver.class.isAssignableFrom(type))
            return resolveWebDriver(extensionContext, parameter, testInstance, type);
        else
            throw new RuntimeException("Could not resolve parameter. " + type + " is neither assignable from " +
                    "WebDriver nor VerisoftDriver / VerisoftMobileDriver");
    }


    /**
     * Get the capabilities and create new VerisoftDriver insatnce
     * @param extensionContext Junit 5 context object
     * @param parameter paameter extracted from the test to be initizlized with WebDriver based object
     * @param testInstance Junit 5 instance object
     * @param type Type definition of the object to be initialized
     * @return a VerisoftDriver object.
     */
    private Object resolveWebDriver(ExtensionContext extensionContext, Parameter parameter, Optional<Object> testInstance, Class<?> type) {
        Optional<Capabilities> capabilities = annotationsReader.getCapabilities(parameter,
                extensionContext.getTestInstance());

        Optional<Object> commandExecutor = annotationsReader.getCommandExecutor(parameter,
                testInstance);

        Optional<URL> url = annotationsReader.getUrl(parameter, testInstance, "");

        if (commandExecutor.isPresent())
            return new VerisoftDriver(((HttpCommandExecutor) commandExecutor.get()), capabilities.orElse(null));
        else if (url.isPresent())
            return new VerisoftDriver(url.orElse(null), capabilities.orElse(null));
        else
            return new VerisoftDriver(capabilities.orElse(null));
    }


    /**
     * Get the capabilities and create new VerisoftobileDriver insatnce
     * @param extensionContext Junit 5 context object
     * @param testInstance Junit 5 instance object
     * @param parameter paameter extracted from the test to be initizlized with WebDriver based object
     * @return a VerisoftMobileDriver object.
     */
    private Object resolveMobileDriver(ExtensionContext extensionContext, Optional<Object> testInstance, Parameter parameter) {

        Optional<Capabilities> capabilities = annotationsReader.getCapabilities(parameter,
                extensionContext.getTestInstance());

        Optional<Object> commandExecutor = annotationsReader.getCommandExecutor(parameter,
                testInstance);

        Optional<URL> url = annotationsReader.getUrl(parameter, testInstance, "");

        if (commandExecutor.isPresent())
            return new VerisoftMobileDriver(((HttpCommandExecutor) commandExecutor.get()), capabilities.orElse(null));
        else
            return new VerisoftMobileDriver(url.orElse(null), capabilities.orElse(null));
    }


    /**
     * Is the test method marked as Junit 5 TestTemplate?
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
