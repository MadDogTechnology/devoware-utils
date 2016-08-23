package org.devoware.configuration;

import java.io.IOException;
import java.io.InputStream;

public interface ConfigurationSourceProvider {

  InputStream open(String path) throws IOException;

}
