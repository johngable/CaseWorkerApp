package presentation;

import reports.LoginSuccessReport;

import reports.Report;
import reports.ReportObserver;
import reports.ReportObserverConnector;
import sharedData.UserRoleEnum;

/**
 * @author Michael Umbelina
 * 
 *         Controls which window is displayed. Uses ReportObserver to know when
 *         a login is successful or failed.
 */
public class Launcher implements ReportObserver {
	private LoginUI login;
	private CaseWorkerMonitoringsWindow cwMonitoringWindow;
	private AbstractUserWindow supWindow;

	private State state;
	/**
	 * Create a Launcher
	 * @param m CaseWorkerWindow after login
	 */
	public Launcher() {
		login = new LoginUI(); // Login Window Object
		login.setVisible(true);
		login.doAction(this);
		// registers state machine as observer of the login report
		ReportObserverConnector.getSingleton().registerObserver(this, LoginSuccessReport.class);
	}

	/**
	 * State the current state of the Launcher
	 * 
	 * @param state the new state called by the UI windows
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return Get the current state of the Launcher
	 */
	public State getState() {
		return state;
	}

	/**
	 * Set login window to not visible, main window to visible, and set the current
	 * state to CaseWorkerWindow
	 */

	public void loginCaseWorker(LoginSuccessReport loginReport) {
		login.setVisible(false);
		cwMonitoringWindow = new CaseWorkerMonitoringsWindow(loginReport.getUserID(), loginReport.getUsername());
		cwMonitoringWindow.setWindowVisible(true);
		cwMonitoringWindow.doAction(this);

	}
	
	
	/**
	 * Set login window to not visible, supervisor window to visible, and set current state
	 * to SupervisorWindow
	 */
	public void loginSupervisor(LoginSuccessReport loginReport) {
		login.setVisible(false);
		supWindow = new SupervisorWindow(loginReport.getUserID(), loginReport.getUsername());
		supWindow.setVisible(true);
		supWindow.doAction(this);
	}

	/**
	 * Receive a report from the model
	 * 
	 * @param report the LoginReport received from ReportObserverConnector
	 */
	public void receiveReport(Report report) {
		if (report.getClass() == LoginSuccessReport.class) {
			LoginSuccessReport rep = (LoginSuccessReport) report;
			if (rep.getRole().toString() == UserRoleEnum.CASEWORKER.toString()) {
				loginCaseWorker(rep);
			}
			if (rep.getRole().toString() == UserRoleEnum.SUPERVISOR.toString()) {
				loginSupervisor(rep);

			}
		}
	}
}
