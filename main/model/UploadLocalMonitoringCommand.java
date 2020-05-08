package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import datasource.MonitoringsRowDataGateway;
import datasource.MonitoringsRowDataGatewayMock;
import datasource.MonitoringsRowDataGatewayRDS;
import datasource.MonitoringsTableDataGateway;
import datasource.MonitoringsTableDataGatewayMock;
import reports.ReportObserverConnector;
import reports.VersionCheckFailedReport;
import reports.VersionCheckSuccessReport;
import sharedData.MonitoringDTO;
import sharedData.MonitoringStatusEnum;

/**
 * Uploads all local client changes to the main database
 * 
 * @author amy
 *
 */
public class UploadLocalMonitoringCommand implements Command {
	private List<MonitoringDTO> localMonitorings = new ArrayList<>();; // DTO that holds the local monitoring info to be uploaded
	private ReportObserverConnector roc = ReportObserverConnector.getSingleton(); // Used to send report
	private boolean versionCheck;
	private MonitoringsRowDataGateway gateway;

	/**
	 * Constructor for the command. Takes in a list of locally saved monitorings to upload.
	 */
	public UploadLocalMonitoringCommand(List<MonitoringDTO> localMonitorings, boolean versionCheck) {
		this.localMonitorings = localMonitorings;
		this.versionCheck = versionCheck;
	}
	
	/**
	 * Constructor for the command. Takes in a single monitoring. This is used when user chooses to upload a monitoring 
	 * with a version check fail. A single monitoring will be send back.
	 */
	public UploadLocalMonitoringCommand(MonitoringDTO singleLocalMonitoring, boolean versionCheck) {
		this.localMonitorings.add(singleLocalMonitoring);
		this.versionCheck = versionCheck;
	}

	/**
	 * Executes the command
	 */
	@Override
	public void execute() {

		for (MonitoringDTO m : localMonitorings) {

			// Creates the gateway based on the OptionsManager setting
			if (OptionsManager.getSingleton().isUsingMockDataSource()) {
				gateway = new MonitoringsRowDataGatewayMock(m.getMonitoringID());
			} else {
				gateway = new MonitoringsRowDataGatewayRDS(m.getMonitoringID());
			}

			if (versionCheck == true) {
				// The monitoring version upstream (in the database) is ahead of the local copy
				if (gateway.getVersion() > m.getVersion()) {
					VersionCheckFailedReport report = new VersionCheckFailedReport(m);
					roc.sendReport(report);
				}
				// The monitoring version upstream is behind and needs to be updated with the
				// local
				else {
					VersionCheckSuccessReport report = new VersionCheckSuccessReport();
					roc.sendReport(report);
					upload(m);
					deleteFile(m);
				}

			} else {
				upload(m);
				deleteFile(m);
			}
		}
	}

	/**
	 * Uploads a local monitoring to the database
	 * @param localMonitoring - the monitoring being uploaded
	 */
	public void upload(MonitoringDTO localMonitoring) {
		gateway.setMultiple(localMonitoring.getAnswers(), localMonitoring.getStatus(), localMonitoring.getVisitDate(),
				localMonitoring.getVersion());
	}
	
	/**
	 * delete file after successful upload
	 * @param m
	 */
	public void deleteFile(MonitoringDTO m) {
		Path path;
		String clientName = m.getClient().getClientName().replace(" ", "");
		if (gateway.getClass() == MonitoringsRowDataGatewayRDS.class) {
			path = Paths.get("downloads/"+ clientName + "/" + m.getMonitoringID() + ".json");
		} else {
			path = Paths.get("testDownloads/"+ clientName + "/" + m.getMonitoringID() + ".json");
		}
		
		try {
			Files.deleteIfExists(path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
