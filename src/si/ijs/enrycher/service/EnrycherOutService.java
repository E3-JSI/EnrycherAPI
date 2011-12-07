package si.ijs.enrycher.service;

import java.io.OutputStream;

import si.ijs.enrycher.doc.Document;
import si.ijs.enrycher.doc.EnrycherException;

/**
 * Enrycher Transformation Service
 * 
 * @author Blaz Fortuna
 */
public abstract class EnrycherOutService extends EnrycherAbstractService {
	
	/** Implementation of Enrycher service functionality */ 
	public abstract void transform(Document doc, OutputStream outputStream) throws EnrycherException;
	
	/** MIME type of the output */
	public String getContentType() { return "text/xml"; }
}
