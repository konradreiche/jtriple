package berlin.reiche.jtriple.example;

import java.util.List;

import berlin.reiche.jtriple.rdf.Label;
import berlin.reiche.jtriple.rdf.RdfIdentifier;
import berlin.reiche.jtriple.rdf.RdfProperty;
import berlin.reiche.jtriple.rdf.RdfType;

@RdfType("http://dbpedia.org/page/Philosopher")
public class Philosopher {

	public static final String NAMESPACE = "http://konrad-reiche.com/philosophy/";
	
	@Label
	@RdfIdentifier
	String name;

	@RdfProperty("http://www.foafrealm.org/xfoaf/0.1/nationality")
	String nationality;

	List<Branch> interests;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public List<Branch> getInterests() {
		return interests;
	}

	public void setInterests(List<Branch> interests) {
		this.interests = interests;
	}

}
