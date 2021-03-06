/*
 *
 *  The contents of this file are subject to the Terracotta Public License Version
 *  2.0 (the "License"); You may not use this file except in compliance with the
 *  License. You may obtain a copy of the License at
 *
 *  http://terracotta.org/legal/terracotta-public-license.
 *
 *  Software distributed under the License is distributed on an "AS IS" basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 *  the specific language governing rights and limitations under the License.
 *
 *  The Covered Software is Entity API.
 *
 *  The Initial Developer of the Covered Software is
 *  Terracotta, Inc., a Software AG company
 *
 */
package org.terracotta.passthrough;

import java.util.Collection;

import org.terracotta.entity.ServiceConfiguration;
import org.terracotta.entity.ServiceProvider;
import org.terracotta.entity.ServiceProviderConfiguration;


/**
 * The provider of PassthroughCommunicatorService, to server-side entities.  It has no meaningful implementation beyond
 * providing that.
 */
public class PassthroughCommunicatorServiceProvider implements ServiceProvider {
  @Override
  public void close() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean initialize(ServiceProviderConfiguration configuration) {
    // We always return true on initialize of this service (it has no state).
    return true;
  }

  @Override
  public <T> T getService(long consumerID, ServiceConfiguration<T> configuration) {
    return configuration.getServiceType().cast(new PassthroughCommunicatorService());
  }

  @Override
  public Collection<Class<?>> getProvidedServiceTypes() {
    // TODO Auto-generated method stub
    return null;
  }
}
