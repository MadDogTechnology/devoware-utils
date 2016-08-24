# homonculus-messaging

The **homonculus-messaging** library encapsulates functionality needed to:

* read data from a RabbitMQ input channel using a higher level InputChannel interface.

* write data to a RabbitMQ output channel using a higher level OutputChannel interface.  

The **homonculus-messaging** library is intended to be used in conjunction with a
**[homonculus](https://github.com/cpdevoto/devoware-utils/tree/master/homonculus-core)** application, and it includes Dagger 2 modules
which allows for the creation of singleton InputChannel and OutputChannel objects that can be easily injected into other objects.

#Configuration class
To introduce a RabbitMQ input channel or output channel into your existing **homonculus** application, modify your main configuration class to
include a ```rabbitMq``` field as shown below:

```java
package com.doradosystems.simpleapp.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.devoware.homonculus.messaging.config.RabbitMqConfiguration;
import org.devoware.homonculus.validators.validation.PortRange;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleApplicationConfiguration {

  @NotNull
  @JsonProperty
  private String hostName;

  @PortRange
  @JsonProperty
  private int port;

  @Valid
  @NotNull
  @JsonProperty
  private RabbitMqConfiguration rabbitMq;
  
  public String getHostName() {
    return hostName;
  }

  public int getPort() {
    return port;
  }

  public RabbitMqConfiguration getRabbitMq() {
    return rabbitMq;
  }
}
```
## YAML configuration file

Once you have updated your configuration class, you can modify your YAML configuration file as shown below to configure the data source:

```yaml
# the name of the host to connect to
hostName: localhost

# the port to connect to
port: 8080

rabbitMq:
  # the host that is running the RabbitMq server
  host: localhost

  # the port that the RabbitMq server is listening on
  port: 5672

  # the username
  user: guest

  # the password
  password: guest

  # the virtual host
  virtualHost: /

  # the exchange name
  exchange:  

  # the queue name
  queue: task_queue
```

## InputChannel
The input channel subscribes itself to RabbitMQ using the exchange name and queue name specified in the configuration file. Upon
receiving a message from RabbitMq, the input channel will transform it into a ```com.doradosystems.messaging.input.Message``` and
post it to a Guava event bus. Application classes wishing to receive these messages should include a method annotated with the 
```@Subscribe``` annotation which matches the following signature:

```java
  @Subscribe
  public void myMethod(Message message) {
    // Do something with the message
  }
```
Such classes will need to be registered with the input channel's event bus, which can be injected into the class's constructor via
Dagger 2 as shown below:
```java
package com.doradosystems.simpleapp.messaging;

import static org.devoware.homonculus.messaging.MessageExchangeQualifiers.INPUT_EVENT_BUS;

import javax.inject.Inject;
import javax.inject.Named;

import org.devoware.homonculus.messaging.input.Message;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class MessageProcessor {

  @Inject
  public MessageProcessor(@Named(INPUT_EVENT_BUS) EventBus eventBus) {
    eventBus.register(this);
  }
  
  @Subscribe
  public void process(Message message) {
    System.out.println(message);
  }

}
```

## OutputChannel

The output channel can be injected into any application class that must publish messages to RabbitMQ.  It exposes a simple ```write(String message)```
and a ```write(String message, Map<String,Object> headers)``` method.  Note that only strings and primitive wrapper classes should be
used as header values. Here is what a class that writes to the RabbitMq output channel might look like:

```java
package com.doradosystems.messaging.output;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.collect.ImmutableMap;

public class OutputChannelHealthCheck extends HealthCheck implements OutputChannelHealthCheckMBean {
  private OutputChannel channel;

  @Inject
  OutputChannelHealthCheck(@Nonnull OutputChannel channel) {
    this.channel = requireNonNull(channel);
  }

  @Override
  protected Result check() throws Exception {
    try {
      channel.write("healthcheck", ImmutableMap.of("messageType", "healthcheck");
      return HealthCheck.Result.healthy();
    } catch (IOException io) {
      return HealthCheck.Result.unhealthy(io);
    }
  }

}
```
## Build dependencies
A **homonculus** application which uses the **homonculus-messaging** library must include the following additional dependencies:

```groovy
dependencies {
    compile  'com.google.dagger:dagger:2.6',
             'com.rabbitmq:amqp-client:3.6.5',
             'org.devoware:homonculus-messaging:1.0'
            
    compileOnly 'com.google.dagger:dagger-compiler:2.6'         
}
```
