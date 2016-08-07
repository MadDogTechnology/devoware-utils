package org.devoware.homonculus;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;

import org.devoware.configuration.ClasspathConfigurationSourceProvider;
import org.devoware.configuration.ConfigurationException;
import org.devoware.configuration.ConfigurationFactory;
import org.devoware.configuration.ConfigurationSourceProvider;
import org.devoware.configuration.YamlConfigurationFactory;
import org.devoware.configuration.validation.Validators;
import org.devoware.homonculus.lifecycle.LifecycleManager;
import org.devoware.homonculus.lifecycle.ManagedAdapter;
import org.devoware.homonculus.setup.Environment;
import org.devoware.homonculus.shutdown.Terminator;
import org.devoware.homonculus.util.Generics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Application<T> extends ManagedAdapter {

  private static final Logger appLog = LoggerFactory.getLogger(Application.class);

  private final LifecycleManager lifecycleManager;
  
  public Application () {
    this.lifecycleManager = new LifecycleManager(getTerminationPort(), new Environment());
  }
  
  public Application (LifecycleManager lifecycleManager) {
    this.lifecycleManager = lifecycleManager;
  }
  
  public final void appInit(String[] args) throws Exception {
    T configuration = getConfiguration(checkNotNull(args));
    lifecycleManager.getEnvironment().manage(this);

    initialize(configuration, lifecycleManager.getEnvironment());
    UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {

      @Override
      public void uncaughtException(Thread thread, Throwable t) {
        doGracefulShutdown();
      }

    };
    // For some reason, the uncaught exception handler is never invoked on my Mac OSx PC!
    Thread.setDefaultUncaughtExceptionHandler(handler);
    Thread.currentThread().setUncaughtExceptionHandler(handler);
  }

  public final void appStart() throws Exception {
    try {
      lifecycleManager.start();
    } catch (Throwable t) {
      appLog.error(String.format("A problem occurred while attempting to start the %s application.", getName()), t);
      lifecycleManager.stop();
    }
  }

  public final void appStop() throws Exception {
    appLog.info(String.format("Attempting to stop the %s application...", getName()));
    Terminator terminator = new Terminator(this.lifecycleManager);
    try {
        terminator.terminate();
    } catch (ConnectException ex) {
        appLog.info(String.format("Could not stop the %s application because it does not appear to be running.", getName()));
    } catch (Exception ex) {
        appLog.error(String.format("A problem occurred while attempting to stop the %s application", getName()), ex);
    }

  }

  public final Class<T> getConfigurationClass() {
    return Generics.getTypeParameter(getClass(), Object.class);
  }

  public int getTerminationPort() {
    return LifecycleManager.DEFAULT_TERMINATION_PORT;
  }
  
  @Override
  public int getPriority() {
    return 999;
  }
  
  public abstract String getName();

  protected abstract void initialize(T configuration, Environment environment);

  private T getConfiguration(String[] args) throws IOException, ConfigurationException {
    if (args.length < 2) {
      throw new RuntimeException(
          "You must specify the path to a configuration file as the second program argument.");
    }
    String configFilePath = args[1];
    appLog.info("Loading configuration file " + configFilePath + "...");
    ConfigurationSourceProvider provider = new ClasspathConfigurationSourceProvider();

    ConfigurationFactory<T> configFactory = new YamlConfigurationFactory<T>(getConfigurationClass(),
        new ObjectMapper(), Validators.newValidator());
    T config = configFactory.build(provider, configFilePath);
    appLog.info("Configuration file loaded successfully.");
    return config;
  }
  
  private void doGracefulShutdown() {
    appLog.info(String.format("Attempting stop the %s application....", getName()));
    if (lifecycleManager != null) {
        try {
          lifecycleManager.stop();
        } catch (Exception e) {
          appLog.error("A problem occurred while attempting to stop the application.", e);
        }
    }
}
  
}
