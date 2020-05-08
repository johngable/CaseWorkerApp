package reports;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class TestCaseWorkerListReport {

  @Test
  void testCaseWorkerListReport() {

    List<String> lst = new ArrayList<>();

    lst.add("Bing Bong");
    lst.add("Joe Shmoe");

    ClientAnswerReport car = new ClientAnswerReport(lst);

    assertEquals(lst, car.getAnswers());

  }

}
