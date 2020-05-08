package datasource;

import sharedData.UserRoleEnum;

/**
 * Enum to save temporary test login info
 * 
 * @author Tehmi Lowe
 */
public enum UserLoginDataEnum {

	/**
	 * 
	 */
	JohnDoe("Supervisor1", "password", UserRoleEnum.SUPERVISOR),

	/**
	 * 
	 */
	JaneSmith("Supervisor2", "password", UserRoleEnum.SUPERVISOR),

	/**
	 * 
	 */
	GregBlack("CaseWorker1", "password", UserRoleEnum.CASEWORKER),

	/**
	 * 
	 */
	SusanStone("CaseWorker2", "password", UserRoleEnum.CASEWORKER),

	/**
	 * 
	 */
	ZachWilliams("CaseWorker3", "password", UserRoleEnum.CASEWORKER),

	/**
	 * 
	 */
	PeterLawrence("CaseWorker4", "password", UserRoleEnum.CASEWORKER);

	private final String username; // user's username
	private final String password; // user's password
	private final UserRoleEnum role; // user's role

	/**
	 * Constructor for User enums
	 * 
	 * @param username user's username
	 * @param password user's password
	 * @param role     user's role
	 */
	private UserLoginDataEnum(String username, String password, UserRoleEnum role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}

	/**
	 * get a unique ID number for this user
	 * 
	 * @return the enum's ordinal value + 1 (so no one is user zero)
	 */
	public int getUserID() {
		return this.ordinal() + 1;
	}

	/**
	 * @return the user's username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return user's password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return user's role
	 */
	public UserRoleEnum getRole() {
		return role;
	}
}
