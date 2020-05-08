package presentation;

import java.awt.EventQueue;

/**
 * Runner class used to launch the application. The state machine is given
 * control of setting the current window being displayed
 * 
 * @author michaelpermyashkin
 *
 */
public class Runner {

	/**
	 * Main method of the runner class launches the application
	 * 
	 * @param args not used
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					new Launcher(); // State machine is given control of both windows

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

}
