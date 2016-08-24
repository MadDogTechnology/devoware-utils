package org.devoware.homonculus.messaging;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import org.devoware.homonculus.core.lifecycle.Managed;
import org.devoware.homonculus.core.setup.Environment;
import org.devoware.homonculus.messaging.config.RabbitMqConfiguration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class MessageExchangeModule {

  @Provides
  @Singleton
  @Nonnull
  public Connection providesRabbitMqConnection (RabbitMqConfiguration config, Environment env) {
    final ConnectionFactory factory = new ConnectionFactory();
    factory.setUsername(config.getUser());
    factory.setPassword(config.getPassword());
    factory.setVirtualHost(config.getVirtualHost());
    factory.setHost(config.getHost());
    factory.setPort(config.getPort());
    try {
      final Connection connection = factory.newConnection();
      env.manage(new ConnectionManager(connection));
      return connection;
    } catch (IOException | TimeoutException e) {
      throw new MessageExchangeInitializationException(
          "A problem occurred while attempting to create the RabbitMQ connection", e);
    }
  }
  
  @Provides
  @Singleton
  @Nonnull
  public Channel providesRabbitMqChannel (RabbitMqConfiguration config, Environment env, Connection connection) {
    // It's OK to declare the channel as a singleton, since it will only be accessed by a single thread at a time.
    // If this were a multi-threaded application, we would want to have one channel per thread (i.e. use a ThreadLocal)
    try {
      Channel channel = connection.createChannel();
      channel.queueDeclare(config.getQueue(), true, false, false, null);
      env.manage(new ChannelManager(channel));
      return channel;
    } catch (IOException e) {
      throw new MessageExchangeInitializationException(
          "A problem occurred while attempting to create the RabbitMQ channel", e);
    }
  }
  
  
  private static class ConnectionManager implements Managed {
    private Connection connection;
    
    ConnectionManager(@Nonnull Connection connection) {
      this.connection = requireNonNull(connection);
    }
    
    @Override
    public void stop() throws IOException {
        connection.close();      
    }
    
    @Override
    public int getPriority() {
      return 998; // We want to make sure the connection is closed after the channel
    }
  }
  
  private static class ChannelManager implements Managed {
    private Channel channel;
    
    ChannelManager(@Nonnull Channel channel) {
      this.channel = requireNonNull(channel);
    }
    
    @Override
    public void stop() throws IOException, TimeoutException {
        channel.close();      
    }
    
  }
}
