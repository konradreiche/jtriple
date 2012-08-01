package berlin.reiche.jtriple.converter;

import berlin.reiche.jtriple.Binding;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class ObjectConverter extends AbstractConverter {

    /**
     * Default constructor.
     * 
     * @param priority
     *            the priority of this converter.
     */
    public ObjectConverter(int priority, Binding binding) {
        super(priority, binding);
    }

    @Override
    public void convertEntity(Resource subject, Property predicate, Object object) throws Exception {
        Resource nextResource = binding.createNewResource(object, object.getClass());
        binding.bind(object);
        subject.addProperty(predicate, nextResource);                 
    }

    
    @Override
    public boolean canConvert(Class<?> type, Object object) {
        return true;
    }
    
}
