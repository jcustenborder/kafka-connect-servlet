# Introduction

This connector is designed is designed to make the implementation of [webhook](https://en.wikipedia.org/wiki/Webhook) style connectors much easier to implement.
This works by embedding [Jetty](http://www.eclipse.org/jetty/) and registering a [Google Guice](https://github.com/google/guice/wiki/Servlets) 
module with your web service implementations. All of the other things you would need to do this securely are handled by 
the base task including SSL configuration.

# Getting Started. 

Building your own connector to receive [webhook](https://en.wikipedia.org/wiki/Webhook) events is pretty easy. The following sections are 
all that are needed to implement your [webhook](https://en.wikipedia.org/wiki/Webhook).

## Inheriting from BaseWebHookTask

```java
public class MockWebHookTask extends BaseWebHookTask<MockWebHookConnectorConfig> {
  @Override
  protected MockWebHookConnectorConfig config(Map<String, String> settings) {
    return new MockWebHookConnectorConfig(settings);
  }

  @Override
  protected ServletModule servletModule() {
    return new ServletModule() {
      @Override
      protected void configureServlets() {
        serve("/test").with(TestServlet.class);
      }
    };
  }
}
```

## Implementing your servlet

In the example below `SourceRecordConcurrentLinkedDeque records` is injected by Guice. `BaseWebHookTask` creates an 
instance of a `SourceRecordConcurrentLinkedDeque` and binds it globally. This allows your service to create SourceRecord(s)
that returned when the framework calls `poll()` on `BaseWebHookTask`.

```java
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
```

## Configuration 

`BaseWebHookConnectorConfig` is the class your configurations should inherit from. This provides
all of the base functionality for SSL and things like health checks.

| Name                       | Description                                                                       | Type     | Default      | Valid Values                     | Importance |
|----------------------------|-----------------------------------------------------------------------------------|----------|--------------|----------------------------------|------------|
| http.enable                | Flag to determine if http should be enabled.                                      | boolean  | true         |                                  | high       |
| http.port                  | Port the http listener should be started on.                                      | int      | 8080         | ValidPort{start=1000, end=65535} | high       |
| https.enable               | Flag to determine if https should be enabled.                                     | boolean  | false        |                                  | high       |
| https.port                 | Port the https listener should be started on.                                     | int      | 8443         | ValidPort{start=1000, end=65535} | high       |
| health.check.enable        | Flag to determine if a health check url for a load balancer should be configured. | boolean  | true         |                                  | medium     |
| health.check.path          | Path that will respond with a health check.                                       | string   | /healthcheck |                                  | medium     |
| https.key.manager.password | The key manager password.                                                         | password | [hidden]     |                                  | medium     |
| https.key.store.password   | The password for the ssl keystore.                                                | password | [hidden]     |                                  | medium     |
| https.key.store.path       | Path on the local filesystem that contains the ssl keystore.                      | string   | ""           |                                  | medium     |
| https.trust.store.password | The password for the ssl trust store.                                             | password | [hidden]     |                                  | medium     |
| https.trust.store.path     | The key manager password.                                                         | string   | ""           |                                  | medium     |
| thread.pool.max.size       | The maximum number of threads for the thread pool to allocate.                    | int      | 100          | [10,...,1000]                    | medium     |
| thread.pool.min.size       | The minimum number of threads for the thread pool to allocate.                    | int      | 10           | [10,...,1000]                    | medium     |
| http.idle.timeout.ms       | The number of milliseconds idle before a connection has timed out.                | int      | 30000        | [5000,...,300000]                | low        |
| https.idle.timeout.ms      | The number of milliseconds idle before a connection has timed out.                | int      | 30000        | [5000,...,300000]                | low        |
