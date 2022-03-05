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
 * Find elements which are of type <b>input</b>.<br>
 * This mechanism used to locate elements within a form.
 * </p>
 * 
 * 
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a>
 * @since 0.1
 * 
 * @see org.openqa.selenium.support.pagefactory.ByAll
 * @see AllBy
 * @see ElementBy
 * @see AnyBy
 * @see NotBy
 * @see TdBy
 */
public final class InputBy {
	
	private InputBy() {
		
	}
	
	/**
	 * <p>
	 * Will return a list of {@link By By} objects, which are
	 * <ul>
	 * <li><b>a.</b> Of type input</li>
	 * <li><b>b.</b> Contains the label sent as parameter</li>
	 * <li><b>c.</b> Has a general structure of {@code <label><input /> </label> }</li>
	 * </ul>
	 * For example:<br>
	 * The following html code:
	 * <pre>
	 * {@code
	 * <html>
	 * 	<body>
	 * 		<label>This is a label 
	 * 			<input id="text">
	 * 		</label>
	 *	</body>
	 * </html>
	 * } </pre>
	 * can be located using any of the words 'This is a label': <br>
	 * {@code driver.findElements(InputBy.label("label"));} <br>
	 * If the structure of the form does not comply with {@code <label> <input /> </label> } then the 
	 * search will not find the input
	 *
	 * @param labelText the name of the label to look for
	 * @return By object to be searched for in findElements
	 */
	public static By label(String labelText) {
		return By.xpath("//label[contains(.,'" + labelText + "')]/input");
	}

}
