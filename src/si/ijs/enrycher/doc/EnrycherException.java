package si.ijs.enrycher.doc;

public class EnrycherException extends Exception {

	private static final long serialVersionUID = -8013406209148447170L;

	public EnrycherException(String message) {
		super(message);
	}

	public EnrycherException(Throwable cause) {
		super(cause);
	}

	public EnrycherException(String message, Throwable cause) {
		super(message, cause);
	}

}
