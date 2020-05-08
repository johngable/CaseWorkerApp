package model;

import datasource.InvalidCredentialsException;
import datasource.LoginRowDataGateway;
import reports.LoginFailedReport;
import reports.LoginSuccessReport;
import reports.ReportObserverConnector;
import sharedData.UserRoleEnum;

/**
 * AuthenticateCommand checks if the login credentials are correct. If they are
 * it sends a successful LoginReport. If not, it sends a Failed LoginReport
 * 
 * @author kimberlyoneill
 *
 */
public class AuthenticateCommand implements Command {

	private String username;
	private String password;
	private UserRoleEnum role;

	/**
	 * Create a command to check to see if a user's credentials are valid
	 * 
	 * @param username the username
	 * @param password the password
	 */
	public AuthenticateCommand(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * execute checks credentials against the LoginDataStore and sends a report with
	 * the status
	 */
	@Override
	public void execute() {
		LoginRowDataGateway lds;
		try {
			lds = new LoginRowDataGateway(this.username, this.password);
			ReportObserverConnector.getSingleton().sendReport(new LoginSuccessReport(lds.getRole(),lds.getUserName(), lds.getUserID()));
		} catch (InvalidCredentialsException e) {
			ReportObserverConnector.getSingleton().sendReport(new LoginFailedReport());
		}
	}

	/**
	 * getter for username
	 * 
	 * @return the username currently attached to this Command
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * getter for password
	 * 
	 * @return the password currently attached to this Command
	 */
	public String getPassword() {
		return this.password;
	}

}
