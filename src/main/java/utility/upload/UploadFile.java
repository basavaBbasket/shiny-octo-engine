package utility.upload;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class UploadFile extends WebSettings {


    private IReport report;


    public UploadFile(IReport report) {

        this.report = report;


    }


    public String create_upload_file(ArrayList<Object[]> dataToWrite, String path, String templateFileName, String sheetName) throws IOException {
        Workbook workbook = new XSSFWorkbook(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                path + templateFileName)));
        Sheet sheet = workbook.getSheet(sheetName);
        //Get the current count of rows in excel file
        int rowCount = 0;//sheet.getLastRowNum() - sheet.getFirstRowNum();
        //Create a new row and append it at last of sheet
        Row newRow = sheet.createRow(++rowCount);
        //Create a loop over the cell of newly created Row
        for (Object[] objects : dataToWrite) {
            for (int j = 0; j < objects.length; j++) {
                //Fill data in row
                Cell cell = newRow.createCell(j);
                if (objects[j] == null)
                    cell.setCellValue((String) null);
                else
                    cell.setCellValue(objects[j].toString());
            }
            newRow = sheet.createRow(++rowCount);
        }
        String fileName = System.getProperty("user.dir") + "//target//" + templateFileName;
        FileOutputStream outputStream = new FileOutputStream(fileName);
        workbook.write(outputStream);
        outputStream.close();
        return fileName;
    }
}
