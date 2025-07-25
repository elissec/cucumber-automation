package feature_files;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.DataProvider;

public class RunCucumberIT extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        // This method acts like a @BeforeClass / @BeforeSuite
        // Methods / logic that needs to be instantiated before the whole test starts should be declared here.

        if(System.getProperty("TEST_THREADS") == null) {
            System.setProperty("dataproviderthreadcount", "5");
        } else {
            System.setProperty("dataproviderthreadcount", System.getProperty("TEST_THREADS"));
        }

        return super.scenarios();
    }

}
