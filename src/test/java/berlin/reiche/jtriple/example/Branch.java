package berlin.reiche.jtriple.example;

import berlin.reiche.jtriple.rdf.Label;
import berlin.reiche.jtriple.rdf.SameAs;

public enum Branch {

	@SameAs({ "dbpedia.org/resource/Epistemology" })
	EPISTEMOLOGY("Epistemology"),
	
	@SameAs({ "dbpedia.org/resource/Mathematic" })
	MATHEMATIC("Mathematic"),

	@SameAs({ "dbpedia.org/resource/Metaphysic" })
	METAPHYSISC("Metaphysic"),

	@SameAs({ "dbpedia.org/resource/Philosophy_of_mind" })
	PHILOSOPHY_OF_MIND("Philosophy of Mind");
	
	@Label
	String name;
	
	Branch(String name) {
		this.name = name;
	}
	
}
