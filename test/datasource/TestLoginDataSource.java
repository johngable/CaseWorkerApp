package datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests User data gateway and enum accessibility
 * 
 * @author michaelpermyashkin
 *
 */
class TestLoginDataSource {

	/**
	 * Tests that a username that doesn't exist in the data source throws the
	 * correct exception
	 */
	@Test
	void testLoginDoesntExist() {
		Assertions.assertThrows(InvalidCredentialsException.class, () -> {
			new LoginRowDataGateway("user", "doesnt exist");
		});
	}

	/**
	 * Tests that a correct username and password returns true (succeeds)
	 * 
	 * @throws InvalidCredentialsException shouldn't
	 */
	@Test
	void testLoginComboDoesExist() throws InvalidCredentialsException {
		LoginRowDataGateway lds = new LoginRowDataGateway(UserLoginDataEnum.JaneSmith.getUsername(),
				UserLoginDataEnum.JaneSmith.getPassword());
		assertEquals(UserLoginDataEnum.JaneSmith.getRole(), lds.getRole());
		assertEquals(UserLoginDataEnum.JaneSmith.getUsername(), lds.getUserName());
		assertEquals(UserLoginDataEnum.JaneSmith.ordinal() + 1, lds.getUserID());
	}

	/**
	 * Tests that a correct username, but an incorrect password throws the
	 * appropriate exception (fails)
	 */
	@Test
	void testLoginWrongPassword() {
		Assertions.assertThrows(InvalidCredentialsException.class, () -> {
			new LoginRowDataGateway(UserLoginDataEnum.JaneSmith.getUsername(),
					UserLoginDataEnum.JaneSmith.getPassword() + "pineapple");
		});
	}

}
