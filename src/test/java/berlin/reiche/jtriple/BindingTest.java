package berlin.reiche.jtriple;

import static berlin.reiche.jtriple.example.Branch.EPISTEMOLOGY;
import static berlin.reiche.jtriple.example.Branch.METAPHYSISC;
import static berlin.reiche.jtriple.example.Branch.PHILOSOPHY_OF_MIND;
import static berlin.reiche.jtriple.example.Philosopher.NAMESPACE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import berlin.reiche.jtriple.example.Branch;
import berlin.reiche.jtriple.example.Dummy;
import berlin.reiche.jtriple.example.Philosopher;
import berlin.reiche.jtriple.rdf.IdentifierException;
import berlin.reiche.jtriple.rdf.Label;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Tests the whole binding procedure. This unit test is the most general test of
 * the library functionality.
 * 
 * @author Konrad Reiche
 * 
 */
public class BindingTest {

	Binding binding;

	Model model;

	@Before
	public void setUp() {
		binding = new Binding(NAMESPACE);
		model = binding.getModel();
		model.setNsPrefix("philosophy", NAMESPACE);
		model.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		model.setNsPrefix("xfoaf", "http://www.foafrealm.org/xfoaf/0.1/");
		model.setNsPrefix("dbpedia", "http://dbpedia.org/resource/");
	}

	@Test
	public void testMinimumRequirements() {

		Dummy dummy = new Dummy();
		dummy.setValue(42);
		Binding binding = new Binding("http://konrad-reiche.com/dummy");

		try {
			binding.createNewResource(dummy);
		} catch (IdentifierException e) {
			return;
		}

		fail("No " + IdentifierException.class.getSimpleName() + "was thrown.");
	}

	@Test
	public void testBind() {

		Philosopher locke = new Philosopher();
		locke.setName("John Locke");
		locke.setNationality("English");

		List<Branch> branches = new ArrayList<>();
		branches.add(METAPHYSISC);
		branches.add(EPISTEMOLOGY);
		branches.add(PHILOSOPHY_OF_MIND);
		locke.setInterests(branches);

		binding.bind(locke);
		model.write(System.out, "TURTLE");

		Property label = model.getProperty(Label.uri);
		Property nationality = model
				.getProperty("http://www.foafrealm.org/xfoaf/0.1/nationality");
		Property interests = model.getProperty(NAMESPACE + "interests");

		Resource res = model.getResource(NAMESPACE + "philosopher/John locke");
		assertFalse(res.hasProperty(model.getProperty(Label.uri)));

		res = model.getResource(NAMESPACE + "philosopher/John_locke");
		assertTrue(res.hasProperty(label));
		assertTrue(res.hasProperty(nationality));
		assertTrue(res.hasProperty(interests));
	}
}
