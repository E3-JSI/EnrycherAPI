package si.ijs.enrycher.doc;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A tool that was used in the pipeline.
 * 
 * @author Tadej Stajner, Blaz Fortuna
 *
 */
public class Tool {
	protected final String name;
	protected final Date time;
	protected final long duration;

	public static final SimpleDateFormat xsDateTime;
	static {
		xsDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	}
	
	public Tool(String _name, Date _time, long _duration) {
		name = _name;
		time = _time;
		duration = _duration;
	}
	
	public String getName() {
		return name;
	}

	public Date getTime() {
		return time;
	}

	public String getTimeString() {
		return xsDateTime.format(time);
	}	

	public long getDuration() {
		return duration;
	}	
}
