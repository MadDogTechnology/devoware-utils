package com.doradosystem.messaging.config;

import javax.validation.constraints.NotNull;

import org.devoware.validators.validation.PortRange;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RabbitMqConfiguration {

  @NotNull
  @JsonProperty
  private String host;
  
  @NotNull
  @PortRange
  @JsonProperty
  private int port;
  
  @NotNull
  @JsonProperty
  private String user;
  
  @NotNull
  @JsonProperty
  private String password;

  @NotNull
  @JsonProperty
  private String virtualHost;
  
  @JsonProperty
  private String exchange;

  @NotNull
  @JsonProperty
  private String queue;

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }

  public String getVirtualHost() {
    return virtualHost;
  }

  public String getExchange() {
    return exchange != null ? exchange.trim() : "";
  }

  public String getQueue() {
    return queue;
  }
}
