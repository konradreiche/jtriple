package berlin.reiche.jtriple.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import berlin.reiche.jtriple.Binding;
import berlin.reiche.jtriple.Util;
import berlin.reiche.jtriple.rdf.IdentifierException;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * A specific converter for plain old Java objects.
 * 
 * @author Konrad Reiche
 * 
 */
public class ObjectConverter extends AbstractConverter {

	/**
	 * Logger object to facilitate logging in this class.
	 */
	private final Logger logger = LoggerFactory
			.getLogger(ObjectConverter.class);

	/**
	 * Default constructor.
	 * 
	 * @param binding
	 *            Binding object for recursive conversion invocations.
	 */
	public ObjectConverter(Binding binding) {
		super(binding);
	}

	/**
	 * If the object's fields are not throughout <code>null</code> it will be
	 * converted by a recursive conversion invocation.
	 * 
	 * @see berlin.reiche.jtriple.converter.Converter#convertEntity(com.hp.hpl.jena.rdf.model.Resource,
	 *      com.hp.hpl.jena.rdf.model.Property, java.lang.Object)
	 */
	@Override
	public void convertEntity(Resource subject, Property predicate,
			Object object) {

		try {

			if (!Util.isEmpty(object)) {
				Resource nextResource = null;
				nextResource = binding.createNewResource(object);
				binding.bind(object);
				subject.addProperty(predicate, nextResource);
			}

		} catch (IdentifierException e) {
			logger.error("Subject cannot be converted. Identifier "
					+ "could not be determined for {}", object);
			return;
		}
	}

	/**
	 * Since this is the most general type it is always convertible.
	 * 
	 * @see berlin.reiche.jtriple.converter.Converter#canConvert(java.lang.Class,
	 *      java.lang.Object)
	 */
	@Override
	public boolean canConvert(Class<?> type, Object object) {
		return object != null;
	}

}
