package si.ijs.enrycher.doc;

/**
 * A semantic attribute of an annotation. 
 * 
 * Somewhat analogous to triplet - the attribute is a part of an entity, which plays 
 * the role of subject. The predicate is always semantic (an URI), whereas the value is 
 * either a literal or another URI.
 * 
 * @author Tadej Stajner, Blaz Fortuna
 */
public class Attribute {
	/** Attribute type (URI of predicate) */
	protected final String type;
	/** Value of resource */
	protected final String resource;
	/** Value of literal */
	protected final String literal;
	/** Comment, help when URIs are not human friendly */
	protected final String displayName;
	
	private Attribute(String _type, String _resource, String _literal, String _displayName) {
		type = _type;
		resource = _resource;
		literal = _literal;
		displayName = _displayName;
	}
	
	public static Attribute getResource(String type, String resource) {
		return new Attribute(type, resource, "", "");
	}

	public static Attribute getResource(String type, String resource, String displayName) {
		return new Attribute(type, resource, "", displayName);
	}

	public static Attribute getLiteral(String type, String literal) {
		return new Attribute(type, "", literal, "");
	}

	public static Attribute getLiteral(String type, String literal, String displayName) {
		return new Attribute(type, "", literal, displayName);
	}

	public String getType() {
		return type;
	}

	public boolean isResource() {
		return resource.length() > 0;
	}
	
	public String getResource() {
		return resource;
	}
	
	public boolean isLiteral() {
		return literal.length() > 0;
	}

	public String getLiteral() {
		return literal;
	}
	
	public boolean isDisplayName() {
		return displayName.length() > 0;
	}

	public String getDisplayName() {
		return displayName;
	}

	
}
