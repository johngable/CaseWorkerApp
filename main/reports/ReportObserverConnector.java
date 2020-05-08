package reports;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * Portions of the system that are outside of the model need to be notified when
 * the mopdel's state changes. In other words, they need to be observers of a
 * variety of places within the Model. However, we don't want things that are
 * outside of the model to have knowledge of where in the model those place are.
 * Therefore, we have created the ReportObserverConnector (which is a singleton)
 * on the edge of the Model. Entities outside of (or inside) the model can use
 * this to become an observer of all of the places within the model that report
 * messages of a given type on state change.
 * 
 * Changes are reflected in Report objects and we want to make sure that each
 * observer only gets the reports in which it is interested. Therefore,
 * ReportObserverConnector allows the observer to specify exactly which type of
 * report they want to receive. For example, registering to receive a report
 * that a Thing was added:
 * 
 * <p>
 * 
 * <pre>
 * ReportObserverConnector.getSingleton().registerObserver(this, ThingAddedReport.class);
 * </pre>
 * 
 * 
 * ThingAddedReport must implement Report and presumably has fields and getters
 * containing the pieces of information necessary to describe what has happened
 * (perhaps a ThingID and some attributes of that Thing?).
 * 
 * @author Merlin
 * 
 */
public class ReportObserverConnector {

	private static ReportObserverConnector singleton;

	private HashMap<Class<? extends Report>, ArrayList<ReportObserver>> observers;

	private ReportObserverConnector() {
		observers = new HashMap<Class<? extends Report>, ArrayList<ReportObserver>>();
	}

	/**
	 * @return the only one of these in the system
	 */
	public synchronized static ReportObserverConnector getSingleton() {
		if (singleton == null) {
			singleton = new ReportObserverConnector();
		}
		return singleton;
	}

	/**
	 * 
	 */
	public static void resetSingleton() {
		singleton = null;
	}

	/**
	 * Distributes a given report to everyone who is interested in reports of that
	 * type
	 * 
	 * @param report the report
	 */
	public void sendReport(Report report) {
		synchronized (this) {
			ArrayList<ReportObserver> relevantObservers = observers.get(report.getClass());
			if (relevantObservers != null) {
				for (ReportObserver a : relevantObservers) {
					a.receiveReport(report);
				}
			}
		}
	}

	/**
	 * Used when an observer wants to receive reports of a given type
	 * 
	 * @param observer   the observer who is interested
	 * @param reportType the report type the observer wants to receive
	 */
	public synchronized void registerObserver(ReportObserver observer, Class<? extends Report> reportType) {
		rememberObserver(observer, reportType);
	}

	/**
	 * @param observer   the observer we should remember
	 * @param reportType the report type this observer is interested in
	 * @return true if this is a new observer for this report type and false if it
	 *         was a duplicate request
	 */
	private boolean rememberObserver(ReportObserver observer, Class<? extends Report> reportType) {
		ArrayList<ReportObserver> relevantObservers = observers.get(reportType);
		if (relevantObservers == null) {
			relevantObservers = new ArrayList<ReportObserver>();
			observers.put(reportType, relevantObservers);
		}
		if (!relevantObservers.contains(observer)) {
			relevantObservers.add(observer);
			return true;
		}
		return false;
	}

	/**
	 * This is called when an observer no longer wants to receive reports of a given
	 * type
	 * 
	 * @param observer   the observer who is no longer interested
	 * @param reportType the report types they no longer want to receive
	 */
	public void unregisterObserver(ReportObserver observer, Class<? extends Report> reportType) {
		synchronized (this) {
			ArrayList<ReportObserver> observerList = observers.get(reportType);
			if (observerList != null) {
				observerList.remove(observer);
			}
		}
	}

	/**
	 * Allows an observer to see if it is currently listening to a given report type
	 * 
	 * @param obs        the observer
	 * @param reportType the report type
	 * @return true if the observer is hooked up for that report type
	 */
	public boolean doIObserve(ReportObserver obs, Class<? extends Report> reportType) {
		synchronized (this) {
			ArrayList<ReportObserver> relavantObservers = observers.get(reportType);
			if (relavantObservers == null) {
				return false;
			}
			return relavantObservers.contains(obs);
		}
	}
}
