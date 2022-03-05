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
package co.verisoft.fw.utils;


/**
 * Attempt interface, to use in Retry object. Mostly used as anonymous class
 *
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a> @ <a href="http://www.verisoft.co">www.VeriSoft.co</a>
 * @since 1.9.6 (2019)
 */
public interface Attemptable {


    /**
     * Attempt - the actual action to be performed. Try to perform a small as possible action
     * to avoid recursive attempt. Compose the attempt method to throw an Error or Exception
     * (Throwable object) if the attempt fails.
     *
     * @throws Throwable an object to throw if the attempt method did not succeed.
     */
    void attempt() throws Throwable;


    /**
     * Activated when the attempt method fails
     */
    void onAttemptFail();
}
