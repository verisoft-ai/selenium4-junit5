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
 * Find elements which do <u><b>NOT</b></u> has a specific {@link By By} locator.<br>
 * This mechanism used to locate elements within a document using a single
 * lookup. This class will find <b>ALL</b> DOM elements which does not contain the search
 * specified. For example: <br><br>
 * {@code List<WebElement> myList = driver.findElements(NotBy.not(By.xpath("path1"));}<br><br>
 *  will find all elements that <u><b>does not match</b></u> xpath "path1" and return a 
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
 * @see AllBy
 * @see TdBy
 */
public class NotBy extends By{
	
	private final By by;
	
	private NotBy(By by) {
		this.by = by;
	}
	
	/**
	 * 
	 * @param by {@link By} object to be used when {@link #findElements(SearchContext context) findElements} is called.}
	 * @return <code>{@link By }</code> object with the locator to avoid
	 */
	public static By not(By by) {
		return new NotBy(by);
	}
	
	@Override
	public List<WebElement> findElements(SearchContext context){
		
		List<WebElement> elements = context.findElements(By.cssSelector("*"));
		elements.removeAll(context.findElements(by));
		return elements;
	}

}
