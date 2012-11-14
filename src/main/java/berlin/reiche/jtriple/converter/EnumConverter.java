package berlin.reiche.jtriple.converter;

import java.lang.reflect.Field;

import berlin.reiche.jtriple.Binding;
import berlin.reiche.jtriple.rdf.SameAs;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;

/**
 * A specific converter for {@link Enum} types.
 * 
 * @author Konrad Reiche
 * 
 */
public class EnumConverter extends AbstractConverter {

	/**
	 * Default constructor.
	 * 
	 * @param binding
	 *            Binding object for recursive conversion invocations.
	 */
	public EnumConverter(Binding binding) {
		super(binding);
	}

	/**
	 * Converts the Enum entity by creating a new resource and linking it to the
	 * subject resource with passed predicate.
	 * 
	 * The Enum constant is checked for the {@link SameAs} annotation. If it is
	 * present the new resource is associated with a owl:sameAs property.
	 * 
	 * @see berlin.reiche.jtriple.converter.Converter#convertEntity(com.hp.hpl.jena.rdf.model.Resource,
	 *      com.hp.hpl.jena.rdf.model.Property, java.lang.Object)
	 */
	@Override
	public void convertEntity(Resource subject, Property predicate,
			Object object) throws Exception {

		Class<?> type = object.getClass();
		Resource newResource = binding.createNewResource(object);
		subject.addProperty(predicate, newResource);

		Field enumField = type.getField(((Enum<?>) object).name());
		if (enumField.isAnnotationPresent(SameAs.class)) {
			for (String uri : enumField.getAnnotation(SameAs.class).value()) {
				Resource sameAs = binding.getModel().createResource(uri);
				newResource.addProperty(OWL.sameAs, sameAs);
			}
		}
		binding.bind(object);
	}

	/**
	 * 
	 * The object is convertible if it is an Enum type.
	 * 
	 * @see berlin.reiche.jtriple.converter.Converter#canConvert(java.lang.Class,
	 *      java.lang.Object)
	 */
	@Override
	public boolean canConvert(Class<?> type, Object object) {
		return type.isEnum();
	}

}
