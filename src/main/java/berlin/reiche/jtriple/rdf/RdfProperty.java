package berlin.reiche.jtriple.rdf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Provides namespace information for a field or method.
 * 
 * @author Konrad Reiche
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RdfProperty {

    String value();
}
