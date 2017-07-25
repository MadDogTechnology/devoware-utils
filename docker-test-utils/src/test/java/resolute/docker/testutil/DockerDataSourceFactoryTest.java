package resolute.docker.testutil;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;

public class DockerDataSourceFactoryTest {

  @Test
  public void test_data_source_build() throws IOException, ConfigurationException {
    DockerDataSourceFactory dataSourceFactory =
        new DockerDataSourceFactory(DockerDataSourceFactoryTest.class, "resolute/docker/testutil/test.yml");
    ManagedDataSource dataSource = dataSourceFactory.build();
    assertNotNull(dataSource);
  }

  @Test
  public void test_data_source_factory_with_custom_port() throws IOException, ConfigurationException {
    System.setProperty("docker.database.port", "5467");
    DataSourceFactory dataSourceFactory =
        new DockerDataSourceFactory(DockerDataSourceFactoryTest.class, "resolute/docker/testutil/test.yml").getDataSourceFactory();
    assertThat(dataSourceFactory.getUrl(), equalTo("jdbc:postgresql://localhost:5467/test_database"));
  }
}
