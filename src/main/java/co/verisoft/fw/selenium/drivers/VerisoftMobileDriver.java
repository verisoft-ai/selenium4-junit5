/*
 * (C) Copyright 2022 VeriSoft (http://www.verisoft.co)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.verisoft.fw.selenium.drivers;

import co.verisoft.fw.report.observer.Report;
import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.*;
import io.appium.java_client.battery.BatteryInfo;
import io.appium.java_client.battery.HasBattery;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import io.appium.java_client.remote.SupportsLocation;
import io.appium.java_client.remote.SupportsRotation;
import io.appium.java_client.screenrecording.BaseStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.BaseStopScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.html5.RemoteLocationContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;


/**
 * VeriSoft mobile driver. A driver for mobile application. <b>This driver refers to Android driver and
 * ios driver only. All other drivers are not supported by VerisoftMobileDriver at present time</b>. The driver
 * supports both local and remote driver:- <br>
 *
 * <p>
 * VeriSoft mobile driver is a concrete class which implements all the interfaces AppiumDriver and DefaultGenericMobileDriver. <br>
 * <p>
 * VeriSoftMobileDriver implements WebDriver behavior, and in addition adds functionality. The main functionality which
 * is currently supported:<br>
 * 2. Extended logging<br>
 * 3. All available events are registered. See events in the "See Also" section<br>
 * 4. WebDriver is wrapped with EventFiringDecorator. See in the "See Also" section <br>
 * <br><br>
 * The driver is instanciated by specifying the relevant DesiredCapabilities, and if the driver is a remote driver,
 * specifying remote url. All of VeriSoft's ctors expectes at least
 * a DesiredCapabilities object.
 * <br>
 *
 * @author David Yehezkel
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a> @ <a href="http://www.verisoft.co">www.VeriSoft.co</a>
 * @since 1.9.6
 */
