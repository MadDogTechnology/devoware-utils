package org.devoware.config.db;

public class DatabaseInitializationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DatabaseInitializationException(String message, Throwable cause) {
    super(message, cause);
  }

}
