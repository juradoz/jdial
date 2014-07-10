package al.jdi.dao.beans;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

public interface SessionHandler {

  void close();

  Configuration getConfiguration();

  Configuration getConfiguration(String identifier);

  Session getSession();

  Session getSession(String identifier);

  Statistics getStatistics();

  Statistics getStatistics(String identifier);

}
