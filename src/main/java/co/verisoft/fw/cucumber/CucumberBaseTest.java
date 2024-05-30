package co.verisoft.fw.cucumber;


import io.cucumber.core.runtime.MyRunner;

import java.util.ArrayList;
import java.util.Arrays;


public abstract class CucumberBaseTest {


    public void executeTest(String testName, String packageName, String... tags) {
        String className = this.getClass().getSimpleName();
       // String packageName = this.getClass().getPackage().getName();
        // Add your test execution logic here
        String extractedTestName = className.substring(0, this.getClass().getSimpleName().length() - 4);
        ArrayList<String> optionsList = new ArrayList<>(Arrays.asList(
                "--plugin", "json:target/Cucumber/cucumber-report.json",
                "--glue", packageName,
                "--monochrome",
                "src/test/resources/Features/" + extractedTestName + ".feature"
        ));

        if (tags.length > 0) {
            optionsList.add("--tags");
            optionsList.addAll(Arrays.asList(tags));
        }
//        for (Pickle scenario : pickles) {
//            String[] cucumberOptions = optionsList.toArray(new String[0]);
//            MyRunner.run(cucumberOptions, Thread.currentThread().getContextClassLoader(), scenario.getName());
//        }


        String[] cucumberOptions = optionsList.toArray(new String[0]);
        MyRunner.run(cucumberOptions, Thread.currentThread().getContextClassLoader(),testName);
    }
}
