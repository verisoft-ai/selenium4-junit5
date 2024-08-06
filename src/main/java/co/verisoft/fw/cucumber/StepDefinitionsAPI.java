package co.verisoft.fw.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class StepDefinitionsAPI implements APIDictionary {

    private final APIDictionary apiDictionary;

    @Autowired
    public StepDefinitionsAPI(APIDictionary apiDictionary) {
        this.apiDictionary = apiDictionary;
    }

    @Given("User has initialized the SSL and certificates at {string} with password {string}")
    public void initCertificates(String certificatePath, String password){
        apiDictionary.initCertificates(certificatePath, password);
    }

    @When("User sets the following headers")
    public void setHeaders(Map<String, String> headersTable) {
        apiDictionary.setHeaders(headersTable);
    }

    @When("User sets the bearer token {string}")
    public void setBearerToken(String token) {
        apiDictionary.setBearerToken(token);
    }

    @When("User sets the jwt as bearer token")
    public void setJwtAsBearerToken() {
        apiDictionary.setJwtAsBearerToken();
    }

    @When("User sends a POST request to {string} with the following parameters")
    public void sendPostRequest(String url, Map<String, String> params) {
        apiDictionary.sendPostRequest(url, params);
    }

    @When("User sends a GET request to {string}")
    public void sendGetRequest(String url) {
        apiDictionary.sendGetRequest(url);
    }

    @When("User sends a GET request to {string} with the following parameters")
    public void sendGetRequestWithParams(String url, Map<String, String> params) {
        apiDictionary.sendGetRequestWithParams(url, params);
    }

    @When("User sends a PUT request to {string} with the following parameters")
    public void sendPutRequest(String url, Map<String, String> params) {
        apiDictionary.sendPutRequest(url, params);
    }
    @When("User sends a DELETE request to {string} with the following parameters")
    public void sendDeleteRequest(String url, Map<String, String> params) {
        apiDictionary.sendDeleteRequest(url, params);
    }
    @When("User generates JWT by the url {string}")
    public void generateJwt(String url) {
        apiDictionary.generateJwt(url);
    }

    @Then("User should receive a response with status code {int}")
    public void verifyStatusCode(int statusCode) {
        apiDictionary.verifyStatusCode(statusCode);
    }

    @Then("The response should contain the following fields")
    public void verifyResponseFields(Map<String, String> expectedFields) {
        apiDictionary.verifyResponseFields(expectedFields);
    }

    @Then("The response header {string} should be {string}")
    public void verifyResponseHeader(String headerName, String expectedValue) {
        apiDictionary.verifyResponseHeader(headerName, expectedValue);
    }

    @Then("The response should contain an error with message {string}")
    public void verifyErrorMessage(String expectedMessage) {
        apiDictionary.verifyErrorMessage(expectedMessage);
    }

    @Then("The response should contain a field {string} with value {string}")
    public void verifyFieldWithValue(String fieldName, String expectedValue) {
        apiDictionary.verifyFieldWithValue(fieldName, expectedValue);
    }
}
