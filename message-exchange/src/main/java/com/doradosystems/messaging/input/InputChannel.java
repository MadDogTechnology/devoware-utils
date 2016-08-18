package com.doradosystems.messaging.input;

public interface InputChannel {
  
  public void register(Object object);
  public void unregister(Object object);

}
