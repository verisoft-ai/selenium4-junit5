package co.verisoft.fw.perfecto;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Original version url:
 * https://github.com/qmetry/qaf-perfecto-support/blob/master/src/com/qmetry/qaf/automation/support/perfecto/PerfectoUtils.java
 *
 * @since October, 2023
 */
@Slf4j
public class PerfectoUtils {


    private static final String HTTPS = "https://";
    private static final String MEDIA_REPOSITORY = "/services/repositories/media/";
    private static final String LIST_HANDSETS = "/services/handsets?operation=list";

    private static final String UPLOAD_OPERATION = "operation=upload&overwrite=true";
    private static final String UTF_8 = "UTF-8";


    /**
     * Uploads a file to the media repository. Example:
     * uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com",
     * "123456", "C:\\test\\ApiDemos.apk", "PRIVATE:apps/ApiDemos.apk");
     */
    public static void uploadMedia(String host, String user, String password, String path,
                                   String repositoryKey) throws IOException {
        File file = new File(path);
        byte[] content = readFile(file);
        uploadMedia(host, user, password, content, repositoryKey);
    }

    /**
     * Uploads a file to the media repository. Example: URL url = new URL(
     * "http://file.appsapk.com/wp-content/uploads/downloads/Sudoku%20Free.apk")
     * ; uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com",
     * "123456", url, "PRIVATE:apps/ApiDemos.apk");
     */
    public static void uploadMedia(String host, String user, String password,
                                   URL mediaURL, String repositoryKey) throws IOException {
        byte[] content = readURL(mediaURL);
        uploadMedia(host, user, password, content, repositoryKey);
    }

    /**
     * Uploads content to the media repository. Example:
     * uploadMedia("demo.perfectomobile.com", "john@perfectomobile.com",
     * "123456", content, "PRIVATE:apps/ApiDemos.apk");
     */
    public static void uploadMedia(String host, String user, String password,
                                   byte[] content, String repositoryKey)
            throws UnsupportedEncodingException, MalformedURLException, IOException {
        if (content != null) {
            String encodedUser = URLEncoder.encode(user, "UTF-8");
            String encodedPassword = URLEncoder.encode(password, "UTF-8");
            String urlStr = HTTPS + host + MEDIA_REPOSITORY + repositoryKey + "?"
                    + UPLOAD_OPERATION + "&user=" + encodedUser + "&password="
                    + encodedPassword;
            URL url = new URL(urlStr);

            sendRequest(content, url);
        }
    }


    private static void sendRequest(byte[] content, URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.connect();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(content);
        outStream.writeTo(connection.getOutputStream());
        outStream.close();
        int code = connection.getResponseCode();
        if (code > HttpURLConnection.HTTP_OK) {
            handleError(connection);
        }
    }

    private static void handleError(HttpURLConnection connection) throws IOException {
        String msg = "Failed to upload media.";
        InputStream errorStream = connection.getErrorStream();
        if (errorStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(errorStream, UTF_8);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            try {
                StringBuilder builder = new StringBuilder();
                String outputString;
                while ((outputString = bufferReader.readLine()) != null) {
                    if (builder.length() != 0) {
                        builder.append("\n");
                    }
                    builder.append(outputString);
                }
                String response = builder.toString();
                msg += "Response: " + response;
            } finally {
                bufferReader.close();
            }
        }
        throw new RuntimeException(msg);
    }

    private static byte[] readFile(File path) throws FileNotFoundException, IOException {
        int length = (int) path.length();
        byte[] content = new byte[length];
        InputStream inStream = new FileInputStream(path);
        try {
            inStream.read(content);
        } finally {
            inStream.close();
        }
        return content;
    }

    private static byte[] readURL(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        int code = connection.getResponseCode();
        if (code > HttpURLConnection.HTTP_OK) {
            handleError(connection);
        }
        InputStream stream = connection.getInputStream();

        if (stream == null) {
            throw new RuntimeException(
                    "Failed to get content from url " + url + " - no response stream");
        }
        byte[] content = read(stream);
        return content;
    }

