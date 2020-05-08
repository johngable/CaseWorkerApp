package model;


import org.easymock.EasyMock;
import org.junit.Before;

import org.junit.jupiter.api.Test;

import datasource.UserLoginDataEnum;
import reports.LoginFailedReport;
import reports.LoginSuccessReport;
import reports.ReportObserver;
import reports.ReportObserverConnector;

/**
 * Test class to test the AuthenticateCommand
 * 
 * @author kimberlyoneill
 *
 */

class testAuthenticateCommand {

	private ReportObserverConnector roc = ReportObserverConnector.getSingleton();

	@Before
	public void setup() {
		ReportObserverConnector.resetSingleton();
	}


	/**
	 * Tests that the datasource checks credentials and the command gets the right
	 * return value. Tests that the correct values are sent in the report and the
	 * report returns the right status
	 */
	@Test
	void testGood() {
		// initialize
		UserLoginDataEnum jdoe = UserLoginDataEnum.JohnDoe;
		AuthenticateCommand cmd = new AuthenticateCommand(jdoe.getUsername(), jdoe.getPassword());
		ReportObserver obs = (ReportObserver) EasyMock.createMock(ReportObserver.class);
		roc.registerObserver(obs, LoginSuccessReport.class);
		obs.receiveReport(EasyMock.eq(new LoginSuccessReport(jdoe.getRole(), jdoe.getUsername(), jdoe.getUserID())));
		EasyMock.replay(obs);


		cmd.execute();

		EasyMock.verify(obs);
	}

	/**
	 * Tests that the datasource checks credentials and the command gets the right
	 * return value. Tests that the correct values are sent in the report and the
	 * report returns the right status
	 */
	@Test
	void testBad() {
		// initialize
		AuthenticateCommand cmd = new AuthenticateCommand(UserLoginDataEnum.JohnDoe.getUsername() + 'Z',
				UserLoginDataEnum.JohnDoe.getPassword());
		ReportObserver obs = (ReportObserver) EasyMock.createMock(ReportObserver.class);
		roc.registerObserver(obs, LoginFailedReport.class);
		obs.receiveReport(EasyMock.anyObject(LoginFailedReport.class));
		EasyMock.replay(obs);

		cmd.execute();

		EasyMock.verify(obs);

	}

}