package selenium.drivers;

import co.verisoft.fw.report.observer.Report;
import lombok.extern.log4j.Log4j;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;


public class ExampleExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Report.debug("YES!!!!!!!!!!!!!!!!!!!");
    }
}
