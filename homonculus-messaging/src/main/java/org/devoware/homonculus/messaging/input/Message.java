package org.devoware.homonculus.messaging.input;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.annotation.Nonnull;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

public class Message {
  
  private final String consumerTag; 
  private final Envelope envelope; 
  private final BasicProperties properties;
  private final byte [] body;
  private final String sBody;
  
  @Nonnull
  public static Builder builder() {
    return new Builder();
  }
  
  private Message(@Nonnull Builder builder) {
    this.consumerTag = builder.consumerTag;
    this.envelope = builder.envelope;
    this.properties = builder.properties;
    this.body = builder.body;
    this.sBody = new String(body, StandardCharsets.UTF_8);
  }
  
  @Nonnull
  public String getConsumerTag() {
    return consumerTag;
  }

  @Nonnull
  public Envelope getEnvelope() {
    return envelope;
  }

  @Nonnull
  public BasicProperties getProperties() {
    return properties;
  }

  @Nonnull
  public byte[] getBody() {
    return body;
  }

  @Nonnull
  public String getBodyAsString() {
    return sBody;
  }
  
  @Override
  public String toString() {
    return "Message [consumerTag=" + consumerTag + ", envelope=" + envelope + ", properties="
        + properties + ", body=" + sBody.substring(0, Math.min(20, sBody.length())) + "]";
  }

  public static class Builder {
    private String consumerTag; 
    private Envelope envelope; 
    private BasicProperties properties;
    private byte [] body;
    
    private Builder () {}

    @Nonnull 
    public Builder withConsumerTag(@Nonnull String consumerTag) {
      this.consumerTag = requireNonNull(consumerTag, "consumerTag cannot be null");
      return this;
    }

    @Nonnull 
    public Builder withEnvelope(@Nonnull Envelope envelope) {
      this.envelope = requireNonNull(envelope, "envelope cannot be null");
      return this;
    }

    @Nonnull 
    public Builder withProperties(@Nonnull BasicProperties properties) {
      this.properties = requireNonNull(properties, "properties cannot be null");
      return this;
    }

    @Nonnull 
    public Builder withBody(@Nonnull byte [] body) {
      this.body = Arrays.copyOf(requireNonNull(body, "body cannot be null"), body.length);
      return this;
    }
    
    @Nonnull
    public Message build () {
      requireNonNull(consumerTag, "consumerTag cannot be null");
      requireNonNull(envelope, "envelope cannot be null");
      requireNonNull(properties, "properties cannot be null");
      requireNonNull(body, "body cannot be null");
      return new Message(this);
    }
    
  }
}
