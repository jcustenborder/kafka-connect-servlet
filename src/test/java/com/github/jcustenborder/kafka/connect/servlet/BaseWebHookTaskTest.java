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

import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;


public class BaseWebHookTaskTest {

  MockWebHookTask task;

  @BeforeEach
  public void before() {
    this.task = new MockWebHookTask();
    this.task.start(ImmutableMap.of());
  }

  @AfterEach
  public void after() {
    this.task.stop();
  }

  @Test
  public void poll() throws InterruptedException, IOException {

    URL website = new URL("http://127.0.0.1:8080/test");
    ByteStreams.copy(website.openStream(), ByteStreams.nullOutputStream());

    assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
      List<SourceRecord> records = this.task.poll();
      assertFalse(records.isEmpty(), "records should not be empty");
    });
  }


}
