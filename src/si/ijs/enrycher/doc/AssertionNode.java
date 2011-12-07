package si.ijs.enrycher.doc;

import java.util.ArrayList;
import java.util.List;

/**
 * A grammar node is a component of a triplet. It represents the link between an annotation and
 * its role in the semantic graph. 
 * 
 * It also contains modifiers which can help in understanding the semantics of the node or 
 * the relationship the assertion represents.
 * 
 * @author Tadej Stajner, Blaz Fortuna
 */
public class AssertionNode {	
	protected Annotation annotation;
	protected Instance instance;
	protected List<Annotation> modifierAnnV;
	protected List<Instance> modifierInstV;
	
	public AssertionNode(Annotation _annotation, Instance _instance){		
		this.annotation = _annotation;
		this.instance = _instance;
		this.modifierAnnV = new ArrayList<Annotation>();
		this.modifierInstV = new ArrayList<Instance>();
	}
	
	public AssertionNode(Annotation _annotation, Instance _instance,
			List<Annotation> _modifiersAnnV, List<Instance> _modifierInstV){
		
		this.annotation = _annotation;
		this.instance = _instance;
		this.modifierAnnV = _modifiersAnnV;
		this.modifierInstV = _modifierInstV;
		// make sure each annotation has an instance
		assert(modifierAnnV.size() == modifierInstV.size());
	}
		
	public Annotation getAnnotation(){
		return annotation;
	}
	
	public void setAnnotation (Annotation _annotation) {
		this.annotation = _annotation;
	}
	
	public Instance getInstance(){
		return instance;
	}
	
	public int getModifiers() {
		return modifierAnnV.size();
	}

	public List<Annotation> getModifierAnns() {
		return modifierAnnV;
	}

	public List<Instance> getModifierInsts() {
		return modifierInstV;
	}	
}
