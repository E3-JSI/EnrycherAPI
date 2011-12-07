package si.ijs.enrycher.exe;

import java.io.File;
import java.util.Map;

import si.ijs.enrycher.doc.EnrycherException;

import si.ijs.conf.ConfReader;
import si.ijs.conf.ConfReaderException;
import si.ijs.conf.XmlConfReader;

public class EnrycherEnvironment {
	
	private static Map<String, String> variables = null;;
	
	private static String enrycherHome = null;
	
	private EnrycherEnvironment()
	{		
		
	}
	
	private static Map<String, String> getVariables() throws ConfReaderException
	{
		if(variables == null)
		{
			ConfReader confReader = new XmlConfReader(getEnrycherHome() + File.separator + "config.xml");
			variables = confReader.read();
		}
		return variables;
	}
	
	private static String getEnrycherHome()
	{
		if(enrycherHome == null)
		{
			enrycherHome = System.getenv().get("ENRYCHER_HOME");
		}
		return enrycherHome;		
	}
	
	public static String getPath(String relPathKey) throws EnrycherException
	{
		try {
			String relPath = getVariables().get(relPathKey);
			return getEnrycherHome() + File.separator + relPath;
		} catch (ConfReaderException e) {
			throw new EnrycherException("Could nor read configurations: " + e.getMessage());
		}
	}
	
	public static String getVar(String key) throws EnrycherException {
		try {
			return getVariables().get(key);
		} catch (ConfReaderException e) {
			throw new EnrycherException("Could nor read configurations: " + e.getMessage());
		}
	}
}
