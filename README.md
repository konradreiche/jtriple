# JTriple

JTriple is a Java tool which creates a RDF data model out of a Java object model by making use of reflection, a small set of annotations and Jena's flexible RDF/OWL API.

### Why another RDF binding for Java?

The most popular tool for persisting Java objects to RDF is [JenaBean]. JTriple was developed, respectively Jenabean was not modified due to the following reasons:

* JenaBean aims for a persistence layer (object serialization). This fact is often expressed by missing con?guration, for instance a field cannot be declared as transient.

* Not the whole functionality of JenaBean is required. Additional data is serialized, for instance the serialization of the package names. Package names are vital for deserialization but for the pure data translation (one-way) it only interferes.

* Data (RDF) and schema (OWL) should be translated into two separate RDF graphs. Jenabean creates only one graph.

## How does it work?

The Java objects are instantiated and passed to JTriple which then examines the class and its different fields and methods with reflection. The run-time state of the Java objects is read and used to create a RDF graph by using [Jena API].

```java
Binding binding = new Binding(NAMESPACE);
binding.bind(object);
```

The only required annotation is `@RdfIdentifier`. One field needs to be specified as identifier. Otherwise JTriple does not know how to construct the URI. The mapping between Java and RDF is as follows:

A statement in RDF consist of Subject, Predicate and Object

<table>
  <tr>
    <th>RDF</th><th>Java</th>
  </tr>
  <tr>
    <td>Subject</td><td>Object</td>
  </tr>
  <tr>
    <td>Predicate</td><td>Field</td>
  </tr>
  <tr>
    <td>Object</td><td>Field value</td>
  </tr>
  <tr>
    <td>URI</td><td>Annotated Field/Method</td>
  </tr> 
</table>




### Example

Considering the following example, a class Philosopher

```java
@RdfType("http://dbpedia.org/page/Philosopher")
public class Philosopher {

	@Label
	@RdfIdentifier
	String name;

	@RdfProperty("http://www.foafrealm.org/xfoaf/0.1/nationality")
	String nationality;

	List<Branch> interests;
}
```

with the enum type Branch

```java
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
```

A default namespace is passed to the `Binding` object. Unless further specified this namespace plus the simple name of the class or field is used to construct the URI. With the following statements:

```java
Philosopher locke = new Philosopher();
locke.setName("John Locke");
locke.setNationality("English");

List<Branch> branches = new ArrayList<>();
branches.add(METAPHYSISC);
branches.add(EPISTEMOLOGY);
branches.add(PHILOSOPHY_OF_MIND);
locke.setInterests(branches);

Binding binding = new Binding(NAMESPACE);
Model model = binding.getModel();
model.setNsPrefix("philosophy", NAMESPACE);
model.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
model.setNsPrefix("xfoaf", "http://www.foafrealm.org/xfoaf/0.1/");
model.setNsPrefix("dbpedia", "http://dbpedia.org/resource/");

binding.bind(locke);
model.write(System.out, "TURTLE");
```

this RDF is produced:

```
@prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#> .
@prefix xfoaf:   <http://www.foafrealm.org/xfoaf/0.1/> .
@prefix philosophy:  <http://konrad-reiche.com/philosophy/> .
@prefix dbpedia:  <http://dbpedia.org/resource/> .

<http://konrad-reiche.com/philosophy/branch/Metaphysisc>
      a       philosophy:branch ;
      rdfs:label "Metaphysic"^^<http://www.w3.org/2001/XMLSchema#string> ;
      <http://www.w3.org/2002/07/owl#sameAs>
              dbpedia:Metaphysic .

<http://konrad-reiche.com/philosophy/branch/Philosophy_of_mind>
      a       philosophy:branch ;
      rdfs:label "Philosophy of Mind"^^<http://www.w3.org/2001/XMLSchema#string> ;
      <http://www.w3.org/2002/07/owl#sameAs>
              dbpedia:Philosophy_of_mind .

<http://konrad-reiche.com/philosophy/philosopher/John_locke>
      a       <http://dbpedia.org/page/Philosopher> ;
      rdfs:label "John Locke"^^<http://www.w3.org/2001/XMLSchema#string> ;
      philosophy:NAMESPACE
              "http://konrad-reiche.com/philosophy/"^^<http://www.w3.org/2001/XMLSchema#string> ;
      philosophy:interests
              <http://konrad-reiche.com/philosophy/branch/Metaphysisc> ,
              <http://konrad-reiche.com/philosophy/branch/Philosophy_of_mind> ,
              <http://konrad-reiche.com/philosophy/branch/Epistemology> ;
      xfoaf:nationality "English"^^<http://www.w3.org/2001/XMLSchema#string> .

<http://konrad-reiche.com/philosophy/branch/Epistemology>
      a       philosophy:branch ;
      rdfs:label "Epistemology"^^<http://www.w3.org/2001/XMLSchema#string> ;
      <http://www.w3.org/2002/07/owl#sameAs>
              dbpedia:Epistemology .

```

### Annotations

What annotations are there and how can they be used?

<table>
  <tr>
    <th>Name</th><th>Use</th><th>Effect</th>
  </tr>
  <tr>
    <td>@RdfIdentifier</td><td>Fields, Methods</td><td>Value to be used for constructing the resource URI</td>
  </tr>
  <tr>
    <td>@RdfProperty</td><td>Fields, Methods</td><td>Value to define another property URI</td>
  </tr>
  <tr>
    <td>@RdfType</td><td>Classes</td><td>Value to define a rdfs:type property on the resource</td>
  </tr>
  <tr>
    <td>@Transient</td><td>Fields</td><td>Indicate that this field must not be converted</td>
  </tr>
  <tr>
    <td>@SameAs</td><td>Enum Constants</td><td>Value to define a owl:sameAs property on the resource</td>
  </tr>
  <tr>
    <td>@Label</td><td>Fields, Methods</td><td>Value to define a rdfs:label property on the resource</td>
  </tr>
</table>

## Using it

JTriple can used as a Maven dependency. You have to define this repository:

```xml
<repository>
     <id>berlin.reiche.jtriple</id>
     <url>https://github.com/platzhirsch/jtriple/raw/master/repository/snapshots</url>
</repository>
```

Then it can be added as a dependency:

```xml
<dependency>
     <groupId>berlin.reiche.jtriple</groupId>
     <artifactId>jtriple</artifactId>
     <version>0.1-RELEASE</version>
     <scope>compile</scope>
</dependency>
```

Apart from that you can head to the [download section] and get the JAR.

## Future Work

Some ideas for the future development.

* Implementing OWL binding
* Increase the configuration flexibility

If you feel something is amiss, feel free to open an issue. The implementation is very lightweight and allows to change the functionality very quickly.

[JenaBean]: http://code.google.com/p/jenabean/
[Jena API]: http://jena.apache.org/
[download section]: https://github.com/platzhirsch/jtriple/downloads
