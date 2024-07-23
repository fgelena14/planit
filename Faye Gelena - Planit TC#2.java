import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.UUID;

public class ContactFormTest {
    private WebDriver driver;
    private SoftAssert softAssert;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        // Set the path to your ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:/Program Files/chromedriver/chromedriver.exe");

        // Initialization
        driver = new ChromeDriver();
        softAssert = new SoftAssert();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Maximum of 10 seconds wait
    }

    @Test(invocationCount = 5) // This will run 5 times
    public void testContactFormSubmission() {
        // Generate unique values for each test run
        String unqForename = "Faye" + UUID.randomUUID().toString().substring(0, 5);
        String unqEmail = "faye" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com";
        String unqMsg = "Test Only " + UUID.randomUUID().toString().substring(0, 5);

        // Navigate to the home page
        driver.get("https://jupiter.cloud.planittesting.com/#/home");

        // Go to the contact page
        driver.findElement(By.linkText("Contact")).click();

        // Populate mandatory fields
        driver.findElement(By.id("forename")).sendKeys(unqForename);
        driver.findElement(By.id("email")).sendKeys(unqEmail);
        driver.findElement(By.id("message")).sendKeys(unqMsg);

        // Click the submit button
        driver.findElement(By.linkText("Submit")).click();

        // Validate successful submission message
        try {
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert"))); //will wait the element until found with the specific condition
            softAssert.assertTrue(successMessage.isDisplayed(), "Success message should be displayed");
        } catch (Exception e) {
            softAssert.fail("Success message element not found");
        }

        softAssert.assertAll(); // to continously check remaining element ; use to report all failed assertion
    }

    @AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
