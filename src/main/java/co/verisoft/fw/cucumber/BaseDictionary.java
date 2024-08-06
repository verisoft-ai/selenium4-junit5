package co.verisoft.fw.cucumber;

public interface BaseDictionary {
    void userOnPage(String pageName);
    void startActivity(String appActivity,String appPackage);
    void uploadDriver();

    void tapButton(String buttonId);
    void tapField(String fieldId);
    void enterValueInField(String fieldId, String value);
    void deleteValueInField(String fieldId);
    void selectDate(String date,String fieldId);
    void tapCheckbox(String checkboxId);
    void tapIcon(String iconId);
    void swipe(String direction);
    void chooseOption(String optionId);
    void login(String userName,String password);

    void getElement(String elementId);
    void getScreenTitle(String screenTitleTxt);
    void getScreenSubtitle(String subtitleTxt);
    void getTextOnElement(String txt, String elementId);
    void seeButton(String btnID);
    void seeField(String fieldID);
    void getFieldWithPlaceholder(String fieldId, String placeholderTxt);
    void getErrorMessage(String errorType, String errorMessageTxt);
}
