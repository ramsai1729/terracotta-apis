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
package org.terracotta.entity;

/**
 * Abstraction for dumping a component's state using key-value mappings in hierarchical manner
 *
 * <b>Usage</b>
 * <p>
 *   StateDumper topLevelStateDumper;
 *   StateDumper ehcacheEntityDumper = topLevelStateDumper.subStateDumper("ehcache");
 *   StateDumper flatFileStorageDumper = topLevelStateDumper.subStateDumper("flatFileStorage");
 *   StateDumper locationFlatFileStorageDumper = flatFileStorageDumper.subStateDumper("location");
 *   locationFlatFileStorageDumper.dump("storageDir", "/var/log/flatfiledir");
 *   StateDumper storageUsageFlatFileStorageDumper = flatFileStorageDumper.subStateDumper("location");
 *   storageUsageFlatFileStorageDumper.dump("used-space", "100MB");
 *   storageUsageFlatFileStorageDumper.dump("free-space", "900MB");
 *   storageUsageFlatFileStorageDumper.dump("allocated-space", "1000MB");
 * </p>
 *
 *
 * @author vmad
 */
public interface StateDumper {
  /**
   * Returns a namespace'd {@link StateDumper}
   *
   * @param name Sub {@link StateDumper} name
   * @return A {@link StateDumper}
   */
  StateDumper subStateDumper(String name);

  /**
   * Adds given key-value mapping to this dumper
   *
   * @param key
   * @param value
   */
  void dump(String key, String value);

}
