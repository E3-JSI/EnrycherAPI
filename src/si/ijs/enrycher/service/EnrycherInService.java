package si.ijs.enrycher.service;

import java.io.InputStream;

import si.ijs.enrycher.doc.Document;
import si.ijs.enrycher.doc.EnrycherException;

/**
 * Enrycher Pre-processing Service
 * 
 * @author Blaz Fortuna
 */
public abstract class EnrycherInService extends EnrycherAbstractService {
	/** Implementation of Enrycher service functionality */ 
	public abstract Document preprocess(InputStream inputStream) throws EnrycherException;
}
