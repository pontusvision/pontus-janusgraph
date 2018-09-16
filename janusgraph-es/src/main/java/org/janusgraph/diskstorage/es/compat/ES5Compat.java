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
 * Mapping and query object builder for Elasticsearch 5.x.
 */
public class ES5Compat extends AbstractESCompat {

    private static final IndexFeatures FEATURES = coreFeatures().supportsGeoContains().build();

    private static final Boolean useParametersInScripts = Boolean.parseBoolean(System.getProperty("index.elastic.use_params", "false"));

    @Override
    public IndexFeatures getIndexFeatures() {
        return FEATURES;
    }

    // LPPM added new entries to check whether a script with parameters can be sent (this reduces the burden on Elastic as new updates don't have to be compiled each time)
    @Override
    public boolean scriptWithParameters()
    {
        return useParametersInScripts;
    }

    public ImmutableMap.Builder prepareScript(String inline) {
        final Map<String, String> script = ImmutableMap.of(ES_SOURCE_KEY, inline, ES_LANG_KEY, scriptLang());
        return ImmutableMap.builder().put(ES_SCRIPT_KEY, script);
    }

}
