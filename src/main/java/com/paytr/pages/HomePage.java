package com.paytr.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Ana sayfadaki (www.paytr.com) slider ve ek bileşenleri yöneten Page sınıfı.
 */
public class HomePage extends BasePage {

    private static final String SLIDER_CARD_CSS_SELECTOR = "[tab-id] [aria-live] [role='group']:nth-of-type(%d)";
    private static final By NEXT_BUTTON_SELECTOR = By.cssSelector(".next-btn-container.custom-swiper-button-next");

    // Ekstra kontrol edilmesi gereken elementler
    private static final By PRODUCT_MANAGEMENT = By.id("urunleri-magaza-panelinizden-kolayca-yonetin");
    private static final By FOR_DEVELOPERS = By.id("gelistiriciler-icin");
    private static final By WHY_CHOOSE_US = By.xpath("/html/body/div[1]/div[4]/h2");
    private static final By BUSINESS_PARTNERS = By.xpath("/html/body/div[1]/section[5]/div[1]/h2");
    private static final By FAQ_SECTION = By.xpath("/html/body/div[1]/section[6]/div[1]/h2");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Slider içerisindeki toplam kart sayısını döndürür.
     */
    public int getTotalCardCount() {
        return driver.findElements(By.cssSelector("[tab-id] [aria-live] [role='group']")).size();
    }

    /**
     * Belirtilen kart index'indeki kartın görünür olup olmadığını kontrol eder.
     */
    public boolean isCardVisible(int cardIndex) {
        try {
            String selector = String.format(SLIDER_CARD_CSS_SELECTOR, cardIndex);
            WebElement card = driver.findElement(By.cssSelector(selector));
            scrollToElement(card);
            return isElementPresent(card);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Slider'ı sağa doğru kaydırarak bir sonraki karta geçmeye çalışır.
     */
    public void slideRight(int currentCardIndex) {
        WebElement nextButton = driver.findElement(NEXT_BUTTON_SELECTOR);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int attempts = 0;
        boolean isNextCardVisible = false;

        while (attempts < 5) {
            js.executeScript("arguments[0].click();", nextButton);
            if (isCardVisible(currentCardIndex + 1)) {
                isNextCardVisible = true;
                break;
            }
            attempts++;
        }

        if (!isNextCardVisible) {
            throw new RuntimeException(
                    "Bir sonraki kart 5 denemede de görünür olmadı. Mevcut kart index: " + currentCardIndex
            );
        }
    }

    /**
     * "Ürün Yönetimi" başlığının görünürlüğünü kontrol eder.
     */
    public boolean isProductManagementVisible() {
        WebElement element = driver.findElement(PRODUCT_MANAGEMENT);
        scrollToElement(element);
        return isElementPresent(element);
    }

    /**
     * "Geliştiriciler İçin" başlığının görünürlüğünü kontrol eder.
     */
    public boolean isForDevelopersVisible() {
        WebElement element = driver.findElement(FOR_DEVELOPERS);
        scrollToElement(element);
        return isElementPresent(element);
    }

    /**
     * "Neden bizi tercih etmelisiniz?" başlığının görünürlüğünü kontrol eder.
     */
    public boolean isWhyChooseUsVisible() {
        WebElement element = driver.findElement(WHY_CHOOSE_US);
        scrollToElement(element);
        return isElementPresent(element);
    }

    /**
     * "İş Ortaklarımız" başlığının görünürlüğünü kontrol eder.
     */
    public boolean isBusinessPartnersVisible() {
        WebElement element = driver.findElement(BUSINESS_PARTNERS);
        scrollToElement(element);
        return isElementPresent(element);
    }

    /**
     * SSS (FAQ) bölümünün görünürlüğünü kontrol eder.
     */
    public boolean isFAQSectionVisible() {
        WebElement element = driver.findElement(FAQ_SECTION);
        scrollToElement(element);
        return isElementPresent(element);
    }
}
