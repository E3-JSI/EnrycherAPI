package si.ijs.enrycher.doc;

/**
 * An instance of an annotation, appearing in text. 
 * The NLP grounding for an annotation.
 * 
 * @author Tadej Stajnar, Blaz Fortuna
 */
public class Instance {
	/** Identifier */
	protected int id;
	/** Literal words that represent the annotation */
	protected final String displayName;
	/** list of token ids */
	protected final int[] tokenIdV;
	/** list of sentence ids */
	protected final int[] sentenceIdV;

	public Instance(String _displayName, int[] _tokenIdV, int[] _sentenceIdV) {
		id = -1;
		displayName = _displayName;
		tokenIdV = _tokenIdV;
		sentenceIdV = _sentenceIdV;
		// each token needs a sentence id
		assert(tokenIdV.length != sentenceIdV.length);
	}
	
	public String getDisplayName() {
		return displayName;
	}
	public int getId() {
		return id;
	}
	
	public void setId(int val)
	{
		this.id = val;
	}

	public int getTokens() {
		return tokenIdV.length;
	}
	
	public int getSentenceId(int tokenN) {
		return sentenceIdV[tokenN];
	}
	
	public int getTokenId(int tokenN) {
		return tokenIdV[tokenN];
	}	
}
