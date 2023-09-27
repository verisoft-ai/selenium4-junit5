package co.verisoft.fw.selenium.junit.extensions;

import co.verisoft.fw.extentreport.ReportManager;
import co.verisoft.fw.report.observer.Report;
import co.verisoft.fw.report.observer.ReportLevel;
import co.verisoft.fw.report.observer.ReportSource;
import co.verisoft.fw.store.StoreManager;
import co.verisoft.fw.store.StoreType;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.test.result.TestResultFactory;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.opentest4j.TestAbortedException;

public class PerfectoLogExtension implements BeforeEachCallback,AfterEachCallback {


    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        String [] tags = extensionContext.getTags().toArray(new String[0]);
        String testName =extensionContext.getDisplayName();
        StoreManager.getStore(StoreType.LOCAL_THREAD).putValueInStore("TAGS",tags);
        StoreManager.getStore(StoreType.LOCAL_THREAD).putValueInStore("TESTNAME",testName);
//
//        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
//                .withWebDriver(driver)
//                .build();
//        ReportiumClient reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
//        reportiumClient.testStart(testName, new TestContext(tags));
//        StoreManager.getStore(StoreType.LOCAL_THREAD).putValueInStore("REPORTIUM",reportiumClient);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        ReportiumClient reportiumClient= StoreManager.getStore(StoreType.LOCAL_THREAD).getValueFromStore("REPORTIUM");
        if (context.getExecutionException().isPresent()) //we have an exception-skip or fail
        {
            reportiumClient.testStop(TestResultFactory.createFailure("Test Failed"));

        } else {
            reportiumClient.testStop(TestResultFactory.createSuccess());
        }
    }
}
