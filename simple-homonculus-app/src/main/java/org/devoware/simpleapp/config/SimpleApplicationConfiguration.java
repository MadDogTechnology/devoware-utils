package org.devoware.simpleapp.config;

import javax.validation.constraints.NotNull;

import org.devoware.homonculus.validators.validation.PortRange;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleApplicationConfiguration {

  @NotNull
  @JsonProperty
  private String hostName;

  @PortRange
  @JsonProperty
  private int port;

  public String getHostName() {
    return hostName;
  }

  public int getPort() {
    return port;
  }

}
