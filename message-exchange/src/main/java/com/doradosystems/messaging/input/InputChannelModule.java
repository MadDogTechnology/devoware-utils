package com.doradosystems.messaging.input;

import static com.doradosystem.messaging.MessageExchangeQualifiers.INPUT_EVENT_BUS;

import javax.annotation.Nonnull;
import javax.inject.Named;
import javax.inject.Singleton;

import org.devoware.homonculus.setup.Environment;

import com.google.common.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;

@Module
public class InputChannelModule {

  private static final String EVENT_BUS_NAME = "input-channel";

  @Provides @Named(INPUT_EVENT_BUS)
  @Singleton
  @Nonnull
  public EventBus providesInputChannelEventBus() {
    return new EventBus(EVENT_BUS_NAME);
  }
  
  @Provides
  @Singleton
  @Nonnull
  public InputChannel providesInputChannel(RabbitMqInputChannel inputChannel, Environment environment) {
    environment.manage(inputChannel);
    return inputChannel;
  }
  
}
