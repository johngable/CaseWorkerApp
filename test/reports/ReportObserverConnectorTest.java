package reports;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.jupiter.api.Test;

/**
 * Tests the ReportObserverConnector to be sure you can add and remove listeners
 * @author merlin
 *
 */
public class ReportObserverConnectorTest {
	/**
	 * reset the singleton before each test
	 */
	@Before
	public void setUp() {
		ReportObserverConnector.resetSingleton();
	}

	/**
	 * Test the singleton functionality. First, make sure you get the same object
	 * and then reset the singleton and make sure you get a different one
	 */
	@Test
	public void isSingleton() {
		ReportObserverConnector connector = ReportObserverConnector.getSingleton();
		assertNotNull(connector);
		ReportObserverConnector connector2 = ReportObserverConnector.getSingleton();
		assertNotNull(connector2);
		assertSame(connector, connector2);
		ReportObserverConnector.resetSingleton();
		connector2 = ReportObserverConnector.getSingleton();
		assertNotSame(connector, connector2);
	}

	/**
	 * If we register the same observer for the same report type, we should ignore
	 * it
	 */
	@Test
	public void addingSameObserverTwiceIgnoresSecondCall() {
		// set up the connection
		ReportObserverConnector connector = ReportObserverConnector.getSingleton();
		ReportObserver mockObserver = EasyMock.createMock(ReportObserver.class);
		connector.registerObserver(mockObserver, TestReport.class);
		connector.registerObserver(mockObserver, TestReport.class);

		// we should expect an single update on notification
		mockObserver.receiveReport(EasyMock.anyObject(TestReport.class));
		EasyMock.replay(mockObserver);

		// now cause the notification
		connector.sendReport(new TestReport());
		EasyMock.verify(mockObserver);
	}

	/**
	 * Make sure that if we unregister an observer, it no longer gets updated on
	 * notification
	 */
	@Test
	public void canUnRegisterAnObserver() {
		// set up the connection
		ReportObserverConnector connector = ReportObserverConnector.getSingleton();
		ReportObserver mockObserver = EasyMock.createMock(ReportObserver.class);
		connector.registerObserver(mockObserver, TestReport.class);

		// no notification should be expected
		EasyMock.replay(mockObserver);

		connector.unregisterObserver(mockObserver, TestReport.class);
		ReportObserverConnector.getSingleton().sendReport(new TestReport());
		EasyMock.verify(mockObserver);
	}

	/**
	 * If we unregister an observer, adding subsequent observables for that report
	 * type should NOT connect them
	 */
	@Test
	public void unregistrationForgetsObserver() {
		// set up the connection
		ReportObserverConnector connector = ReportObserverConnector.getSingleton();
		ReportObserver mockObserver = EasyMock.createMock(ReportObserver.class);
		connector.registerObserver(mockObserver, TestReport.class);
		connector.unregisterObserver(mockObserver, TestReport.class);

		// no notification should be expected
		EasyMock.replay(mockObserver);

		ReportObserverConnector.getSingleton().sendReport(new TestReport());
		EasyMock.verify(mockObserver);
	}

	/**
	 * We just want to be sure that, if you ask to unregister for something you
	 * aren't connected to, we just ignore you.
	 */
	@Test
	public void observerUnregistrationWhenNotRegistered() {
		ReportObserverConnector connector = ReportObserverConnector.getSingleton();
		ReportObserver mockObserver = EasyMock.createMock(ReportObserver.class);
		connector.unregisterObserver(mockObserver, TestReport.class);
	}

	private class TestReport implements Report {

	}

}
