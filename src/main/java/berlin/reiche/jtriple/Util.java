package berlin.reiche.jtriple;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import berlin.reiche.jtriple.rdf.RdfIdentifier;
import berlin.reiche.jtriple.rdf.Transient;

/**
 * Utility methods for Java Reflection and other auxiliary functionality to
 * realize the binding.
 * 
 * @author Konrad Reiche
 * 
 */
public class Util {

	/**
	 * Returns a list of field objects reflecting all the fields declared by the
	 * class or interface represented by this class object as well as the fields
	 * of the super classes.
	 * 
	 * @param fields
	 *            the list of fields collected so far for the recursive method
	 *            invocation.
	 * @param cls
	 *            the class to be reflected.
	 * @return a list of all declared fields.
	 */
	private static List<Field> getAllFields(List<Field> fields, Class<?> cls) {
		for (Field field : cls.getDeclaredFields())
			fields.add(field);
		if (cls.getSuperclass() != null)
			fields = getAllFields(fields, cls.getSuperclass());
		return fields;
	}

	/**
	 * Returns a list of {@link Field} objects reflecting all the fields
	 * declared by the class or interface represented by this {@link Class}
	 * object as well as the fields of the super classes.
	 * 
	 * @param cls
	 *            the class to be reflected.
	 * @return a list of all declared fields.
	 */
	public static List<Field> getAllFields(Class<?> cls) {
		return getAllFields(new ArrayList<Field>(), cls);
	}

	/**
	 * Returns an list of {@code Method} objects reflecting all the methods
	 * declared by the class or interface represented by this {@code Class}
	 * object as well as the methods of the super classes.
	 * 
	 * @param methods
	 *            the list of methods collected so far for the recursive method
	 *            invocation.
	 * @param cls
	 *            the class to be reflected.
	 * @return a list of all declared methods.
	 */
	private static List<Method> getAllMethods(List<Method> methods, Class<?> cls) {
		for (Method method : cls.getDeclaredMethods())
			methods.add(method);
		if (cls.getSuperclass() != null)
			methods = getAllMethods(methods, cls.getSuperclass());
		return methods;
	}

	/**
	 * Returns an list of {@code Method} objects reflecting all the methods
	 * declared by the class or interface represented by this {@code Class}
	 * object as well as the methods of the super classes.
	 * 
	 * @param cls
	 *            the class to be reflected.
	 * @return a list of all declared methods.
	 */
	public static List<Method> getAllMethods(Class<?> cls) {
		return getAllMethods(new ArrayList<Method>(), cls);
	}

	/**
	 * Sometimes the fields of an object are throughout <code>null</code> or all
	 * fields are annotated as {@link Transient}. If this is the case the object
	 * does not hold any information for the resource it should be associated
	 * with. This method checks whether this is the case.
	 * 
	 * @param object
	 *            the object to be checked for <code>null</code> fields.
	 * @return whether the object's fields are throughout <code>null</code>
	 */
	public static boolean isEmpty(Object object) {

		Class<?> type = object.getClass();
		for (Field field : Util.getAllFields(type)) {

			if (field.isAnnotationPresent(RdfIdentifier.class)
					|| field.isAnnotationPresent(Transient.class)) {
				continue;
			}

			try {
				field.setAccessible(true);
				Object value = field.get(object);
				field.setAccessible(false);
				if (value != null) {
					return false;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return !type.isEnum();
	}

}
