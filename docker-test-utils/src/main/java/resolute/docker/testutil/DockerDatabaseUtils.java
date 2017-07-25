package resolute.docker.testutil;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.db.ManagedDataSource;

public class DockerDatabaseUtils {
  
  public static ManagedDataSource getDataSource(Class<?> klass) throws IOException, ConfigurationException {
    return getDataSource(klass, "dao-test.yml");
  }
  
  public static ManagedDataSource getDataSource(Class<?> klass, String configFile) throws IOException, ConfigurationException {
    requireNonNull(klass, "klass cannot be null");
    requireNonNull(configFile, "configFile cannot be null");
    DockerDataSourceFactory dataSourceFactory =
        new DockerDataSourceFactory(klass, configFile);
    ManagedDataSource dataSource = dataSourceFactory.build();
    return dataSource;
  }
  
  public static void seedDatabase(Class<?> klass, DataSource dataSource, String xmlFile) throws SQLException, DatabaseUnitException  {
    try (Connection jdbcConnection = dataSource.getConnection()) {
      IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
      IDataSet dataSet = new FlatXmlDataSetBuilder()
          .build(klass.getResourceAsStream(xmlFile));
      try {
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
      } finally {
        connection.close();
      }
    }
  }
  
  private DockerDatabaseUtils () {}

}
