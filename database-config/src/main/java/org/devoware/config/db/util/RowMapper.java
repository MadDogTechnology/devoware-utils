package org.devoware.config.db.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {

  public T mapRow(int rowNumber, ResultSet rs) throws SQLException;

}
