package reports;

import java.util.ArrayList;
import java.util.List;

/**
 * Receives a list of client answers and stores them in clientAnswers Can return
 * the list of clientAnswers
 * 
 * @author johngable
 */
public class ClientAnswerReport implements Report {

	private List<String> clientAnswers = new ArrayList<>(); // List of client answers
	private String status;

	/**
	 * Constructor to populate the clientAnswers list
	 * 
	 * @param clientAnswers
	 */
	public ClientAnswerReport(List<String> clientAnswers) {
		this.clientAnswers = clientAnswers;
	}

	/**
	 * Returns the clientAnswers list
	 * 
	 * @return clientAnswers
	 */
	public List<String> getAnswers() {
		return clientAnswers;
	}

}
