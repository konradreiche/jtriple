package berlin.reiche.jtriple.rdf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies whether a resource is an instance of a certain class by making use
 * of the rdf:type property.
 * 
 * @author Konrad Reiche
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RdfType {

	/**
	 * @return the URI of the class.
	 */
	String value();

}
