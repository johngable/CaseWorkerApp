package model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datasource.MonitoringsMockDatabase;
import datasource.MonitoringsTableDataGatewayMock;
import reports.MonitoringListReport;
import reports.Report;
import reports.ReportObserver;
import reports.ReportObserverConnector;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

class TestAdvancedSearchCommand implements ReportObserver {
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton();
	private MonitoringListReport report;

	@BeforeEach
	public void setup() {
		OptionsManager.getSingleton().setUsingMocKDataSource(true);
		MonitoringsMockDatabase.resetSingleton();
	}

	/**
	 * Method to create a localdatetime for the ease of testing
	 * 
	 * @param minusMonth - how many months back to go
	 * @param minusDay   - how many days back to go
	 * @return
	 */
	public static LocalDateTime formatDate(int minusMonth, int minusDay) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String formatDateTime = LocalDateTime.now().minusMonths(minusMonth).minusDays(minusDay).format(formatter);
		LocalDateTime dateTime = LocalDateTime.parse(formatDateTime, formatter);
		return dateTime;
	}

	/**
	 * Test to ensure the advanced search command can find the monitorings with a
	 * full constructor of filter material.
	 */
	@Test
	void testFullConstructor() {
		roc.registerObserver(this, MonitoringListReport.class);
		MonitoringsTableDataGatewayMock gateway = new MonitoringsTableDataGatewayMock();
		Client client = new Client("Amy Jones", 1);

		List<MonitoringDTO> monitorings = gateway.filterByNameStatusDate(3, client,
				MonitoringStatusEnum.PENDING_FOR_CORRECTION, formatDate(1, 2).toLocalDate(),
				formatDate(0, 0).toLocalDate(), "visitDate");

		AdvancedSearchCommand cmd = new AdvancedSearchCommand(3, client, MonitoringStatusEnum.PENDING_FOR_CORRECTION,
				formatDate(1, 2), formatDate(0, 0), "visitDate");
		cmd.execute();

		List<MonitoringDTO> list = report.getMonitorings();
		assertEquals(monitorings.size(), list.size());
	}

	/**
	 * Receives the report from the command
	 */
	@Override
	public void receiveReport(Report report) {
		if (report.getClass() == MonitoringListReport.class) {
			this.report = (MonitoringListReport) report;
		}
	}

}
