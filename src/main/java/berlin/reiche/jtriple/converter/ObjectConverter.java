package berlin.reiche.jtriple.converter;

import java.lang.reflect.Field;

import berlin.reiche.jtriple.Binding;
import berlin.reiche.jtriple.rdf.RdfProperty;

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
    public void convertField(Resource subject, Field predicate, Object object) throws Exception {
        
        Resource nextResource = binding.createNewResource(object, predicate.getType());
        binding.bind(object);
        
        String name = predicate.getName();
        String uri = binding.getNamespace() + name;
        if (predicate.isAnnotationPresent(RdfProperty.class)) {
            uri = predicate.getAnnotation(RdfProperty.class).value();
        }
        
        Property property = binding.getModel().createProperty(uri);
        subject.addProperty(property, nextResource);                 
    }

    
    @Override
    public boolean canConvert(Class<?> type, Object object) {
        return true;
    }
    
}
