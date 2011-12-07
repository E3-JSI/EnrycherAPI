package si.ijs.conf;

import java.util.HashMap;

public interface ConfReader 
{
	HashMap<String, String> read() throws ConfReaderException;
}
