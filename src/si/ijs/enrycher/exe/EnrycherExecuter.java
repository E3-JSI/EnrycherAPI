/**
 * 
 */
package si.ijs.enrycher.exe;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import si.ijs.enrycher.doc.Document;
import si.ijs.enrycher.doc.EnrycherException;
import si.ijs.enrycher.doc.Tool;
import si.ijs.enrycher.service.EnrycherAbstractService;
import si.ijs.enrycher.service.EnrycherInService;
import si.ijs.enrycher.service.EnrycherOutService;
import si.ijs.enrycher.service.EnrycherService;

/**
 * Enrycher Executer
 * 
 * @author Blaz Fortuna
 *
 */
public class EnrycherExecuter {
	
	private static void addTimeDuration(EnrycherAbstractService service, Document doc, Date start) {
		if (!service.isSilent()) {
			Date end = new Date();
			long duration = end.getTime() - start.getTime();
			doc.getPipeline().addTool(new Tool(service.getName(), start, duration));
		}
	}
	
	/** Single service on Enrycher document 
	 * @throws EnrycherException */
	public static void execute(EnrycherService service, Document doc) throws EnrycherException { 
		Date start = new Date();	
		service.enrych(doc);		
		addTimeDuration(service, doc, start);
	}
	
	/** Pre-processing on input stream 
	 * @throws EnrycherException */
	public static Document execute(EnrycherInService inService, InputStream inputStream) throws EnrycherException { 
		Date start = new Date();	
		Document doc = inService.preprocess(inputStream);		
		addTimeDuration(inService, doc, start);
		return doc;
	}
	
	/** Final transformation on Enrycher document 
	 * @throws EnrycherException */
	public static void execute(EnrycherOutService outService, Document doc, OutputStream outputStream) throws EnrycherException {
		Date start = new Date();	
		outService.transform(doc, outputStream);		
		addTimeDuration(outService, doc, start);
	}
	
	/** Final transformation on Enrycher document 
	 * @throws EnrycherException */
	public static String execute(EnrycherOutService outService, Document doc) throws EnrycherException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		execute(outService, doc, outStream);
		return outStream.toString();
	}

	/** Sequence of services on Enrycher document 
	 * @throws EnrycherException */
	public static void execute(EnrycherService[] serviceV, Document doc) throws EnrycherException {
		for (EnrycherService service : serviceV) {
			System.out.println("Executing service "+service.getClass().getName()+" "+service.getAttributes());
			execute(service, doc);
		}
	}
	
	/** Sequence of services with pre-processing on input stream 
	 * @throws EnrycherException */
	public static Document execute(EnrycherInService inService,
			EnrycherService[] serviceV, InputStream inputStream) throws EnrycherException {

		Document doc = execute(inService, inputStream);
		for (EnrycherService service : serviceV) {
			System.out.println("Executing service "+service.getClass().getName()+" "+service.getAttributes());
			execute(service, doc);
		}
		return doc;
	}
	
	/** Sequence of services on Enrycher document with final transformation 
	 * @throws EnrycherException */
	public static void execute(EnrycherService[] serviceV,
			EnrycherOutService outService, Document doc, 
			OutputStream outputStream) throws EnrycherException {
		
		for (EnrycherService service : serviceV) {
			System.out.println("Executing service "+service.getClass().getName()+" "+service.getAttributes());
			execute(service, doc);
		}
		execute(outService, doc, outputStream);
	}
	
	/** Pre-processing on input stream with final transformation 
	 * @throws EnrycherException */
	public static void execute(EnrycherInService inService,
			EnrycherOutService outService, InputStream inputStream, 
			OutputStream outputStream) throws EnrycherException {

		Document doc = execute(inService, inputStream);
		execute(outService, doc, outputStream);
	}	
	
	/** Sequence of services with preprocessing on input stream and final trasnformation 
	 * @throws EnrycherException */
	public static void execute(EnrycherInService inService, 
			EnrycherService[] serviceV, EnrycherOutService outService,
			InputStream inputStream, OutputStream outputStream) throws EnrycherException {

		Document doc = execute(inService, inputStream);
		for (EnrycherService service : serviceV) {
			System.out.println("Executing service "+service.getClass().getName()+" "+service.getAttributes());
			execute(service, doc);
		}
		execute(outService, doc, outputStream);
	}
}
