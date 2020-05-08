package reports;

import sharedData.UserRoleEnum;

/**
 * LoginReport object holds a boolean value with the status of the
 * authentication command
 * 
 * @author michaelpermyashkin
 *
 */
public class LoginSuccessReport implements Report {
	private UserRoleEnum role; // holds the role of who logged in
	private String username;
	private int userID;

	/**
	 * Constructor of the LoginReport that takes a boolean value which holds Trues
	 * if the login was successful, False if the login was failed, and has a String
	 * of the user's role.
	 * 
	 * @param role     user's role
	 * @param username user's login name
	 * @param userID   user's unique identifier
	 */
	public LoginSuccessReport(UserRoleEnum role, String username, int userID) {
		this.role = role;
		this.username = username;
		this.userID = userID;
	}

	/**
	 * @return the user's username from their login credentials
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the user's unique ID
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + userID;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginSuccessReport other = (LoginSuccessReport) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (userID != other.userID)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	/**
	 * Allows observers of this report to check the role of the user
	 * 
	 * @return String of the role, "CaseWorker" or "Supervisor"
	 */
	public UserRoleEnum getRole() {
		return role;
	}
}
