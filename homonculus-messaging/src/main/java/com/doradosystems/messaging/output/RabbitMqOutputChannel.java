package com.doradosystems.messaging.output;

import static com.codahale.metrics.MetricRegistry.name;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.doradosystem.messaging.config.RabbitMqConfiguration;
import com.google.common.collect.ImmutableMap;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

public class RabbitMqOutputChannel implements OutputChannel {
  Logger log = LoggerFactory.getLogger(RabbitMqOutputChannel.class);
  
  private final Channel channel;
  private final String exchangeName;
  private final String queueName;
  private final Meter messagesSent;

  @Inject
  public RabbitMqOutputChannel(@Nonnull Channel channel, @Nonnull RabbitMqConfiguration config, @Nonnull MetricRegistry metrics) {
    this.channel = requireNonNull(channel);
    this.exchangeName = requireNonNull(config.getExchange());
    this.queueName = requireNonNull(config.getQueue());
    this.messagesSent = requireNonNull(metrics).meter(name(RabbitMqOutputChannel.class, "messages-sent"));
  }
  
  @Override
  public void write(@Nonnull String message) throws IOException {
      write(message, null);
  }

  @Override
  public void write(@Nonnull String message, @Nullable Map<String, Object> headers) throws IOException {
    BasicProperties props = null;
    if (headers != null) {
      props = new BasicProperties.Builder()
          .headers(ImmutableMap.copyOf(headers)).build();
    }
    channel.basicPublish(exchangeName, queueName, props, message.getBytes(StandardCharsets.UTF_8));
    messagesSent.mark();
    if (log.isDebugEnabled()) {
      log.debug("Successfully published message: " + message);
    }
  }
}
