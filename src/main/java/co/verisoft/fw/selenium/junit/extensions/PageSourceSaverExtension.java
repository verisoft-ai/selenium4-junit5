package co.verisoft.fw.selenium.junit.extensions;

import co.verisoft.fw.selenium.drivers.VerisoftDriverManager;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
public class PageSourceSaverExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isPresent()) {
            WebDriver driver = VerisoftDriverManager.getDriver();

            String pageSource = driver.getPageSource();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
            LocalDateTime now = LocalDateTime.now();

            //File dir = new File("target/pageSources/" + dtf.format(now));
            File dir = new File("target/pageSources/");

            if (!dir.exists())
                Files.createDirectories(dir.toPath());

            Method method = context.getTestMethod().get();
            dtf = DateTimeFormatter.ofPattern("HHmmss");
            File file = new File(dir, String.format("%s_%s_%s.html",
                    method.getDeclaringClass().getName(),
                    method.getName(),
                    dtf.format(now)));
            Files.write(Paths.get(file.getPath()), pageSource.getBytes());
        }
    }
}
