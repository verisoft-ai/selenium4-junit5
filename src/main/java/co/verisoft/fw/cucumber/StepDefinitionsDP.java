package co.verisoft.fw.cucumber;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class StepDefinitionsDP implements BaseDictionary {

    protected BaseDictionary baseDictionary;

    @Autowired
    public StepDefinitionsDP(BaseDictionary baseDictionary) {
        this.baseDictionary = baseDictionary;
    }

    @When("I tap on button {string}")
    public void tapButton(String buttonId) {
        this.baseDictionary.tapButton(buttonId);
    }

    @When("I tap on field {string}")
    public void tapField(String fieldId) {
        this.baseDictionary.tapField(fieldId);
    }

    @When("I enter on field {string} the value of {string}")
    public void enterValueInField(String fieldId, String value) {
        this.baseDictionary.enterValueInField(fieldId, value);
    }

    @When("I delete value in field {string}")
    public void deleteValueInField(String fieldId) {
        this.baseDictionary.deleteValueInField(fieldId);
    }

    @When("I select date {string} in field {string}")
    public void selectDate(String fieldId, String date) {
        this.baseDictionary.selectDate(fieldId, date);
    }

    @When("I tap on checkbox {string}")
    public void tapCheckbox(String checkboxId) {
        this.baseDictionary.tapCheckbox(checkboxId);
    }

    @Then("I should see element {string}")
    public String getElement(String elementId) {
        return this.baseDictionary.getElement(elementId);
    }

    @Then("I should see screen title {string}")
    public String getScreenTitle(String screenTitleTxt) {
        return this.baseDictionary.getScreenTitle(screenTitleTxt);
    }

    @Then("I should see screen subtitle {string}")
    public String getScreenSubtitle(String subtitleTxt) {
        return this.baseDictionary.getScreenSubtitle(subtitleTxt);
    }

    @When("I choose option {string}")
    public void chooseOption(String optionId) {
        this.baseDictionary.chooseOption(optionId);
    }

    @Then("I should see field {string} with placeholder of {string}")
    public void getFieldWithPlaceholder(String fieldId, String placeholderTxt) {
        this.baseDictionary.getFieldWithPlaceholder(fieldId, placeholderTxt);
    }

    @Then("I should see error message {string} with text {string}")
    public void getErrorMessage(String errorType, String errorMessageTxt) {
        this.baseDictionary.getErrorMessage(errorType, errorMessageTxt);
    }

    @Then("I should see text {string} on element {string}")
    public String getTextOnElement(String txt, String elementId) {
        return this.baseDictionary.getTextOnElement(txt, elementId);
    }
    @When("I swipe the screen from {double} to {double} in {int} seconds")
    public void swipeScreenByTouchAction(double percentHeightStart, double percentHeightEnd, int secDuration) {
        this.baseDictionary.swipeScreenByTouchAction(percentHeightStart,percentHeightEnd,secDuration);
    }
    @When("I start the activity {string} in package {string}")
    public void startActivity(String appActivity,String appPackage) {
        this.baseDictionary.startActivity(appActivity,appPackage);
    }

    @When("I upload driver")
    public void uploadDriver()
    {
        this.baseDictionary.uploadDriver();
    }
}