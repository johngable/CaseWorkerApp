package reports;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import datasource.UserLoginDataEnum;

/**
 * Test class for LoginReport
 * 
 * @author michaelpermyashkin
 *
 */
class TestLoginSuccessReport {
	/**
	 * Test initializing and access to the instance variable via getter
	 * 
	 * Testing authentication success and fail
	 */
	@Test
	void test() {
		UserLoginDataEnum jdoe = UserLoginDataEnum.JohnDoe;
		LoginSuccessReport lsr1 = new LoginSuccessReport(jdoe.getRole(), jdoe.getUsername(), jdoe.getUserID());
		assertEquals(jdoe.getRole(), lsr1.getRole());
		assertEquals(jdoe.getUsername(), lsr1.getUsername());
		assertEquals(jdoe.getUserID(), lsr1.getUserID());
	}
}
