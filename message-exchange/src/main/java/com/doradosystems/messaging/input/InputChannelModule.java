package com.doradosystems.messaging.input;

import javax.annotation.Nonnull;
import javax.inject.Named;
import javax.inject.Singleton;

import org.devoware.homonculus.setup.Environment;

import com.google.common.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;

@Module
public class InputChannelModule {

  @Provides @Named("input-channel-event-bus")
  @Singleton
  @Nonnull
  public EventBus providesInputChannelEventBus() {
    return new EventBus("input-channel");
  }
  
  @Provides
  @Singleton
  @Nonnull
  public InputChannel providesInputChannel(RabbitMqInputChannel inputChannel, Environment environment) {
    environment.manage(inputChannel);
    return inputChannel;
  }
  
}