@SuppressWarnings({"deprecation", "rawtypes"})
@Slf4j
@ToString
//@Deprecated
public class VerisoftMobileDriver extends VerisoftDriver implements
        SupportsContextSwitching, SupportsRotation, SupportsLocation,
        HidesKeyboard, HasDeviceTime, PullsFiles,
        InteractsWithApps, SupportsLegacyAppManagement,
        HasAppStrings, PerformsTouchActions, HasOnScreenKeyboard, LocksDevice,
        PushesFiles, CanRecordScreen, HasBattery, HasSettings {

    public VerisoftMobileDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public VerisoftMobileDriver(URL remoteAddress, Capabilities capabilities) {
        super(remoteAddress, capabilities);
    }

    public VerisoftMobileDriver(HttpCommandExecutor commandExecutor, Capabilities capabilities) {
        super(commandExecutor, capabilities);
    }

    @Override
    public BatteryInfo getBatteryInfo() {
        Report.debug("Appium Driver using: BatteryInfo");

        if (((WrapsDriver) this.driver).getWrappedDriver() instanceof AndroidDriver)
            return ((AndroidDriver) this.driver).getBatteryInfo();
        else if (((WrapsDriver) this.driver).getWrappedDriver() instanceof IOSDriver)
            return ((IOSDriver) this.driver).getBatteryInfo();
        else
            return null;
    }

    @Override
    public Response execute(String s, Map<String, ?> map) {
        Report.debug("Appium Driver using: execute -> s: " + s + " map: " + Arrays.toString(map.entrySet().toArray()));
        return ((AppiumDriver) this.driver).execute(s, map);
    }

    @Override
    public Response execute(String s) {
        Report.debug("Appium Driver using: execute -> s: " + s);
        return ((AppiumDriver) this.driver).execute(s);
    }

    @Override
    public DeviceRotation rotation() {
        DeviceRotation result = ((SupportsRotation) this.driver).rotation();
        Report.debug("Appium Driver using: rotation -> Rotation is: " + result.toString());
        return result;
    }

    @Override
    public void rotate(DeviceRotation rotation) {
        Report.debug("Appium Driver using: rotate -> to: " + rotation.toString());
        ((SupportsRotation) this.driver).rotate(rotation);
    }

    @Override
    public void rotate(ScreenOrientation orientation) {
        Report.debug("Appium Driver using: rotate -> to orientation: " + orientation.toString());
        ((SupportsRotation) this.driver).rotate(orientation);
    }

    @Override
    public ScreenOrientation getOrientation() {
        ScreenOrientation orientation = ((SupportsRotation) this.driver).getOrientation();
        Report.debug("Appium Driver using: screen orientation is: " + orientation);
        return orientation;
    }

    @Override
    public <T extends BaseStartScreenRecordingOptions> String startRecordingScreen(T options) {
        Report.debug("Appium Driver using: startRecordingScreen-> options : " + options);
        return ((CanRecordScreen) this.driver).startRecordingScreen(options);
    }

    @Override
    public String startRecordingScreen() {
        Report.debug("Appium Driver using: startRecordingScreen");
        return ((CanRecordScreen) this.driver).startRecordingScreen();
    }

    @Override
    public <T extends BaseStopScreenRecordingOptions> String stopRecordingScreen(T options) {
        Report.debug("Appium Driver using: stopRecordingScreen");
        return ((CanRecordScreen) this.driver).stopRecordingScreen(options);
    }

    @Override
    public String stopRecordingScreen() {
        Report.debug("Appium Driver using: stopRecordingScreen");
        return ((CanRecordScreen) this.driver).stopRecordingScreen();
    }

    @Override
    public RemoteLocationContext getLocationContext() {
        RemoteLocationContext ctx = ((SupportsLocation) this.driver).getLocationContext();
        Report.debug("Appium Driver using: getLocationContext-> context is " + ctx);
        return ctx;
    }

    @Override
    public Location location() {
        Location location = ((SupportsLocation) this.driver).location();
        Report.debug("Appium Driver using: location-> location is " + location);
        return location;
    }

    @Override
    public void setLocation(Location location) {
        Report.debug("Appium Driver using: setLocation-> location is " + location);
        ((SupportsLocation) this.driver).setLocation(location);
    }

    @Override
    public Map<String, String> getAppStringMap() {
        Map<String, String> result = ((HasAppStrings) this.driver).getAppStringMap();
        Report.debug("Appium Driver using: getAppStringMap-> result is " + Arrays.toString(result.entrySet().toArray()));
        return result;
    }

    @Override
    public Map<String, String> getAppStringMap(String language) {
        Map<String, String> result = ((HasAppStrings) this.driver).getAppStringMap(language);
        Report.debug("Appium Driver using: getAppStringMap-> result is " + Arrays.toString(result.entrySet().toArray()));
        return result;
    }

    @Override
    public Map<String, String> getAppStringMap(String language, String stringFile) {
        Map<String, String> result = ((HasAppStrings) this.driver).getAppStringMap(language, stringFile);
        Report.debug("Appium Driver using: getAppStringMap-> result is " + Arrays.toString(result.entrySet().toArray()));
        return result;
    }

    @Override
    public String getDeviceTime(String format) {
        String deviceTime = ((HasDeviceTime) this.driver).getDeviceTime(format);
        Report.debug("Appium Driver using: getDeviceTime-> time is " + deviceTime);
        return deviceTime;
    }

    @Override
    public String getDeviceTime() {
        String deviceTime = ((HasDeviceTime) this.driver).getDeviceTime();
        Report.debug("Appium Driver using: getDeviceTime-> time is " + deviceTime);
        return deviceTime;
    }

    @Override
    public boolean isKeyboardShown() {
        boolean isShown = ((HasOnScreenKeyboard) this.driver).isKeyboardShown();
        Report.debug("Appium Driver using: isKeyboardShown-> result " + isShown);
        return isShown;
    }

    @Override
    public void hideKeyboard() {
        Report.debug("Appium Driver using: hideKeyboard");
        ((HidesKeyboard) this.driver).hideKeyboard();
    }

    @Override
    public void installApp(String appPath) {
        Report.debug("Appium Driver using: installApp -> app: " + appPath);
        ((InteractsWithApps) this.driver).installApp(appPath);
    }

    @Override
    public void installApp(String appPath, @Nullable BaseInstallApplicationOptions options) {
        Report.debug("Appium Driver using: installApp -> app: " + appPath + " Options: " + options);
        ((InteractsWithApps) this.driver).installApp(appPath, options);
    }

    @Override
    public boolean isAppInstalled(String bundleId) {
        boolean isInstalled = ((InteractsWithApps) this.driver).isAppInstalled(bundleId);
        Report.debug("Appium Driver using: isAppInstalled -> result: " + isInstalled);
        return isInstalled;
    }

    @Override
    public void runAppInBackground(Duration duration) {
        Report.debug("Appium Driver using: runAppInBackground -> duration: " + duration);
        ((InteractsWithApps) this.driver).runAppInBackground(duration);
    }

    @Override
    public boolean removeApp(String bundleId) {
        boolean result = ((InteractsWithApps) this.driver).removeApp(bundleId);
        Report.debug("Appium Driver using: removeApp -> app: " + bundleId + " result: " + result);
        return result;
    }

    @Override
    public boolean removeApp(String bundleId, @Nullable BaseRemoveApplicationOptions options) {
        boolean result = ((InteractsWithApps) this.driver).removeApp(bundleId, options);
        Report.debug("Appium Driver using: removeApp -> app: " + bundleId + " result: " + result);
        return result;
    }

    @Override
    public void activateApp(String bundleId) {
        Report.debug("Appium Driver using: activateApp -> app: " + bundleId);
        ((InteractsWithApps) this.driver).activateApp(bundleId);
    }

    @Override
    public void activateApp(String bundleId, @Nullable BaseActivateApplicationOptions options) {
        Report.debug("Appium Driver using: activateApp -> app: " + bundleId + " options: " + options);
        ((InteractsWithApps) this.driver).activateApp(bundleId, options);
    }

    @Override
    public ApplicationState queryAppState(String bundleId) {
        ApplicationState state = ((InteractsWithApps) this.driver).queryAppState(bundleId);
        Report.debug("Appium Driver using: queryAppState -> state: " + state);
        return state;
    }

    @Override
    public boolean terminateApp(String bundleId) {
        boolean terminate = ((InteractsWithApps) this.driver).terminateApp(bundleId);
        Report.debug("Appium Driver using: terminateApp -> app: " + bundleId + " result: " + terminate);
        return terminate;
    }

    @Override
    public boolean terminateApp(String bundleId, @Nullable BaseTerminateApplicationOptions options) {
        boolean terminate = ((InteractsWithApps) this.driver).terminateApp(bundleId, options);
        Report.debug("Appium Driver using: terminateApp -> app: " + bundleId + " result: " + terminate);
        return terminate;
    }

    @Override
    public void lockDevice() {
        Report.debug("Appium Driver using: lockDevice");
        ((LocksDevice) this.driver).lockDevice();
    }

    @Override
    public void lockDevice(Duration duration) {
        Report.debug("Appium Driver using: lockDevice. Duration: " + duration);
        ((LocksDevice) this.driver).lockDevice(duration);
    }

    @Override
    public void unlockDevice() {
        Report.debug("Appium Driver using: unlockDevice");
        ((LocksDevice) this.driver).unlockDevice();
    }

    @Override
    public boolean isDeviceLocked() {
        boolean isDeviceLocked = ((LocksDevice) this.driver).isDeviceLocked();
        Report.debug("Appium Driver using: isDeviceLocked -> result: " + isDeviceLocked);
        return isDeviceLocked;
    }

    @Override
    public TouchAction performTouchAction(TouchAction touchAction) {
        TouchAction action = ((PerformsTouchActions) this.driver).performTouchAction(touchAction);
        Report.debug("Appium Driver using: performTouchAction -> result: " + action);
        return action;
    }

    @Override
    public MultiTouchAction performMultiTouchAction(MultiTouchAction multiAction) {
        MultiTouchAction action = ((PerformsTouchActions) this.driver).performMultiTouchAction(multiAction);
        Report.debug("Appium Driver using: performMultiTouchAction -> result: " + action);
        return action;
    }

    @Override
    public byte[] pullFile(String remotePath) {
        byte[] result = ((PullsFiles) this.driver).pullFile(remotePath);
        Report.debug("Appium Driver using: pullFile -> path " + remotePath + " result: " + Arrays.toString(result));
        return result;
    }

    @Override
    public byte[] pullFolder(String remotePath) {
        byte[] result = ((PullsFiles) this.driver).pullFile(remotePath);
        Report.debug("Appium Driver using: pullFolder -> path " + remotePath);
        return result;
    }

    @Override
    public void pushFile(String remotePath, byte[] base64Data) {
        Report.debug("Appium Driver using: pushFile -> path " + remotePath);
        ((PushesFiles) this.driver).pushFile(remotePath, base64Data);
    }

    @Override
    public void pushFile(String remotePath, File file) throws IOException {
        Report.debug("Appium Driver using: pushFile -> path " + remotePath);
        ((PushesFiles) this.driver).pushFile(remotePath, file);
    }

    @Override
    public void launchApp() {
        Report.debug("Appium Driver using: launchApp ");
        Report.info("Relaunch app");
        ((SupportsLegacyAppManagement) this.driver).launchApp();
    }

    @Override
    public void resetApp() {
        Report.debug("Appium Driver using: resetApp ");
        ((SupportsLegacyAppManagement) this.driver).resetApp();
    }


    @Override
    public void closeApp() {
        Report.debug("Appium Driver using: closeApp ");
        ((SupportsLegacyAppManagement) this.driver).closeApp();
    }

    @Override
    public WebDriver context(String name) {
        WebDriver driver = ((SupportsContextSwitching) this.driver).context(name);
        Report.debug("Appium Driver using: context-> name: " + name + " result " + driver);
        return driver;
    }

    @Override
    public Set<String> getContextHandles() {
        Set<String> result = ((SupportsContextSwitching) this.driver).getContextHandles();
        Report.debug("Appium Driver using: getContextHandles-> result: " + result);
        return result;
    }

    @Nullable
    @Override
    public String getContext() {
        String result = ((SupportsContextSwitching) this.driver).getContext();
        Report.debug("Appium Driver using: getContext-> result: " + result);
        return result;
    }
}
