package berlin.reiche.jtriple.rdf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that the field should not be bound to a property.
 * 
 * @author Konrad Reiche
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Transient {

}
