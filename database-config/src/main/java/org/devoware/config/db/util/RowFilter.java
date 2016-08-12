package org.devoware.config.db.util;

public interface RowFilter<T> {

  public boolean accept(T entity);

}
