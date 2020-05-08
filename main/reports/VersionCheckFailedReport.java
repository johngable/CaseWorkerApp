package reports;

import sharedData.MonitoringDTO;

/**
 * Sent if the version check command finds that there are 
 * unsync changes in the DB
 * 
 * @author Michael Umbelina
 */
public class VersionCheckFailedReport implements Report {

		private MonitoringDTO failedUpload;
		
		public VersionCheckFailedReport (MonitoringDTO failedUpload) {
			this.failedUpload = failedUpload;
		}
		
		public MonitoringDTO getFailedDTO() {
			return failedUpload;
		}
}
