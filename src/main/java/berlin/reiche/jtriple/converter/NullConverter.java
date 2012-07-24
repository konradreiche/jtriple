package berlin.reiche.jtriple.converter;

import java.lang.reflect.Field;

import berlin.reiche.jtriple.Binding;

import com.hp.hpl.jena.rdf.model.Resource;

public class NullConverter extends AbstractConverter {

    public NullConverter(int priority, Binding binding) {
        super(priority, binding);
    }

    @Override
    public void convertField(Resource subject, Field predicate, Object object)
            throws Exception {
        return;
    }

    @Override
    public boolean canConvert(Class<?> type, Object object) {
        return object == null;
    }

}
