package org.terracotta.entity;

import org.terracotta.connection.entity.Entity;

import java.util.ServiceLoader;

/**
 * @author twu
 */
public class EntityClientServiceFactory {
  public static <T extends Entity> EntityClientService<T> creationServiceForType(Class<T> cls) {
    return creationServiceForType(cls, EntityClientServiceFactory.class.getClassLoader());
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static <T extends Entity> EntityClientService<T> creationServiceForType(Class<T> cls, final ClassLoader classLoader) {
    ServiceLoader<EntityClientService> serviceLoader = ServiceLoader.load(EntityClientService.class,
        classLoader);
    for (EntityClientService<T> entityClientService : serviceLoader) {
      if (entityClientService.handlesEntityType(cls)) {
        return entityClientService;
      }
    }
    throw new IllegalArgumentException("Can't handle entity type " + cls.getName());
  }
}
