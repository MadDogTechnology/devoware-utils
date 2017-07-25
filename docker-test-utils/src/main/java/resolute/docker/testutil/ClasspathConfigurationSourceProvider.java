package resolute.docker.testutil;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import io.dropwizard.configuration.ConfigurationSourceProvider;

/**
 * An implementation of {@link ConfigurationSourceProvider} that reads the configuration from the
 * classpath.
 */
class ClasspathConfigurationSourceProvider implements ConfigurationSourceProvider {
  private final ClassLoader cl;

  ClasspathConfigurationSourceProvider() {
    this(ClasspathConfigurationSourceProvider.class.getClassLoader());
  }

  ClasspathConfigurationSourceProvider(ClassLoader cl) {
    this.cl = checkNotNull(cl);
  }

  @Override
  public InputStream open(String path) throws IOException {
    InputStream input = cl.getResourceAsStream(path);
    if (input == null) {
      throw new FileNotFoundException("Classpath resource " + path + " could not be found.");
    }
    return input;
  }
}
