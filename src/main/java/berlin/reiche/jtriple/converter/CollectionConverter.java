package berlin.reiche.jtriple.converter;

import java.util.Collection;

import berlin.reiche.jtriple.Binding;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class CollectionConverter extends AbstractConverter {

    public CollectionConverter(Binding binding) {
        super(binding);
    }

    @Override
    public void convertEntity(Resource subject, Property predicate,
            Object object) throws Exception {

        Collection<?> collection = (Collection<?>) object;
        for (Object element : collection) {
            Class<?> cls = element.getClass();
            binding.getConverter().convertEntity(cls, element, subject,
                    predicate, element);
        }
    }

    @Override
    public boolean canConvert(Class<?> type, Object object) {
        return Collection.class.isAssignableFrom(type);
    }

}
