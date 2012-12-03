package berlin.reiche.jtriple;

import static berlin.reiche.jtriple.example.Philosopher.NAMESPACE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import berlin.reiche.jtriple.converter.CollectionConverter;
import berlin.reiche.jtriple.converter.Converter;
import berlin.reiche.jtriple.converter.EnumConverter;
import berlin.reiche.jtriple.converter.NullConverter;
import berlin.reiche.jtriple.converter.ObjectConverter;
import berlin.reiche.jtriple.converter.SimpleConverter;
import berlin.reiche.jtriple.example.Branch;

public class ConverterTest {

	@Test
	public void testConverterApplicability() {

		Binding binding = new Binding(NAMESPACE);
		Converter nullConverter = new NullConverter(binding);
		Converter collectionConverter = new CollectionConverter(binding);
		Converter enumConverter = new EnumConverter(binding);
		Converter simpleConverter = new SimpleConverter(binding);
		Converter objectConverter = new ObjectConverter(binding);

		// Test NullConverter
		assertTrue(nullConverter.canConvert(null, null));
		assertFalse(nullConverter.canConvert(null, 42));

		// Test CollectionConverter
		assertTrue(collectionConverter.canConvert(Collection.class,
				new ArrayList<>()));
		assertFalse(collectionConverter.canConvert(Integer.class,
				new ArrayList<>()));
		assertFalse(collectionConverter.canConvert(Collection.class,
				new Integer(5)));

		// Test EnumConverter
		assertTrue(enumConverter.canConvert(Branch.class, Branch.EPISTEMOLOGY));
		assertFalse(enumConverter.canConvert(Branch.class, 42));
		assertFalse(enumConverter
				.canConvert(Integer.class, Branch.EPISTEMOLOGY));

		// Test SimpleConverter
		assertTrue(simpleConverter.canConvert(Integer.class, 42));
		assertFalse(simpleConverter.canConvert(Collection.class, 42));
		assertFalse(simpleConverter
				.canConvert(Integer.class, new ArrayList<>()));

		// Test ObjectConverter
		assertTrue(objectConverter.canConvert(Object.class, new Object()));
		assertFalse(objectConverter.canConvert(Object.class, null));

	}

}
