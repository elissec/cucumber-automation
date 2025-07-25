package step_definitions;

import helpers.GlobalVariables;
import helpers.Utilities;
import io.cucumber.java.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import PageObject.PageObjectModel;
import webdriver.DriverController;

import java.io.File;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Hooks {

    private final DriverController objDriverController;
    private final GlobalVariables objGlobalVariables = new GlobalVariables();
    private final Utilities objUtilities = new Utilities();

    public Hooks (DriverController objDriverController) {
        this.objDriverController = objDriverController;
    }

    // -----------------------
    // Start of @Before tags.
    // These methods will run
    // BEFORE a test case is
    // executed.
    // -----------------------
    // On before tags, 0 runs first.
    @Before(order = 0)
    public void printTestName(Scenario scenario) {
        objGlobalVariables.strSessionId = scenario.getId();
        for(String tag : scenario.getSourceTagNames()){
            if (tag.contains("@qa-")){
                objGlobalVariables.strTestCaseNumber = tag.toUpperCase().replaceAll("@","");
            }
        }
        objGlobalVariables.strDebugMessage = "[DEBUG] [" + objGlobalVariables.strTestCaseNumber + "] " +  "[" + objGlobalVariables.strSessionId.substring(0,5) + "] [" + Thread.currentThread().getStackTrace()[2].getClassName() + "] [" + Thread.currentThread().getStackTrace()[2].getMethodName() +"] : ";

        scenario.log("----------------------------------------");
        scenario.log("• Starting test: [" + objGlobalVariables.strTestCaseNumber + "] ["+objGlobalVariables.strSessionId+"]" + scenario.getName());
        scenario.log("----------------------------------------");

        System.out.println("----------------------------------------");
        System.out.println("• Starting test: [" + objGlobalVariables.strTestCaseNumber + "] ["+objGlobalVariables.strSessionId+"] " + scenario.getName());
        System.out.println("----------------------------------------");
    }

    @Before(order = 1)
    public void startWebDriver(Scenario scenario) {
        boolean bSkipWebDriver = false;
        for(String tag : scenario.getSourceTagNames()){
            if(tag.contains("@api")){
                bSkipWebDriver = true;
                break;
            }
        }

        if(!bSkipWebDriver) {
            objDriverController.setWebDriver();
        }
    }

    // -----------------------
    // Start of @After tags.
    // These methods will be
    // ran AFTER a test
    // case has been executed.
    // -----------------------
    // Be sure to close the test / WebDriver last. When using @After tags, 0 is always the last to run.
    @After(order = 0)
    public void printResults(Scenario scenario) {
        scenario.log("----------------------------------------");
        scenario.log("• Test Case Completed: " + scenario.getName());
        scenario.log("• Test Case Result: " + scenario.getStatus());
        scenario.log("• Test Case Tags: " + scenario.getSourceTagNames());
        scenario.log("• Test Case Path: " + scenario.getUri() + ":" +scenario.getLine());
        scenario.log("• Test Case Session ID: " + scenario.getId());
        scenario.log("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");

        System.out.println("----------------------------------------");
        System.out.println("• Test Case Completed: " + scenario.getName());
        System.out.println("• Test Case Result: " + scenario.getStatus());
        System.out.println("• Test Case Tags: " + scenario.getSourceTagNames());
        System.out.println("• Test Case Path: " + scenario.getUri() + ":" +scenario.getLine());
        System.out.println("• Test Case Session ID: " + scenario.getId());
        System.out.println("----------------------------------------");
    }

    @After(order = 1)
    public void tearDown() {
        if(objDriverController.getDriver() != null) {
            objDriverController.getDriver().close();
            objDriverController.getDriver().quit();

            // Safari always leaves a window open after quit()
            // Uncomment in case safari needs to be fully quit and closed after a test is finished
            //if("safari".equals(System.getProperty("browser").toLowerCase())) {
            //	killSafariProcess();
            //}
        }
    }

    @After(order = 2)
    public void screenshotOnFail(Scenario scenario) {
        if (objDriverController.getDriver() != null && (scenario.isFailed() || scenario.getStatus().toString().equalsIgnoreCase("SKIPPED"))) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("MM-dd-yy_HH-mm-ss");
                Date date = new Date();
                String TCNum = scenario.getName().replace(" ", "_");
                byte[] screenshot = ((TakesScreenshot) objDriverController.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", TCNum);
                File scrFile = ((TakesScreenshot) objDriverController.getDriver()).getScreenshotAs(OutputType.FILE);
                String strPath = "target/cucumber-reports/failed_tests/" + dateFormat.format(date) + "_" + TCNum + ".png";
                FileUtils.copyFile(scrFile, new File((strPath)));
            } catch (Exception e) {
                objUtilities.logError(e.getMessage());
            }
        }
    }

//    @BeforeAll
//    public static void beforeAll() {
//        //This method runs before a feature file is executed. Method name must be 'beforeAll' to work.
//        System.out.println("Printing before the test suite...");
//    }
//
//    @BeforeStep
//    public void BeforeTestStep() {
//        //This method runs before every gherkin step..
//        System.out.println("Before test step...");
//        System.out.println("Step running in Thread #"+Thread.currentThread().getId());
//    }
//
//    @Before
//    public void BeforeTest() {
//        //This method runs before a scenario is executed.
//        System.out.println("Printing before test case runs...");
//        System.out.println("Test running in Thread #"+Thread.currentThread().getId());
//    }
//
//    @After
//    public void AfterTest() {
//        //This method runs after a scenario is executed.
//        System.out.println("Printing after test case runs...");
//    }
//
//    @AfterStep
//    public void AfterTestStep() {
//        //This method runs after every gherkin step..
//        System.out.println("After test step...");
//    }
//
//    @AfterAll
//    public static void afterAll() {
//        //This method runs after a feature file is executed. Method name must be 'afterAll' to work.
//        System.out.println("Printing after the test suite...");
//    }

}
