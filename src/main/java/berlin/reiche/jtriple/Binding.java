package berlin.reiche.jtriple;

import static berlin.reiche.jtriple.converter.Converter.Priority.MEDIUM;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;

import berlin.reiche.jtriple.converter.Converter;
import berlin.reiche.jtriple.converter.SimpleConverter;
import berlin.reiche.jtriple.rdf.Id;

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
    private final PriorityQueue<Converter> converters;

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
        this.converters = new PriorityQueue<>();
        this.converters.add(new SimpleConverter(MEDIUM.value(), model, namespace));
    }

    /**
     * Binds an object to the RDF model.
     * 
     * @param object
     *            the object which will be bind.
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public void bind(Object object) throws IllegalArgumentException,
            IllegalAccessException {

        Class<?> cls = object.getClass();
        String name = cls.getSimpleName();
        String id = getId(object).toString();

        Resource resource = model.createResource(namespace + name + "/" + id);
        resource.addProperty(RDF.type, namespace + name);

        Deque<Field> fields = new ArrayDeque<>(Util.getAllFields(cls));
        Field field = null;
        while ((field = fields.pollFirst()) != null) {
            field.setAccessible(true);
            Object instance = field.get(object);
            field.setAccessible(false);
            
            Class<?> type = field.getType();
            Converter converter = determineConverter(type);            
            converter.convert(resource, field, instance);
        }

    }

    /**
     * In order to convert the field with the appropriate converter the given
     * class is checked against the available converter.
     * 
     * @return the corresponding converter for the given class.
     */
    private Converter determineConverter(Class<?> cls) {

        for (Converter converter : converters) {
            if (converter.canConvert(cls)) {
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
    public Object getId(Object object) throws IllegalArgumentException,
            IllegalAccessException {

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

}
