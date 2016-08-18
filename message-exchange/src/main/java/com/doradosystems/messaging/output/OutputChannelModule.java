package com.doradosystems.messaging.output;

import java.lang.management.ManagementFactory;

import javax.annotation.Nonnull;
import javax.inject.Singleton;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.doradosystem.messaging.MessageExchangeInitializationException;

import dagger.Module;
import dagger.Provides;

@Module
public class OutputChannelModule {

  @Provides
  @Singleton
  @Nonnull
  public OutputChannel providesOutputChannel(RabbitMqOutputChannel outputChannel, HealthCheckRegistry healthchecks) {
    OutputChannelHealthCheck healthcheck = new OutputChannelHealthCheck(outputChannel);
    healthchecks.register("outputChannel", healthcheck);
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
    try {
      ObjectName name = new ObjectName("healthchecks:type=OutputChannel"); 
      mbs.registerMBean(healthcheck, name);
    } catch (MalformedObjectNameException | InstanceAlreadyExistsException
        | MBeanRegistrationException | NotCompliantMBeanException e) {
      throw new MessageExchangeInitializationException(
          "A problem occurred while attempting to create the output channel", e);
    } 
    return outputChannel;
  }
  
}
