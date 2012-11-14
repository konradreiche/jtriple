package berlin.reiche.jtriple.converter;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * A converter is specialized on a certain type or a set of types and defines
 * how the type or type set is converted to RDF.
 * 
 * @author Konrad Reiche
 * 
 */
public interface Converter {

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
	void convertEntity(Class<?> type, Object instance, Resource subject,
			Property predicate, Object object) throws Exception;

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
	boolean canConvert(Class<?> type, Object instance);

	/**
	 * Sets the successor converter to which the request is passed if this
	 * converter is not applicable.
	 * 
	 * @param succesor
	 *            the successor converter with the next higher priority.
	 */
	void setSuccessor(Converter succesor);

}
