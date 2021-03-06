/*******************************************************************************
 * Copyright 2016 Jalian Systems Pvt. Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.sourceforge.marathon.runtime.api;

public interface ILogger {
    public static final int MESSAGE = 4;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;

    public void msg(String module, String message);

    public void msg(String module, String message, String description);

    public void info(String module, String message);

    public void info(String module, String message, String description);

    public void warning(String module, String message);

    public void warning(String module, String message, String description);

    public void error(String module, String message);

    public void error(String module, String message, String description);

    public void setLogLevel(int level);

    public int getLogLevel();

}
