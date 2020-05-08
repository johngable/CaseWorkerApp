package model;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.RowListProcessor;

import datasource.ClientEnum;
import reports.ClientListReport;
import reports.ReportObserverConnector;

/**
 * Gets clients for a caseworker and sends client list report
 * 
 * @author Kim O./John G :D
 *
 */
public class GetClientsByCaseWorkerCommand implements Command {
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton(); // Used to send report

	private int caseWorkerID;

	/**
	 * Given CaseWorker
	 * 
	 * @param caseWorkerID
	 */
	public GetClientsByCaseWorkerCommand(int caseWorkerID) {
		this.caseWorkerID = caseWorkerID;
	}

	/**
	 * Goes through ClientEnum and pulls out all clients of given CaseWorker
	 */
	@Override
	public void execute() {
		List<Client> clients = new ArrayList<Client>();
		for (ClientEnum c : ClientEnum.values()) {
			if (c.getCaseWorkerID() == this.caseWorkerID) {
				Client client = new Client(c.getClientName(), c.getClientID());
				clients.add(client);
			}
		}
		
		ClientListReport report = new ClientListReport(clients);
		roc.sendReport(report);
	}
}
