package reports;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class TestClientAnswerReport {

	/**
	 * Test to make sure we can create a ClientAnswerReport and that we can also
	 * populate the report with the list of answers.
	 */
	@Test
	void testClientAnswersReport() {

		List<String> lst = new ArrayList<>();

		lst.add("Yes I love you");
		lst.add("Kim's cool");

		ClientAnswerReport car = new ClientAnswerReport(lst);

		assertEquals(lst, car.getAnswers());

	}

}
