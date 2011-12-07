package si.ijs.enrycher.doc;

import java.util.ArrayList;
import java.util.List;

/**
 * A sentence is a list of tokens with an original plaintext representation
 * and an identifier.
 *  
 * @author Tadej Stajnar, Blaz Fortuna
 *
 */
public class Sentence {
	/** sentence Id */
	protected int id;
	/** sentence string */
	protected final String plainText;
	/** sentence tokens */
	protected List<Token> tokenV;
	/** constituency parse */
	protected final String constituencyParse;
	
	public Sentence(String _plainText) {
		id = -1;
		plainText = _plainText;
		tokenV = null;
		constituencyParse = null;
	}

	// only called by Document, when assigned proper IDs
	public Sentence(int _id, String _plainText, String _constituencyParse) {
		id = _id;
		plainText = _plainText;
		tokenV = null;
		constituencyParse = _constituencyParse;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getPlainText() {
		return plainText;
	}
	
	public boolean isTokenized() {
		return tokenV != null;
	}
	
	public List<Token> getTokens() {
		return tokenV;
	}
	
	public void addToken(Token token) {
		// create a new list, if one does not exist already
		if (tokenV == null) { tokenV = new ArrayList<Token>(); }
		// add token to the end of the list
		tokenV.add(token);
	}			

	public boolean isConstituencyParsed() {
		return constituencyParse != null;
	}
	
	public String getConstituencyParse() {
		return constituencyParse;
	}
}
