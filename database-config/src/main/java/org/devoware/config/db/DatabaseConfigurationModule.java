package org.devoware.config.db;

import javax.inject.Singleton;
import javax.sql.DataSource;

import org.devoware.homonculus.setup.Environment;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseConfigurationModule {

  @Provides
  @Singleton
  public DataSource provideDataSource(DataSourceFactory dataSourceFactory, Environment environment) {
    ManagedDataSource dataSource = dataSourceFactory.build(environment.metrics(), "<default>");
    environment.manage(dataSource);
    return dataSource;
  }

}
