package helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import webdriver.DriverController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class SeleniumMethods {

    private final ConfigReader objConfigReader = new ConfigReader();
    private final Utilities objUtilities = new Utilities();
    private final GlobalVariables objGlobalVariables = new GlobalVariables();
    private final DriverController objDriverController;

    public SeleniumMethods(DriverController objDriverController) {
        this.objDriverController = objDriverController;
    }

    public static final byte bEleWaitTime = 30;

// -----------------------------------------------
// GLOSSARY
// -----------------------------------------------
// Driver navigators -------------------- [WD_NAV]
// Element waits ---------------------- [ELE_WAIT]
// Element controllers ---------------- [ELE_CTRL]
// Element assertions ----------------- [ELE_ASRT]
// Getter methods ----------------------- [MT_GET]
// Setter methods ----------------------- [MT_SET]
// -----------------------------------------------

    // -----------------------------------------------
// Driver navigators [WD_NAV]
// -----------------------------------------------
    public void goToUrl(String strUrl) {
        objDriverController.getDriver().get(strUrl);
    }

    public String getCurrentUrl() {
        return objDriverController.getDriver().getCurrentUrl();
    }


    // -----------------------------------------------
// Element waits [ELE_WAIT]
// -----------------------------------------------
    public void pauseScript() {
        try {
            Thread.sleep(Integer.parseInt(objConfigReader.readGlobalConfig("PAUSE_SCRIPT_MILLISECONDS")));
        } catch (Throwable e) {
            objUtilities.logDebug(e.getMessage());
        }
    }

    public void waitImplicitly(int seconds) {
        objDriverController.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    // -----------------------------------------------
    // Summary:
    //     Waits for the specified element to be clickable
    //     (Visible and Enabled)
    // Parameters:
    //     byEle - Element locator
    // -----------------------------------------------
    public WebElement waitForElementToBeClickable(By byEle) {
        WebDriverWait wait = new WebDriverWait(objDriverController.getDriver(), Duration.ofSeconds(bEleWaitTime));
        WebElement webEle = null;
        try {
            webEle = wait.until(ExpectedConditions.elementToBeClickable(byEle));
        } catch (Throwable e) {
            objUtilities.logDebug("Element " + byEle + ", is not clickable, retrying...");
            FluentWait<WebDriver> fluentWait = new FluentWait<>(objDriverController.getDriver())
                    .withTimeout(Duration.ofSeconds(bEleWaitTime))
                    .pollingEvery(Duration.ofSeconds(1))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(NullPointerException.class)
                    .ignoring(ElementNotInteractableException.class)
                    .ignoring(ElementClickInterceptedException.class);

            try {
                webEle = fluentWait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        if(!driver.findElement(byEle).isEnabled()){
                            throw new NoSuchElementException("Element is not enabled, continue...");
                        }
                        return driver.findElement(byEle);
                    }
                });
            } catch (Exception exception) {
                Assert.fail(objGlobalVariables.strDebugMessage + byEle + " Element is NOT clickable due to " + exception.getMessage());
            }
        }

        return webEle;
    }

    public void waitForPageToLoad() {
        WebDriverWait wait = new WebDriverWait(objDriverController.getDriver(), Duration.ofSeconds(60));
        wait.until(new Function<WebDriver, Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                        .equals("complete");
            }
        });
    }

    // -----------------------------------------------
