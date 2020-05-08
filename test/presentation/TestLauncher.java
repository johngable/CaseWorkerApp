package presentation;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import reports.LoginSuccessReport;
import reports.ReportObserverConnector;
import sharedData.UserRoleEnum;

/**
 * @author Michael Umbelina
 * 
 * Tests the Launcher class functionality
 */
class TestLauncher {

	/**
	 * When created, it should show the login window only
	 * 
	 * @throws InterruptedException
	 */
	@Test
	void testLoginState() throws InterruptedException {
		LoginUI login = new LoginUI();

		Launcher machine = new Launcher();
		
		assertEquals(login.getClass(), machine.getState().getClass());
		login.setVisible(false);
	}

	/**
	 * Test if loginCaseWorker method switches from login window to main window
	 * 
	 * @throws InterruptedException
	 */
	@Test
	void testCaseWorkerState() throws InterruptedException {
		LoginSuccessReport loginReport = new LoginSuccessReport(UserRoleEnum.CASEWORKER, "username", 3);
		
		CaseWorkerMonitoringsWindow caseWorker = new CaseWorkerMonitoringsWindow(loginReport.getUserID(), loginReport.getUsername());
		caseWorker.setVisible(false);
		
		Launcher machine = new Launcher();
		machine.loginCaseWorker(loginReport);
		
		assertEquals(caseWorker.getClass(), machine.getState().getClass());
		caseWorker.dispose();
	}

	/**
	 * Test if a successful CaseWorker report calls loginCaseWorker
	 * 
	 * @throws InterruptedException
	 */
	@Test
	void testSupervisorState() throws InterruptedException {
		LoginSuccessReport loginReport = new LoginSuccessReport(UserRoleEnum.SUPERVISOR, "username", 1);
		
		SupervisorWindow supervisor = new SupervisorWindow(loginReport.getUserID(), loginReport.getUsername());
		supervisor.setVisible(false);
		
		Launcher machine = new Launcher();
		machine.loginSupervisor(loginReport);;
		
		assertEquals(supervisor.getClass(), machine.getState().getClass());
		supervisor.dispose();
	}
}
