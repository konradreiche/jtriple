package berlin.reiche.jtriple.converter;

import java.util.Collection;

import berlin.reiche.jtriple.Binding;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * A specific converter for collection types implementing the {@link Collection}
 * interface.
 * 
 * @author Konrad Reiche
 * 
 */
public class CollectionConverter extends AbstractConverter {

	/**
	 * Default constructor.
	 * 
	 * @param binding
	 *            Binding object for recursive conversion invocations.
	 */
	public CollectionConverter(Binding binding) {
		super(binding);
	}

	/**
	 * Converts the collection entity by iterating its elements and making a
	 * recursive binding invocation with them.
	 * 
	 * @see berlin.reiche.jtriple.converter.Converter#convertEntity(com.hp.hpl.jena.rdf.model.Resource,
	 *      com.hp.hpl.jena.rdf.model.Property, java.lang.Object)
	 */
	@Override
	public void convertEntity(Resource subject, Property predicate,
			Object object) {

		Collection<?> collection = (Collection<?>) object;
		for (Object element : collection) {
			Class<?> cls = element.getClass();
			binding.getConverter().convertEntity(cls, element, subject,
					predicate, element);
		}
	}

	/**
	 * The object is convertible if it implements the {@link Collection}
	 * interface.
	 * 
	 * @see berlin.reiche.jtriple.converter.Converter#canConvert(java.lang.Class,
	 *      java.lang.Object)
	 */
	@Override
	public boolean canConvert(Class<?> type, Object object) {
		return Collection.class.isAssignableFrom(type);
	}

}
