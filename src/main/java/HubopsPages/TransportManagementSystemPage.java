package HubopsPages;

import com.bigbasket.automation.reports.AutomationReport;
import framework.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;

public class TransportManagementSystemPage {

    private WebElement getModuleElement(String moduleName) {
        return driver.findElement(By.xpath("//span[text()='" + moduleName + "']/.."));
    }

    private WebElement getSubModuleElement(String subModuleName) {
        return driver.findElement(By.xpath("//a[text()=\"" + subModuleName + "\"]"));
    }

    private List<WebElement> rowsData() {
        return driver.findElements(By.xpath("//table//tr"));
    }
    private List<WebElement> tableRowsData() {
        return driver.findElements(By.xpath("//table//tr"));
    }
    private List<WebElement> columnDetails(WebElement element) {
        return element.findElements(By.xpath(".//td"));
    }
    private List<WebElement> tableColumnDetails(WebElement element) {
        return element.findElements(By.xpath(".//td"));
    }

    private List<WebElement> headerDetails(WebElement element) {
        return element.findElements(By.xpath(".//th"));
    }

    private WebElement serviceAbilityAreasAll() {
        return driver.findElement(By.xpath("//span[text()='By serviceability areas']//following-sibling::ul//a[text()='All']"));
    }
    private WebElement nextButton() {
        return driver.findElement(By.xpath("//a[text()='Next']"));
    }
    private List<WebElement> paginationLinks() {
        return driver.findElements(By.xpath("//div[@id='data_table_paginate']//ul//li//a"));
    }

    private WebDriver driver;
    private WebDriverWait wait;

    public TransportManagementSystemPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    //////***This method is Getting the Values from Croma Pages and rows and save it into list***//////
    public List<List<String>> navigateToTransportManagementSystem(String moduleName, String subModule) throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//span[text()='" + moduleName + "']/..")));
        scrollIntoView(getModuleElement(moduleName));
        getModuleElement(moduleName).click();
        Thread.sleep(5000);
        scrollIntoView(getSubModuleElement(subModule));
        getSubModuleElement(subModule).click();
        Thread.sleep(5000);
        List<WebElement> rowDetails = rowsData();
        List<List<String>> allDetails = new ArrayList<>();
        for (WebElement rowDetail : rowDetails) {
            List<String> rows = new ArrayList<>();
            List<WebElement> columnDetails = columnDetails(rowDetail);
            if (columnDetails.size() == 0) {
                columnDetails = headerDetails(rowDetail);
            }
            for (int i = 0; i < columnDetails.size(); i++) {
                String text = columnDetails.get(i).getText();
                if (text.equalsIgnoreCase("No data available in table")) {
                    serviceAbilityAreasAll().click();
                    Thread.sleep(5000);
                    PageFactory.initElements(driver, this);
                    List<WebElement> allPageLinks=paginationLinks();
                    for(int z=1;z<allPageLinks.size()-1;z++){
                        PageFactory.initElements(driver, this);
                        allPageLinks=paginationLinks();
                        allPageLinks.get(z).click();
                        Thread.sleep(2000);
                        List<WebElement> rowRecords = tableRowsData();
                        for (int m = 1; m < rowRecords.size(); m++) {
                            rows = new ArrayList<>();
                            List<WebElement> columnRecords = tableColumnDetails(rowRecords.get(m));
                            for (int n = 0; n < columnRecords.size(); n++) {
                                String textaVlue = columnRecords.get(n).getText();
                                rows.add(textaVlue);
                            }
                            allDetails.add(rows);
                        }
                    }
                    return allDetails;
                }
                if (text.equalsIgnoreCase("false")) {
                    rows.add("No");
                } else if (text.equalsIgnoreCase("true")) {
                    rows.add("Yes");

                } else if (text.equalsIgnoreCase("")) {
                    rows.add(".");
                }

                rows.add(text);
            }
            allDetails.add(rows);
            System.out.println("");
        }
        return allDetails;

    }

    //////***This method is Getting the Values from Croma Pages and rows without serial numbers and save it into list***//////

    public List<List<String>> getDetailsWithoutSerialNumber(String moduleName, String subModule) throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//span[text()='" + moduleName + "']/..")));
        scrollIntoView(getModuleElement(moduleName));
        getModuleElement(moduleName).click();
        Thread.sleep(5000);
        scrollIntoView(getSubModuleElement(subModule));
        getSubModuleElement(subModule).click();
        Thread.sleep(5000);
        List<WebElement> rowDetails = rowsData();
        List<List<String>> allDetails = new ArrayList<>();
        for (WebElement rowDetail : rowDetails) {
            List<String> rows = new ArrayList<>();
            List<WebElement> columnDetails = columnDetails(rowDetail);
            if (columnDetails.size() == 0) {
                columnDetails = headerDetails(rowDetail);
            }
            for (int i = 0; i < columnDetails.size(); i++) {
                String text = columnDetails.get(i).getText();
                rows.add(text);
            }
            allDetails.add(rows);
            System.out.println("");
        }
        return allDetails;

    }

    public void navigateToSubModukle(String moduleName, String subModule) throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//span[text()='" + moduleName + "']/..")));
        scrollIntoView(getModuleElement(moduleName));
        getModuleElement(moduleName).click();
        Thread.sleep(5000);
        scrollIntoView(getSubModuleElement(subModule));
        getSubModuleElement(subModule).click();
        Thread.sleep(5000);
    }
}
