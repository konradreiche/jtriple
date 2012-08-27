package berlin.reiche.jtriple.rdf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for class
 * 
 * @author Konrad Reiche
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RdfType {

    String value();
    
}
