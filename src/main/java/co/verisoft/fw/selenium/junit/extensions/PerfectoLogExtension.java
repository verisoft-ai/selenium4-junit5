package co.verisoft.fw.selenium.junit.extensions;

import co.verisoft.fw.store.Store;
import co.verisoft.fw.store.StoreManager;
import co.verisoft.fw.store.StoreType;
import co.verisoft.fw.utils.Property;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.test.result.TestResultFactory;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PerfectoLogExtension implements BeforeEachCallback, AfterTestExecutionCallback {


    /**
     * Executed before each test method.
     * <p>
     * This method is executed before each individual test method and is responsible for retrieving
     * tags and test name from the extension context and storing them in the thread-local store
     * for later use in reporting.
     *
     * @param extensionContext The ExtensionContext representing the current test context.
     * @throws Exception if any error occurs during the execution of the method.
     * @author Gili Eliach
     * @since 09.23
     */
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        String[] tags = extensionContext.getTags().toArray(new String[0]);
        String testName = extensionContext.getDisplayName();
        StoreManager.getStore(StoreType.LOCAL_THREAD).putValueInStore("PERFECTO_LOG", true);
        setTagsAndTestName(tags, testName);

    }

    /**
     * Executed after each test execution.
     * <p>
     * This method is executed  after test execution individual test method and is responsible for stopping the
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
     */
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (new Property("application.properties").getBooleanProperty("perfecto.report")) {
            ReportiumClient reportiumClient = StoreManager.getStore(StoreType.LOCAL_THREAD).getValueFromStore("REPORTIUM");
            if (context.getExecutionException().isPresent()) {
                reportiumClient.testStop(TestResultFactory.createFailure("Test Failed"));

            } else {
                reportiumClient.testStop(TestResultFactory.createSuccess());
            }
        }
    }

    /**
     * This function retrieves tags and a test name, storing them in the thread-local store for later use in reporting.
     * @see StoreManager#getStore(StoreType) StoreManager.getStore(StoreType.LOCAL_THREAD)
     * @see Store#getValueFromStore(Object) Store.getValueFromStore(Object)
     * @author Gili Eliach
     * @since 10.23
     */
    public static void setTagsAndTestName(String[] tags, String testName) {
        StoreManager.getStore(StoreType.LOCAL_THREAD).putValueInStore("TAGS", tags);
        StoreManager.getStore(StoreType.LOCAL_THREAD).putValueInStore("TESTNAME", testName);
    }

}
