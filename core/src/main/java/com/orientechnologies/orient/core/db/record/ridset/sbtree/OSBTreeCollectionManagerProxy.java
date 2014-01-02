/*
 * Copyright 2010-2013 Luca Garulli (l.garulli(at)orientechnologies.com)
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

package com.orientechnologies.orient.core.db.record.ridset.sbtree;

import com.orientechnologies.orient.core.db.record.ODatabaseRecord;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.db.record.OProxedResource;
import com.orientechnologies.orient.core.index.sbtreebonsai.local.OBonsaiBucketPointer;
import com.orientechnologies.orient.core.index.sbtreebonsai.local.OSBTreeBonsai;

public class OSBTreeCollectionManagerProxy extends OProxedResource<OSBTreeCollectionManager> implements OSBTreeCollectionManager {
  public OSBTreeCollectionManagerProxy(ODatabaseRecord database, OSBTreeCollectionManager delegate) {
    super(delegate, database);
  }

  @Override
  public OSBTreeBonsai<OIdentifiable, Integer> createSBTree() {
    return delegate.createSBTree();
  }

  @Override
  public OSBTreeBonsai<OIdentifiable, Integer> loadSBTree(OBonsaiBucketPointer rootIndex) {
    return delegate.loadSBTree(rootIndex);
  }

  @Override
  public void releaseSBTree(OBonsaiBucketPointer rootIndex) {
    delegate.releaseSBTree(rootIndex);
  }
}