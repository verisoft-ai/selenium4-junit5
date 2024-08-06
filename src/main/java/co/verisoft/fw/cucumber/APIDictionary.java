package co.verisoft.fw.cucumber;

import java.util.Map;

public interface APIDictionary {
    void initCertificates(String certificatePath, String password);

    void setHeaders(Map<String, String> headersTable);

    void setBearerToken(String token);

    void setJwtAsBearerToken();

    void sendPostRequest(String url, Map<String, String> params);

    void sendGetRequest(String url);

    void sendGetRequestWithParams(String url, Map<String, String> params);

    void sendPutRequest(String url, Map<String, String> params);

    void sendDeleteRequest(String url, Map<String, String> params);
    void generateJwt(String url);

    void verifyStatusCode(int statusCode);

    void verifyResponseFields(Map<String, String> expectedFields);

    void verifyResponseHeader(String headerName, String expectedValue);

    void verifyErrorMessage(String expectedMessage);

    void verifyFieldWithValue(String fieldName, String expectedValue);
}


