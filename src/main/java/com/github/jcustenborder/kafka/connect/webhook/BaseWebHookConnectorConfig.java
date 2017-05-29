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
package com.github.jcustenborder.kafka.connect.webhook;

import com.github.jcustenborder.kafka.connect.utils.config.ValidPort;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class BaseWebHookConnectorConfig extends AbstractConfig {
  public final static String HTTPS_ENABLE_CONF = "https.enable";
  public final static String HTTPS_PORT_CONF = "https.port";
  public final static String HTTP_ENABLE_CONF = "http.enable";
  public final static String HTTP_PORT_CONF = "http.port";
  public final static String HTTP_IDLE_TIMEOUT_MS_CONF = "http.idle.timeout.ms";
  public final static String HTTPS_IDLE_TIMEOUT_MS_CONF = "https.idle.timeout.ms";
  public final static String THREAD_POOL_MAX_SIZE_CONF = "thread.pool.max.size";
  public final static String THREAD_POOL_MIN_SIZE_CONF = "thread.pool.min.size";
  public static final String HEALTH_CHECK_ENABLE_CONF = "health.check.enable";
  public static final String HEALTH_CHECK_PATH_CONF = "health.check.path";
  public static final String HTTPS_KEY_STORE_PATH_CONF = "https.key.store.path";
  public static final String HTTPS_KEY_STORE_PASSWORD_CONF = "https.key.store.password";
  public static final String HTTPS_KEY_MANAGER_PASSWORD_CONF = "https.key.manager.password";
  public static final String HTTPS_TRUST_STORE_PATH_CONF = "https.trust.store.path";
  public static final String HTTPS_TRUST_STORE_PASSWORD_CONF = "https.trust.store.password";
  static final String HTTPS_ENABLE_DOC = "Flag to determine if https should be enabled.";
  static final String HTTPS_PORT_DOC = "Port the https listener should be started on.";
  static final String HTTP_ENABLE_DOC = "Flag to determine if http should be enabled.";
  static final String HTTP_PORT_DOC = "Port the http listener should be started on.";
  static final String HTTP_IDLE_TIMEOUT_MS_DOC = "The number of milliseconds idle before a connection has timed out.";
  static final String HTTPS_IDLE_TIMEOUT_MS_DOC = "The number of milliseconds idle before a connection has timed out.";
  static final String THREAD_POOL_MAX_SIZE_DOC = "The maximum number of threads for the thread pool to allocate.";
  static final String THREAD_POOL_MIN_SIZE_DOC = "The minimum number of threads for the thread pool to allocate.";
  static final String HEALTH_CHECK_ENABLE_DOC = "Flag to determine if a health check url for a load balancer should be configured.";
  static final String HEALTH_CHECK_PATH_DOC = "Path that will respond with a health check.";
  static final String HTTPS_KEY_STORE_PATH_DOC = "Path on the local filesystem that contains the ssl keystore.";
  static final String HTTPS_KEY_STORE_PASSWORD_DOC = "The password for the ssl keystore.";
  static final String HTTPS_KEY_MANAGER_PASSWORD_DOC = "The key manager password.";
  static final String HTTPS_TRUST_STORE_PATH_DOC = "The key manager password.";
  static final String HTTPS_TRUST_STORE_PASSWORD_DOC = "The password for the ssl trust store.";

  public final boolean httpsEnable;
  public final int httpsPort;
  public final String httpsKeyStorePath;
  public final String httpsKeyStorePassword;
  public final String httpsKeyManagerPassword;
  public final String httpsTrustStorePath;
  public final String httpsTrustStorePassword;
  public final int httpsIdleTimeoutMs;

  public final boolean httpEnable;
  public final int httpPort;
  public final int httpIdleTimeoutMs;
  public final int threadPoolMaxSize;
  public final int threadPoolMinSize;
  public final boolean healthCheckEnable;
  public final String healthCheckPath;


  public BaseWebHookConnectorConfig(ConfigDef definition, Map<?, ?> originals) {
    super(definition, originals);
    this.httpsEnable = getBoolean(HTTPS_ENABLE_CONF);
    this.httpsPort = getInt(HTTPS_PORT_CONF);
    this.httpsKeyStorePath = getString(HTTPS_KEY_STORE_PATH_CONF);
    this.httpsKeyStorePassword = getPassword(HTTPS_KEY_STORE_PASSWORD_CONF).value();
    this.httpsKeyManagerPassword = getPassword(HTTPS_KEY_MANAGER_PASSWORD_CONF).value();
    this.httpsTrustStorePath = getString(HTTPS_TRUST_STORE_PATH_CONF);
    this.httpsTrustStorePassword = getPassword(HTTPS_TRUST_STORE_PASSWORD_CONF).value();
    this.httpsIdleTimeoutMs = getInt(HTTPS_IDLE_TIMEOUT_MS_CONF);

    this.httpEnable = getBoolean(HTTP_ENABLE_CONF);
    this.httpPort = getInt(HTTP_PORT_CONF);
    this.httpIdleTimeoutMs = getInt(HTTP_IDLE_TIMEOUT_MS_CONF);

    this.threadPoolMaxSize = getInt(THREAD_POOL_MAX_SIZE_CONF);
    this.threadPoolMinSize = getInt(THREAD_POOL_MIN_SIZE_CONF);
    this.healthCheckEnable = getBoolean(HEALTH_CHECK_ENABLE_CONF);
    this.healthCheckPath = getString(HEALTH_CHECK_PATH_CONF);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        //HTTPS
        .define(HTTPS_ENABLE_CONF, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.HIGH, HTTPS_ENABLE_DOC)
        .define(HTTPS_PORT_CONF, ConfigDef.Type.INT, 8443, ValidPort.of(1000, 65535), ConfigDef.Importance.HIGH, HTTPS_PORT_DOC)
        .define(HTTPS_KEY_STORE_PATH_CONF, ConfigDef.Type.STRING, "", ConfigDef.Importance.MEDIUM, HTTPS_KEY_STORE_PATH_DOC)
        .define(HTTPS_KEY_STORE_PASSWORD_CONF, ConfigDef.Type.PASSWORD, "", ConfigDef.Importance.MEDIUM, HTTPS_KEY_STORE_PASSWORD_DOC)
        .define(HTTPS_KEY_MANAGER_PASSWORD_CONF, ConfigDef.Type.PASSWORD, "", ConfigDef.Importance.MEDIUM, HTTPS_KEY_MANAGER_PASSWORD_DOC)
        .define(HTTPS_TRUST_STORE_PATH_CONF, ConfigDef.Type.STRING, "", ConfigDef.Importance.MEDIUM, HTTPS_TRUST_STORE_PATH_DOC)
        .define(HTTPS_TRUST_STORE_PASSWORD_CONF, ConfigDef.Type.PASSWORD, "", ConfigDef.Importance.MEDIUM, HTTPS_TRUST_STORE_PASSWORD_DOC)
        .define(HTTPS_IDLE_TIMEOUT_MS_CONF, ConfigDef.Type.INT, 30000, ConfigDef.Range.between(5000, 1000 * 300), ConfigDef.Importance.LOW, HTTPS_IDLE_TIMEOUT_MS_DOC)

        .define(HTTP_ENABLE_CONF, ConfigDef.Type.BOOLEAN, true, ConfigDef.Importance.HIGH, HTTP_ENABLE_DOC)
        .define(HTTP_PORT_CONF, ConfigDef.Type.INT, 8080, ValidPort.of(1000, 65535), ConfigDef.Importance.HIGH, HTTP_PORT_DOC)
        .define(HTTP_IDLE_TIMEOUT_MS_CONF, ConfigDef.Type.INT, 30000, ConfigDef.Range.between(5000, 1000 * 300), ConfigDef.Importance.LOW, HTTP_IDLE_TIMEOUT_MS_DOC)

        .define(THREAD_POOL_MAX_SIZE_CONF, ConfigDef.Type.INT, 100, ConfigDef.Range.between(10, 1000), ConfigDef.Importance.MEDIUM, THREAD_POOL_MAX_SIZE_DOC)
        .define(THREAD_POOL_MIN_SIZE_CONF, ConfigDef.Type.INT, 10, ConfigDef.Range.between(10, 1000), ConfigDef.Importance.MEDIUM, THREAD_POOL_MIN_SIZE_DOC)
        .define(HEALTH_CHECK_ENABLE_CONF, ConfigDef.Type.BOOLEAN, true, ConfigDef.Importance.MEDIUM, HEALTH_CHECK_ENABLE_DOC)
        .define(HEALTH_CHECK_PATH_CONF, ConfigDef.Type.STRING, "/healthcheck", ConfigDef.Importance.MEDIUM, HEALTH_CHECK_PATH_DOC);
  }
}
