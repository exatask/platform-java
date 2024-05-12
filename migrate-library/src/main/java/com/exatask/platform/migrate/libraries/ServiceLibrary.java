package com.exatask.platform.migrate.libraries;

import com.exatask.platform.migrate.entities.AppEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceLibrary extends AppLibrary {

  private static final String VALUE_PLACEHOLDER = "?";

  protected Connection connection;

  public ServiceLibrary(Connection connection) {
    this.connection = connection;
  }

  protected void insert(String table, List<String> columns, List<? extends AppEntity> entities) throws SQLException {

    String[] valuePlaceholder = new String[columns.size()];
    Arrays.fill(valuePlaceholder, VALUE_PLACEHOLDER);

    List<String> queries = new ArrayList<>();
    String insertQuery = String.format("INSERT INTO `%s` (`%s`) VALUES (%s);",
        table,
        String.join("`,`", columns),
        String.join(",", valuePlaceholder));

    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

    for (AppEntity entity : entities) {

      int i = 1;
      for (String column : columns) {
        preparedStatement.setObject(i++, entity.get(column));
      }
      preparedStatement.addBatch();
      queries.add(preparedStatement.toString());
    }

    LOGGER.debug("SQL statement: " + String.join("\n", queries));
    preparedStatement.executeBatch();
    connection.commit();
    preparedStatement.clearBatch();
  }
}
