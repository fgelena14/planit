
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ContactFormTest {
    private WebDriver driver;
    private SoftAssert softAssert;

    @BeforeClass
    public void setUp() {
        // Set the path to your ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:/Program Files/chromedriver/chromedriver.exe");

        // Initialization
        driver = new ChromeDriver();
        softAssert = new SoftAssert();
    }

	//first step to execute
    @Test 
    public void navigateToContactPage() {
        // Navigate to the home page
        driver.get("https://jupiter.cloud.planittesting.com/#/home");

        // Go to the contact page
        driver.findElement(By.linkText("Contact")).click();
    }

	//2nd test
    @Test(dependsOnMethods = "navigateToContactPage")
    public void clickSubmitButton() {
        // Click the submit button
        driver.findElement(By.linkText("Submit")).click();
    }
	
	//3rd test
    @Test(dependsOnMethods = "clickSubmitButton")
    public void verifyErrorMessages() {
        String[] errorIds = {"forename-err", "email-err", "message-err"}; // defining Array for error IDs

        for (String errorId : errorIds) { // iteration on each errorids
            try {
				softAssert.assertTrue(driver.findElement(By.id(errorId)).isDisplayed(),// assert if errmsg displayed
                                errorId + " error message should be displayed"); //if fails, this err msg display 
            } catch (NoSuchElementException e) {
                softAssert.fail(errorId + " error message element not found");
            }
        }

        softAssert.assertAll(); // to continously check remaining element ; use to report all failed assertion
    }

    @Test(dependsOnMethods = "verifyErrorMessages")
    public void populateMandatoryFields() {
        // Populate mandatory fields
        driver.findElement(By.id("forename")).sendKeys("Faye");
        driver.findElement(By.id("email")).sendKeys("fayegelena@gmail.com");
        driver.findElement(By.id("message")).sendKeys("Test Only");

        // Click the submit button again
       driver.findElement(By.linkText("Submit")).click();
    }

    @Test(dependsOnMethods = "populateMandatoryFields")
    public void validateErrorsGone() {
        // Validate errors are gone
        String[] errorIds = {"forename-err", "email-err", "message-err"}; // defining Array for error IDs

        for (String errorId : errorIds) { // iteration on each errorids
            try {
                driver.findElement(By.id(errorId));
                softAssert.fail(errorId + " error message is still displayed"); // if element displayed, test should failed
            } catch (NoSuchElementException e) {
                // empty; expected behaviour that msg is gone
            }
        }

        softAssert.assertAll();  // to continously check remaining element ; use to report all failed assertion
    }

    @AfterClass 
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
