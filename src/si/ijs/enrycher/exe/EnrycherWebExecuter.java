package si.ijs.enrycher.exe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import si.ijs.enrycher.doc.Annotation;
import si.ijs.enrycher.doc.Document;
import si.ijs.enrycher.doc.EnrycherException;

public abstract class EnrycherWebExecuter {
	// timeout
	private static int timeout = 30 * 1000; // half a minute

	/** Synchronous pipeline execution without transformation service 
	 * @throws EnrycherException */
	public static Document processSync(URL pipelineUrl, InputStream docStream) throws EnrycherException {
		try {
			HttpURLConnection connection =
				(HttpURLConnection)pipelineUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			// serialize the input
			connection.setDoOutput(true);
			OutputStream outputStream = connection.getOutputStream();
			byte[] buf = new byte[8192];
			while (true) {
				int length = docStream.read(buf);
				if (length < 0) { break; }
				outputStream.write(buf, 0, length);
			}
			// execute the connection
			connection.connect();
			// read response
			return new Document(connection.getInputStream());
		} catch (MalformedURLException e) {
			throw new EnrycherException(e);
		} catch (IOException e) {
			throw new EnrycherException(e);
		}
	}
	
	/** Synchronous pipeline execution with transformation service 
	 * @throws EnrycherException */
	public static InputStream processTransSync(URL pipelineUrl, InputStream docStream) throws EnrycherException {
		try {
			HttpURLConnection connection =
				(HttpURLConnection)pipelineUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			// serialize the input
			connection.setDoOutput(true);
			OutputStream outputStream = connection.getOutputStream();
			byte[] buf = new byte[8192];
			while (true) {
				int length = docStream.read(buf);
				if (length < 0) { break; }
				outputStream.write(buf, 0, length);
			}
			// execute the connection
			connection.connect();
			// read response
			return connection.getInputStream();
		} catch (MalformedURLException e) {
			throw new EnrycherException(e);
		} catch (IOException e) {
			throw new EnrycherException(e);
		}
	}	
	
	
	public static void Test() throws MalformedURLException, EnrycherException {
		// input document
		String docString = "This is my document ...";
		// URL of Enrycher web service
		URL pipelineUrl = new URL("http://example.com/enrycher");
		// convert input document to input stream
		InputStream  docStream = new ByteArrayInputStream(docString.getBytes());
		// call Enrycher web service
		Document doc = EnrycherWebExecuter.processSync(pipelineUrl, docStream);
		// iterate over all the annotations
		for (Annotation ann : doc.getAnnotations()) {
			// do something with the annotation
			ann.getId();
		}
	}
}
