package berlin.reiche.jtriple.converter;

import berlin.reiche.jtriple.Binding;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class EnumConverter extends AbstractConverter {

    public EnumConverter(int priority, Binding binding) {
        super(priority, binding);
    }

    @Override
    public void convertEntity(Resource subject, Property predicate,
            Object object) throws Exception {

        Resource newResource = binding.createNewResource(object);
        subject.addProperty(predicate, newResource);
    }

    @Override
    public boolean canConvert(Class<?> type, Object object) {
        return type.isEnum();
    }

}
