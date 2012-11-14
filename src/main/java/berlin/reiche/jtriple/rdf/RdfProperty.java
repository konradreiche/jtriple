package berlin.reiche.jtriple.rdf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides additional namespace information for a field or method.
 * 
 * @author Konrad Reiche
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface RdfProperty {

    /**
     * @return the URI of the property.
     */
    String value();
}
