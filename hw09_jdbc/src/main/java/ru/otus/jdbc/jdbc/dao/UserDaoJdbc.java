package ru.otus.jdbc.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbc.api.dao.UserDao;
import ru.otus.jdbc.api.dao.UserDaoException;
import ru.otus.jdbc.api.model.User;
import ru.otus.jdbc.jdbc.DbExecutor;
import ru.otus.jdbc.jdbc.SqlQueryGenerator;
import ru.otus.jdbc.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class UserDaoJdbc implements UserDao {
  private static Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

  private final SessionManagerJdbc sessionManagerJdbc;
  private final DbExecutor<User> dbExecutor;
  private final SqlQueryGenerator sqlQueryGenerator;

  public UserDaoJdbc(SessionManagerJdbc sessionManagerJdbc, DbExecutor<User> dbExecutor, SqlQueryGenerator sqlQueryGenerator) {
    this.sessionManagerJdbc = sessionManagerJdbc;
    this.dbExecutor = dbExecutor;
    this.sqlQueryGenerator = sqlQueryGenerator;
  }


  @Override
  public Optional<User> findById(long id) {
    try {
      sqlQueryGenerator.createSelect(User.class);
      return dbExecutor.selectRecord(getConnection(), sqlQueryGenerator.getSqlSelect(), id, resultSet -> {
        try {
          if (resultSet.next()) {
            return new User(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("age"));
          }
        } catch (SQLException e) {
          logger.error(e.getMessage(), e);
        }
        return null;
      });
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }


  @Override
  public long saveUser(User user) {
    try {
      sqlQueryGenerator.createInsert(user);
      return dbExecutor.insertRecord(getConnection(), sqlQueryGenerator.getSqlInsert(), sqlQueryGenerator.getOtherfieldsvalues());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
  }

  @Override
  public void updateUser(User user, long id) {
    try {
      sqlQueryGenerator.createUpdate(user);
      dbExecutor.updateRecord(getConnection(), sqlQueryGenerator.getSqlUpdate(), id, sqlQueryGenerator.getOtherfieldsvalues());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
  }

  @Override
  public ru.otus.jdbc.api.sessionmanager.SessionManager getSessionManagerJdbc() {
    return sessionManagerJdbc;
  }

  private Connection getConnection() {
    return sessionManagerJdbc.getCurrentSession().getConnection();
  }
}
