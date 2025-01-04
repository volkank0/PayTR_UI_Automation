package com.paytr.tests;

import com.paytr.pages.PayWithLinks;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * "Linkle Ödeme" sayfasıyla ilgili test senaryoları.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class PayWithLinksTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(PayWithLinksTest.class);
    private PayWithLinks payWithLinks;

    @BeforeAll
    public void setupClass() {
        driver.get("https://www.paytr.com/linkle-odeme");
    }

    @BeforeEach
    public void setupTest() {
        logger.info("Navigating to Pay with Links page.");
        payWithLinks = new PayWithLinks(driver);

    }

    @Test
    @DisplayName("Test - 1: Form doldurma ve gönderme")
    public void testCompleteFormSubmission() throws IOException {
        logger.info("Accepting cookies.");
        payWithLinks.clickElement(PayWithLinks.ACCEPT_COOKIES);

        logger.info("Reading data from Excel file.");
        Map<String, String> data = readDataFromExcel(
                "src/test/resources/data.xlsx",
                "First Name", "Surname", "Email", "Website", "Phone"
        );

        logger.info("Filling the form with Excel data.");
        payWithLinks.fillForm(
                data.get("First Name"),
                data.get("Surname"),
                data.get("Email"),
                data.get("Website"),
                data.get("Phone")
        );

        logger.info("Selecting business type.");
        payWithLinks.clickElement(PayWithLinks.BUSINESS_TYPE_DROPDOWN);
        payWithLinks.clickElement(PayWithLinks.TAX_OPTION);

        logger.info("Agreeing to terms.");
        payWithLinks.jsClickElement(PayWithLinks.AGREEMENT_CHECKBOX);

        logger.info("Submitting the form.");
        payWithLinks.clickElement(PayWithLinks.SUBMIT_BUTTON);

        logger.info("Form submitted successfully.");
    }

    @Test
    @DisplayName("Test - 2: Ön Başvuru URL kontrolü ve form doldurma")
    public void testVerifyUrlAndClickPreApplication() {
        logger.info("Verifying current URL for 'Üye İşyeri Olun'.");
        payWithLinks.verifyCurrentUrl("https://www.paytr.com/uye-isyeri-olun");

        logger.info("Clicking 'Ön Başvuru Yap' button.");
        payWithLinks.jsClickElement(PayWithLinks.PRELIMINARY_APPLICATION);

        logger.info("Filling additional form fields for pre-application.");
        payWithLinks.fillField(PayWithLinks.COMPANY_TITLE_INPUT, "Volkan A.S.");
        payWithLinks.fillField(PayWithLinks.TC_NO_INPUT, "29657511646");
        payWithLinks.fillField(PayWithLinks.TAX_OFFICE_INPUT, "Ankara");
        payWithLinks.fillField(PayWithLinks.MONTHLY_SALE_INPUT, "200000");

        logger.info("Clicking 'Ön Başvuru Tamamla' button.");
        payWithLinks.clickElement(PayWithLinks.COMPLETE_PRE_APPLICATION_BUTTON);
    }

    @Test
    @DisplayName("Test - 3: Teşekkür sayfası ve referans kaydı")
    public void testVerifyThankYouMessageAndSaveReference() throws IOException {
        logger.info("Verifying thank you message.");
        String expectedMessage = "Teşekkürler, ön başvurunuz tarafımıza ulaşmıştır.";
        String actualMessage = payWithLinks.getElementText(PayWithLinks.THANK_YOU_MESSAGE);
        assertEquals(expectedMessage, actualMessage, "Thank you message does not match!");
        logger.info("Thank you message verified successfully: {}", actualMessage);

        logger.info("Fetching reference details.");
        String referenceText = payWithLinks.getElementText(PayWithLinks.REFERENCE_DETAILS);

        // Referans bilgisini ayrıştır
        String[] referenceParts = referenceText.split(": ");
        String key = referenceParts[0].trim();   // Örn: "Referans no"
        String value = referenceParts[1].trim(); // Referans numarası

        logger.info("Reference Key: {}, Value: {}", key, value);

        // CSV dosyasına kaydet
        saveReferenceToCsv(key, value);
        logger.info("Reference details saved to CSV.");
    }

    /**
     * Referans detaylarını bir CSV dosyasına kaydeder.
     */
    private void saveReferenceToCsv(String key, String value) throws IOException {
        String filePath = "src/test/resources/reference_data.csv"; // CSV dosyasının yolu

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.append(key)
                    .append(",")
                    .append(value)
                    .append("\n");
            logger.info("Reference details saved to CSV file: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to save reference details to CSV.", e);
            throw e;
        }
    }

    /**
     * Excel dosyasından belirli başlıklara sahip verileri okur.
     */
    private Map<String, String> readDataFromExcel(String filePath, String... headers) throws IOException {
        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Map<String, String> data = new HashMap<>();

            // Başlık satırını oku
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                headerMap.put(cell.getStringCellValue(), cell.getColumnIndex());
            }

            // Veri satırını oku
            Row dataRow = sheet.getRow(1);
            for (String header : headers) {
                int columnIndex = headerMap.get(header);
                Cell cell = dataRow.getCell(columnIndex);

                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            data.put(header, cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            if (header.equalsIgnoreCase("Phone")) {
                                // Telefon numarasını formatlamak
                                String phone = String.format("%.0f", cell.getNumericCellValue());
                                data.put(header, phone);
                            } else {
                                data.put(header, String.valueOf(cell.getNumericCellValue()));
                            }
                            break;
                        case BOOLEAN:
                            data.put(header, String.valueOf(cell.getBooleanCellValue()));
                            break;
                        default:
                            data.put(header, "");
                    }
                } else {
                    data.put(header, "");
                }
            }
            return data;
        }
    }
}
