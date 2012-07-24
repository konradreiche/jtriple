package berlin.reiche.jtriple.converter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import berlin.reiche.jtriple.rdf.Id;
import berlin.reiche.jtriple.rdf.RdfProperty;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Simple converter is for primitive types, their corresponding wrapper classes
 * and for {@link String}.
 * 
 * @author Konrad Reiche
 * 
 */
public class SimpleConverter extends AbstractConverter {

    /**
     * Set of simple types which can be mapped directly as RDF properties.
     */
    private final Set<Class<?>> simpleTypes;

    /**
     * The Apache Jena RDF model.
     */
    private final Model model;

    /**
     * The default namespace.
     */
    private final String namespace;

    /**
     * Default constructor that also adds all supported simple types to a set
     * for subsequent checks.
     * 
     * @param priority
     *            the priority of this converter.
     * @param model
     *            the Apache Jena RDF model.
     * @param namespace
     *            the default namespace.
     */
    public SimpleConverter(int priority, Model model, String namespace) {
        super(priority);
        this.model = model;
        this.namespace = namespace;

        simpleTypes = new HashSet<>();
        simpleTypes.add(BigDecimal.class);
        simpleTypes.add(Boolean.class);
        simpleTypes.add(Date.class);
        simpleTypes.add(Double.class);
        simpleTypes.add(Integer.class);
        simpleTypes.add(String.class);
    }

    /**
     * Simply creates a property out of the field and creates the triple out of
     * the given parent resource, the property and the field value as literal.
     * 
     * @see berlin.reiche.jtriple.converter.Converter#convert(com.hp.hpl.jena.rdf.model.Resource,
     *      java.lang.reflect.Field, java.lang.Object)
     */
    @Override
    public void convert(Resource resource, Field field, Object object) {

        if (field.isAnnotationPresent(Id.class)) {
            return;
        }
        
        String name = field.getName();
        String uri = namespace + name;
        if (field.isAnnotationPresent(RdfProperty.class)) {
            uri = field.getAnnotation(RdfProperty.class).value();
        }

        Property property = model.createProperty(uri);
        resource.addProperty(property, object.toString());
    }

    /**
     * Defines rules when a field is a simple type. For the RDF model it means
     * that every non-simple type will be structured under an empty node in RDF.
     * 
     * @return whether a given field is a simple type.
     * @see berlin.reiche.jtriple.converter.Converter#canConvert(java.lang.Class)
     */
    @Override
    public boolean canConvert(Class<?> type) {
        return type.isPrimitive() || type.isEnum()
                || simpleTypes.contains(type);
    }

}
