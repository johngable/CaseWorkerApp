
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

/**
 * @author merlin
 *
 */
@RunWith(JUnitPlatform.class)
@SuiteDisplayName("Testing all of the packages")
@SelectPackages({"datasource", "model", "reports"})
public class AllTests {
}
