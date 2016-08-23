package com.doradosystems.messaging.output;

import java.io.IOException;
import java.util.Map;

public interface OutputChannel {

  public void write (String message) throws IOException;

  public void write(String message, Map<String, Object> headers) throws IOException;
  
}
