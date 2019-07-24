// Copyright 2017 JanusGraph Authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.janusgraph.diskstorage.es.compat;

import com.google.common.collect.ImmutableMap;
import org.janusgraph.diskstorage.indexing.IndexFeatures;

import java.util.Map;

import static org.janusgraph.diskstorage.es.ElasticSearchConstants.*;

/**
 * Mapping and query object builder for Elasticsearch 6.x.
 */
// LPPM - changed inheritance from AbsbractESCompat to ES5 compat, as many of the 5.x changes are also relevant to 6.x.
public class ES6Compat extends ES5Compat {

    private static final IndexFeatures FEATURES = coreFeatures().setWildcardField(CUSTOM_ALL_FIELD).supportsGeoContains().build();

    @Override
    public IndexFeatures getIndexFeatures() {
        return FEATURES;
    }


}
