package com.paytr.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * "Linkle Ödeme" sayfasındaki form vb. elementlerin yönetildiği sınıf.
 */
public class PayWithLinks extends BasePage {

    // Element selectors
    public static final By FIRST_NAME_INPUT = By.id("first-name");
    public static final By SURNAME_INPUT = By.name("surname");
    public static final By EMAIL_INPUT = By.id("email");
    public static final By WEBSITE_INPUT = By.name("website");
    public static final By PHONE_INPUT = By.name("tel");
    public static final By BUSINESS_TYPE_DROPDOWN = By.xpath("//form[@id='contact-form']//div[.='İşletme Tipi']");
    public static final By TAX_OPTION = By.xpath("//form[@id='contact-form']//div[.='Şahıs İşletmesi (Vergi Levham Var)']");
    public static final By AGREEMENT_CHECKBOX = By.id("telefon");
    public static final By SUBMIT_BUTTON = By.xpath("//*[@id='contact-form']/button");
    public static final By ACCEPT_COOKIES = By.xpath("/html/body/div[3]/div[@class='section-container']//button[.='Tümünü Kabul Et']");
    public static final By PRELIMINARY_APPLICATION = By.xpath("//*[@id=\"__next\"]/div/div[2]/div[2]/div/div/form/div[8]/div/button");
    public static final By COMPANY_TITLE_INPUT = By.id("company_title");
    public static final By TC_NO_INPUT = By.id("tc_no");
    public static final By TAX_OFFICE_INPUT = By.id("tax_office");
    public static final By MONTHLY_SALE_INPUT = By.id("monthly_sale");
    public static final By COMPLETE_PRE_APPLICATION_BUTTON = By.xpath("//*[@id='__next']/div/div[2]/div[2]/div/div/form/div[6]/div/button");
    public static final By THANK_YOU_MESSAGE = By.xpath("//*[@id='__next']/div/div/div/h3");
    public static final By REFERENCE_DETAILS = By.xpath("//*[@id='__next']/div/div/div/div/a");

    public PayWithLinks(WebDriver driver) {
        super(driver);
    }

    /**
     * Form alanlarına veri doldurur (BasePage içerisindeki fillField metodunu kullanır).
     */
    public void fillForm(String firstName, String surname, String email, String website, String phone) {
        fillField(FIRST_NAME_INPUT, firstName);
        fillField(SURNAME_INPUT, surname);
        fillField(EMAIL_INPUT, email);
        fillField(WEBSITE_INPUT, website);
        fillField(PHONE_INPUT, phone);
    }
}
