package si.ijs.conf;

import java.util.HashMap;

public class Configuration 
{
	private HashMap<String, String> confMap;
	
	public Configuration(ConfReader confRd) throws ConfReaderException
	{
		confMap = confRd.read();
	}
	
	public String get(String key)
	{
		return confMap.get(key);
	}
}
