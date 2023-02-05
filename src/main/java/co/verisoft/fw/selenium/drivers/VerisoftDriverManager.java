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


import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;


/**
 * A global, thread safe, singleton class. It handles augmentation of WebDriver objects of different types
 * @author David Yehezkel
 * @since 1.9.6
 */
@ToString
@Log4j2
public final class VerisoftDriverManager{

	private static Map<Integer, WebDriver> driverMap = new HashMap<Integer, WebDriver>();
	
	private VerisoftDriverManager() {}
	
	/**
	 * Register driver with current thread id to hashMap
	 * @param driver WebDriver object to be added to map
	 */
	public static  void addDriverToMap(WebDriver driver) {
		Integer threadID = (int) Thread.currentThread().getId();
		log.debug("Register driver " + driver.toString() + " by Thread ID " + threadID);
		driverMap.put(threadID, driver);
	}

	/**
	 * Returns driver with the current thread id
	 * @return T - template of WebDriver. i.e, if you know that the stored WebDriver object is VeriSoftDriver,
	 * just do VerisoftDriver driver = VerisoftDriverMananer.getDriver();. No casting needed
	 */
	public static @Nullable <T extends WebDriver> T getDriver() {
		Integer threadID = (int) Thread.currentThread().getId();
		log.debug("Get driver by Thread ID (Key)" + threadID);
		return (T)driverMap.get(threadID);
	}
}
