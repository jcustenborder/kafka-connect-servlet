/**
 * Copyright © 2017 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Introduction("This connector is designed to make the implementation of webhook style connectors " +
        "much easier to implement. This works by embedding Jetty and registering a Google Guice module " +
        "with your web service implementations. All of the other things you would need to do this securely " +
        "are handled by the base task including SSL configuration.")
@Title("Servlet")
@PluginOwner("jcustenborder")
@PluginName("kafka-connect-servlet")
package com.github.jcustenborder.kafka.connect.servlet;

import com.github.jcustenborder.kafka.connect.utils.config.Introduction;
import com.github.jcustenborder.kafka.connect.utils.config.PluginName;
import com.github.jcustenborder.kafka.connect.utils.config.PluginOwner;
import com.github.jcustenborder.kafka.connect.utils.config.Title;