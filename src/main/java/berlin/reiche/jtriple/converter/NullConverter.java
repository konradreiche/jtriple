package berlin.reiche.jtriple.converter;

import berlin.reiche.jtriple.Binding;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * A specific converter for the <code>null</code> type.
 * 
 * @author Konrad Reiche
 * 
 */
public class NullConverter extends AbstractConverter {

	/**
	 * Default constructor.
	 * 
	 * @param binding
	 *            Binding object for recursive conversion invocations.
	 */
	public NullConverter(Binding binding) {
		super(binding);
	}

	/**
	 * Converts the passed <code>null</code> reference by doing nothing.
	 * 
	 * @see berlin.reiche.jtriple.converter.Converter#convertEntity(com.hp.hpl.jena.rdf.model.Resource,
	 *      com.hp.hpl.jena.rdf.model.Property, java.lang.Object)
	 */
	@Override
	public void convertEntity(Resource subject, Property predicate,
			Object object) {
		return;
	}

	/**
	 * The object is convertible if its reference equals <code>null</code>.
	 * 
	 * @see berlin.reiche.jtriple.converter.Converter#canConvert(java.lang.Class,
	 *      java.lang.Object)
	 */
	@Override
	public boolean canConvert(Class<?> type, Object object) {
		return object == null;
	}

}
