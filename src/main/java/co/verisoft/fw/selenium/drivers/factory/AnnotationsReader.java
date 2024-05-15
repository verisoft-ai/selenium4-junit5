/*
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
package co.verisoft.fw.selenium.drivers.factory;

import co.verisoft.fw.utils.CapabilitiesReader;
import io.appium.java_client.MobileCommand;
import io.appium.java_client.remote.AppiumCommandExecutor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.http.HttpClient;
import org.springframework.context.ApplicationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * This class is copied from Boni Garcia code, Selenium-Jupiter. <br>
 * Original code can be found
 * <a href="https://github.com/bonigarcia/selenium-jupiter/blob/master/src/main/java/io/github/bonigarcia/seljup/AnnotationsReader.java">here</a>.
 * <br>This class reads the following annotations from the test- @DriverUrl and @DriverCapabilities <br>
 * <br><b>Exmample</b><br>
 * <pre>{@code
 * public class DriverTest {
 *     <code>@DriverUrl</code>
 *     private URL url = new URL("http://localhost:4723/");
 *
 *     <code>@DriverCapabilities</code>
 *     DesiredCapabilities capabilities = new DesiredCapabilities();
 *     {
 *         capabilities.setCapability("platformName", "android");
 *         capabilities.setCapability("deviceName", "emulator-5554");
 *         capabilities.setCapability("appPackage", "com.android.chrome");
 *         capabilities.setCapability("appActivity", "com.google.android.apps.chrome.Main");
 *         capabilities.setCapability("platformVersion", "11");
 *         capabilities.setCapability("automationName", "uiAutomator2");
 *     }
 * }
 * }</pre><br><br>
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@ToString
@NoArgsConstructor
@Slf4j
public class AnnotationsReader {

    public Optional<Capabilities> getCapabilities(ApplicationContext context,Parameter parameter,
                                                  Optional<Object> testInstance) {
        Optional<Capabilities> out = empty();
        Capabilities capabilities;
        try {
            DriverCapabilities driverCapabilities = parameter.getAnnotation(DriverCapabilities.class);
            if (driverCapabilities != null) {
                String driverCapabilitiesKey = driverCapabilities.value();
                if (driverCapabilitiesKey != null) {
                        capabilities = CapabilitiesReader.getDesiredCapabilities(driverCapabilitiesKey, "./src/test/resources/capabilities.json");
                    if (capabilities==null) {
                        capabilities = context.getBean(driverCapabilities.value(), Capabilities.class);
                    }
                    out = Optional.of(capabilities);
                }
            }
            else {
                // If not, search DriverCapabilities in any field
                Optional<Object> annotatedField = seekFieldAnnotatedWith(
                        testInstance, DriverCapabilities.class);
                if (annotatedField.isPresent()) {
                    capabilities = (Capabilities) annotatedField.get();
                    out = of(capabilities);
                }
            }
        } catch (Exception e) {
            log.warn("Exception getting capabilities", e);
        }
        return out;
    }

    public Optional<URL> getUrl(ApplicationContext applicationContext,Parameter parameter,
                                Optional<Object> testInstance) {
        Optional<URL> out = empty();

        try {
            Object urlValue;
            DriverUrl driverUrl = parameter.getAnnotation(DriverUrl.class);
            if (driverUrl != null) {
                // Search first DriverUrl annotation in parameter
                try{
                   urlValue = driverUrl.value();
                   urlValue= new URL (urlValue.toString());
                }
                catch(MalformedURLException e) {
                    urlValue = applicationContext.getBean(driverUrl.value(), URL.class);
                    urlValue= new URL (urlValue.toString());
                }
                out = of((URL) urlValue);

            } else {
                // If not, search DriverUrl in any field
                Optional<Object> annotatedField = seekFieldAnnotatedWith(
                        testInstance, DriverUrl.class);
                if (annotatedField.isPresent()) {
                    urlValue = annotatedField.get();
                    out = of(new URL(urlValue.toString()));
                }
            }
        } catch (Exception e) {
            log.warn("Exception getting URL", e);
        }
        return out;
    }


    public Optional<Object> getCommandExecutor(ApplicationContext applicationContext,Parameter parameter,
                                               Optional<Object> testInstance) {
        Optional<Object> out = empty();

        try {
            Object commandExecutorValue;
            DriverCommandExecutor commandExecutor = parameter.getAnnotation(DriverCommandExecutor.class);
            if (commandExecutor != null) {
                try {
                    // Search first DriverUrl annotation in parameter
                    commandExecutorValue = commandExecutor.value();
                    Class<?> cls = Class.forName((String) commandExecutorValue);
                    HttpClient.Factory instance = (HttpClient.Factory) cls.getDeclaredConstructor().newInstance();
                    Optional<URL> optionalUrl = getUrl(applicationContext, parameter, testInstance);
                    URL url = optionalUrl.orElse(new URL("http://127.0.0.1:4723/wd/hub/"));
                    out = Optional.of(new AppiumCommandExecutor(MobileCommand.commandRepository, url, instance));
                    return out;
                } catch (ClassNotFoundException e) {
                    commandExecutorValue = applicationContext.getBean(commandExecutor.value(), HttpCommandExecutor.class);
                    out = Optional.of(commandExecutorValue);
                }
            }else {
                // If not, search DriverUrl in any field
                Optional<Object> annotatedField = seekFieldAnnotatedWith(
                        testInstance, DriverCommandExecutor.class);
                if (annotatedField.isPresent()) {
                    commandExecutorValue = annotatedField.get();
                    out = Optional.of(commandExecutorValue);
                }
            }

        } catch (Exception e) {
            log.warn("Exception getting HttpCommandExecutor", e);
        }
        return out;
    }

    public boolean isBoolean(String s) {
        boolean isBool = s.equalsIgnoreCase("true")
                || s.equalsIgnoreCase("false");
        if (!isBool) {
            log.trace("Value {} is not boolean", s);
        }
        return isBool;
    }

    public boolean isNumeric(String s) {
        boolean numeric = StringUtils.isNumeric(s);
        if (!numeric) {
            log.trace("Value {} is not numeric", s);
        }
        return numeric;
    }

    public <T> T getFromAnnotatedField(Optional<Object> testInstance,
                                       Class<? extends Annotation> annotationClass,
                                       Class<T> capabilitiesClass) {
        if (capabilitiesClass == null) {
            throw new RuntimeException("The parameter capabilitiesClass must not be null");
        }
        return seekFieldAnnotatedWith(testInstance, annotationClass,
                capabilitiesClass).orElse(null);
    }

    public Optional<Object> seekFieldAnnotatedWith(
            Optional<Object> testInstance,
            Class<? extends Annotation> annotation) {
        return seekFieldAnnotatedWith(testInstance, annotation, null);
    }

    private static <T> Optional<T> seekFieldAnnotatedWith(
            Optional<Object> testInstance,
            Class<? extends Annotation> annotation, Class<T> annotatedType) {
        Optional<T> out = empty();
        try {
            if (testInstance.isPresent()) {
                Object object = testInstance.get();
                Class<?> clazz = object.getClass();
                out = getField(annotation, annotatedType, clazz, object);
                if (!out.isPresent()) {
                    // If annotation not present in class, look for it in the
                    // parent(s)
                    Class<?> superclass;
                    while ((superclass = clazz
                            .getSuperclass()) != Object.class) {
                        out = getField(annotation, annotatedType, superclass,
                                object);
                        if (out.isPresent()) {
                            break;
                        }
                        clazz = clazz.getSuperclass();
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Exception seeking field in {} annotated with {}",
                    annotatedType, annotation, e);
        }
        return out;
    }

    @SuppressWarnings("unchecked")
    private static <T> Optional<T> getField(
            Class<? extends Annotation> annotation, Class<T> annotatedType,
            Class<?> clazz, Object object)
            throws IllegalAccessException {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(annotation) && (annotatedType == null
                    || annotatedType.isAssignableFrom(field.getType()))) {
                field.setAccessible(true);
                if (annotatedType != null) {
                    return of(annotatedType.cast(field.get(object)));
                }
                return (Optional<T>) of(field.get(object));
            }
        }
        return empty();
    }


    public Optional<List<Object>> getKeyValue(String keyValue) {
        StringTokenizer st = new StringTokenizer(keyValue, "=");
        if (st.countTokens() != 2) {
            log.warn("Invalid format in {} (expected key=value)", keyValue);
            return empty();
        }
        String key = st.nextToken();
        String value = st.nextToken();
        Object returnedValue = value;
        if (isBoolean(value)) {
            returnedValue = Boolean.valueOf(value);
        } else if (isNumeric(value)) {
            returnedValue = Integer.valueOf(value);
        }
        return of(asList(key, returnedValue));
    }
}
