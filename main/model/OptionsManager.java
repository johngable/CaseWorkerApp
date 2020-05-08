package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Manage global options like whether we are using mock data sources
 *
 */
public class OptionsManager
{

	/**
	 * Used to get an existing singleton (it must have already been created). If it
	 * hasn't been created, you must use the getSingleton where you specify the
	 * testing mode
	 *
	 * @return the existing singleton
	 */
	public static synchronized OptionsManager getSingleton()
	{
		if (singleton == null)
		{
			singleton = new OptionsManager();
		}
		return singleton;
	}

	/**
	 * Reset our instance
	 */
	public static void resetSingleton()
	{
		singleton = null;
	}

	private static OptionsManager singleton;
	private boolean testMode;
	private String dbIdentifier;

	/**
	 * @return get the identifier of the DB we should be using
	 */
	public String getDbIdentifier()
	{
		if (dbIdentifier == null)
		{
			try {
				Scanner s = new Scanner(new File("config"));
				dbIdentifier = s.nextLine().trim();
				s.close();
			} catch (FileNotFoundException e) {
				return null;
			}
			
		}
		return dbIdentifier;
	}

	/**
	 * @param dbIdentifier defines which db we should use
	 */
	public void setDbIdentifier(String dbIdentifier)
	{
		this.dbIdentifier = dbIdentifier;
	}

	/**
	 * I'm a singleton
	 *
	 */
	private OptionsManager()
	{

	}



	/**
	 * returns true if this server is running on mock data for testing purposes
	 * where appropriate
	 *
	 * @return local mode
	 */
	public boolean isUsingMockDataSource()
	{
		return testMode;
	}





	/**
	 * @param b
	 *            if true, we will use mock data whenever possible
	 */
	public void setUsingMocKDataSource(boolean b)
	{
		this.testMode = b;
	}

}
