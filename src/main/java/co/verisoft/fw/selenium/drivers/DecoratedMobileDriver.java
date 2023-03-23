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
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.html5.RemoteLocationContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

public class DecoratedMobileDriver extends DecoratedDriver
        implements SupportsContextSwitching, SupportsRotation, SupportsLocation,
        HidesKeyboard, HasDeviceTime, PullsFiles,
        InteractsWithApps, SupportsLegacyAppManagement,
        HasAppStrings, PerformsTouchActions, HasOnScreenKeyboard, LocksDevice,
        PushesFiles, CanRecordScreen, HasBattery, HasSettings {
    public DecoratedMobileDriver(@Nullable Capabilities capabilities) {
        super(capabilities);
    }

    public DecoratedMobileDriver(URL url, @Nullable Capabilities capabilities) {
        super(capabilities);
    }

    @Override
    public BatteryInfo getBatteryInfo() {
        Report.debug("Appium Driver using: BatteryInfo");

        if (((WrapsDriver) decoratedDriver).getWrappedDriver() instanceof AndroidDriver)
            return ((AndroidDriver) decoratedDriver).getBatteryInfo();
        else if (((WrapsDriver) decoratedDriver).getWrappedDriver() instanceof IOSDriver)
            return ((IOSDriver) decoratedDriver).getBatteryInfo();
        else
            return null;
    }

    @Override
    public Response execute(String s, Map<String, ?> map) {
        return ((AppiumDriver) this.decoratedDriver).execute(s, map);
    }

    @Override
    public Response execute(String s) {
        return ((AppiumDriver) this.decoratedDriver).execute(s);
    }

    @Override
    public DeviceRotation rotation() {
        return ((SupportsRotation) this.decoratedDriver).rotation();
    }

    @Override
    public void rotate(DeviceRotation rotation) {
        ((SupportsRotation) this.decoratedDriver).rotate(rotation);
    }

    @Override
    public void rotate(ScreenOrientation orientation) {
        ((SupportsRotation) decoratedDriver).rotate(orientation);
    }

    @Override
    public ScreenOrientation getOrientation() {
        return ((SupportsRotation) decoratedDriver).getOrientation();
    }

    @Override
    public <T extends BaseStartScreenRecordingOptions> String startRecordingScreen(T options) {
        return ((CanRecordScreen) this.decoratedDriver).startRecordingScreen(options);
    }

    @Override
    public String startRecordingScreen() {
        return ((CanRecordScreen) this.decoratedDriver).startRecordingScreen();
    }

    @Override
    public <T extends BaseStopScreenRecordingOptions> String stopRecordingScreen(T options) {
        return ((CanRecordScreen) decoratedDriver).stopRecordingScreen(options);
    }

    @Override
    public String stopRecordingScreen() {
        return ((CanRecordScreen) decoratedDriver).stopRecordingScreen();
    }

    @Override
    public RemoteLocationContext getLocationContext() {
        return ((SupportsLocation) decoratedDriver).getLocationContext();
    }

    @Override
    public Location location() {
        return ((SupportsLocation) decoratedDriver).location();
    }

    @Override
    public void setLocation(Location location) {
        ((SupportsLocation) decoratedDriver).setLocation(location);
    }

    @Override
    public Map<String, String> getAppStringMap() {
        return ((HasAppStrings) decoratedDriver).getAppStringMap();
    }

    @Override
    public Map<String, String> getAppStringMap(String language) {
        return ((HasAppStrings) decoratedDriver).getAppStringMap(language);
    }

    @Override
    public Map<String, String> getAppStringMap(String language, String stringFile) {
        return ((HasAppStrings) decoratedDriver).getAppStringMap(language, stringFile);
    }

    @Override
    public String getDeviceTime(String format) {
        return ((HasDeviceTime) decoratedDriver).getDeviceTime(format);
    }

    @Override
    public String getDeviceTime() {
        return ((HasDeviceTime) decoratedDriver).getDeviceTime();
    }

    @Override
    public boolean isKeyboardShown() {
        return ((HasOnScreenKeyboard) decoratedDriver).isKeyboardShown();
    }

    @Override
    public void hideKeyboard() {
        ((HidesKeyboard) decoratedDriver).hideKeyboard();
    }

    @Override
    public void installApp(String appPath) {
        ((InteractsWithApps) decoratedDriver).installApp(appPath);
    }

    @Override
    public void installApp(String appPath, @Nullable BaseInstallApplicationOptions options) {
        ((InteractsWithApps) decoratedDriver).installApp(appPath, options);
    }

    @Override
    public boolean isAppInstalled(String bundleId) {
        return ((InteractsWithApps) decoratedDriver).isAppInstalled(bundleId);
    }

    @Override
    public void runAppInBackground(Duration duration) {
        ((InteractsWithApps) decoratedDriver).runAppInBackground(duration);
    }

    @Override
    public boolean removeApp(String bundleId) {
        return ((InteractsWithApps) decoratedDriver).removeApp(bundleId);
    }

    @Override
    public boolean removeApp(String bundleId, @Nullable BaseRemoveApplicationOptions options) {
        return ((InteractsWithApps) decoratedDriver).removeApp(bundleId, options);
    }

    @Override
    public void activateApp(String bundleId) {
        ((InteractsWithApps) decoratedDriver).activateApp(bundleId);
    }

    @Override
    public void activateApp(String bundleId, @Nullable BaseActivateApplicationOptions options) {
        ((InteractsWithApps) decoratedDriver).activateApp(bundleId, options);
    }

    @Override
    public ApplicationState queryAppState(String bundleId) {
        return ((InteractsWithApps) decoratedDriver).queryAppState(bundleId);
    }

    @Override
    public boolean terminateApp(String bundleId) {
        return ((InteractsWithApps) decoratedDriver).terminateApp(bundleId);
    }

    @Override
    public boolean terminateApp(String bundleId, @Nullable BaseTerminateApplicationOptions options) {
        return ((InteractsWithApps) decoratedDriver).terminateApp(bundleId, options);
    }

    @Override
    public void lockDevice() {
        ((LocksDevice) decoratedDriver).lockDevice();
    }

    @Override
    public void lockDevice(Duration duration) {
        ((LocksDevice) decoratedDriver).lockDevice(duration);
    }

    @Override
    public void unlockDevice() {
        ((LocksDevice) decoratedDriver).unlockDevice();
    }

    @Override
    public boolean isDeviceLocked() {
        return ((LocksDevice) decoratedDriver).isDeviceLocked();
    }

    @Override
    public TouchAction performTouchAction(TouchAction touchAction) {
        return ((PerformsTouchActions) decoratedDriver).performTouchAction(touchAction);
    }

    @Override
    public MultiTouchAction performMultiTouchAction(MultiTouchAction multiAction) {
        return ((PerformsTouchActions) decoratedDriver).performMultiTouchAction(multiAction);
    }

    @Override
    public byte[] pullFile(String remotePath) {
        return ((PullsFiles) decoratedDriver).pullFile(remotePath);
    }

    @Override
    public byte[] pullFolder(String remotePath) {
        return ((PullsFiles) decoratedDriver).pullFile(remotePath);
    }

    @Override
    public void pushFile(String remotePath, byte[] base64Data) {
        ((PushesFiles) decoratedDriver).pushFile(remotePath, base64Data);
    }

    @Override
    public void pushFile(String remotePath, File file) throws IOException {
        ((PushesFiles) decoratedDriver).pushFile(remotePath, file);
    }

    @Override
    public void launchApp() {
        ((SupportsLegacyAppManagement) decoratedDriver).launchApp();
    }

    @Override
    public void resetApp() {
        ((SupportsLegacyAppManagement) decoratedDriver).resetApp();
    }


    @Override
    public void closeApp() {
        ((SupportsLegacyAppManagement) decoratedDriver).closeApp();
    }

    @Override
    public WebDriver context(String name) {
        return ((SupportsContextSwitching) decoratedDriver).context(name);
    }

    @Override
    public Set<String> getContextHandles() {
        return ((SupportsContextSwitching) decoratedDriver).getContextHandles();
    }

    @Nullable
    @Override
    public String getContext() {
        return ((SupportsContextSwitching) decoratedDriver).getContext();
    }
}
