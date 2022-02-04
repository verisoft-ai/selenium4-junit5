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
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * <p>
 * Find elements which comply to <u><b>ALL</b></u> the specified By objects.<br>
 * This mechanism used to locate elements within a document using a series of
 * lookups. This class will find all DOM elements that matches any of the
 * locators in sequence. For example: <br><br>
 * {@code List<WebElement> myList = driver.findElements(AllBy.all(By.xpath("path1"),By.xpath("path2"));}<br><br>
 * will find all elements that match xpath "path1" <u><b>AND</b></u> xpath "path2" and return a 
 * {@link List} of {@link WebElement}.
 * </p>
 * 
 * 
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a>
 * @since 0.1
 * 
 * @see org.openqa.selenium.support.pagefactory.ByAll
 * @see AnyBy
 * @see ElementBy
 * @see InputBy
 * @see NotBy
 * @see TdBy
 */
public class AllBy extends By {

	private final By[] bys;

	private AllBy(By... bys) {
		this.bys = bys;
	}

	/**
	 * 
	 * @param bys list of {@link By} objects to be used when {@link #findElements(SearchContext context) findElements} is called.}
	 * @return <code>{@link By }</code> object with all the locators in it
	 */
	public static AllBy all(By... bys) {
		return new AllBy(bys);
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {

		List<WebElement> elements = null;

		for (By by : bys) {

			List<WebElement> newElements = context.findElements(by);

			// If it is a new set
			if (null == elements) {
				elements = newElements;
			} else {
				elements.retainAll(newElements);
			}
		}

		return elements;

	}

}
