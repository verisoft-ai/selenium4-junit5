package co.verisoft.fw.cucumber;

import co.verisoft.fw.utils.Property;
import io.cucumber.core.runtime.MyRunner;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class CucumberBaseTest {

    public void executeTest(String testName, String dictionaryImplementationProfile, String packageName) {
        String className = this.getClass().getSimpleName();
        String extractedTestName = className.substring(0, this.getClass().getSimpleName().length() - 4);

        System.setProperty("spring.profiles.active", dictionaryImplementationProfile);
        System.setProperty("cucumber.object-factory", "io.cucumber.spring.SpringFactory");

        ArrayList<String> optionsList = new ArrayList<>(Arrays.asList(
                "--plugin", "json:target/Cucumber/cucumber-report.json",
                "--glue", packageName,
                "--monochrome",
                "src/test/resources/Features/" + extractedTestName + ".feature"
        ));

        String[] cucumberOptions = optionsList.toArray(new String[0]);
        MyRunner.run(cucumberOptions, Thread.currentThread().getContextClassLoader(), testName);
    }

    public void executeTest(String testName, String dictionaryImplementationProfile) {
        String defaultPackageName = "co.verisoft.fw.cucumber";
        executeTest(testName, dictionaryImplementationProfile, defaultPackageName);
    }

    public void executeTest(String testName) {
        String dictionaryImplementationProfile = new Property("application.properties").getProperty("dictionary.implementation.profile");
        executeTest(testName, dictionaryImplementationProfile);
    }

    public void executeTestWithPackage(String testName, String packageName) {
        String dictionaryImplementationProfile = new Property("application.properties").getProperty("dictionary.implementation.profile");
        executeTest(testName, dictionaryImplementationProfile, packageName);
    }
}
