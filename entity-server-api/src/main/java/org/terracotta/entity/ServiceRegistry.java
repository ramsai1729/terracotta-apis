package org.terracotta.entity;

import java.util.Optional;

/**
 * @author twu
 */
public interface ServiceRegistry {

  /**
   * Get the unwrapped service instance of a given type.
   *
   * @param type type of the service to get
   * @param <T> type of service
   * @return instance of the service if it exists
   */
  <T> Optional<T> getService(Class<T> type);
}
