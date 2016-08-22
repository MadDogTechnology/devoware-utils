package com.doradosystems.messaging.output;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.collect.ImmutableMap;

public class OutputChannelHealthCheck extends HealthCheck implements OutputChannelHealthCheckMBean {
  private OutputChannel channel;

  OutputChannelHealthCheck(@Nonnull OutputChannel channel) {
    this.channel = requireNonNull(channel);
  }

  @Override
  public String healthCheck() throws Exception {
    Result result = check();
    if (result.isHealthy()) {
      return "The output channel is healthy";
    }
    return "The output channel is unhealthy: " + result.getError().getMessage();
  }

  @Override
  protected Result check() throws Exception {
    try {
      channel.write("healthcheck", ImmutableMap.of("message-type", "health-check"));
      return HealthCheck.Result.healthy();
    } catch (IOException io) {
      return HealthCheck.Result.unhealthy(io);
    }
  }

}
