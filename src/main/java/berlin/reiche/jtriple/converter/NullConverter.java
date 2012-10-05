package berlin.reiche.jtriple.converter;

import berlin.reiche.jtriple.Binding;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class NullConverter extends AbstractConverter {

    public NullConverter(Binding binding) {
        super(binding);
    }

    @Override
    public boolean canConvert(Class<?> type, Object object) {
        return object == null;
    }

    @Override
    public void convertEntity(Resource subject, Property predicate,
            Object object) {
        return;
    }

}
