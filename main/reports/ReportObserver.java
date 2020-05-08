package reports;

/**
 * Required of everyone who is interested in receiving reports from the model.
 * 
 * @author Merlin
 *
 */
public interface ReportObserver
{

	/**
	 * Receive a report from the model
	 * 
	 * @param report the report
	 */
	public void receiveReport(Report report);
}
