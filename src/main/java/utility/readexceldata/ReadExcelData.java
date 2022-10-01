package utility.readexceldata;

import com.bigbasket.automation.WebSettings;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReadExcelData extends WebSettings {

    public static Object[] getTestData(String filePath, String sheetName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir")+"//" + filePath);
        XSSFWorkbook workbook=new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet= workbook.getSheet(sheetName);
        System.out.println(sheet.getLastRowNum());
        Object[] a=new Object[sheet.getLastRowNum()];
        for(int i=1;i<=sheet.getLastRowNum();i++){
            Map<String,String> rowData=new HashMap<>();
            XSSFRow row=sheet.getRow(i);
            System.out.println(row.getLastCellNum());
            for(int j=0;j<row.getLastCellNum();j++){
                XSSFCell cell=row.getCell(j);
                rowData.put(sheet.getRow(0).getCell(j).getStringCellValue(),cell.getStringCellValue());
            }
            a[i-1]=rowData;
        }
        return a;
    }
}
