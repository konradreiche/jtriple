package berlin.reiche.jtriple;

import java.lang.reflect.Field;

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
     * Initializes a new binding by creating a RDF model with default
     * specification and Standard reification style.
     * 
     * @param namespace
     *            the default namespace to be used for the resources.
     */
    public Binding(String namespace) {
        this.model = ModelFactory.createDefaultModel();
        this.namespace = namespace;
    }

    /**
     * Initializes a new binding by using a given model.
     * 
     * @param model
     *            the model on which the binding will be performed.
     * @param namespace
     *            the default namespace to be used for the resources.
     */
    public Binding(Model model, String namespace) {
        this.model = model;
        this.namespace = namespace;
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
        String id = null;

        for (Field field : Util.getAllFields(cls)) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                id = field.get(object).toString();
                field.setAccessible(false);
                break;
            }
        }

        Resource resource = model.createResource(namespace + name + "/" + id);
        resource.addProperty(RDF.type, namespace + name);
    }

    public Model getModel() {
        return model;
    }

}
