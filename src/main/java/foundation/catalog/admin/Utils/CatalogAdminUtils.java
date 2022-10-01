package foundation.catalog.admin.Utils;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import foundation.catalog.admin.fcstatus.CatalogFCStatus;
import foundation.catalog.admin.uploadService.UploadPage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class CatalogAdminUtils extends WebSettings {


    private AutomationReport report;

    public CatalogAdminUtils(AutomationReport report) {
        this.report = report;
    }


    /**
     * Create Excel file with given data
     * @param dataToWrite The Data to be Put in each row of the file
     * @param templateFileName Location of the template file
     * @return Name of the output file
     * @throws IOException
     */
    public static String create_upload_file(ArrayList<String[]> dataToWrite,String templateFileName) throws IOException {
        Workbook workbook = new XSSFWorkbook(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "foundation.catalogUpload/"+templateFileName));
        Sheet sheet = workbook.getSheet("Page1");
        //Get the current count of rows in excel file
        int rowCount = 0;//sheet.getLastRowNum() - sheet.getFirstRowNum();
        //Get the first row from the sheet
        Row row = sheet.getRow(sheet.getFirstRowNum());
        //Create a new row and append it at last of sheet
        Row newRow = sheet.createRow(++rowCount);
        //Create a loop over the cell of newly created Row
        for (int i = 0; i < dataToWrite.size(); i++) {
            for (int j = 0; j < row.getLastCellNum(); j++) {
                //Fill data in row
                Cell cell = newRow.createCell(j);
                cell.setCellValue(dataToWrite.get(i)[j]);
            }
            newRow = sheet.createRow(++rowCount);
        }
        String fileName = System.getProperty("user.dir") + "//target//"+templateFileName;
        FileOutputStream outputStream = new FileOutputStream(fileName);
        workbook.write(outputStream);
        outputStream.close();
        return fileName;
    }

    public void updateCatalogStatus(int sku_id,int fc_id,int status_id) throws Exception {
        /*WebDriver driver;
        try {
            driver = DriverManager.getInstance().getDriver();
        }
        catch (SessionNotCreatedException | MalformedURLException s) {
            driver = DriverManager.getInstance().getDriver();
        }
        driver.manage().deleteAllCookies();
        ArrayList<Integer> skulist =new ArrayList<Integer>();
        skulist.add(sku_id);
        UploadPage uploadPage = new UploadPage(driver,report);
        CatalogFCStatus StatusPage = new CatalogFCStatus(driver,report);
        MenuOptions menuOptions = new MenuOptions(driver,report);
        launchSite("catalogAdmin", driver);
        menuOptions.Login("supernovas","supernovas");
        uploadPage.navigateToUpload();
        ArrayList<String[]> PreparedData =  StatusPage.catalogStatusUploadData(fc_id,skulist,status_id);
        String outputFile1 = create_upload_file(PreparedData,"catalog_status_upload.xlsx");
        String uploadType ="catalog_status_upload_hqa";
        String fileName = outputFile1;
        uploadPage.UploadByTypeandFile(uploadType,fileName);
        driver.close();*/
    }

}
