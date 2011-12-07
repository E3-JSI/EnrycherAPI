package si.ijs.enrycher.doc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A single occurrence of a token in text.
 * Has: 
 * 	- literal value
 * 	- part of speech role 
 *  - unique token identifier (of format sentenceId.sequenceOfToken
 * @author Tadej
 *
 */
public class Token {
	/** Token Id */
	protected int id;
	/** Token String */
	protected final String str;
	/** added values to the token, e.g. part-of-speach */
	protected Map<String, String> keyValH;
	
	//protected int start;
	//protected int end;
	
	public Token(String _str) {
		id = -1;
		str = _str;
		keyValH = new HashMap<String, String>();
	}

	public Token(int _id, String _str) {
		id = _id;
		str = _str;
		keyValH = new HashMap<String, String>();
	}

	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;		
	}

	public String getStr() {
		return str;
	}
	
	/*public int getStart(){
		return start;
	}
	
	public int getEnd(){
		return end;
	}*/
	
	/*public void setStart(int start){
		this.start = start;
	}
	
	public void setEnd(int end){
		this.end = end;
	}*/
	
	public boolean isKey(String key) {
		return keyValH.containsKey(key);
	}
	
	public Set<String> getKeys() {
		return keyValH.keySet();
	}
	
	public String getKeyVal(String key) {
		return keyValH.get(key);
	}
	
	public void addKeyVal(String key, String val) {
		keyValH.put(key, val);
	}
}
