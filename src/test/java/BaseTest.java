import io.github.bonigarcia.wdm.WebDriverManager;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    WebDriver driver;
    @BeforeEach
    public void setup(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();
        WebElement userNametextbox = new WebDriverWait(driver, Duration.ofSeconds(10)).
                until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        userNametextbox.sendKeys("standard_user");

        WebElement passwordTextBox = new WebDriverWait(driver, Duration.ofSeconds(10)).
                until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        passwordTextBox.sendKeys("secret_sauce");

        WebElement LoginButton = driver.findElement(By.name("login-button"));
        LoginButton.click();
    }
    @AfterEach
    public void cleanUp()
    {
        driver.quit();
    }


}