// Element controllers [ELE_CTRL]
// -----------------------------------------------
    public void enterText(By byEle, String value) {
        getDisplayedElement(byEle).sendKeys(value);
    }

    public void clearText(By byEle) {
        getDisplayedElement(byEle).clear();
    }

    // -----------------------------------------------
    // Clear textbox and input a value
    // -----------------------------------------------
    public void clearTextboxBeforeInput(By byEle, String input) {
        clearText(byEle);
        if (!getElementValue(byEle).equals("")) {
            selectTextThenDeleteViaKeyboard(byEle);
        }
        enterText(byEle, input);
    }

    public void selectTextThenDeleteViaKeyboard(By byEle) {
        getDisplayedElement(byEle).sendKeys(Keys.chord(
                objUtilities.getOS().contains("mac") ? Keys.COMMAND : Keys.CONTROL, "a"), Keys.DELETE);
    }

    // -----------------------------------------------
    // Summary:
    //     Attempts to click an element using the
    //     WebDriver
    // Parameters:
    //     webEle - element to be clicked
    // -----------------------------------------------
    public void clickElement(By byEle) {
        objDriverController.getDriver().switchTo().window(objDriverController.getDriver().getWindowHandle());
        waitForElementToBeClickable(byEle).click();
    }

    // -----------------------------------------------
    // Summary:
    //     Attempts to submit a form
    // Parameters:
    //     webEle - element to be clicked
    // -----------------------------------------------
    public void submitElement(By byEle) {
        waitForElementToBeClickable(byEle).submit();
    }

    // -----------------------------------------------
    // Summary:
    //     Attempts to click an element using the
    //     JavaScriptExecutor, in case the WebDriver
    //     click method does not work
    // Parameters:
    //     webEle - element to be clicked
    // -----------------------------------------------
    public void jseClickElement(By byEle) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) objDriverController.getDriver();
            jse.executeScript("arguments[0].click();", waitForElementToBeClickable(byEle));
        } catch (Throwable e) {
            objUtilities.logDebug("Can't click element using javascript executor: " + e.getMessage());
        }
    }

    // -----------------------------------------------
    // Summary:
    //     Hovers over a specific element then clicks
    //     another element
    // Parameters:
    //     hoverOver - element to be hovered over
    //     webEle - element to be clicked
    // -----------------------------------------------
    public void hoverWithClick(By hoverOver, By byEle) {
        waitForPageToLoad();
        Actions action = new Actions(objDriverController.getDriver());
        action.moveToElement(getDisplayedElement(hoverOver)).build().perform();
        waitForElementToBeClickable(byEle);
        action.moveToElement(waitForElementToBeClickable(byEle)).click().perform();
        waitForPageToLoad();
    }

    // -----------------------------------------------
    // Summary:
    //     Refresh the page
    // Parameters:
    //     N/A
    // -----------------------------------------------
    public void refreshPage() {
        objDriverController.getDriver().navigate().refresh();
        waitForPageToLoad();
    }

    // -----------------------------------------------
