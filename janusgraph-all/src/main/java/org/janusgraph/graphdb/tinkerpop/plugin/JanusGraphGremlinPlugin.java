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

package org.janusgraph.graphdb.tinkerpop.plugin;

import org.apache.tinkerpop.gremlin.jsr223.*;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.attribute.Geo;
import org.janusgraph.core.attribute.Text;
import org.janusgraph.example.GraphOfTheGodsFactory;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
//=======
import org.janusgraph.graphdb.management.ConfigurationManagementGraph;
//import org.apache.tinkerpop.gremlin.groovy.plugin.GremlinPlugin;
//import org.apache.tinkerpop.gremlin.groovy.plugin.PluginAcceptor;
//>>>>>>> b75fc6ce32f1184b99efa01a8768285bd83ce62d

//import org.apache.tinkerpop.gremlin.groovy.plugin.GremlinPlugin;
//import org.apache.tinkerpop.gremlin.groovy.plugin.PluginAcceptor;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class JanusGraphGremlinPlugin extends AbstractGremlinPlugin {

    private static final String IMPORT = "import ";
    private static final String IMPORT_STATIC = IMPORT + "static ";
//<<<<<<< HEAD
    private static final String DOT_STAR = ""; //".*";
    private static final String NAME = "janusgraph.imports";
    private static final Set<String> IMPORTS = new HashSet<String>() {
        {
            add(IMPORT + "org.janusgraph.core" + DOT_STAR);
            add(IMPORT + "org.janusgraph.core.attribute" + DOT_STAR);
            add(IMPORT + "org.janusgraph.core.schema" + DOT_STAR);
            add(IMPORT + "org.janusgraph.hadoop.MapReduceIndexManagement");
            add(IMPORT + "java.time" + DOT_STAR);
            add(IMPORT + JanusGraphIoRegistry.class.getName());
            add(IMPORT + ConfigurationManagementGraph.class.getName());
            add(IMPORT + GraphOfTheGodsFactory.class.getName());
        }};
    private static final Set<Class> STATIC_IMPORTS = new HashSet<Class>() {{
//=======
    //private static final String DOT_STAR = ".*";
//
    //private static final Set<String> IMPORTS = new HashSet<String>() {{
        //add(IMPORT + "org.janusgraph.core" + DOT_STAR);
        //add(IMPORT + "org.janusgraph.core.attribute" + DOT_STAR);
        //add(IMPORT + "org.janusgraph.core.schema" + DOT_STAR);
        //add(IMPORT + GraphOfTheGodsFactory.class.getName());
        //add(IMPORT + "org.janusgraph.hadoop.MapReduceIndexManagement");
        //add(IMPORT + "java.time" + DOT_STAR);
        //add(IMPORT + JanusGraphIoRegistry.class.getName());
        //add(IMPORT + ConfigurationManagementGraph.class.getName());
//>>>>>>> b75fc6ce32f1184b99efa01a8768285bd83ce62d

        // Static imports on enum values used in query constraint expressions
        add(Geo.class);
        add(Text.class);
        add(Multiplicity.class);
        add(Cardinality.class);
        add(ChronoUnit.class);

    }};

