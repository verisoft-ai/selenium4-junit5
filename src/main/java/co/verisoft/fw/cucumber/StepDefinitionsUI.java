package co.verisoft.fw.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class StepDefinitionsUI implements BaseDictionary {

    protected BaseDictionary baseDictionary;

    @Autowired
    public StepDefinitionsUI(BaseDictionary baseDictionary) {
        this.baseDictionary = baseDictionary;
    }

    @Given("user on {string} page")
    public void userOnPage(String pageName){
        this.baseDictionary.userOnPage(pageName);
    }
    @Given("user starts the activity {string} in package {string}")
    public void startActivity(String appActivity, String appPackage) {
        this.baseDictionary.startActivity(appActivity, appPackage);
    }
    @Given("user uploads driver")
    public void uploadDriver() {
        this.baseDictionary.uploadDriver();
    }


    @When("user taps on {string} button")
    public void tapButton(String buttonId) {
        this.baseDictionary.tapButton(buttonId);
    }

    @When("user taps on {string} field")
    public void tapField(String fieldId) {
        this.baseDictionary.tapField(fieldId);
    }

    @When("user inserts into {string} the value {string}")
    public void enterValueInField(String fieldId, String value) {
        this.baseDictionary.enterValueInField(fieldId, value);
    }
    @When("user deletes the value from the field {string}")
    public void deleteValueInField(String fieldId) {
        this.baseDictionary.deleteValueInField(fieldId);
    }
    @When("user selects date {string} from the calendar {string}")
    public void selectDate(String date, String fieldId) {
        this.baseDictionary.selectDate(date, fieldId);
    }
    @When("user taps on {string} checkbox")
    public void tapCheckbox(String checkboxId) {
        this.baseDictionary.tapCheckbox(checkboxId);
    }
    @When("user taps on {string} icon")
    public void tapIcon(String iconId) {
        this.baseDictionary.tapIcon(iconId);
    }
    @When("user swipes {string}")
    public void swipe(String direction) {
        this.baseDictionary.swipe(direction);
    }
    @When("user chooses an option {string}")
    public void chooseOption(String optionId) {
        this.baseDictionary.chooseOption(optionId);
    }
    @When("user logs into the system with username {string} and password {string}")
    public void login(String userName, String password) {
        this.baseDictionary.login(userName, password);
    }


    @Then("user should see the element {string}")
    public void getElement(String elementId) {
        this.baseDictionary.getElement(elementId);
    }
    @Then("user should see the title {string}")
    public void getScreenTitle(String screenTitleTxt) {
        this.baseDictionary.getScreenTitle(screenTitleTxt);
    }
    @Then("user should see the subtitle {string}")
    public void getScreenSubtitle(String subtitleTxt) {
        this.baseDictionary.getScreenSubtitle(subtitleTxt);
    }
    @Then("user should see the text {string} in the element {string}")
    public void getTextOnElement(String txt, String elementId) {
        this.baseDictionary.getTextOnElement(txt, elementId);
    }
    @Then("user should see the button {string}")
    public void seeButton(String btnID) {
        this.baseDictionary.seeButton(btnID);
    }
    @Then("user should see the field {string}")
    public void seeField(String fieldID) {
        this.baseDictionary.seeField(fieldID);
    }
    @Then("user should see field {string} with placeholder of {string}")
    public void getFieldWithPlaceholder(String fieldId, String placeholderTxt) {
        this.baseDictionary.getFieldWithPlaceholder(fieldId, placeholderTxt);
    }
    @Then("user should see the error message {string} in the element {string}")
    public void getErrorMessage(String errorType, String errorMessageTxt) {
        this.baseDictionary.getErrorMessage(errorType, errorMessageTxt);
    }
}