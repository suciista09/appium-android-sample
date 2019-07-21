package page;

import driver.AppiumHelper;
import io.appium.java_client.android.AndroidElement;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage extends AppiumHelper {

    public static void clickElement(String elementName){
        getElement(elementName).click();
    }

    public static void clickElement(String elementName, int timeout){
        WebDriverWait wait = new WebDriverWait(AppiumHelper.getAndroidDriver(), timeout);
        AndroidElement element = (AndroidElement) wait.until(ExpectedConditions.visibilityOf(getElement(elementName)));
        element.click();
    }

    public static void typeElement(String elementName, String input){
        getElement(elementName).sendKeys(input);
    }

    public static void typeElement(String elementName, String input, int timeout){
        WebDriverWait wait = new WebDriverWait(AppiumHelper.getAndroidDriver(), timeout);
        AndroidElement element = (AndroidElement) wait.until(ExpectedConditions.visibilityOf(getElement(elementName)));
        element.sendKeys(input);
    }

    public static void verifyElementTobeVisible(String elementName, int timeout){
        WebDriverWait wait = new WebDriverWait(AppiumHelper.getAndroidDriver(), timeout);
        AndroidElement element = (AndroidElement) wait.until(ExpectedConditions.visibilityOf(getElement(elementName)));

        Assert.assertTrue("Element " + elementName + " is not visible yet after waiting for " + timeout + " seconds", element.isDisplayed());
    }

    public static boolean isElementDisplay(String elementName){
        return getElement(elementName).isDisplayed();
    }

    public static boolean isElementDisplay(String elementName, int timeout){
        try{
            WebDriverWait wait = new WebDriverWait(AppiumHelper.getAndroidDriver(), timeout);
            AndroidElement element = (AndroidElement) wait.until(ExpectedConditions.visibilityOf(getElement(elementName)));
            return element.isDisplayed();
        }catch (Exception e){
            return false;
        }




    }
}
