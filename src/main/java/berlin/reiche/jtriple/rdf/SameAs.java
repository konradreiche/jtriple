package berlin.reiche.jtriple.rdf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides a flexible way to add semantic links between resources by using the
 * owl:sameAs property.
 * 
 * @author Konrad ReicheS
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface SameAs {

	/**
	 * @return the set of URIs specifying the sameAs relationships.
	 */
	String[] value();

}
