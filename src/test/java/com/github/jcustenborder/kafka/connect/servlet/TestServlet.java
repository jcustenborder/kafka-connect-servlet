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
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Singleton
class TestServlet extends HttpServlet {

  @Inject
  SourceRecordConcurrentLinkedDeque records;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try (PrintWriter writer = resp.getWriter()) {
      writer.print("OK");
    }
    resp.setStatus(200);

    Map<String, ?> sourceOffset = ImmutableMap.of();
    Map<String, ?> sourcePartition = ImmutableMap.of();

    records.add(
        new SourceRecord(
            sourcePartition,
            sourceOffset,
            "test",
            null,
            Schema.STRING_SCHEMA,
            "test",
            Schema.STRING_SCHEMA,
            "test"
        )
    );
  }
}
