package io.cucumber.core.runtime;

import co.verisoft.fw.report.observer.Report;
import io.cucumber.core.options.CommandlineOptionsParser;
import io.cucumber.core.options.CucumberProperties;
import io.cucumber.core.options.CucumberPropertiesParser;
import io.cucumber.core.options.RuntimeOptions;
import io.cucumber.plugin.event.Status;

import java.util.Arrays;
import java.util.List;

public class MyRunner {

    public static void run(String[] argv, ClassLoader classLoader,String testName)  {
        Report.info(testName);
        List<String> scenariosToRun = Arrays.asList(testName);
        RuntimeOptions propertiesFileOptions = (new CucumberPropertiesParser()).parse(CucumberProperties.fromPropertiesFile()).build();
        RuntimeOptions environmentOptions = (new CucumberPropertiesParser()).parse(CucumberProperties.fromEnvironment()).build(propertiesFileOptions);
        RuntimeOptions systemOptions = (new CucumberPropertiesParser()).parse(CucumberProperties.fromSystemProperties()).build(environmentOptions);
        CommandlineOptionsParser commandlineOptionsParser = new CommandlineOptionsParser(System.out);
        RuntimeOptions runtimeOptions = commandlineOptionsParser.parse(argv).addDefaultGlueIfAbsent().addDefaultFeaturePathIfAbsent().addDefaultSummaryPrinterIfNotDisabled().enablePublishPlugin().build(systemOptions);
        MyRuntime runtime = MyRuntime.builder()
                .withRuntimeOptions(runtimeOptions)
                .withClassLoader(() -> classLoader)
                .withScenarioNames(scenariosToRun)
                .build();
        runtime.run();
        if (runtime.exitingStatus().is(Status.FAILED))
            throw new RuntimeException(runtime.getError());

    }
}
