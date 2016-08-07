package org.devoware.homonculus.lifecycle;

public interface Managed {
  public static final int HIGH_PRIORITY = 10;
  public static final int MEDIUM_PRIORITY = 20;
  public static final int LOW_PRIORITY = 30;

  public void start() throws Exception;

  public void stop() throws Exception;

  public int getPriority();

}
