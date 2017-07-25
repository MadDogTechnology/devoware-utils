package resolute.docker.testutil;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Validator;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.validation.BaseValidator;

public class DockerDataSourceFactory {
  private static final Pattern JDBC_PATTERN = Pattern.compile("//[^:]+:(\\d+)/");

  private final String configFilePath;
  private final ConfigurationSourceProvider provider;
  
  public DockerDataSourceFactory (Class<?> klass, String configFilePath) {
    requireNonNull(klass);
    this.configFilePath = requireNonNull(configFilePath);
    this.provider = new ClasspathConfigurationSourceProvider(klass.getClassLoader());
  }
  
  public ManagedDataSource build () throws IOException, ConfigurationException {
    DataSourceFactory dataSourceFactory = getDataSourceFactory();
    
    MetricRegistry metrics = new MetricRegistry();
    ManagedDataSource dataSource = dataSourceFactory.build(metrics, "test-data-source");
    return dataSource;
  }

  public DataSourceFactory getDataSourceFactory() throws IOException, ConfigurationException {
    Validator validator = BaseValidator.newConfiguration().buildValidatorFactory().getValidator();
    ObjectMapper objectMapper = new ObjectMapper();
    ConfigurationFactory<DataSourceFactory> configFactory =
        new ConfigurationFactory<>(DataSourceFactory.class, validator, objectMapper, "dw");
    
    DataSourceFactory dataSourceFactory = configFactory.build(provider, configFilePath);
    String customPort = System.getProperty("docker.database.port");
    if (customPort != null) {
      String url = dataSourceFactory.getUrl();
      Matcher m = JDBC_PATTERN.matcher(url);
      if (m.find()) {
        String newUrl = new StringBuilder(url).replace(m.start(1), m.end(1), customPort).toString();
        dataSourceFactory.setUrl(newUrl);
      } else {
        throw new IOException("A problem occurred while attempting to configure a custom database port " + customPort + " within the url " + url + ". The URL is probably not properly formatted");
      }
    }
    return dataSourceFactory;
  }
}
