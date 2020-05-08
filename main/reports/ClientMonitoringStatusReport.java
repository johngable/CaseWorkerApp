package reports;

import sharedData.MonitoringStatusEnum;

/**
 * Report that holds the status of a monitoring
 * 
 * @author kimberlyoneill
 *
 */
public class ClientMonitoringStatusReport implements Report {

  private MonitoringStatusEnum status;

  /**
   * Constructor to create report
   * 
   * @param status
   */
  public ClientMonitoringStatusReport(MonitoringStatusEnum status) {
    this.status = status;
  }

  /**
   * getter for status
   * 
   * @return
   */
  public MonitoringStatusEnum getStatus() {
    return this.status;
  }

}
