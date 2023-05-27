package co.verisoft.fw.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RootConfigProperties {

    @Value("${selenium.logs.verbose}")
    private boolean seleniumLogsVerbose;

    @Value("${selenium.wait.timeout}")
    private int seleniumWaitTimeout;

    @Value("${polling.interval}")
    private int pollingInterval;

    @Value("${max_retry_number}")
    private int maxRetryNumber;
}
