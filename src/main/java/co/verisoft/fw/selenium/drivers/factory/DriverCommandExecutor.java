package co.verisoft.fw.selenium.drivers.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class is an addition to Boni Garcia code, Selenium-Jupiter. <br>
 * It allows to define HttpClientExecutorObject to init WebDriver
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface DriverCommandExecutor {
    String value() default "";
}