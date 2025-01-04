package com.paytr.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

/**
 * Tüm sayfa nesnelerinin kalıtım alacağı temel sınıf.
 * Sık kullanılan Selenium ve WebDriverWait fonksiyonları içerir.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Verilen elementin görünür olmasını bekler.
     */
    public void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Sayfadaki belirli bir elemente scroll yapar.
     */
    public void scrollToElement(WebElement element) {
        if (!isElementInViewport(element)) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        }
    }

    /**
     * Elementin görünür alanda olup olmadığını kontrol eder.
     */
    private boolean isElementInViewport(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Boolean isInViewport = (Boolean) js.executeScript(
                "var rect = arguments[0].getBoundingClientRect();" +
                        "return (" +
                        "rect.top >= 0 && rect.left >= 0 && " +
                        "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
                        "rect.right <= (window.innerWidth || document.documentElement.clientWidth)" +
                        ");",
                element
        );
        return isInViewport != null && isInViewport;
    }

    /**
     * Elementin tıklanabilir olmasını bekler.
     */
    public void waitForClickability(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Verilen lokator ile alanı bulur ve belirtilen metni yazar.
     */
    public void fillField(By locator, String value) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            scrollToElement(element);
            element.clear();
            element.sendKeys(value);
            logger.info("Filled field {} with value: {}", locator, value);
        } catch (Exception e) {
            logger.error("Failed to fill field {} with value: {}", locator, value, e);
            throw new RuntimeException("Failed to fill field: " + locator);
        }
    }

    /**
     * Belirli bir locatordan bulduğu elemana tıklar (scroll + click).
     */
    public void clickElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            scrollToElement(element);
            waitForClickability(element);
            element.click();
            logger.info("Clicked element by locator: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to click element by locator: {}", locator, e);
            throw new RuntimeException("Failed to click element by locator: " + locator);
        }
    }

    /**
     * Bir elementi JavaScript ile tıklamak için kullanılır.
     */
    public void jsClickElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.info("Elemente JS ile tıklandı: {}", locator);
        } catch (Exception e) {
            logger.error("JS ile tıklama başarısız: {}", locator, e);
            throw new RuntimeException("JS ile tıklama başarısız: " + locator);
        }
    }

    /**
     * Verilen locator ile elementin metnini alır.
     */
    public String getElementText(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String text = element.getText();
            logger.info("Text of element {}: {}", locator, text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text of element: {}", locator, e);
            throw new RuntimeException("Failed to get text of element: " + locator);
        }
    }

    /**
     * Elementin sayfada bulunup bulunmadığını ve görüntülenebilir olup olmadığını kontrol eder.
     */
    public boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            logger.error("Element not found or not visible: {}", element, e);
            return false;
        }
    }

    /**
     * Mevcut URL'nin beklenen URL ile eşleşip eşleşmediğini kontrol eder.
     */
    public boolean verifyCurrentUrl(String expectedUrl) {
        try {
            WebDriverWait urlWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            boolean isUrlMatched = urlWait.until((ExpectedCondition<Boolean>) drv -> {
                if (drv == null) return false;
                String currentUrl = drv.getCurrentUrl();
                logger.info("Checking URL... Current: {}", currentUrl);
                return currentUrl.equals(expectedUrl);
            });

            if (isUrlMatched) {
                logger.info("Current URL matches expected URL: {}", expectedUrl);
                return true;
            } else {
                logger.warn("Current URL does not match. Expected: {}, Actual: {}", expectedUrl, driver.getCurrentUrl());
                return false;
            }
        } catch (Exception e) {
            logger.error("Error verifying current URL. Expected: {}", expectedUrl, e);
            return false;
        }
    }
}
