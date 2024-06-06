package co.verisoft.fw.cucumber;

public interface BaseDictionary {

    void tapButton(String buttonId);
    void tapField(String fieldId);
    void enterValueInField(String fieldId, String value);
    void deleteValueInField(String fieldId);
    void selectDate(String fieldId, String date);
    void tapCheckbox(String checkboxId);
    String getElement(String elementId);
    String getScreenTitle(String screenTitleTxt);
    String getScreenSubtitle(String subtitleTxt);
    void chooseOption(String optionId);
    void getFieldWithPlaceholder(String fieldId, String placeholderTxt);
    void getErrorMessage(String errorType, String errorMessageTxt);
    String getTextOnElement(String txt, String elementId);
    void swipeScreenByTouchAction(double percentHeightStart, double percentHeightEnd, int secDuration);
    void startActivity(String appPackage,String appActivity);
    void uploadDriver();
}
