package driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.net.UrlChecker;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class AppiumHelper {

    private static final Logger LOGGER = LogManager.getLogger(AppiumHelper.class);

    private static final String APPIUM_LOG_FILE_PATH = System.getProperty("user.dir") + "/appium.log";
    private static final String APPIUM_LOG_LEVEL = "error";

    private static boolean initialized = false;
    private static URL appiumServerUrl;
    private static AppiumDriverLocalService appiumService;
    private static DesiredCapabilities capabilities;
    private static AndroidDriver<AndroidElement> androidDriver;
    private static Properties properties = null;

    public static void initialize(){
        if (!initialized){

            //run appium
            startAppiumServer();

            //set capabilities
            setCapabilities();

            //loadDriver
            loadDriver();

            //loadElementProperties
            loadElementProperties();

            initialized = true;
        }

    }

    private static void loadDriver() {
        try {
            androidDriver = new AndroidDriver<AndroidElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static void setCapabilities() {
        capabilities = new DesiredCapabilities();

        // Set Desired Capabilities
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);
        capabilities.setCapability(AndroidMobileCapabilityType.ANDROID_INSTALL_TIMEOUT, 240000);
        capabilities.setCapability("uiautomator2ServerInstallTimeout", 240000);
        capabilities.setCapability("uiautomator2ServerLaunchTimeout", 240000);
        HashMap<String, String> customFindModules = new HashMap<>();
        customFindModules.put("ai", "test-ai-classifier");

        capabilities.setCapability("customFindModules", customFindModules);
        capabilities.setCapability("shouldUseCompactResponses", false);

        // Set Desired Capabilities from Property File
        Properties capabilitiesProperties = getCapabilitiesProperties();
        for (Map.Entry<Object, Object> capability : capabilitiesProperties.entrySet()) {
            capabilities.setCapability(capability.getKey().toString(), capability.getValue());
        }
    }

    private static Properties getCapabilitiesProperties() {
        Properties capabilitiesProperties = new Properties();
        try {
            capabilitiesProperties.load(new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/capabilities/realdevice.caps.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return capabilitiesProperties;
    }

    private static void startAppiumServer(){

        //starting appium server
        File appiumLogFile = new File(APPIUM_LOG_FILE_PATH);
        appiumService = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().
                usingAnyFreePort().withArgument(GeneralServerFlag.LOG_LEVEL, APPIUM_LOG_LEVEL).withLogFile(appiumLogFile));
        appiumService.start();

        //check appium server if it's successfully run
        URL status = null;
        try {
            appiumServerUrl = new URL(getAppiumServerUrl());
            status = new URL(appiumServerUrl + "/sessions");
        } catch (MalformedURLException e) {
            throw new ExceptionInInitializerError("Malformed appium URL : " + e.getMessage());
        }

        try {
            new UrlChecker().waitUntilAvailable(50, TimeUnit.SECONDS, status);
        } catch (UrlChecker.TimeoutException e) {
            throw new ExceptionInInitializerError("Timeout while waiting Appium server to be ready : " + e.getMessage());
        }

        LOGGER.info("Appium server started successfully at :" + getAppiumServerUrl());

    }

    private static String getAppiumServerUrl() {
        LOGGER.info("Appium Server URL:" + String.format("%s/wd/hub", appiumService.getUrl().toString()));
        return appiumService.getUrl().toString();
    }

    public static AndroidElement getLocatorFromValue(String value) {
        if (!value.contains("_")) {
            LOGGER.error(String.format("Locator %s is BAD DEFINED !!", value));
            throw new NoSuchElementException(String.format("Locator %s is BAD DEFINED !!", value));
        }

        String[] values = value.split("_");
        String locatorType = values[0].toLowerCase();
        String locatorValue = value.substring(value.indexOf("_") + 1);

        switch (locatorType) {
            case "id":
                return androidDriver.findElementById(locatorValue);
            case "xpath":
                return androidDriver.findElementByXPath(locatorValue);
            case "class":
                return androidDriver.findElementByClassName(locatorValue);
            case "text":
                return androidDriver.findElementByXPath("/*//*[@text=\"" + locatorValue + "\"]");
            case "containsText":
                return androidDriver.findElementByXPath("/*//*[contains(@text, '" + locatorValue + "')]");
            case "custom":
                return androidDriver.findElementByCustom(locatorValue);

            default:
                LOGGER.error("Couldn't find locator : " + locatorValue + " ! Please check properties file!");
                throw new NoSuchElementException("Couldn't find locator : " + locatorValue);
        }
    }

    public static AndroidElement getElement(String elementName) {
        String elementNameValue;
        try {
            elementNameValue = getPropertyValue(elementName);
            return getLocatorFromValue(elementNameValue);
        } catch (Exception e) {
            throw new NoSuchElementException(e.getMessage());
        }

    }

    public static String getPropertyValue(String key) {
        if (properties.getProperty(key) == null) {
            if (key.startsWith("id_") || key.startsWith("name_") || key.startsWith("label_") || key.startsWith("value_") ||
                    key.startsWith("classname_") || key.startsWith("labelcontain_") || key.startsWith("xpath_")) {
                return key;
            } else {
                throw new NoSuchFieldError("Property key not found in properties file:" + key);
            }
        } else {
            return properties.getProperty(key);
        }
    }

    public static synchronized void loadElementProperties() {
        String propertiesFileName = "element.properties";
        String elmtFile = System.getProperty("user.dir") + "/src/main/resources/element/" + propertiesFileName;

        if (properties == null) {
            properties = new Properties();
            try (FileInputStream Master = new FileInputStream(elmtFile)) {
                properties.load(Master);
            } catch (IOException e) {
                throw new ExceptionInInitializerError("Error while load property file : " + e.getMessage());
            }
        }
    }

    public static AndroidDriver<AndroidElement> getAndroidDriver() {
        return androidDriver;
    }
}
