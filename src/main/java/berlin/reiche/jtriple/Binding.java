package berlin.reiche.jtriple;

import static berlin.reiche.jtriple.converter.Converter.Priority.HIGH;
import static berlin.reiche.jtriple.converter.Converter.Priority.LOW;
import static berlin.reiche.jtriple.converter.Converter.Priority.MEDIUM;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import berlin.reiche.jtriple.converter.CollectionConverter;
import berlin.reiche.jtriple.converter.Converter;
import berlin.reiche.jtriple.converter.NullConverter;
import berlin.reiche.jtriple.converter.ObjectConverter;
import berlin.reiche.jtriple.converter.SimpleConverter;
import berlin.reiche.jtriple.rdf.RdfIdentifier;
import berlin.reiche.jtriple.rdf.RdfProperty;
import berlin.reiche.jtriple.rdf.Transient;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Represents the binding for the Java object model to RDF.
 * 
 * @author Konrad Reiche
 * 
 */
public class Binding {

    /**
     * The Apache Jena RDF model.
     */
    private final Model model;

    /**
     * The default namespace.
     */
    private final String namespace;

    /**
     * Different converters specialized to convert certain types.
     */
    private final List<Converter> converters;

    /**
     * Initializes a new binding by creating a RDF model with default
     * specification and Standard reification style.
     * 
     * @param namespace
     *            the default namespace to be used for the resources.
     */
    public Binding(String namespace) {
        this.model = ModelFactory.createDefaultModel();
        this.namespace = namespace;
        this.converters = new ArrayList<>();
        this.converters.add(new ObjectConverter(LOW.value(), this));
        this.converters.add(new SimpleConverter(MEDIUM.value(), this));
        this.converters.add(new CollectionConverter(MEDIUM.value(), this));
        this.converters.add(new NullConverter(HIGH.value(), this));
        Collections.sort(converters);
    }

    /**
     * Binds an object to the RDF model.
     * 
     * @param individual
     *            the object which will be bind.
     * @throws Exception
     */
    public void bind(Object individual) throws Exception {

        if (Util.isEmpty(individual)) {
            return;
        }
        
        Class<?> type = individual.getClass();
        Resource resource = createNewResource(individual, type);

        for (Field field : Util.getAllFields(type)) {

            if (field.isAnnotationPresent(Transient.class)
                    || field.isAnnotationPresent(RdfIdentifier.class))
                continue;

            field.setAccessible(true);
            Object fieldValue = field.get(individual);
            field.setAccessible(false);

            String name = field.getName();
            String uri = getNamespace() + name;
            if (field.isAnnotationPresent(RdfProperty.class)) {
                uri = field.getAnnotation(RdfProperty.class).value();
            }

            Property property = model.createProperty(uri);
            Class<?> fieldType = field.getType();
            Converter converter = determineConverter(fieldType, fieldValue);
            converter.convertEntity(resource, property, fieldValue);
        }
    }

    public Resource createNewResource(Object object, Class<?> type)
            throws Exception {

        String name = type.getSimpleName();
        String id = getId(object).toString();

        Resource resource = model.createResource(namespace + name + "/" + id);
        resource.addProperty(RDF.type, namespace + name);
        return resource;
    }

    /**
     * In order to convert the field with the appropriate converter the given
     * class is checked against the available converter.
     * 
     * @return the corresponding converter for the given class.
     */
    public Converter determineConverter(Class<?> cls, Object object) {

        for (Converter converter : converters) {
            if (converter.canConvert(cls, object)) {
                return converter;
            }
        }

        return null;
    }

    /**
     * Retrieves the id of an object in order to construct an URI for the
     * resource.
     * 
     * @param object
     *            the object for which the id is requested.
     * @return the id.
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public Object getId(Object object) throws Exception {

        Object id = null;
        for (Field field : Util.getAllFields(object.getClass())) {
            if (field.isAnnotationPresent(RdfIdentifier.class)) {
                field.setAccessible(true);
                id = field.get(object);
                field.setAccessible(false);
                return id;
            }
        }
        
        for (Method method : Util.getAllMethods(object.getClass())) {
            if (method.isAnnotationPresent(RdfIdentifier.class)) {
                id = method.invoke(object);
                return id;
            }
        }
        
        throw new Exception("No identifier definined in " + object.getClass());
    }

    public Model getModel() {
        return model;
    }

    public String getNamespace() {
        return namespace;
    }

}
