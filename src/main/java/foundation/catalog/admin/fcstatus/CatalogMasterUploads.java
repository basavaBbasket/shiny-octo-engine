package foundation.catalog.admin.fcstatus;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;

public class CatalogMasterUploads extends WebSettings
{


    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    /**
     * Setting up constructor for catalog master upload class to set driver and report
     * @param driver driver to be set
     * @param report custom report to be added
     */
    public CatalogMasterUploads(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
    }

    /**
     * Creating Data for the master SKU SA upload
     * @param fc_id FC_id fro which the visibility has to be enabled
     * @param sku_id SKU_id for which visiblisty has to be enabled
     * @param sa_id SA_id for which visibility has to be enabled
     * @param path_id path_id for which visibility has to be enabled
     * @return An array list with each entry consisting of one row for upload
     */
    public ArrayList<String[]> catalogMasterSASkuUploadData(int fc_id, ArrayList<Integer> sku_id,int sa_id,int path_id){
        ArrayList<String[]> resultSet = new ArrayList<String[]>();
        for(int i=0;i<sku_id.size();i++){
            String []temp = new String[]
                    {String.valueOf(sku_id.get(i)),String.valueOf(fc_id),
                            String.valueOf(sa_id),String.valueOf(path_id),"True","True",""};
            resultSet.add(temp);
            System.out.println("array Size"+ resultSet.size());
            System.out.println(resultSet.toString());
            System.out.println(Arrays.toString(resultSet.get(i)));
        }
        return resultSet;
    }
}
