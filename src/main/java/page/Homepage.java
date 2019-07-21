package page;

public class Homepage extends BasePage {

    public static void clickCart(){
        getAndroidDriver().findElementByCustom("ai:cart").click();
    }

    public static  boolean isCartVisible(){
        return getAndroidDriver().findElementByCustom("ai:cart").isDisplayed();
    }

    public static void firstLaunchApp(){
        clickElement("MULAI_TUR_TXT", 5);
        while (isElementDisplay("ONB_VIEW", 5)){

            if (isElementDisplay("LANJUT_TXT", 2)){
                clickElement("LANJUT_TXT");
            }else if (isElementDisplay("SELESAI_TXT", 2)){
                clickElement("SELESAI_TXT");
            }

        }
    }

    public static void isInHomepge(){
        verifyElementTobeVisible("TEXT_BOTTOMBAR_HOME_NAV", 15);
        verifyElementTobeVisible("TEXT_BOTTOMBAR_DISCOVER_NAV", 15);
        verifyElementTobeVisible("TEXT_BOTTOMBAR_BUKAMALL_NAV", 15);
        verifyElementTobeVisible("TEXT_BOTTOMBAR_TRANSACTION_NAV", 15);
        verifyElementTobeVisible("TEXT_BOTTOMBAR_AKUN_NAV", 15);
    }

}
