package com.doradosystems.messaging.input;

import static com.codahale.metrics.MetricRegistry.name;
import static java.util.Objects.requireNonNull;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

import org.devoware.homonculus.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.doradosystem.messaging.config.RabbitMqConfiguration;
import com.google.common.eventbus.EventBus;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RabbitMqInputChannel implements InputChannel, Managed {
  Logger log = LoggerFactory.getLogger(RabbitMqInputChannel.class);

  private final Channel channel;
  private final String queueName;
  private final EventBus eventBus;
  private final Meter messagesReceived;

  @Inject
  public RabbitMqInputChannel(@Nonnull Channel channel, @Nonnull RabbitMqConfiguration config,
      @Nonnull @Named("input-channel-event-bus") EventBus eventBus, @Nonnull MetricRegistry metrics) {
    this.channel = requireNonNull(channel);
    this.queueName = requireNonNull(config).getQueue();
    this.eventBus = requireNonNull(eventBus);
    this.messagesReceived = metrics.meter(name(RabbitMqInputChannel.class, "messages-received"));
  }


  @Override
  public void start() throws IOException {
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
          byte[] body) throws IOException {
        messagesReceived.mark();
        Message message = Message.builder().withBody(body).withConsumerTag(consumerTag)
            .withEnvelope(envelope).withProperties(properties).build();
        if (log.isDebugEnabled()) {
          log.info("Received message: " + message.getBodyAsString());
        }
        eventBus.post(message);
      }
    };
    channel.basicConsume(queueName, true, consumer);
  }


  @Override
  public void register(@Nonnull Object object) {
    eventBus.register(requireNonNull(object));
  }


  @Override
  public void unregister(@Nonnull Object object) {
    eventBus.unregister(requireNonNull(object));
  }
}
