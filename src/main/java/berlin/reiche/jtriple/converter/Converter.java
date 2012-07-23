package berlin.reiche.jtriple.converter;

import java.lang.reflect.Field;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * A converter is specialized on a certain type or a type set and defines how
 * the type or type set is converted to RDF.
 * 
 * @author Konrad Reiche
 * 
 */
public interface Converter extends Comparable<Converter> {

    /**
     * Predefined priorities.
     */
    enum Priority {

        LOW(1), MEDIUM(50), HIGH(100);

        /**
         * The priority value.
         */
        private final int value;

        /**
         * Default constructor.
         * 
         * @param value
         *            of the priority.
         */
        Priority(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

    }

    /**
     * Converts the object to RDF statements which are immediately added to the
     * model.
     * 
     * @param resource
     *            the parent resource to which the field value is added as a
     *            property.
     * @param field
     *            the field to be converted.
     * @param object
     *            the field instance to be converted.
     */
    void convert(Resource resource, Field field, Object object);

    /**
     * In order to determine the appropriate converter this method is used to
     * check it.
     * 
     * @param type
     *            the type of the field to be converted.
     * @return whether this converted can be applied to the field.
     */
    boolean canConvert(Class<?> type);

    int getPriority();

}
