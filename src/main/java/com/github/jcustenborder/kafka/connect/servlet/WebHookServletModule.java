/**
 * Copyright © 2017 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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
package com.github.jcustenborder.kafka.connect.servlet;

import com.github.jcustenborder.kafka.connect.utils.data.SourceRecordConcurrentLinkedDeque;
import com.google.inject.servlet.ServletModule;

class WebHookServletModule extends ServletModule {
  final BaseWebHookConnectorConfig config;
  final SourceRecordConcurrentLinkedDeque records;

  public WebHookServletModule(BaseWebHookConnectorConfig config, SourceRecordConcurrentLinkedDeque records) {
    this.config = config;
    this.records = records;
  }

  @Override
  protected void configureServlets() {
    if (this.config.healthCheckEnable) {
      serve(this.config.healthCheckPath).with(HealthCheckServlet.class);
    }
    bind(SourceRecordConcurrentLinkedDeque.class).toInstance(this.records);
  }
}
