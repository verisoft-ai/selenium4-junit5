package co.verisoft.fw.selenium.junit.extensions;

import co.verisoft.fw.extentreport.ReportManager;
import co.verisoft.fw.report.observer.Report;
import co.verisoft.fw.report.observer.ReportLevel;
import co.verisoft.fw.report.observer.ReportSource;
import co.verisoft.fw.store.Store;
import co.verisoft.fw.store.StoreManager;
import co.verisoft.fw.store.StoreType;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.test.result.TestResultFactory;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.opentest4j.TestAbortedException;

public class PerfectoLogExtension implements BeforeEachCallback, AfterEachCallback {


    /**
     * Executed before each test method.
     * <p>
     * This method is executed before each individual test method and is responsible for retrieving
     * tags and test name from the extension context and storing them in the thread-local store
     * for later use in reporting.
     *
     * @param extensionContext The ExtensionContext representing the current test context.
     * @throws Exception if any error occurs during the execution of the method.
     * @see StoreManager#getStore(StoreType) StoreManager.getStore(StoreType.LOCAL_THREAD)
     * @see Store#getValueFromStore(Object) Store.getValueFromStore(Object)
     * @since 09.23
     * @author Gili Eliach
     */
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        String[] tags = extensionContext.getTags().toArray(new String[0]);
        String testName = extensionContext.getDisplayName();
        StoreManager.getStore(StoreType.LOCAL_THREAD).putValueInStore("TAGS", tags);
        StoreManager.getStore(StoreType.LOCAL_THREAD).putValueInStore("TESTNAME", testName);
    }

    /**
     * Executed after each test method.
     * <p>
     * This method is executed after each individual test method and is responsible for stopping the
     * Perfecto Reportium test and reporting the test result (success or failure) based on the presence
     * of an execution exception in the test context.
     *
     * @param context The ExtensionContext representing the current test context.
     * @throws Exception if any error occurs during the execution of the method.
     * @author Gili Eliach
     * @see StoreManager#getStore(StoreType) StoreManager.getStore(StoreType.LOCAL_THREAD)
     * @see Store#getValueFromStore(Object) Store.getValueFromStore(Object)
     * @see ReportiumClient
     * @see TestResultFactory#createFailure(String) TestResultFactory.createFailure(String)
     * @see TestResultFactory#createSuccess() TestResultFactory.createSuccess()
     * @since 09.23
     * @author Gili Eliach
     */
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        ReportiumClient reportiumClient = StoreManager.getStore(StoreType.LOCAL_THREAD).getValueFromStore("REPORTIUM");
        if (context.getExecutionException().isPresent()) {
            reportiumClient.testStop(TestResultFactory.createFailure("Test Failed"));

        } else {
            reportiumClient.testStop(TestResultFactory.createSuccess());
        }
    }
}
