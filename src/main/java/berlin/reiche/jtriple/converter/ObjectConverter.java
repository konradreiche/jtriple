package berlin.reiche.jtriple.converter;

import java.lang.reflect.Field;

import berlin.reiche.jtriple.Binding;
import berlin.reiche.jtriple.Util;
import berlin.reiche.jtriple.rdf.Id;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class ObjectConverter extends AbstractConverter {

    /**
     * Default constructor.
     * 
     * @param priority
     *            the priority of this converter.
     */
    public ObjectConverter(int priority, Binding binding) {
        super(priority, binding);
    }

    @Override
    public void convertEntity(Resource subject, Property predicate,
            Object object) throws Exception {

        if (!isEmpty(object)) {
            Resource nextResource = binding.createNewResource(object,
                    object.getClass());
            binding.bind(object);
            subject.addProperty(predicate, nextResource);
        }
    }

    /**
     * Sometimes the fields of an object are throughout <code>null</code>. If
     * this is the case the object does not hold any information for the
     * resource it should be associated with. This method checks whether this is
     * the case.
     * 
     * @param object
     *            the object to be checked for <code>null</code> fields.
     * @return whether the object's fields are throughout <code>null</code>
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private boolean isEmpty(Object object) throws IllegalArgumentException,
            IllegalAccessException {

        for (Field field : Util.getAllFields(object.getClass())) {

            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(object);
            field.setAccessible(false);
            if (value != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canConvert(Class<?> type, Object object) {
        return true;
    }

}
