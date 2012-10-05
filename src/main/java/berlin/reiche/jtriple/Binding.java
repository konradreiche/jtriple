package berlin.reiche.jtriple;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import berlin.reiche.jtriple.converter.CollectionConverter;
import berlin.reiche.jtriple.converter.Converter;
import berlin.reiche.jtriple.converter.EnumConverter;
import berlin.reiche.jtriple.converter.NullConverter;
import berlin.reiche.jtriple.converter.ObjectConverter;
import berlin.reiche.jtriple.converter.SimpleConverter;
import berlin.reiche.jtriple.rdf.RdfIdentifier;
import berlin.reiche.jtriple.rdf.RdfProperty;
import berlin.reiche.jtriple.rdf.RdfType;
import berlin.reiche.jtriple.rdf.Transient;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Represents the binding for the Java object model to RDF.
 * 
 * @author Konrad Reiche
 * 
 */
public class Binding {

    /**
     * The Apache Jena RDF model.
     */
    private final Model model;

    /**
     * The default namespace.
     */
    private final String namespace;

    /**
     * A base converter which starts the conversion procedure.
     */
    private final Converter converter;

    /**
     * Initializes a new binding by creating a RDF model with default
     * specification and Standard reification style.
     * 
     * @param namespace
     *            the default namespace to be used for the resources.
     */
    public Binding(String namespace) {
        this.model = ModelFactory.createDefaultModel();
        this.namespace = namespace;

        Converter nullConverter = new NullConverter(this);
        Converter collectionConverter = new CollectionConverter(this);
        Converter enumConverter = new EnumConverter(this);
        Converter simpleConverter = new SimpleConverter(this);
        Converter objectConverter = new ObjectConverter(this);

        nullConverter.setSuccessor(collectionConverter);
        collectionConverter.setSuccessor(enumConverter);
        enumConverter.setSuccessor(simpleConverter);
        simpleConverter.setSuccessor(objectConverter);

        this.converter = nullConverter;
    }

    /**
     * Binds an object to the RDF model.
     * 
     * @param individual
     *            the object which will be bind.
     * @throws Exception
     */
    public void bind(Object individual) throws Exception {

        if (Util.isEmpty(individual)) {
            return;
        }

        Class<?> type = individual.getClass();
        Resource resource = createNewResource(individual);

        for (Field field : Util.getAllFields(type)) {

            if (field.isAnnotationPresent(Transient.class)
                    || field.isAnnotationPresent(RdfIdentifier.class)
                    || field.isEnumConstant()
                    || field.getDeclaringClass() == Enum.class)
                continue;

            field.setAccessible(true);
            Object fieldValue = field.get(individual);
            field.setAccessible(false);

            String name = field.getName();
            String uri = getNamespace() + name;
            if (field.isAnnotationPresent(RdfProperty.class)) {
                uri = field.getAnnotation(RdfProperty.class).value();
                if (!uri.startsWith("http://")) {
                    uri = namespace + uri;
                }
            }

            Property property = model.createProperty(uri);
            Class<?> fieldType = field.getType();
            converter.convertEntity(fieldType, fieldValue, resource, property,
                    fieldValue);
        }

        for (Method method : Util.getAllMethods(type)) {

            if (method.isAnnotationPresent(RdfProperty.class)) {
                String uri = method.getAnnotation(RdfProperty.class).value();
                if (!uri.startsWith("http://")) {
                    uri = namespace + uri;
                }

                method.setAccessible(true);
                Object methodValue = method.invoke(individual);
                method.setAccessible(false);
                Property property = model.createProperty(uri);
                converter.convertEntity(method.getReturnType(), methodValue,
                        resource, property, methodValue);
            }

        }

    }

    /**
     * Each object which is not a simple type with respect to the
     * {@link SimpleConverter} and was not bound to the model already needs two
     * resources. One resource describes the type of the resource and one
     * resource represents the object itself.
     * 
     * If the object was already bound to the model Jena will return the
     * existing resources with the correct URI and model.
     * 
     * @param object
     *            the object to be bound to the model.
     * @return the resource representing the object.
     */
    public Resource createNewResource(Object object) throws Exception {

        Class<?> type = object.getClass();
        String typeName = type.getSimpleName();
        String id = getId(object).toString();

        String typeUri = namespace + typeName;
        if (type.isAnnotationPresent(RdfType.class)) {
            typeUri = type.getAnnotation(RdfType.class).value();
        }

        String individualUri = namespace + typeName;
        if (typeUri.endsWith("#")) {
            individualUri += id;
        } else {
            individualUri += "/" + id;
        }

        Resource rdfType = model.createResource(typeUri);
        Resource resource = model.createResource(individualUri);
        resource.addProperty(RDF.type, rdfType);
        return resource;
    }

    /**
     * Retrieves the id of an object in order to construct an URI for the
     * resource.
     * 
     * @param object
     *            the object for which the id is requested.
     * @return the id.
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public Object getId(Object object) throws Exception {

        Object id = null;
        for (Field field : Util.getAllFields(object.getClass())) {
            if (field.isAnnotationPresent(RdfIdentifier.class)) {
                field.setAccessible(true);
                id = field.get(object);
                field.setAccessible(false);
                return id;
            }
        }

        for (Method method : Util.getAllMethods(object.getClass())) {
            if (method.isAnnotationPresent(RdfIdentifier.class)) {
                id = method.invoke(object);
                return id;
            }
        }

        if (object.getClass().isEnum()) {
            return object.toString();
        }

        throw new Exception("No identifier definined in " + object.getClass());
    }

    public Model getModel() {
        return model;
    }

    public String getNamespace() {
        return namespace;
    }

    public Converter getConverter() {
        return converter;
    }

}
