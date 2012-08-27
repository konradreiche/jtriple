package berlin.reiche.jtriple.converter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import berlin.reiche.jtriple.Binding;

import com.hp.hpl.jena.rdf.model.Literal;
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
     * Default constructor that also adds all supported simple types to a set
     * for subsequent checks.
     * 
     * @param priority
     *            the priority of this converter.
     */
    public SimpleConverter(int priority, Binding binding) {
        super(priority, binding);
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
     * @see berlin.reiche.jtriple.converter.Converter#convertEntity(com.hp.hpl.jena.rdf.model.Resource,
     *      java.lang.reflect.Field, java.lang.Object)
     */
    @Override
    public void convertEntity(Resource resource, Property predicate,
            Object object) {

        if (object.getClass() == String.class) {
            object = object.toString().replace("\"", "'");
        }

        if (object instanceof Date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) object);
            object = calendar;
        }

        Literal literal = binding.getModel().createTypedLiteral(object);
        resource.addProperty(predicate, literal);
    }

    /**
     * Defines rules when a field is a simple type. For the RDF model it means
     * that every non-simple type will be structured under an empty node in RDF.
     * 
     * @return whether a given field is a simple type.
     * @see berlin.reiche.jtriple.converter.Converter#canConvert(java.lang.Class)
     */
    @Override
    public boolean canConvert(Class<?> type, Object object) {
        return type.isPrimitive() || simpleTypes.contains(type);
    }

}
