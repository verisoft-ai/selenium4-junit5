package co.verisoft.fw.cucumber;

import co.verisoft.fw.utils.Property;
import io.cucumber.core.runtime.MyRunner;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class CucumberBaseTest {

    public void executeTest(Scenario scenarioName, FeatureFile featureFileName, Dictionary dictionaryImplementationProfile, PackageName packageName) {
        System.clearProperty("spring.profiles.active");
        System.setProperty("spring.profiles.active", dictionaryImplementationProfile.getName());
        System.setProperty("cucumber.object-factory", "io.cucumber.spring.SpringFactory");

        ArrayList<String> optionsList = new ArrayList<>(Arrays.asList(
                "--plugin", "json:target/Cucumber/cucumber-report.json",
                "--glue", packageName.getName(),
                "--glue", "co.verisoft.fw.cucumber",
                "--monochrome",
                "src/test/resources/features/" + featureFileName.getName() + ".feature"
        ));

        String[] cucumberOptions = optionsList.toArray(new String[0]);
        MyRunner.run(cucumberOptions, Thread.currentThread().getContextClassLoader(), scenarioName.getName());
    }

    public void executeTest(Scenario scenarioName, FeatureFile featureFile,Dictionary dictionaryImplementationProfile) {
        PackageName defaultPackageName = getDefaultPackageName();
        executeTest(scenarioName, featureFile, dictionaryImplementationProfile, defaultPackageName);
    }
    public void executeTest(Scenario scenarioName, FeatureFile featureFile,PackageName packageName) {
        Dictionary dictionaryImplementationProfile=getDefaultDictionary();
        executeTest(scenarioName, featureFile, dictionaryImplementationProfile, packageName);
    }
    public void executeTest(Scenario scenarioName,Dictionary dictionaryImplementationProfile, PackageName packageName) {
        FeatureFile defaultFeatureFile = getDefaultFeatureFile();
        executeTest(scenarioName, defaultFeatureFile, dictionaryImplementationProfile, packageName);
    }

    public void executeTest(Scenario scenarioName, Dictionary dictionaryImplementationProfile) {
        PackageName defaultPackageName = getDefaultPackageName();
        FeatureFile defaultFeatureFile = getDefaultFeatureFile();
        executeTest(scenarioName, defaultFeatureFile, dictionaryImplementationProfile, defaultPackageName);
    }
    public void executeTest(Scenario scenarioName, PackageName packageName) {
        Dictionary dictionaryImplementationProfile=getDefaultDictionary();
        FeatureFile defaultFeatureFile = getDefaultFeatureFile();
        executeTest(scenarioName, defaultFeatureFile, dictionaryImplementationProfile, packageName);
    }
    public void executeTest(Scenario scenarioName, FeatureFile featureFile ) {
        PackageName defaultPackageName = getDefaultPackageName();
        Dictionary dictionaryImplementationProfile=getDefaultDictionary();
        executeTest(scenarioName, featureFile, dictionaryImplementationProfile, defaultPackageName);
    }

    public void executeTest(Scenario scenarioName) {
        PackageName defaultPackageName = getDefaultPackageName();
        FeatureFile defaultFeatureFile = getDefaultFeatureFile();
        Dictionary dictionaryImplementationProfile=getDefaultDictionary();
        executeTest(scenarioName, defaultFeatureFile, dictionaryImplementationProfile, defaultPackageName);
    }

    private FeatureFile getDefaultFeatureFile() {
        String className = this.getClass().getSimpleName();
        String extractedTestName = className.substring(0, this.getClass().getSimpleName().length() - 4);
        return new FeatureFile(extractedTestName);
    }

    private PackageName getDefaultPackageName() {
        return new PackageName("co.verisoft.fw.cucumber");
    }

    private Dictionary getDefaultDictionary() {
        String dictionaryImplementationProfile = new Property("application.properties").getProperty("dictionary.implementation.profile");
        return new Dictionary(dictionaryImplementationProfile);
    }
}
