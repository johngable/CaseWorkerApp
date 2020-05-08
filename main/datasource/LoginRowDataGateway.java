package datasource;

import sharedData.UserRoleEnum;

/**
* Gateway to the datasource that checks entered username and password against
* stored user login data. 
* 
* @author Amy M
*/
public class LoginRowDataGateway {

  private UserLoginDataEnum userEnum = null;
  
/**
 * Checks the given credentials
 * @param username the login username
 * @param password the login password
 * @throws InvalidCredentialsException if the username/password doesn't exist in the data source
 */
public LoginRowDataGateway(String username, String password) throws InvalidCredentialsException{
	  for (UserLoginDataEnum userlog : UserLoginDataEnum.values()) {
			if ((userlog.getUsername().equals(username)) && (userlog.getPassword().equals(password))) {
				userEnum = userlog; // stores role of login successfully validated
			}
		}
		if (userEnum == null)
		{
			throw new InvalidCredentialsException("User credentials invalid " + username + "/" + password);
		}
  }

/**
   * Receives the entered username and password from the calling class and checks if they are legal credentials
   * 
   * @param username the username we are checking
   * @param password the password we are checking
   * @return true if those credentials are valid
   */
  public boolean checkCredentials(String username, String password) {

		for (UserLoginDataEnum userlog : UserLoginDataEnum.values()) {
			if ((userlog.getUsername().equals(username)) && (userlog.getPassword().equals(password))) {
				userEnum = userlog; // stores role of login successfully validated
				return true;
			}
		}
		return false;
	}

	/**
	 * gets role of authenticated user
	 * 
	 * @return String holding the users role
	 */
	public UserRoleEnum getRole() {
		if(userEnum!= null) {
			return userEnum.getRole();
		}
		return null;
	}
	
	/**
	 * @return the user's username
	 */
	public String getUserName() {
		return userEnum.getUsername();
	}
	
	/**
	 * @return a unique ID for this user
	 */
	public int getUserID() {
		return userEnum.getUserID();

	}
}
