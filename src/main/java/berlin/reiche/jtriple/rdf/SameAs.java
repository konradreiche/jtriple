package berlin.reiche.jtriple.rdf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Provides a flexible way to add semantic links between resources by using the
 * owl:sameAs property.
 * 
 * @author Konrad ReicheS
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SameAs {

    String[] value();
    
}
