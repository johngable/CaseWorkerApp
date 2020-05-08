package model;

import java.util.Vector;

/**
 * A queue to pass information between two threads
 * 
 * @author Merlin
 * 
 */
public class CommandQueue
{

	private Vector<Command> commands = new Vector<Command>();

	/**
	 * Put a message in the queue to the view.
	 * 
	 * @param infoPacket the view command to be added to the buffer
	 */
	public synchronized void queueInfoPacket(Command infoPacket)
	{
		commands.addElement(infoPacket);
		notify();
	}

	/**
	 * @return the next message that should be processed by the model
	 * @throws InterruptedException if this synchronized section is interrupted
	 */
	public synchronized Command getCommand() throws InterruptedException
	{
		notify();
		while (commands.size() == 0)
			wait();
		Command command = commands.firstElement();
		commands.removeElement(command);
		return command;
	}

	/**
	 * @return the number of commands that are waiting to be processed by the
	 *         view
	 */
	public int getQueueSize()
	{
		return commands.size();
	}

}
