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
package co.verisoft.fw.selenium.drivers.factory;

import java.lang.annotation.*;

/**
 * This class is copied from Boni Garcia code, Selenium-Jupiter. <br>
 * Original code can be found
 * <a href="https://github.com/bonigarcia/selenium-jupiter/blob/master/src/main/java/io/github/bonigarcia/seljup/SingleSession.java">here</a>.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface SingleSession {
}
