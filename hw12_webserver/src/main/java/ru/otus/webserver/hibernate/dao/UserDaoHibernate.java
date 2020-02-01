package ru.otus.webserver.hibernate.dao;


import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.webserver.api.dao.UserDao;
import ru.otus.webserver.api.dao.UserDaoException;
import ru.otus.webserver.api.model.User;
import ru.otus.webserver.api.sessionmanager.SessionManager;
import ru.otus.webserver.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.webserver.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoHibernate implements UserDao {
  private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

  private final SessionManagerHibernate sessionManager;

  public UserDaoHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
  }


  @Override
  public Optional<User> findById(long id) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }



  @Override
  public long saveUser(User user) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      if (user.getId() > 0) {
        hibernateSession.merge(user);
      } else {
        hibernateSession.persist(user);
      }
      return user.getId();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
  }

  @Override
  public ArrayList<User> findAll() {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      return new ArrayList<>(hibernateSession.createQuery("SELECT a FROM User a", User.class).getResultList());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public Optional<User>  findByLogin(String login) {
    return null;
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
