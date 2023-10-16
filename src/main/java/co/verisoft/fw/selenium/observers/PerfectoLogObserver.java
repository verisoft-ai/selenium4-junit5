package co.verisoft.fw.selenium.observers;

import co.verisoft.fw.report.observer.BaseObserver;
import co.verisoft.fw.report.observer.ReportEntry;
import co.verisoft.fw.report.observer.ReportLevel;
import co.verisoft.fw.selenium.drivers.VerisoftDriverManager;
import co.verisoft.fw.store.StoreManager;
import co.verisoft.fw.store.StoreType;
import com.perfecto.reportium.client.ReportiumClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;


/**
 * The `PerfectoLogObserver` class is a custom observer for reporting in a testing framework.
 * It extends the `BaseObserver` class and is designed to work with Perfecto reporting.
 * This class allows you to control the minimum report level that should be logged.
 *
 * @author Gili Eliach
 * @since 10.23
 */
@Slf4j
public class PerfectoLogObserver extends BaseObserver {

    /**
     * The minimum report level that should be logged.
     */
    @Getter
    private final ReportLevel minReportLevel;

    /**
     * Constructs a new `PerfectoLogObserver` with the specified minimum report level.
     *
     * @param minReportLevel The minimum report level to log.
     */
    public PerfectoLogObserver(ReportLevel minReportLevel) {
        this.minReportLevel = minReportLevel;
    }

    /**
     * This method is called when a report entry is observed.
     * It determines whether the report entry should be logged and at what level.
     *
     * @param reportEntry The report entry to be observed.
     */
    @Override
    public void update(ReportEntry reportEntry) {

        // Check if the report level is below the minimum specified level
        if (reportEntry.getReportLevel().compareTo(minReportLevel) < 0)
            return;

        // Determine if entry should be printed to extent report, and the proper level of entry
        ReportLevel level;
        if (reportEntry.getReportLevel() == ReportLevel.DEBUG)
            level = ReportLevel.DEBUG;
        else if (reportEntry.getReportLevel() == ReportLevel.INFO)
            level = ReportLevel.INFO;
        else if (reportEntry.getReportLevel() == ReportLevel.WARNING)
            level = ReportLevel.WARNING;
        else if (reportEntry.getReportLevel() == ReportLevel.ERROR)
            level = ReportLevel.ERROR;
        else if (reportEntry.getReportLevel() == ReportLevel.FATAL)
            level = ReportLevel.FATAL;
        else
            level = ReportLevel.INFO;

        // Build the report message
        String reportMsg = reportEntry.getMsg();
        try {
            WebDriver driver = VerisoftDriverManager.getDriver();
            // Write to perfecto
            ReportiumClient reportiumClient = StoreManager.getStore(StoreType.LOCAL_THREAD).getValueFromStore("REPORTIUM");
            if (level == ReportLevel.DEBUG)
                reportiumClient.reportiumAssert("DEBUG: " + reportMsg, true);
            else {
                reportiumClient.reportiumAssert(reportMsg, level != ReportLevel.ERROR);//will be false for error level
            }
        }
        catch (Throwable throwable)
        {
            log.warn("no driver anymore");
        }

    }
}