// Getter methods [MT_GET]
// -----------------------------------------------
// -----------------------------------------------
// Summary:
//     Returns the element when found in DOM.
// Parameters:
//     byEle - Element locator
// -----------------------------------------------
    public WebElement getPresentElement(By byEle){
        WebElement webEle = null;
        WebDriverWait wait = new WebDriverWait(objDriverController.getDriver(), Duration.ofSeconds(bEleWaitTime));

        try {
            webEle = wait.until(ExpectedConditions.presenceOfElementLocated(byEle));
        } catch (Throwable e) {
            objUtilities.logDebug("Can't find element " + byEle + ", retrying...");
            FluentWait<WebDriver> fluentWait = new FluentWait<>(objDriverController.getDriver())
                    .withTimeout(Duration.ofSeconds(bEleWaitTime))
                    .pollingEvery(Duration.ofSeconds(1))
                    .ignoring(NoSuchElementException.class);
            try {
                webEle = fluentWait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        return driver.findElement(byEle);
                    }
                });
            }catch (Exception exception){
                Assert.fail(objGlobalVariables.strDebugMessage + byEle + " Element is NOT found in DOM due to " + exception.getMessage());
            }
        }
        return webEle;
    }

    // -----------------------------------------------
    // Summary:
    //     Checks if the element is present in the DOM.
    // Parameters:
    //     byEle - Element locator
    // -----------------------------------------------
    public boolean isElementPresent(By byEle) {
        try {
            return getPresentElement(byEle) != null;
        } catch (AssertionError ae){
            return false;
        }
    }

    // -----------------------------------------------
    // Summary:
    //     Checks if the element is displayed
    // Parameters:
    //     byEle - Element locator
    // -----------------------------------------------
    public boolean isElementDisplayed(By byEle) {
        Boolean boolIsDisplayed = false;
        WebDriverWait wait = new WebDriverWait(objDriverController.getDriver(), Duration.ofSeconds(bEleWaitTime));

        try {
            boolIsDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(byEle)) != null;
        } catch (Throwable e) {
            objUtilities.logDebug("Element " + byEle + ", is not visible, retrying...");
            FluentWait<WebDriver> fluentWait = new FluentWait<>(objDriverController.getDriver())
                    .withTimeout(Duration.ofSeconds(bEleWaitTime))
                    .pollingEvery(Duration.ofSeconds(1))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .ignoring(NullPointerException.class);
            try {
                boolIsDisplayed = fluentWait.until(new Function<WebDriver, Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return driver.findElement(byEle).isDisplayed();
                    }

                });
            } catch (Exception exception) {
                objUtilities.logDebug("Setting isDisplayed to false due to " + exception.getMessage());
            }
        }

        return boolIsDisplayed;
    }

    // -----------------------------------------------
    // Summary:
    //     Returns the element if it is displayed
    // Parameters:
    //     byEle - Element locator
    // -----------------------------------------------
    public WebElement getDisplayedElement(By byEle) {
        if(!isElementDisplayed(byEle)){
            Assert.fail(objGlobalVariables.strDebugMessage + "Element " + byEle + " is NOT displayed.");
        }
        return objDriverController.getDriver().findElement(byEle);
    }

    public String getElementText(By byEle) {
        return getDisplayedElement(byEle).getText();
    }

    public String getElementValue(By byEle) {
        return getDisplayedElement(byEle).getAttribute("value");
    }

    public String getDropdownSelectedOptionText(By byEle) {
        return new Select(getDisplayedElement(byEle)).getFirstSelectedOption().getText();
    }

    public List<WebElement> getDropdownOptions(By selectEle) {
        return new Select(getDisplayedElement(selectEle)).getOptions();
    }

    public List<String> getDropdownOptionsText(By selectEle) {
        List<WebElement> availableDropdownOptionsElements = getDropdownOptions(selectEle);
        List<String> availableDropdownOptions = new ArrayList<>();

        for (int i = 0; i < availableDropdownOptionsElements.size(); i++) {
            availableDropdownOptions.add(availableDropdownOptionsElements.get(i).getText().trim());
        }

        return availableDropdownOptions;
    }

    public String getElementAttribute(By byEle, String att) {
        return getPresentElement(byEle).getAttribute(att);
    }

    public List<WebElement> getAllElements(By byEle) {
        List<WebElement> webElements = new ArrayList<>();
        try {
            webElements = getPresentElement(byEle).findElements(byEle);
        } catch (NoSuchElementException e) {
            objUtilities.logDebug("No elements found for " + byEle + ", returning empty list.");
            FluentWait<WebDriver> fluentWait = new FluentWait<>(objDriverController.getDriver())
                    .withTimeout(Duration.ofSeconds(bEleWaitTime))
                    .pollingEvery(Duration.ofSeconds(1))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .ignoring(NullPointerException.class);
            try {
                webElements = fluentWait.until(new Function<WebDriver, List<WebElement>>() {
                    public List<WebElement> apply(WebDriver driver) {
                        return driver.findElements(byEle);
                    }
                });
            } catch (Exception exception) {
                objUtilities.logDebug("Setting webElements to empty list due to " + exception.getMessage());
            }
        }
        return webElements;
    }

    // -----------------------------------------------
    // Summary:
    //     Checks if the element is NOT displayed
    // Parameters:
    //     byEle - Element locator
    // -----------------------------------------------
    public boolean isElementHidden(By byEle) {
        boolean boolIsHidden;
        WebDriverWait wait = new WebDriverWait(objDriverController.getDriver(), Duration.ofSeconds(bEleWaitTime));
        try {
            boolIsHidden = wait.until(ExpectedConditions.invisibilityOfElementLocated(byEle));
        } catch (Throwable e) {
            objUtilities.logDebug("Element " + byEle + ", is still displayed, retrying...");
            FluentWait<WebDriver> fluentWait = new FluentWait<>(objDriverController.getDriver())
                    .withTimeout(Duration.ofSeconds(bEleWaitTime))
                    .pollingEvery(Duration.ofSeconds(1));

            try {
                boolIsHidden = fluentWait.until(new Function<WebDriver, Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return !driver.findElement(byEle).isDisplayed();
                    }
                });
            }catch (TimeoutException tme){
                objUtilities.logDebug("Setting isHidden to false due to " + tme.getMessage());
                boolIsHidden = false;
            }catch (NullPointerException | NoSuchElementException npe){
                objUtilities.logDebug("Setting isHidden to true due to " + npe.getMessage());
                boolIsHidden = true;
            }
        }
        return boolIsHidden;
    }

    // -----------------------------------------------
    // Summary:
    //     Checks if the element is NOT present, even
    //     in the DOM.
    // Parameters:
    //     byEle - Element locator
    // -----------------------------------------------
    public boolean isElementAbsent(By byEle) {
        boolean boolIsAbsent = false;
        FluentWait<WebDriver> fluentWait = new FluentWait<>(objDriverController.getDriver())
                .withTimeout(Duration.ofSeconds(bEleWaitTime))
                .pollingEvery(Duration.ofSeconds(1));
        try {
            boolIsAbsent = fluentWait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return !(driver.findElements(byEle).size() > 0);
                }
            });
        } catch (Exception exception){
            objUtilities.logDebug("Setting isAbsent to false due to " + exception.getMessage());
        }
        return boolIsAbsent;
    }

    // -----------------------------------------------
    // Summary:
    //     Verify if an alert is present.
    // Parameters:
    //     intTimeToWait - wait time in seconds
    // -----------------------------------------------
    public boolean verifyAlertPresence(int intTimeToWait) {
        try {
            WebDriverWait wait = new WebDriverWait(objDriverController.getDriver(), Duration.ofSeconds(intTimeToWait));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (NoAlertPresentException e) {
            objUtilities.logInfo("Alert is not present.");
            return false;
        }
    }




}