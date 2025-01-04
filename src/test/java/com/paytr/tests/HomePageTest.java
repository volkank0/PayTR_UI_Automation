package com.paytr.tests;

import com.paytr.pages.HomePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ana sayfadaki slider ve ek elementlerin testlerini içerir.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class HomePageTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(HomePageTest.class);
    private HomePage homePage;

    @BeforeEach
    public void setupTest() {
        // Her test öncesi HomePage objesi oluşturuluyor
        homePage = new HomePage(driver);
    }

    @Test
    @DisplayName("Test - 1: Tüm slider kartlarının görünürlüğü")
    public void testAllCardsVisibility() {
        driver.get("https://www.paytr.com/");
        logger.info("Testing if the current URL matches the expected URL.");
        homePage.verifyCurrentUrl("https://www.paytr.com/");

        logger.info("Testing the visibility of all cards in the slider.");
        int totalCards = homePage.getTotalCardCount();

        for (int i = 1; i <= totalCards; i++) {
            logger.info("Checking card number {}.", i);
            assertTrue(homePage.isCardVisible(i),
                    "Card number " + i + " is not found or not visible.");

            if (i < totalCards) {
                logger.info("Sliding the slider to the right.");
                homePage.slideRight(i);
            }
        }
    }

    @Test
    @DisplayName("Test - 2: Ek elementlerin görünürlüğü")
    public void testAdditionalElementVisibility() {
        logger.info("Testing the visibility of additional elements.");

        assertTrue(homePage.isProductManagementVisible(),
                "Product Management element is not visible.");
        assertTrue(homePage.isForDevelopersVisible(),
                "For Developers element is not visible.");
        assertTrue(homePage.isWhyChooseUsVisible(),
                "Why Choose Us element is not visible.");
        assertTrue(homePage.isBusinessPartnersVisible(),
                "Business Partners element is not visible.");
        assertTrue(homePage.isFAQSectionVisible(),
                "FAQ Section element is not visible.");
    }

    @Test
    @DisplayName("Test - 3: Ödeme Menüsü ve Linkle Ödeme butonu")
    public void testPaymentsMenuAndPayWithLinksButton() {
        logger.info("Starting the test for the Payments menu and Pay with Links button.");

        driver.get("https://www.paytr.com/");
        homePage.clickElement(By.xpath("/html/body/header/div[2]/div[2]/div[2]/nav/div[2]"));
        homePage.clickElement(By.xpath("//div[@id='online-odeme-cozumleri']/a[@href='/linkle-odeme']"));

        assertTrue(homePage.verifyCurrentUrl("https://www.paytr.com/linkle-odeme"),
                "URL is not redirected to Linkle Ödeme page properly.");
        logger.info("Payments menu and Pay with Links button works as expected.");
    }
}
