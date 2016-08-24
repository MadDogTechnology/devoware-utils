package org.devoware.homonculus.messaging.input;

public interface InputChannel {
  
  public void register(Object object);
  public void unregister(Object object);

}
