package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import reports.DownloadFailedReport;
import reports.DownloadSuccessReport;

import javax.swing.JButton;
import javax.swing.JLabel;

public class DownloadPopUp extends JFrame implements ActionListener{

	private JPanel contentPane;
	private DownloadSuccessReport successReport;
	private JButton btnOkay;

	/**
	 * Constructor if the download is a failure.
	 * @param df the failed report
	 */
	public DownloadPopUp(DownloadFailedReport df) {
		initialize();
	}
	
	/**
	 * Constructor for when the download is a success.
	 * @param ds the success report
	 */
	public DownloadPopUp(DownloadSuccessReport ds) {
		successReport = ds;
		initialize();
	}
	
	
	/**
	 * Initialization window to set the text for what will be display.
	 */
	public void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnOkay = new JButton("Okay");
		btnOkay.setBounds(171, 183, 89, 23);
		btnOkay.addActionListener(this);
		contentPane.add(btnOkay);
		JLabel lblDownloadMessage = new JLabel("");
		if(successReport != null) {
			lblDownloadMessage.setText("Downloaded Successfully.");
		}else {
			lblDownloadMessage.setText("Download Failed.");
		}
		lblDownloadMessage.setBounds(193, 117, 46, 14);
		contentPane.add(lblDownloadMessage);
	}
	
	/**
	 * Handles button press events for the buttons
	 * 
	 * @param e event that occurred
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOkay) {
			dispose();
		}
	}
}
