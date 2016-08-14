package org.devoware.config.db;

import static java.util.Objects.requireNonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

import com.codahale.metrics.health.HealthCheck;

public class DatabaseHealthCheck extends HealthCheck implements DatabaseHealthCheckMBean {
  private DataSource dataSource;

  DatabaseHealthCheck(@Nonnull DataSource dataSource) {
    this.dataSource = requireNonNull(dataSource);
  }

  @Override
  public String healthCheck() throws Exception {
    Result result = check();
    if (result.isHealthy()) {
      return "The database is healthy";
    }
    return "The database is unhealthy: " + result.getError().getMessage();
  }

  @Override
  protected Result check() throws Exception {
    try {
      try (Connection connection = dataSource.getConnection()) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT 1")) {
          try (ResultSet resultSet = stmt.executeQuery()) {
            return HealthCheck.Result.healthy();
          }
        }
      }
    } catch (Exception io) {
      return HealthCheck.Result.unhealthy(io);
    }
  }

}
