package sharedData;

/**
 * The list of possible roles a user can have
 * @author merlin
 *
 */
public enum UserRoleEnum {
	
	/**
	 * 
	 */
	SUPERVISOR("Supervisor"),
	/**
	 * 
	 */
	CASEWORKER("Case worker");

	private String description;

	private UserRoleEnum(String desc)
	{
		this.description = desc;
	}
	
	/**
	 * @see java.lang.Enum#toString()
	 */
	public String toString()
	{
		return description;
	}
}
