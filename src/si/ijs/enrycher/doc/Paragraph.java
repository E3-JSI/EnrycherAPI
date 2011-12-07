package si.ijs.enrycher.doc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection of sentences.
 * Has a tag associated. Usually "title", "h1", or "p".
 * 
 * @author Tadej Stajnar, Blaz Fortuna
 *
 */
public class Paragraph {
	protected final String tagName;
	protected List<Sentence> sentenceV;
	protected Map<Integer, Sentence> sentIdToSentH;

	public Paragraph(String _tagName) {
		tagName = _tagName;
		sentenceV = new ArrayList<Sentence>();
		sentIdToSentH = new HashMap<Integer, Sentence>();
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public List<Sentence> getSentences() {
		return sentenceV;
	}
	
	public Sentence getSentence(int sentenceId) {
		return sentIdToSentH.get(sentenceId);
	}
	
	public void addSentence(Sentence sentence) {
		sentenceV.add(sentence);
		sentIdToSentH.put(sentence.getId(), sentence);
	}
}