//    private static final BindingsCustomizer bindings;

    private static  ImportCustomizer imports = null;

    private static final Set<String> appliesTo = Collections.emptySet();

    private static List<Class> getClassesForPackage(String pckgname) throws ClassNotFoundException {
        // This will hold a list of directories matching the pckgname. There may be more than one if a package is split over multiple jars/paths
        ArrayList<File> directories = new ArrayList<File>();
        String packageToPath = pckgname.replace('.', '/');
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }

            // Ask for all resources for the packageToPath
            Enumeration<URL> resources = cld.getResources(packageToPath);
            while (resources.hasMoreElements()) {
                directories.add(new File(URLDecoder.decode(resources.nextElement().getPath(), "UTF-8")));
            }
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Null pointer exception)");
        } catch (UnsupportedEncodingException encex) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Unsupported encoding)");
        } catch (IOException ioex) {
            throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname);
        }

        ArrayList<Class> classes = new ArrayList<Class>();
        // For every directoryFile identified capture all the .class files
        while (!directories.isEmpty()){
            File directoryFile  = directories.remove(0);
            if (directoryFile.exists()) {
                // Get the list of the files contained in the package
                File[] files = directoryFile.listFiles();

                for (File file : files) {
                    // we are only interested in .class files
                    if ((file.getName().endsWith(".class")) && (!file.getName().contains("$"))) {
                        // removes the .class extension
                        int index = directoryFile.getPath().indexOf(packageToPath);
                        String packagePrefix = directoryFile.getPath().substring(index).replace('/', '.');;
                        try {
                            String className = packagePrefix + '.' + file.getName().substring(0, file.getName().length() - 6);
                            classes.add(Class.forName(className));
                        } catch (NoClassDefFoundError e)
                        {
                            // do nothing. this class hasn't been found by the loader, and we don't care.
                        }
                    } else if (file.isDirectory()){ // If we got to a subdirectory
                        directories.add(new File(file.getPath()));
                    }
                }
            } else {
                throw new ClassNotFoundException(pckgname + " (" + directoryFile.getPath() + ") does not appear to be a valid package");
            }
        }
        return classes;
    }

    private static List<Method> getStaticMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                methods.add(method);
            }
        }
        return Collections.unmodifiableList(methods);
    }


    private static Customizer[] customizers = null;

    static {
        try {
            DefaultImportCustomizer.Builder importBuilder = DefaultImportCustomizer.build();

            for (String pkgStr: IMPORTS){
                List<Class> classes = getClassesForPackage(pkgStr);
                importBuilder.addClassImports(classes);
            }

            for (Class cls : STATIC_IMPORTS){
                List <Method> methods = getStaticMethods(cls);
                importBuilder.addMethodImports(methods);
            }


            imports = importBuilder.create();

//                .addClassImports(
//                    org.janusgraph.core.Multiplicity.class,
//                    org.janusgraph.core.Cardinality.class,
//                    org.janusgraph.core.JanusGraph.class,
//                    org.janusgraph.core.Multiplicity.class,
//                    org.janusgraph.core.JanusGraphVertex.class,
//                    org.janusgraph.core.PropertyKey.class,
//                    org.janusgraph.core.JanusGraphElement.class,
//                    org.janusgraph.core.JanusGraphRelation.class,
//                    org.janusgraph.core.JanusGraphEdge.class,
//                    org.janusgraph.core.RelationType.class,
//                    org.janusgraph.core.JanusGraphVertexProperty.class,
//                    org.janusgraph.core.JanusGraphTransaction.class,
//                    org.janusgraph.core.JanusGraphException.class,
//                    org.janusgraph.core.EdgeLabel.class,
//                    org.janusgraph.core.VertexLabel.class,
//                    org.janusgraph.core.JanusGraphFactory.class,
//                    org.janusgraph.core.SchemaViolationException.class,
//                    org.janusgraph.core.VertexList.class,
//                    org.janusgraph.core.Transaction.class,
//                    org.janusgraph.core.TransactionBuilder.class,
//                    org.janusgraph.core.JanusGraphComputer.class,
//                    org.janusgraph.core.JanusGraphMultiVertexQuery.class,
//                    org.janusgraph.core.JanusGraphQuery.class,
//                    org.janusgraph.core.JanusGraphVertexQuery.class,
//                    org.janusgraph.core.JanusGraphIndexQuery.class,
//                    org.janusgraph.core.BaseVertexQuery.class,
//                    org.janusgraph.core.JanusGraphConfigurationException.class,
//                    org.janusgraph.core.JanusGraphProperty.class,
//                    org.janusgraph.core.Namifiable.class,
//                    org.janusgraph.core.Idfiable.class,
//                    org.janusgraph.core.InvalidElementException.class,
//                    org.janusgraph.core.ReadOnlyTransactionException.class,
//                    org.janusgraph.core.InvalidIDException.class,
//                    org.janusgraph.core.QueryException.class,
//                    org.janusgraph.core.QueryDescription.class,
//                    org.janusgraph.core.attribute.Geoshape.class,
//                    org.janusgraph.core.attribute.AttributeSerializer.class,
//                    org.janusgraph.core.attribute.JtsGeoshapeHelper.class,
//                    org.janusgraph.core.attribute.GeoshapeHelper.class,
//                    org.janusgraph.core.attribute.Geo.class,
//                    org.janusgraph.core.attribute.Text.class,
//                    org.janusgraph.core.attribute.Cmp.class,
//                    org.janusgraph.core.attribute.Contain.class).create();


            customizers = new Customizer[]{imports};

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public JanusGraphGremlinPlugin() {
        super(NAME, imports);
    }


    @Override
    public String getName() {
        return NAME;
    }

//    @Override
//    public void pluginTo(final PluginAcceptor pluginAcceptor) {
//        pluginAcceptor.addImports(IMPORTS);
//    }

    @Override
    public boolean requireRestart() {
        return true;
    }

    @Override
    public Optional<Customizer[]> getCustomizers(final String scriptEngineName) {
        return null == scriptEngineName || appliesTo.isEmpty() || appliesTo.contains(scriptEngineName) ?
            Optional.of(customizers) : Optional.empty();
    }


}
