package berlin.reiche.jtriple.converter;

import com.hp.hpl.jena.rdf.model.Property;
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

        HIGH(1), MEDIUM(2), LOW(3);

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
     * Converts the object to a RDF statement which is then immediately added to
     * the model.
     * 
     * @param subject
     *            the neighbor resource to which the object is added as a
     *            property.
     * @param predicate
     *            the property for the triple creation connecting both
     *            resources.
     * @param object
     *            the entity value to be converted.
     *            
     * @throws Exception
     */
    void convertEntity(Resource subject, Property predicate, Object object)
            throws Exception;

    /**
     * In order to determine the appropriate converter this method is used to
     * check it.
     * 
     * @param type
     *            the type of the field to be converted.
     * @return whether this converted can be applied to the field.
     */
    boolean canConvert(Class<?> type, Object object);

    int getPriority();

}
