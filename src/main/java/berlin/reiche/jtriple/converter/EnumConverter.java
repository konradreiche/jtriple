package berlin.reiche.jtriple.converter;

import java.lang.reflect.Field;

import berlin.reiche.jtriple.Binding;
import berlin.reiche.jtriple.rdf.SameAs;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;

public class EnumConverter extends AbstractConverter {

    public EnumConverter(Binding binding) {
        super(binding);
    }

    @Override
    public void convertEntity(Resource subject, Property predicate,
            Object object) throws Exception {

        Class<?> type = object.getClass();
        Resource newResource = binding.createNewResource(object);
        subject.addProperty(predicate, newResource);

        Field enumField = type.getField(((Enum<?>) object).name());
        if (enumField.isAnnotationPresent(SameAs.class)) {
            for (String uri : enumField.getAnnotation(SameAs.class).value()) {
                Resource sameAs = binding.getModel().createResource(uri);
                newResource.addProperty(OWL.sameAs, sameAs);
            }
        }

        binding.bind(object);
    }

    @Override
    public boolean canConvert(Class<?> type, Object object) {
        return type.isEnum();
    }

}
