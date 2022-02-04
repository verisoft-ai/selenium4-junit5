package co.verisoft.fw.utils.locators;

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

import org.openqa.selenium.By;

/**
 * <p>
 * A set of static methods to extend the search mechanism.<br>
 * It is used to simplify commonly used {@link org.openqa.selenium.FindBy FindBy}. 
 * For example: <br><br>
 * {@code WebElement myElement = driver.findElement(ElementBy.partialText("searchText"));}<br><br>
 * </p>
 * 
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a>
 * @since 0.1
 * 
 * @see org.openqa.selenium.support.pagefactory.ByAll
 * @see AllBy
 * @see AnyBy
 * @see InputBy
 * @see NotBy
 * @see TdBy
 */
public final class ElementBy {
	
	private ElementBy() {
		
	}
	
	/**
	 * Finds an element by using any part of it's <b>tag value<b>. 
	 * For example - {@code <tagName>value</tagName>} can be discovered by "value" or "val" using the following code:<br>
	 * {@code List<WebElement> myList = driver.findElements(ElementBy.partialText("val"));}<br><br>
	 * @param text the string to be searched
	 * @return a By object with the string to be searched, ready to be sent to findElement or
	 * findElements
	 */
	public static By partialText(String text) {
		return By.xpath("//*[contains(normalize-space(.),'" + text + "')]");
	}
	

}
