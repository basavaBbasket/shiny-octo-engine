package HubopsPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CromaHomePage {
    WebDriver driver;
    WebDriverWait wait;
    public CromaHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }
    @FindBy(id="mat-input-0")
    private WebElement usernameTextBox;
    @FindBy(id="mat-input-1")
    private WebElement passwordTextBox;
    @FindBy(id="mat-input-2")
    private WebElement otpTextBox;
    @FindBy(xpath="//span[text()='Submit']/..")
    private WebElement submitButton;
    @FindBy(xpath="//span[text()='Login']/..")
    private WebElement loginButton;

    public void cromaLogin(String username,String password) throws IOException {
        wait.until(ExpectedConditions.visibilityOf(usernameTextBox));
        usernameTextBox.sendKeys(username);
        passwordTextBox.sendKeys(password);
        loginButton.click();
        wait.until(ExpectedConditions.visibilityOf(otpTextBox));
        Properties prop = new Properties();
        FileInputStream fis=new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
        otpTextBox.sendKeys(prop.getProperty("otp"));
        submitButton.click();
    }
}
