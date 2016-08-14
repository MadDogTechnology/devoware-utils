package org.devoware.config.db;

import java.lang.management.ManagementFactory;

import javax.inject.Singleton;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.sql.DataSource;

import org.devoware.homonculus.setup.Environment;

import com.codahale.metrics.health.HealthCheckRegistry;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseConfigurationModule {

  @Provides
  @Singleton
  public DataSource provideDataSource(DataSourceFactory dataSourceFactory, Environment environment, HealthCheckRegistry healthchecks) {
    ManagedDataSource dataSource = dataSourceFactory.build(environment.metrics(), "<default>");
    environment.manage(dataSource);
    DatabaseHealthCheck healthcheck = new DatabaseHealthCheck(dataSource);
    healthchecks.register("database", healthcheck);
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
    try {
      ObjectName name = new ObjectName("healthchecks:type=Database"); 
      mbs.registerMBean(healthcheck, name);
    } catch (MalformedObjectNameException | InstanceAlreadyExistsException
        | MBeanRegistrationException | NotCompliantMBeanException e) {
      throw new DatabaseInitializationException(
          "A problem occurred while attempting to initialize the database", e);
    } 
    return dataSource;
  }

}
