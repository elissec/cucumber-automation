package helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.security.SecureRandom;

public class Utilities {

    private Logger log4j = LogManager.getLogger(Utilities.class.getName());
    static { System.setProperty("log4j.configurationFile","log4j2.xml"); }
    private final GlobalVariables objGlobalVariables = new GlobalVariables();

    public static String getOS() {
        return System.getProperty("os.name").toLowerCase();
    }

    // -----------------------------------------------
    // Generates random string that contains 0-9,
    // A-Z, a-z based on the provided length.
    // -----------------------------------------------
    public String alphabetGenerator(int stringLength) {
        // Generates string from A-Z, a-z
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int charLength = chars.length();
        SecureRandom randomString = new SecureRandom();
        String generatedString = "";

        for (int i = 0; i < stringLength; i++) {
            generatedString += chars.charAt(randomString.nextInt(charLength));
        }

        return generatedString;
    }

    // -----------------------------------------------
    // Summary:
    // 		Generates random numeric string based on provided length and type
    // 	Parameters:
    // 		stringLength - length of the needed random string
    // -----------------------------------------------
    public static String numericStringGenerator(int stringLength) {
        String numeric = "0123456789";
        SecureRandom randomString = new SecureRandom();
        String generatedString = "";

        for (int i = 0; i < stringLength; i++) {
            generatedString += numeric.charAt(randomString.nextInt(numeric.length()));
        }
        return generatedString;
    }

    public void logInfo(String strMessage){
        log4j.info("[" + Thread.currentThread().getStackTrace()[2].getClassName() + "] [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] : " + strMessage);
    }

    public void logDebug(String strMessage){
        log4j.debug("[" + Thread.currentThread().getStackTrace()[2].getClassName() + "] [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] : " + strMessage);
    }

    public void logWarn(String strMessage){
        log4j.warn("[" + objGlobalVariables.strTestCaseNumber + "] " +  "[" + objGlobalVariables.strSessionId.substring(0,5) + "] [" + Thread.currentThread().getStackTrace()[2].getClassName() + "] [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] : " + strMessage);
    }

    public void logError(String strMessage){
        log4j.error("[" + Thread.currentThread().getStackTrace()[2].getClassName() + "] [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] : " + strMessage);
    }

    public void logFatal(String strMessage){
        log4j.fatal("[" + objGlobalVariables.strTestCaseNumber + "] " + "[" + objGlobalVariables.strSessionId.substring(0,5) + "] [" + Thread.currentThread().getStackTrace()[2].getClassName() + "] [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] : " + strMessage);
    }

    public void assertTrue(Boolean boolCondition, String strErrorMessage) {
        Assert.assertTrue(boolCondition, objGlobalVariables.strDebugMessage + " "+strErrorMessage);
    }

    public void assertFail(String strErrorMessage) {
        Assert.fail(objGlobalVariables.strDebugMessage + " "+strErrorMessage);
    }

    public void assertEquals(Object objActual, Object objExpected, String strErrorMessage) {
        Assert.assertEquals(objActual, objExpected, objGlobalVariables.strDebugMessage + " "+strErrorMessage);
    }

    public void assertNotEquals(Object objActual, Object objExpected, String strErrorMessage) {
        Assert.assertNotEquals(objActual, objExpected, objGlobalVariables.strDebugMessage + " "+strErrorMessage);
    }

    public void assertNull(Object objNull, String strErrorMessage) {
        Assert.assertNull(objNull, objGlobalVariables.strDebugMessage + " "+strErrorMessage);
    }

    public void assertNotNull(Object objNull, String strErrorMessage) {
        Assert.assertNotNull(objNull, objGlobalVariables.strDebugMessage + " "+strErrorMessage);
    }

}