    private static byte[] read(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int nBytes = 0;
            while ((nBytes = input.read(buffer)) > 0) {
                output.write(buffer, 0, nBytes);
            }
            byte[] result = output.toByteArray();
            return result;
        } finally {
            try {
                input.close();
            } catch (IOException e) {

            }
        }
    }


    public static void installApp(String filePath, RemoteWebDriver d,
                                  boolean shouldInstrument) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("file", filePath);
        if (shouldInstrument) {
            params.put("instrument", "instrument");
        }
        d.executeScript("mobile:application:install", params);
    }

    private static Map<String, String> getAppParams(String app, String by) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(by, app);
        return params;
    }

    // by = "name" or "identifier"
    public static void startApp(RemoteWebDriver driver, String app, String by) {
        driver.executeScript("mobile:application:open", getAppParams(app, by));
    }

    // by = "name" or "identifier"
    public static void closeApp(RemoteWebDriver driver, String app, String by) {
        driver.executeScript("mobile:application:close", getAppParams(app, by));
    }

    // by = "name" or "identifier"
    public static void cleanApp(RemoteWebDriver driver, String app, String by) {
        driver.executeScript("mobile:application:clean", getAppParams(app, by));
    }

    // by = "name" or "identifier"
    public static void uninstallApp(RemoteWebDriver driver, String app, String by) {
        driver.executeScript("mobile:application:uninstall", getAppParams(app, by));
    }

    public static void uninstallAllApps(RemoteWebDriver driver) {
        Map<String, String> params = new HashMap<String, String>();
        driver.executeScript("mobile:application:reset", params);
    }

    public static String getAppInfo(RemoteWebDriver driver, String property) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("property", property);
        return (String) driver.executeScript("mobile:application:info", params);
    }


    public static void switchToContext(RemoteWebDriver driver, String context) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", context);
        executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
    }

    public static void waitForPresentTextVisual(RemoteWebDriver driver, String text,
                                                int seconds) {
        isText(driver, text, seconds);
    }

    public static void waitForPresentImageVisual(RemoteWebDriver driver, String image,
                                                 int seconds) {
        isImg(driver, image, seconds);
    }

    private static String isImg(RemoteWebDriver driver, String img, Integer timeout) {
        String context = getCurrentContext(driver);
        switchToContext(driver, "VISUAL");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("content", img);
        params.put("measurement", "accurate");
        params.put("source", "primary");
        params.put("threshold", "90");
        params.put("timeout", timeout);
        params.put("match", "bounded");
        params.put("imageBounds.needleBound", 25);
        Object result = driver.executeScript("mobile:checkpoint:image", params);
        switchToContext(driver, context);
        return result.toString();
    }


    private static String isText(RemoteWebDriver driver, String text, Integer timeout) {
        String context = getCurrentContext(driver);
        switchToContext(driver, "VISUAL");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("content", text);
        if (timeout != null) {
            params.put("timeout", timeout);
        }
        Object result = driver.executeScript("mobile:checkpoint:text", params);
        switchToContext(driver, context);
        return result.toString();
    }

    /**
     * @param driver
     * @return the current context - "NATIVE_APP", "WEBVIEW", "VISUAL"
     */
    public static String getCurrentContext(RemoteWebDriver driver) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        return (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE,
                null);
    }

    // device utils

    /**
     * Clicks on a single or sequence of physical device keys.
     * Mouse-over the device keys to identify them, then input into the Keys
     * parameter according to the required syntax.
     * <p>
     * Common keys include:
     * LEFT, RIGHT, UP, DOWN, OK, BACK, MENU, VOL_UP, VOL_DOWN, CAMERA, CLEAR.
     * <p>
     * The listed keys are not necessarily supported by all devices. The
     * available keys depend on the device.
     *
     * @param driver      the RemoteWebDriver
     * @param keySequence the single or sequence of keys to click
     */
    public static void pressKey(RemoteWebDriver driver, String keySequence) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("keySequence", keySequence);
        driver.executeScript("mobile:presskey", params);
    }

    /**
     * Performs the swipe gesture according to the start and end coordinates.
     * <p>
     * Example swipe left:<br/>
     * start: 60%,50% end: 10%,50%
     *
     * @param driver the RemoteWebDriver
     * @param start  write in format of x,y. can be in pixels or
     *               percentage(recommended).
     * @param end    write in format of x,y. can be in pixels or
     *               percentage(recommended).
     */
    public static void swipe(RemoteWebDriver driver, String start, String end) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("start", start);
        params.put("end", end);

        driver.executeScript("mobile:touch:swipe", params);

    }

    /**
     * Performs the touch gesture according to the point coordinates.
     *
     * @param driver the RemoteWebDriver
     * @param point  write in format of x,y. can be in pixels or
     *               percentage(recommended).
     */
    public static void touch(RemoteWebDriver driver, String point) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("location", point); // 50%,50%

        driver.executeScript("mobile:touch:tap", params);
    }

    /**
     * Hides the virtual keyboard display.
     *
     * @param driver the RemoteWebDriver
     */
    public static void hideKeyboard(RemoteWebDriver driver) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mode", "off");

        driver.executeScript("mobile:keyboard:display", params);

    }

    /**
     * Rotates the device to landscape, portrait, or its next state.
     *
     * @param driver    the RemoteWebDriver
     * @param restValue the "next" operation, or the "landscape" or "portrait" state.
     * @param by        the "state" or "operation"
     */
    // TODO: need additional description.
    public static void rotateDevice(RemoteWebDriver driver, String restValue, String by) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(by, restValue);
        driver.executeScript("mobile:handset:rotate", params);
    }

    // by = "address" or "coordinates"
    public static void setLocation(RemoteWebDriver driver, String location, String by) {

        Map<String, String> params = new HashMap<String, String>();
        params.put(by, location);

        driver.executeScript("mobile:location:set", params);
    }


    public static String getDeviceLocation(RemoteWebDriver driver) {
        Map<String, String> params = new HashMap<String, String>();
        return (String) driver.executeScript("mobile:location:get", params);
    }

    public static void resetLocation(RemoteWebDriver driver) {
        Map<String, String> params = new HashMap<String, String>();
        driver.executeScript("mobile:location:reset", params);
    }

    public static void goToHomeScreen(RemoteWebDriver driver) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("target", "All");

        driver.executeScript("mobile:handset:ready", params);
    }

    public static void lockDevice(RemoteWebDriver driver, int sec) {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("timeout", sec);

        driver.executeScript("mobile:screen:lock", params);
    }

    public static void setTimezone(RemoteWebDriver driver, String timezone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("timezone", timezone);

        driver.executeScript("mobile:timezone:set", params);
    }

    public static String getTimezone(RemoteWebDriver driver) {
        Map<String, String> params = new HashMap<String, String>();

        return (String) driver.executeScript("mobile:timezone:get", params);
    }


    public static void resetTimezone(RemoteWebDriver driver) {
        Map<String, String> params = new HashMap<String, String>();
        driver.executeScript("mobile:timezone:reset", params);
    }

    public static void takeScreenshot(RemoteWebDriver driver, String repositoryPath,
                                      boolean shouldSave) {
        Map<String, String> params = new HashMap<String, String>();
        if (shouldSave) {
            params.put("key", repositoryPath);
        }
        driver.executeScript("mobile:screen:image", params);
    }

    public static void executePerfectoCommand(RemoteWebDriver driver, String command, Map<String, String> params) {

        if (params == null) {
            throw new IllegalArgumentException("params cannot be null");
        }
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }
        if (!command.contains("mobile:")) {
            throw new IllegalArgumentException("command must start with mobile:");
        }

        driver.executeScript("mobile:screen:image", params);
    }


    public static boolean isDevice(Capabilities caps) {
        // first check if driver is a mobile device:
        if (isDesktopBrowser(caps))
            return false;
        return caps.getCapability("deviceName") != null;
    }

    public static boolean isDesktopBrowser(Capabilities caps) {
        // first check if deviceName set to browser name which triggers desktop:
        return Arrays
                .asList("firefox", "chrome", "iexplorer", "internet explorer", "safari")
                .contains((caps.getCapability("browserVersion") + "").toLowerCase());
    }

    /**
     * Checks if is device.
     * <p>
     * TODO: complete me
     *
     * @param driver the driver
     * @return true, if is device
     */
    public static boolean isDevice(RemoteWebDriver driver) {
        return isDevice(driver.getCapabilities());
    }

    public static Map<String, Object> getDeviceProperties(Capabilities capabilities) {
        Map<String, Object> deviceProperties = new HashMap<String, Object>();

        if (!isDevice(capabilities))
            return deviceProperties;

        try {
            //	return ConfigurationConverter.getMap(readDevicesProperties(params).subset("handset"));
        } catch (Exception e) {
            throw new Error("Unable to get device id", e);
        }
        return deviceProperties;
    }
}
