package berlin.reiche.jtriple.converter;

import java.lang.reflect.Field;

import berlin.reiche.jtriple.Binding;
import berlin.reiche.jtriple.rdf.SameAs;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;

public class EnumConverter extends AbstractConverter {

    public EnumConverter(int priority, Binding binding) {
        super(priority, binding);
    }

    @Override
    public void convertEntity(Resource subject, Property predicate,
            Object object) throws Exception {

        Resource newResource = binding.createNewResource(object);
        subject.addProperty(predicate, newResource);

        Field field = object.getClass().getField(((Enum<?>) object).name());
        if (field.isAnnotationPresent(SameAs.class)) {
            for (String uri : field.getAnnotation(SameAs.class).value()) {
            	Resource sameAs = binding.getModel().createResource(uri);
                newResource.addProperty(OWL.sameAs, sameAs);
            }

        }

    }

    @Override
    public boolean canConvert(Class<?> type, Object object) {
        return type.isEnum();
    }

}
