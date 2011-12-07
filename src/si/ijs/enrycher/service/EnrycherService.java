/**
 * 
 */
package si.ijs.enrycher.service;

import si.ijs.enrycher.doc.Document;
import si.ijs.enrycher.doc.EnrycherException;

/**
 * Enrycher Service
 * 
 * @author Blaz Fortuna
 */
public abstract class EnrycherService extends EnrycherAbstractService {
	
	/** Implementation of Enrycher service functionality */ 
	public abstract void enrych(Document doc) throws EnrycherException;

}
