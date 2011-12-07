package si.ijs.enrycher.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Enrycher Service.
 * 
 * @author Blaz Fortuna
 */
public abstract class EnrycherAbstractService {
	
	/** Enrycher Service public */
	public abstract String getName();
	
	/** Enrycher Service optional attributes */ 
	public Map<String, String> getAttributes() {
		return new HashMap<String, String>();
	}
	
	/** Parameter passed after service initialization */
	public void init(String param) { }
	
	/** should the execution of service be hidden in the pipeline trace */
	public boolean isSilent() { return false; }	
}
