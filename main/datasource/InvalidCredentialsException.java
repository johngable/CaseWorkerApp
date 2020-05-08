package datasource;

/**
 * Thrown if someone doesn't have the credentials they need to complete a request they've made
 * @author merlin
 *
 */
public class InvalidCredentialsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param string the error message
	 */
	public InvalidCredentialsException(String string) {
		super(string);
	}

}
