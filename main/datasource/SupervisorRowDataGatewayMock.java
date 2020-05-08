package datasource;

/**
 * The mock version of the data source.  Gets values from SupervisorEnum.
 * @author merlin
 *
 */
public class SupervisorRowDataGatewayMock extends SupervisorRowDataGateway {

	private SupervisorEnum myEnum;
	
	/**
	 * Finder constructor
	 * @param id the supervisor's id
	 */
	public SupervisorRowDataGatewayMock(int id) {
		myEnum = SupervisorEnum.values()[id-1];
	}

	/**
	 * @see datasource.SupervisorRowDataGateway#getName()
	 */
	@Override
	public String getName() {
		return myEnum.getName();
	}

	/**
	 * @see datasource.SupervisorRowDataGateway#getID()
	 */
	@Override
	public int getID() {
		return myEnum.getID();
	}

}
