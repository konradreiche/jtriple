package berlin.reiche.jtriple.example;

import berlin.reiche.jtriple.rdf.Label;
import berlin.reiche.jtriple.rdf.SameAs;

public enum Branch {

	@SameAs({ "http://dbpedia.org/resource/Epistemology" })
	EPISTEMOLOGY("Epistemology"),
	
	@SameAs({ "http://dbpedia.org/resource/Mathematic" })
	MATHEMATIC("Mathematic"),

	@SameAs({ "http://dbpedia.org/resource/Metaphysic" })
	METAPHYSISC("Metaphysic"),

	@SameAs({ "http://dbpedia.org/resource/Philosophy_of_mind" })
	PHILOSOPHY_OF_MIND("Philosophy of Mind");
	
	@Label
	String name;
	
	Branch(String name) {
		this.name = name;
	}
	
}
