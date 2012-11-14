package berlin.reiche.jtriple.converter;

import berlin.reiche.jtriple.Binding;
import berlin.reiche.jtriple.Util;

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
	 * Default constructor.
	 * 
	 * @param priority
	 *            the priority of this converter.
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
			Object object) throws Exception {

		if (!Util.isEmpty(object)) {
			Resource nextResource = binding.createNewResource(object);
			binding.bind(object);
			subject.addProperty(predicate, nextResource);
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
		return true;
	}

}
