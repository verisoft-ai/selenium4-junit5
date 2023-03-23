package co.verisoft.fw.selenium.drivers.decorators;


import co.verisoft.fw.report.observer.Report;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.decorators.Decorated;
import org.openqa.selenium.support.decorators.WebDriverDecorator;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class LoggingDecorator extends WebDriverDecorator {

    @Override
    public void beforeCall(Decorated target, Method method, Object[] args) {
        Report.debug("Before method '" + method.getName() + "' with args: " + Arrays.toString(args));
    }

    @Override
    public void afterCall(Decorated target, Method method, Object[] args, Object res) {
        Report.debug("After method '" + method.getName() + "' with args: " + Arrays.toString(args)
                + " with result " + res);
    }
}

