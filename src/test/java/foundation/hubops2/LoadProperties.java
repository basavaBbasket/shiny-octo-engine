package foundation.hubops2;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.util.Properties;

public class LoadProperties {
    private static Logger logger = Logger.getLogger(foundation.warehouse.LoadProperties.class);
    public static Properties configHubops2 = new Properties();

    /**
     * Method to load all property file specific to warehouse package.
     * This will run as testng before suite.
     */
    @BeforeSuite(alwaysRun = true)
    public void loadPropertiesFile(){
        loadProperty(configHubops2,"/foundation/hubops2/hubops2.properties");
    }

    /**
     * Generic method to load property file & with exception handling.
     * @param properties property object
     * @param path path of the file location inside "src/test/resources" directory.
     */
    private void loadProperty(Properties properties, String path){
        try {
            properties.load(foundation.warehouse.LoadProperties.class.getResourceAsStream(path));
        } catch (IOException e) {
            logger.error("Exception loading property file. Path: " + path);
            e.printStackTrace();
        }
    }
}
