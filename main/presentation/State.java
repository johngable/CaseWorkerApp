package presentation;

/**
 * Interface for the concrete states
 * 
 * @author Michael Umbelina
 */
public interface State {
	/**
	 * Do the action associated with this state
	 * 
	 * @param machine
	 *            the machine this state is a part of
	 */
	public void doAction(Launcher machine);
}
