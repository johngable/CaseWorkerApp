package reports;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import model.Client;

class TestDownloadedClientListReport {

	@Test
	void testDownloadedClientListReport() {
		
		Client c1 = new Client("name", 1);
		List<Client> expected = new ArrayList<>();
		expected.add(c1);
		
		DownloadedClientListReport report = new DownloadedClientListReport(expected);
		
		assertEquals(expected, report.getClientList());
	}
}
