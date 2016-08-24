package org.devoware.simpleapp;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.devoware.homonculus.core.Application;
import org.devoware.homonculus.core.lifecycle.Managed;
import org.devoware.homonculus.core.setup.Environment;
import org.devoware.homonculus.validators.util.Duration;
import org.devoware.simpleapp.config.SimpleApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

public class SimpleApplication extends Application<SimpleApplicationConfiguration> {
  private static final Logger log = LoggerFactory.getLogger(SimpleApplication.class);

  private ScheduledExecutorService executorService;
  private SimpleApplicationConfiguration config;
  private Meter serverCalls;

  @Override
  public String getName() {
    return "simple-homonculus-app";
  }

  @Override
  protected void initialize(SimpleApplicationConfiguration config, Environment env) {
    this.config = config;
    this.executorService = env.scheduledExecutorService("Simple Application")
        .shutdownTime(Duration.seconds(5)).build();
    this.serverCalls =
        env.metrics().meter(MetricRegistry.name(SimpleApplication.class, "server-calls"));
    env.manage(new Managed() {

      @Override
      public void start() throws Exception {
        log.info("Managed resource start() was called");
      }

      @Override
      public void stop() throws Exception {
        log.info("Managed resource stop() was called");
      }

    });
  }

  @Override
  public void start() throws Exception {
    this.executorService.scheduleAtFixedRate(() -> {
      log.info("Calling server at " + config.getHostName() + ":" + config.getPort());
      serverCalls.mark();
    } , 0, 1, TimeUnit.MINUTES);
  }

}
