package datasource;

/**
 * Enum should store the ID of the supervisor and their name
 * 
 * @author tl9649
 *
 */
public enum SupervisorEnum {
	/**
	 * 
	 */
	Supervisor1(1, "Supervisor1"),
	/**
	 * 
	 */
	Supervisor2(2, "Supervisor2");

	private String name;	// the name of the supervisor
	private int id;		// the ID of the supervisor from UserLoginData
	
	/**
	 * constructor that takes in the supervisor's ID and its name
	 *
	 * @param id the ID of the supervisor from UserLoginData
	 * @param name the name of the supervisor
	 */
	private SupervisorEnum(int id, String name) {
		this.id =id;
		this.name = name;
	}

	/**
	 * @return the username of the supervisor
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the ID of the supervisor
	 */
	public int getID() {
		return id;
	}
}
