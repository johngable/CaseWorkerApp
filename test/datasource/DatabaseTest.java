package datasource;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import model.OptionsManager;

/**
 * Defines set up and tear down methods that set up the DB for testing and roll
 * back changes when the test is over
 *
 * @author Merlin
 *
 */
public abstract class DatabaseTest
{

	/**
	 * set up the database and other singletons for a test
	 *
	 * @throws DatabaseException shouldn't
	 *
	 */
	@BeforeEach
	public void setUp() throws DatabaseException
	{
		DatabaseManager.getSingleton().setTesting();
		OptionsManager.getSingleton().setUsingMocKDataSource(false);
	}

	/**
	 * @throws DatabaseException shouldn't
	 * @throws SQLException shouldn't
	 *
	 */
	@AfterEach
	public void tearDown() throws DatabaseException, SQLException
	{
		DatabaseManager.getSingleton().rollBack();
	}
}
