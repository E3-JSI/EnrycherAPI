package si.ijs.enrycher.doc;

import java.util.ArrayList;
import java.util.List;

/**
 * The metadata header of an enrycher document.
 * Contains a list of services applied to the document.  
 * 
 * @author Tadej Stajnar, Blaz Fortuna
 *
 */
public class Pipeline {
	private List<Tool> toolV;

	public List<Tool> getTools() {
		return toolV;
	}
	
	public void addTool(Tool tool) {
		toolV.add(tool);
	}
	
	public void clear() {
		toolV.clear();
	}

	public Pipeline() {
		toolV = new ArrayList<Tool>();
	}
}
