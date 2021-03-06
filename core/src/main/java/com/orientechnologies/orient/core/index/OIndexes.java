/*
 * Copyright 2012 Geomatys.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orientechnologies.orient.core.index;

import static com.orientechnologies.common.util.OClassLoaderHelper.lookupProviderWithOrientClassLoader;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.orientechnologies.common.util.OCollections;
import com.orientechnologies.orient.core.db.record.ODatabaseRecord;
import com.orientechnologies.orient.core.exception.OConfigurationException;

/**
 * Utility class to create indexes. New OIndexFactory can be registered
 * 
 * <p>
 * In order to be detected, factories must implement the {@link OIndexFactory} interface.
 * </p>
 * 
 * <p>
 * In addition to implementing this interface datasouces should have a services file:<br/>
 * <code>META-INF/services/com.orientechnologies.orient.core.index.OIndexFactory</code>
 * </p>
 * 
 * <p>
 * The file should contain a single line which gives the full name of the implementing class.
 * </p>
 * 
 * <p>
 * Example:<br/>
 * <code>org.mycompany.index.MyIndexFactory</code>
 * </p>
 * 
 * @author Johann Sorel (Geomatys)
 */
public final class OIndexes {

  private static Set<OIndexFactory> FACTORIES         = null;
  private static ClassLoader        orientClassLoader = OIndexes.class.getClassLoader();

  private OIndexes() {
  }

  /**
   * Cache a set of all factories. we do not use the service loader directly since it is not concurrent.
   * 
   * @return Set<OIndexFactory>
   */
  private static synchronized Set<OIndexFactory> getFactories() {
    if (FACTORIES == null) {

      final Iterator<OIndexFactory> ite = lookupProviderWithOrientClassLoader(OIndexFactory.class, orientClassLoader);

      final Set<OIndexFactory> factories = new HashSet<OIndexFactory>();
      while (ite.hasNext()) {
        factories.add(ite.next());
      }
      FACTORIES = Collections.unmodifiableSet(factories);
    }
    return FACTORIES;
  }

  /**
   * @return Iterator of all index factories
   */
  public static Iterator<OIndexFactory> getAllFactories() {
    return getFactories().iterator();
  }

  /**
   * Iterates on all factories and append all index types.
   * 
   * @return Set of all index types.
   */
  public static Set<String> getIndexTypes() {
    final Set<String> types = new HashSet<String>();
    final Iterator<OIndexFactory> ite = getAllFactories();
    while (ite.hasNext()) {
      types.addAll(ite.next().getTypes());
    }
    return types;
  }

  /**
   * 
   * 
   * 
   * @param database
   * @param indexType
   *          index type
   * @param algorithm
   * @param valueContainerAlgorithm
   * @return OIndexInternal
   * @throws OConfigurationException
   *           if index creation failed
   * @throws OIndexException
   *           if index type does not exist
   */
  public static OIndexInternal<?> createIndex(ODatabaseRecord database, String indexType, String algorithm,
      String valueContainerAlgorithm) throws OConfigurationException, OIndexException {
    final Iterator<OIndexFactory> ite = getAllFactories();
    while (ite.hasNext()) {
      final OIndexFactory factory = ite.next();
      if (factory.getTypes().contains(indexType)) {
        return factory.createIndex(database, indexType, algorithm, valueContainerAlgorithm);
      }
    }

    throw new OIndexException("Index type : " + indexType + " is not supported. " + "Types are "
        + OCollections.toString(getIndexTypes()));
  }

  /**
   * Scans for factory plug-ins on the application class path. This method is needed because the application class path can
   * theoretically change, or additional plug-ins may become available. Rather than re-scanning the classpath on every invocation of
   * the API, the class path is scanned automatically only on the first invocation. Clients can call this method to prompt a
   * re-scan. Thus this method need only be invoked by sophisticated applications which dynamically make new plug-ins available at
   * runtime.
   */
  public static synchronized void scanForPlugins() {
    // clear cache, will cause a rescan on next getFactories call
    FACTORIES = null;
  }

}
