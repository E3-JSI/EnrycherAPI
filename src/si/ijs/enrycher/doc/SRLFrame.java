package si.ijs.enrycher.doc;

import java.util.Vector;

/**
 * A frame annotation (an instance of a FrameNet frame) 
 * @author mitjat
 */

public class SRLFrame {
	private final int id;
	private final String name;
	private final int sentenceId; // sentence annotated with / evoking this frame
	private final Vector<SRLRole> roleV; // only the roles appearing in this concrete annotation
	
	public SRLFrame(int id, String name, int sentenceId, Vector<SRLRole> roleV) {
		this.id = id;
		this.name = name;
		this.sentenceId = sentenceId;
		this.roleV = roleV;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Vector<SRLRole> getRoleV() {
		return roleV;
	}

	public int getSentenceId() {
		return sentenceId;
	}

}
