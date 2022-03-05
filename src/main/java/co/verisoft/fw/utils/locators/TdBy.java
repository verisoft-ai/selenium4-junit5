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
 * A set of methods to be used when looking for fields value in a table. This
 * mechanism used to locate elements within a document using a single lookup. It
 * converts row number and column number into search objects and returns
 * {@link By By} object with results.
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
 * @see NotBy
 */
public final class TdBy {

	private TdBy() {
	}

	/**
	 * Looks for a cell value with rowNumber and columnNumber. <br>
	 * For example: <br>
	 * with the following table:<br>
	 *
	 *		<table id="table">
	 *			<thead>
	 *				<tr>
	 *					<th>Month</th>
	 *					<th>Savings</th>
	 *				</tr>
	 *			</thead>
	 *			<tfoot>
	 *				<tr>
	 *					<td>Sum</td>
	 *					<td>$180</td>
	 *				</tr>
	 *			</tfoot>
	 *			<tbody>
	 *				<tr>
	 *					<td>January</td>
	 *					<td>$100</td>
	 *				</tr>
	 *				<tr>
	 *					<td>February</td>
	 *					<td>$80</td>
	 *				</tr>
	 *			</tbody>
	 *		</table>
	 * <br><br>The code {@code WebElement element = driver.findElement(TdBy.cellLocation(1,0));} <br>
	 * will return the value "January"
	 * @param rowNumber
	 *            row number to look for. First row is 0
	 * @param columnNumber
	 *            col number to look for. First col is 0
	 * @return <code>{@link By }</code> object with the locators
	 *         ready in it
	 */
	public static By cellLocation(int rowNumber, int columnNumber) {
		return By.xpath(String.format("//tbody//tr[%d]/td[%d]", rowNumber, columnNumber+1));
	}

	
	/**
	 * Looks for a header valuewith a specific columnNumber. <br>
	 * For example: <br>
	 * with the following table:<br>
	 * 
	 *		<table summary="">
	 *			<thead>
	 *				<tr>
	 *					<th>Month</th>
	 *					<th>Savings</th>
	 *				</tr>
	 *			</thead>
	 *			<tfoot>
	 *				<tr>
	 *					<td>Sum</td>
	 *					<td>$180</td>
	 *				</tr>
	 *			</tfoot>
	 *			<tbody>
	 *				<tr>
	 *					<td>January</td>
	 *					<td>$100</td>
	 *				</tr>
	 *				<tr>
	 *					<td>February</td>
	 *					<td>$80</td>
	 *				</tr>
	 *			</tbody>
	 *		</table>
	 * The code {@code WebElement element = driver.findElement(TdBy.tableHeader(0));} <br>
	 * will return the value "Month"
	 *
	 * @param columnNumber
	 *            col number to look for. First col is 0.
	 * @return <code>{@link By }</code> object with the locators
	 *         ready in it
	 */
	public static By tableHeader(int columnNumber) {
		return By.xpath(String.format("//tr/th[%d]", columnNumber+1));
	}

}
