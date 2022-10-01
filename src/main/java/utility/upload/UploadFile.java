package utility.upload;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class UploadFile extends WebSettings {



    private AutomationReport report;


    public UploadFile( AutomationReport report) {

        this.report = report;


    }


    public  String create_upload_file(ArrayList<String[]> dataToWrite, String path, String templateFileName) throws IOException {
        Workbook workbook = new XSSFWorkbook(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                path+templateFileName));
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



}
