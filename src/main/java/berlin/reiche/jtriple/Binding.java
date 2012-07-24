package berlin.reiche.jtriple;

import static berlin.reiche.jtriple.converter.Converter.Priority.HIGH;
import static berlin.reiche.jtriple.converter.Converter.Priority.LOW;
import static berlin.reiche.jtriple.converter.Converter.Priority.MEDIUM;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import berlin.reiche.jtriple.converter.Converter;
import berlin.reiche.jtriple.converter.NullConverter;
import berlin.reiche.jtriple.converter.ObjectConverter;
import berlin.reiche.jtriple.converter.SimpleConverter;
import berlin.reiche.jtriple.rdf.Id;
import berlin.reiche.jtriple.rdf.Transient;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
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

        Class<?> type = individual.getClass();
        Resource resource = createNewResource(individual, type);

        for (Field field : Util.getAllFields(type)) {
            
            if (field.isAnnotationPresent(Transient.class))
                continue;

            field.setAccessible(true);
            Object fieldObject = field.get(individual);
            field.setAccessible(false);

            Class<?> fieldType = field.getType();
            Converter converter = determineConverter(fieldType, fieldObject);
            converter.convertField(resource, field, fieldObject);
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
    private Converter determineConverter(Class<?> cls, Object object) {

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

            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                id = field.get(object);
                field.setAccessible(false);
                break;
            }
        }
        return id;
    }

    public Model getModel() {
        return model;
    }

    public String getNamespace() {
        return namespace;
    }

}
