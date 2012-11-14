package berlin.reiche.jtriple.converter;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import berlin.reiche.jtriple.Binding;

/**
 * Provides an initial implementation of the {@link Converter} interface by
 * providing constructor and {@link Comparable} implementation in order to
 * enforce the prioritization.
 * 
 * @author Konrad Reiche
 * 
 */
public abstract class AbstractConverter implements Converter {

	/**
	 * Binding object for recursive conversion invocations.
	 */
	Binding binding;

	/**
	 * The follow-up converter to be use if this converter is not applicable.
	 */
	Converter succesor;

	/**
	 * Default constructor.
	 * 
	 * @param priority
	 *            the priority for this converter.
	 */
	public AbstractConverter(Binding binding) {
		this.binding = binding;
	}

	/**
	 * Default implementation for entity conversion. Checks whether the entity
	 * type is eligible for the subclassing {@link Converter}. If the converter
	 * is eligible convert the entity otherwise pass the request to the
	 * successor.
	 * 
	 * @see berlin.reiche.jtriple.converter.Converter#convertEntity(java.lang.Class,
	 *      java.lang.Object, com.hp.hpl.jena.rdf.model.Resource,
	 *      com.hp.hpl.jena.rdf.model.Property, java.lang.Object)
	 */
	public void convertEntity(Class<?> type, Object instance, Resource subject,
			Property predicate, Object object) throws Exception {

		if (canConvert(type, instance)) {
			convertEntity(subject, predicate, object);
		} else {
			succesor.convertEntity(type, instance, subject, predicate, object);
		}
	}

	/**
	 * @see berlin.reiche.jtriple.converter.Converter#setSuccessor(berlin.reiche.jtriple.converter.Converter)
	 */
	@Override
	public void setSuccessor(Converter succesor) {
		this.succesor = succesor;
	}

}
