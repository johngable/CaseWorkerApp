package datasource;

/**
 * Class that retrieves the given supervisor's information
 * 
 * @author am2319
 */
public abstract class SupervisorRowDataGateway {


	/**
	 * @return the supervisor's name
	 */
	public abstract String getName();

	/**
	 * @return the supervisor's id
	 */
	public abstract int getID();
}
