package org.devoware.config.db;

import javax.sql.DataSource;

import org.devoware.homonculus.lifecycle.Managed;

public interface ManagedDataSource extends DataSource, Managed {

}
