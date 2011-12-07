package si.ijs.enrycher.doc;

/**
 * Your everyday triplet.
 * 
 * It represents an assertion made in the text. It consists of three grammar nodes:
 *  a subject, predicate and an object. It can also be seen as an edge in the semantic graph,
 *  where the subj and obj are vertices and where the edge semantics is represented by the predicate.
 *  
 *  Each grammar node is also an annotation, which means that it can also have semantics
 *  associated with it.
 * 
 * @author Tadej
 *
 */
public class Assertion {
	/** Assertion id */
	protected final int id;
	/** Triple nodes */
	protected AssertionNode subject;
	protected AssertionNode verb;
	protected AssertionNode object;
	
	public Assertion(int _id, AssertionNode _subject, AssertionNode _verb, AssertionNode _object) {
		id = _id;
		subject = _subject;
		verb = _verb;
		object = _object;
	}

	public int getId() {
		return id;
	}
	
	public AssertionNode getSubject() {
		return subject;
	}

	public AssertionNode getPredicate() {
		return verb;
	}
	
	public AssertionNode getObject() {
		return object;
	}
}
