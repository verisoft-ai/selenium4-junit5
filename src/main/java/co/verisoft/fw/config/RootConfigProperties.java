/*
 * (C) Copyright 2022 VeriSoft (http://www.verisoft.co)
 *
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
package co.verisoft.fw.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * A property loader class for the root.config.properties file
 *
 * @author Nir Gallner, VeriSoft
 * @since 0.1.3 (May, 2023)
 */
@Getter
@Configuration
public class RootConfigProperties {

    @Value("${selenium.logs.verbose}")
    private boolean seleniumLogsVerbose;

    @Value("${selenium.wait.timeout}")
    private int seleniumWaitTimeout;

    @Value("${polling.interval}")
    private int pollingInterval;

    @Value("${max_retry_number}")
    private int maxRetryNumber;
}
