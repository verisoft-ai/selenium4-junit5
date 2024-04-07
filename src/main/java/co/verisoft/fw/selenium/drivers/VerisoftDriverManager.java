package co.verisoft.fw.selenium.drivers;

/*
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


import co.verisoft.fw.store.StoreManager;
import co.verisoft.fw.store.StoreType;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A global, thread safe, singleton class. It handles augmentation of WebDriver objects of different types
 *
 * @author David Yehezkel
 * @since 1.9.6
 */
@ToString
@Slf4j

public final class VerisoftDriverManager {

    private static Map<Integer, Map<String,WebDriver>> driverMap = new HashMap<>();

    private VerisoftDriverManager() {
    }

    /**
     * Register driver with the current thread id to the map
     *
     * @param driver   WebDriver object to be added to the map
     */
    public static void addDriverToMap(WebDriver driver) {
        String driverName= StoreManager.getStore(StoreType.LOCAL_THREAD).getValueFromStore("current driver name");

        Integer threadID = (int) Thread.currentThread().getId();
        log.debug("Register driver " + driver.toString() + " with name " + driverName + " by Thread ID " + threadID);
        driverMap.computeIfAbsent(threadID, k -> new HashMap<>()).put(driverName, driver);
    }

    /**
     * Returns the list of drivers associated with the current thread id
     *
     * @return Map of driver names to WebDriver objects associated with the current thread id
     */
    public static @Nullable Map<String, WebDriver> getDrivers() {
        Integer threadID = (int) Thread.currentThread().getId();
        log.debug("Get drivers by Thread ID (Key)" + threadID);
        return driverMap.get(threadID);
    }

    /**
     * Returns driver with the current thread id
     *
     * @param driverName Name of the driver to retrieve
     * @return T - template of WebDriver. i.e, if you know that the stored WebDriver object is VeriSoftDriver,
     * just do VerisoftDriver driver = VerisoftDriverManager.getDriver();. No casting needed
     */
    public static @Nullable <T extends WebDriver> T getDriver(String driverName) {
        Integer threadID = (int) Thread.currentThread().getId();
        log.debug("Get driver with name " + driverName + " by Thread ID (Key)" + threadID);
        Map<String, WebDriver> drivers = driverMap.get(threadID);
        if (drivers != null) {
            return (T) drivers.get(driverName);
        }
        return null;
    }
    /**
     * Returns driver with the current thread id
     *
     * @return T - template of WebDriver. i.e, if you know that the stored WebDriver object is VeriSoftDriver,
     * just do VerisoftDriver driver = VerisoftDriverMananer.getDriver();. No casting needed
     */
    public static @Nullable <T extends WebDriver> T getDriver() {
        Integer threadID = (int) Thread.currentThread().getId();
        log.debug("Getting driver by Thread ID (Key): {}", threadID);

        Map<String, WebDriver> drivers = driverMap.get(threadID);
        if (drivers == null || drivers.isEmpty()) {
            return null;
        }

        if (drivers.size() > 1) {
            throw new IllegalStateException("More than one driver without a name found.");
        }

        return (T) drivers.values().iterator().next();
    }

}
