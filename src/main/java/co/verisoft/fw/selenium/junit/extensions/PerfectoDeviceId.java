package co.verisoft.fw.selenium.junit.extensions;

import org.apiguardian.api.API;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@API(
        status = API.Status.EXPERIMENTAL,
        since = "0.0.2"
)
public @interface PerfectoDeviceId {
    String value() ;
}