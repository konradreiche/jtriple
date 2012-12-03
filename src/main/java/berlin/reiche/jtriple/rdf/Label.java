package berlin.reiche.jtriple.rdf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A shortcut for declaring a field as a rdfs:label property. Using this
 * annotation is the same as using the {@link RdfProperty} with the value
 * http://www.w3.org/2000/01/rdf-schema#label.
 * 
 * @author Konrad Reiche
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Label {

	public final static String uri = "http://www.w3.org/2000/01/rdf-schema#label";
	
}
