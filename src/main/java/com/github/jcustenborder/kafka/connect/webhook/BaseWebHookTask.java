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
package com.github.jcustenborder.kafka.connect.webhook;

import com.github.jcustenborder.kafka.connect.utils.data.SourceRecordConcurrentLinkedDeque;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public abstract class BaseWebHookTask<CONFIG extends BaseWebHookConnectorConfig> extends SourceTask {
  private static final Logger log = LoggerFactory.getLogger(BaseWebHookTask.class);
  protected CONFIG config;
  Server server;
  protected SourceRecordConcurrentLinkedDeque records;

  protected abstract CONFIG config(Map<String, String> settings);

  protected abstract ServletModule servletModule();

  @Override
  public void start(Map<String, String> settings) {
    this.config = config(settings);
    this.records = new SourceRecordConcurrentLinkedDeque();

    ServletModule servletModule = servletModule();

    List<Module> guiceModules = new ArrayList<>();
    guiceModules.add(new WebHookServletModule(this.config, this.records));
    guiceModules.add(servletModule);


    log.info("Creating Guice Injector");
    Injector injector = Guice.createInjector(guiceModules);


    log.trace("start() - Creating threadpool min = {} max = {}", this.config.threadPoolMaxSize, this.config.threadPoolMinSize);
    QueuedThreadPool threadPool = new QueuedThreadPool(this.config.threadPoolMaxSize, this.config.threadPoolMinSize);
    this.server = new Server(threadPool);

    HttpConfiguration httpConfig = new HttpConfiguration();
    httpConfig.setSecureScheme("https");
    httpConfig.setSecurePort(this.config.httpsPort);
    httpConfig.setSendXPoweredBy(false);
    httpConfig.setSendServerVersion(false);

    if (this.config.httpEnable) {
      log.info("Configuring http server on port {}", this.config.httpPort);
      ServerConnector httpServerConnector = new ServerConnector(
          this.server, new HttpConnectionFactory(httpConfig)
      );
      httpServerConnector.setIdleTimeout(this.config.httpIdleTimeoutMs);
      httpServerConnector.setPort(this.config.httpPort);
      this.server.addConnector(httpServerConnector);
    }

    if (this.config.httpsEnable) {
      SslContextFactory sslContextFactory = new SslContextFactory();
      sslContextFactory.setKeyStorePath(this.config.httpsKeyStorePath);
      sslContextFactory.setKeyStorePassword(this.config.httpsKeyStorePassword);
      sslContextFactory.setKeyManagerPassword(this.config.httpsKeyManagerPassword);
      sslContextFactory.setTrustStorePath(this.config.httpsTrustStorePath);
      sslContextFactory.setTrustStorePassword(this.config.httpsTrustStorePassword);

      log.info("Configuring https server on port {}", this.config.httpsPort);
      ServerConnector httpsServerConnector = new ServerConnector(
          this.server,
          new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
          new HttpConnectionFactory(httpConfig)
      );
      httpsServerConnector.setIdleTimeout(this.config.httpsIdleTimeoutMs);
      httpsServerConnector.setPort(this.config.httpsPort);
      this.server.addConnector(httpsServerConnector);
    }

    ServletContextHandler rootHandler = new ServletContextHandler(this.server, "/", false, false);
    rootHandler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
    rootHandler.addServlet(DefaultServlet.class, "/");

    log.info("Starting Server");

    try {
      this.server.start();
    } catch (Exception e) {
      throw new ConnectException(e);
    }
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    List<SourceRecord> records = new ArrayList<>(1000);
    while (!this.records.drain(records)) {

    }
    log.trace("poll() - Returning {} record(s)", records.size());
    return records;
  }

  @Override
  public void stop() {
    try {
      this.server.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
