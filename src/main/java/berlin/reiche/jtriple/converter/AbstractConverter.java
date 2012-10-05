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

    public void convertEntity(Class<?> type, Object instance, Resource subject,
            Property predicate, Object object) throws Exception {

        if (canConvert(type, instance)) {
            convertEntity(subject, predicate, object);
        } else {
            succesor.convertEntity(type, instance, subject, predicate, object);
        }
    }
    
    @Override
    public void setSuccessor(Converter succesor) {
        this.succesor = succesor;
    }

}
