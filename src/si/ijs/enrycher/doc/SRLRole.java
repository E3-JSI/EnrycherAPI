package si.ijs.enrycher.doc;

/**
 * A role *annotation* (an instance of a FrameNet role) 
 * @author mitjat
 */
public class SRLRole {
	private String name;
	private int bfsIdx;

	/**
	 * @param bfsIdx breadth-first-search index of the node in the constituency parse tree that is annotated with this role. 
	 */
	public SRLRole(String name, int bfsIdx) {
		this.name = name;
		this.bfsIdx = bfsIdx;
	}
	
	public String getName() {
		return name;
	}

	public int getBfsIdx() {
		return bfsIdx;
	}


}