package presentation;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;

import model.Client;
import model.CreateNewMonitoringByDateCommand;
import model.ModelFacade;

/**
 * Creates a window to let a caseworker choose a date and a client and schedule a monitoring
 * @author Amy Minnier
 *
 */
public class ScheduleNewMonitoringWindow extends JFrame implements ActionListener {

	private int caseWorkerID;
	private int clientID;

	JPanel mainPanel = new JPanel();
	private JDateChooser datePicker = new JDateChooser();
	private JButton btnGetDate;
	private JComboBox<Client> clientDropDown;
	private DefaultComboBoxModel<Client> clientModel;
	private ModelFacade remote = ModelFacade.getSingleton(); // Singleton command queue
	private List<Client> clientList;
	private Client selectedClient;

	/**
	 * Create the application.
	 */
	public ScheduleNewMonitoringWindow(int caseWorkerID, List<Client> clientList) {
		setAlwaysOnTop(true);
		this.caseWorkerID = caseWorkerID;
		this.clientList = clientList;

		clientModel = new DefaultComboBoxModel<Client>();

		for (Client c : clientList) {
			clientModel.addElement(c);
		}

		createClientMenu();
		initialize();
		setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().add(mainPanel);
		mainPanel.setLayout(null);

		datePicker.setBounds(97, 123, 236, 35);
		Date current = new Date();
		datePicker.setDate(current);
		mainPanel.add(datePicker);

		btnGetDate = new JButton("Schedule Monitoring");
		btnGetDate.setBounds(119, 190, 182, 35);
		btnGetDate.addActionListener(this);
		mainPanel.add(btnGetDate);
	}

	/**
	 * creates the client selection dropdown
	 */
	private void createClientMenu() {
		clientDropDown = new JComboBox<Client>();
		clientDropDown.setModel(clientModel);
		// override to populate the JComboBox with a Client Object
		clientDropDown.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Client) {
					Client client = (Client) value;
					setText(client.getClientName()); // displays only name
				}
				return this;
			}
		});

		clientDropDown.setBounds(97, 70, 236, 28);
		mainPanel.add(clientDropDown);
	}

	/**
	 * Performs the action for btnGetDate
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnGetDate) {
			selectedClient = (Client) clientDropDown.getSelectedItem();
			clientID = selectedClient.getClientID();
			LocalDateTime date = datePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			remote.queueCommand(new CreateNewMonitoringByDateCommand(date, clientID, caseWorkerID));
			dispose();
		}
	}
}
