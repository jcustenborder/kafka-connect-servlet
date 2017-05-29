package com.github.jcustenborder.kafka.connect.webhook;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.List;
import java.util.Map;

public class MockWebHookConnector extends SourceConnector {
  @Override
  public String version() {
    return null;
  }

  @Override
  public void start(Map<String, String> map) {

  }

  @Override
  public Class<? extends Task> taskClass() {
    return MockWebHookTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int i) {
    return null;
  }

  @Override
  public void stop() {

  }

  @Override
  public ConfigDef config() {
    return MockWebHookConnectorConfig.config();
  }
}
