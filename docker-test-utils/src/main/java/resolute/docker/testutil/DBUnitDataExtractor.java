package resolute.docker.testutil;



import static java.util.Objects.requireNonNull;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;



/**
 * 
 * Extracts a DBUnit flat XML dataset from a database.
 * 
 * 
 * 
 * @author Bill Siggelkow
 * 
 */

public class DBUnitDataExtractor {



  private final DataSource dataSource;

  private final String dataSetName;

  private final List<String> queryList;

  private final List<String> tableList;

  private final Map<String, Object> dbUnitProperties;

  private final Map<String, Boolean> dbUnitFeatures;

  private String schema;



  /**
   * 
   * A regular expression that is used to get the table name
   * 
   * from a SQL 'select' statement.
   * 
   * This pattern matches a string that starts with any characters,
   * 
   * followed by the case-insensitive word 'from',
   * 
   * followed by a table name of the form 'foo' or 'schema.foo',
   * 
   * followed by any number of remaining characters.
   * 
   */

  private static final Pattern TABLE_MATCH_PATTERN =
      Pattern.compile(".*\\s+from\\s+(\\w+(\\.\\w+)?).*",

  Pattern.CASE_INSENSITIVE);

  private static final Logger log = LoggerFactory.getLogger(DBUnitDataExtractor.class);

  public static Builder builder () {
    return new Builder();
  }

  private DBUnitDataExtractor(Builder builder) {
    this.dataSetName = builder.dataSetName;
    this.dataSource = builder.dataSource;
    this.dbUnitFeatures = builder.dbUnitFeatures;
    this.dbUnitProperties = builder.dbUnitProperties;
    this.queryList = builder.queryList;
    this.schema = builder.schema;
    this.tableList = builder.tableList;
  }



  /**
   * 
   * Performs the extraction. If no tables or queries are specified, data from entire database
   * 
   * will be extracted. Otherwise, a partial extraction will be performed.
   * 
   * @throws Exception
   * 
   */

  public void extract() throws Exception {

    Connection conn = null;

    try {

      conn = dataSource.getConnection();

      log.info("Beginning extraction from '" + conn.toString() + "'.");

      IDatabaseConnection connection = new DatabaseConnection(conn, schema);

      configConnection((DatabaseConnection) connection);

      if (tableList != null || queryList != null) {

        // partial database export

        QueryDataSet partialDataSet = new QueryDataSet(connection);

        addTables(partialDataSet);

        addQueries(partialDataSet);

        FlatXmlDataSet.write(partialDataSet,

        new FileOutputStream(dataSetName));

      } else {

        // full database export

        IDataSet fullDataSet = connection.createDataSet();

        FlatXmlDataSet.write(fullDataSet, new FileOutputStream(dataSetName));

      }

    }

    finally {

      if (conn != null)
        conn.close();

    }

    log.info("Completed extraction to '" + dataSetName + "'.");

  }



  @SuppressWarnings("deprecation")
  private void configConnection(DatabaseConnection conn) {

    DatabaseConfig config = conn.getConfig();

    if (dbUnitProperties != null) {

      for (Map.Entry<String, Object> entry : dbUnitProperties.entrySet()) {

        String name = entry.getKey();

        Object value = entry.getValue();

        config.setProperty(name, value);

      }

    }

    if (dbUnitFeatures != null) {

      for (Map.Entry<String, Boolean> entry : dbUnitFeatures.entrySet()) {

        String name = (String) entry.getKey();

        boolean value = entry.getValue();

        config.setFeature(name, value);

      }

    }

  }



  private void addTables(QueryDataSet dataSet) {

    if (tableList == null)
      return;

    for (String table : tableList) {

      try {
        dataSet.addTable(table);
      } catch (AmbiguousTableNameException e) {
        throw new RuntimeException(e);
      }

    }

  }



  private void addQueries(QueryDataSet dataSet) {

    if (queryList == null)
      return;

    for (String query : queryList) {

      Matcher m = TABLE_MATCH_PATTERN.matcher(query);

      if (!m.matches()) {

        log.warn("Unable to parse query. Ignoring '" + query + "'.");

      }

      else {

        String table = m.group(1);

        // only add if the table has not been added

        if (tableList != null && tableList.contains(table)) {

          log.warn("Table '" + table + "' already added. Ignoring '" + query + "'.");

        } else {

          try {
            dataSet.addTable(table, query);
          } catch (AmbiguousTableNameException e) {
            throw new RuntimeException(e);
          }
        }

      }

    }

  }



  public static class Builder {

    private DataSource dataSource;

    private String dataSetName = "dbunit-dataset.xml";

    private List<String> queryList;

    private List<String> tableList;

    private Map<String, Object> dbUnitProperties;

    private Map<String, Boolean> dbUnitFeatures;

    private String schema;

    private Builder () {}

    /**
     * 
     * The data source of the database from which the data will be extracted. This property
     * 
     * is required.
     * 
     * 
     * 
     * @param ds
     * 
     */

    public Builder withDataSource(DataSource dataSource) {

      this.dataSource = requireNonNull(dataSource, "dataSource cannot be null");

      return this;

    }



    /**
     * 
     * Set the schema.
     * 
     * @param schema
     * 
     */

    public Builder withSchema(String schema) {

      this.schema = requireNonNull(schema, "schema cannot be null");

      return this;

    }



    /**
     * 
     * Name of the XML file that will be created. Defaults to <code>dbunit-dataset.xml</code>.
     * 
     * 
     * 
     * @param name file name.
     * 
     */

    public Builder withDataSetName(String name) {

      dataSetName = requireNonNull(name, "name cannot be null");

      return this;

    }



    /**
     * 
     * List of table names to extract data from.
     * 
     * 
     * 
     * @param list of table names.
     * 
     */

    public Builder withTableList(List<String> list) {

      tableList = ImmutableList.copyOf(requireNonNull(list, "list cannot be null"));


      return this;
    }



    /**
     * 
     * List of SQL queries (i.e. 'select' statements) that will be used executed to retrieve
     * 
     * the data to be extracted. If the table being queried is also specified in the
     * <code>tableList</code>
     * 
     * property, the query will be ignored and all rows will be extracted from that table.
     * 
     * 
     * 
     * @param list of SQL queries.
     * 
     */

    public Builder withQueryList(List<String> list) {

      queryList = ImmutableList.copyOf(requireNonNull(list, "list cannot be null"));

      return this;

    }



    public Builder withDbUnitFeatures(Map<String, Boolean> dbUnitFeatures) {

      this.dbUnitFeatures = ImmutableMap.copyOf(requireNonNull(dbUnitFeatures, "dbUnitFeatures cannot be null"));

      return this;

    }



    public Builder withDbUnitProperties(Map<String, Object> dbUnitProperties) {

      this.dbUnitProperties = ImmutableMap.copyOf(requireNonNull(dbUnitProperties, "dbUnitProperties cannot be null"));

      return this;

    }
    
    public DBUnitDataExtractor build () {
      requireNonNull(dataSource, "dataSource cannot be null");
      return new DBUnitDataExtractor(this);
    }

  }

}
