package si.ijs.enrycher.doc;

import java.util.ArrayList;
import java.util.List;

/**
 *	Annotations are basic building blocks of text understanding. 
 *	They represent mentions of some real-world concepts in the input text.
 *	They consist of:
 *		- instances, which are mentions of the entity in input text
 *		- semantics, which is the additional data that we were able to assign to it
 *
 *  In general, an annotation might not have instances (although this does not happen 
 *  in current output we produce). It might also not have semantics. That happens when
 *  we can not safely identify the entity. 
 * 
 *  Semantics can contain several types of information: 
 * 		- identifiers: attributes of type owl:sameAs and URI as value (e.g. owl:sameAs http://dbpedia.org/resource/Switzerland)
 * 		- relationships to other entities (e.g. skos:subject http://dbpedia.org/resource/Category:Federal_countries)
 * 		- attributes (e.g. rdfs:label Switzerland)
 * 
 * We consider entities which have identifiers to be "semantic".
 * 
 * @author Tadej Stajner, Blaz Fortuna
 *
 */
public class Annotation {
	/** Annotation ID */
	protected int id;
	/** Cannonical display string for annotation */
	protected final String displayName;
	/** Annotation type */
	protected String type;
	
	/** The occurrences of this annotation in a particular document */
	protected List<Instance> instanceV;
	/** The attributes, associated with this annotation */
	protected List<Attribute> attributeV;

	public Annotation(int _id, String _displayName, String _type) {
		id = _id;
		displayName = _displayName;
		type = _type;
		
		instanceV = new ArrayList<Instance>();
		attributeV = new ArrayList<Attribute>();		
	}
	
	public int getId() {
		return id;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String _type) {
		type = _type;
	}
	
	public List<Instance> getInstances() {
		return instanceV;
	}
	
	public Instance getInstanceById(int instId) {
		for (Instance inst : instanceV) {
			if (inst.getId() == instId) {
				return inst;
			}
		}
		return null;
	}
	
	public void addInstance(Instance inst) {
		instanceV.add(inst);
	}	

	public List<Attribute> getAttributes() {
		return attributeV;
	}
	
	public void addAttribute(Attribute attr) {
		attributeV.add(attr);
	}
	
	public boolean isPerson() {
		if (this.type.equals("enrycher:person")) {
			return true;
		}
		return false;
	}
	
	public boolean isLocation() {
		if (this.type.equals("enrycher:location")) {
			return true;
		}
		return false;
	}
	
	public boolean isOrganization() {
		if (this.type.equals("enrycher:organization")) {
			return true;
		}
		return false;
	}
	
	public boolean isNamedEntity() {
		if (isPerson() || isLocation() || isOrganization())  {
			return true;
		}
		return false;
	}
	
	public boolean isSubject() {
		if (this.type.equals("enrycher:subject")) {
			return true;
		}
		return false;
	}
	
	public boolean isObject() {
		if (this.type.equals("enrycher:object")) {
			return true;
		}
		return false;
	}
	
	public boolean isSubjOrObj() {
		if (isSubject() || isObject())  {
			return true;
		}
		return false;
	}
	
	public boolean isPredicate() {
		if (this.type.equals("enrycher:predicate")) {
			return true;
		}
		return false;
	}
	
	public boolean isDisambiguation() {
		if (this.type.equals("enrycher:disambiguation")) {
			return true;
		}
		return false;
	}
	
}
