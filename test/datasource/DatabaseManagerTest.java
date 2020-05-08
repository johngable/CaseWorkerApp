package datasource;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.OptionsManager;

/**
 * Test the behavior of the database manager.
 */
public class DatabaseManagerTest extends DatabaseTest
{

	/**
	 * @throws DatabaseException
	 *             shouldn't
	 *
	 */
	@BeforeEach
	public void setTestMode() throws DatabaseException
	{
		OptionsManager.getSingleton().setUsingMocKDataSource(true);
		DatabaseManager.reset();
	}
	
	/**
	 * Tests if the singleton is the same
	 * @throws DatabaseException shouldn't
	 */
	@Test
	public void isSingleton() throws DatabaseException
	{
		DatabaseManager dm = DatabaseManager.getSingleton();
		DatabaseManager dm1 = DatabaseManager.getSingleton();
		
		assertSame(dm, dm1);
		DatabaseManager.reset();
		assertNotSame(dm, DatabaseManager.getSingleton());
	}

	/**
	 * @throws DatabaseException
	 *             If there is an error adding the connection.
	 * @throws SQLException
	 *             If there is an error closing the connection.
	 */
	@Test
	public void testCreateConnectionForId() throws DatabaseException, SQLException
	{
		Connection connection = null;

		try
		{
			assertNull(DatabaseManager.getSingleton().getConnection(7));
			DatabaseManager.getSingleton().addConnection(7);

			connection = DatabaseManager.getSingleton().getConnection(7);
			assertNotNull(connection);
			assertTrue(connection instanceof Connection);
			
		} finally
		{
			if (connection != null)
			{
				connection.close();
			}
		}
	}

	/**
	 * @throws DatabaseException
	 *             If there is an error adding the connection.
	 * @throws SQLException
	 *             If there is an error closing the connection.
	 */
	@Test
	public void testIdMapsToConnection() throws DatabaseException, SQLException
	{
		Connection connection = null;

		try
		{
			assertNull(DatabaseManager.getSingleton().getConnection(7));
			DatabaseManager.getSingleton().addConnection(7);

			connection = DatabaseManager.getSingleton().getConnection(7);
			assertEquals(connection, DatabaseManager.getSingleton().getConnection(7));
		} finally
		{
			if (connection != null)
			{
				connection.close();
			}
		}
	}

}
