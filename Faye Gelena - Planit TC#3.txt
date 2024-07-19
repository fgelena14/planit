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
import java.util.List;

public class CartTest {
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

    @Test
    public void testCart() {
        // Navigate to the shop page
        driver.get("https://jupiter.cloud.planittesting.com/#/shop");

        // Buy 2 Stuffed Frog, 5 Fluffy Bunny, 3 Valentine Bear 
        addProductToCart("Stuffed Frog", 2);
        addProductToCart("Fluffy Bunny", 5);
        addProductToCart("Valentine Bear", 3);

        // Go to the cart page
        driver.findElement(By.cssSelector("#nav-cart > a")).click();

        // Verify the price and subtotal for each product
        verifyProductInCart("Stuffed Frog", 2, 10.99);
        verifyProductInCart("Fluffy Bunny", 5, 9.99);
        verifyProductInCart("Valentine Bear", 3, 14.99);

        // Verify that total = sum of subtotals
        verifyTotal();
        
        softAssert.assertAll();
    }

    private void addProductToCart(String productName, int quantity) { //method of adding to cart
    for (int i = 0; i < quantity; i++) {
        WebElement productElement = driver.findElement(By.xpath("//h4[text()='" + productName + "']")); //use h4 eleement with exact productName
        WebElement buyButton = productElement.findElement(By.cssSelector("#product-2 .btn"));
        buyButton.click();
    }
}

    private void verifyProductInCart(String productName, int quantity, double price) { //locating row of product and verifiying price and subtotal
        WebElement productRow = driver.findElement(By.xpath("//td[text()='" + productName + "']/parent::tr")); //locating of table data that contains exact txt of product name
        double expectedSubtotal = quantity * price; //computation

        // Verify price
        double actualPrice = Double.parseDouble(productRow.findElement(By.xpath(".//td[2]")).getText().replace("$", "")); //getting the price without $ sign and converting to double type
        softAssert.assertEquals(actualPrice, price, "Price for " + productName + " should be correct"); //comparison of declared price and actual price

        // Verify subtotal
        double actualSubtotal = Double.parseDouble(productRow.findElement(By.xpath(".//td[4]")).getText().replace("$", "")); //getting the subtotal without $ sign and converting to double type
        softAssert.assertEquals(actualSubtotal, expectedSubtotal, "Subtotal for " + productName + " should be correct"); //comparison of declared price and actual price

    }

    private void verifyTotal() {
        List<WebElement> subtotalElements = driver.findElements(By.xpath("//td[4]")); //finding all the elements that are in 4th cell in their row
        double total = 0.0;
        for (WebElement subtotalElement : subtotalElements) {
            total += Double.parseDouble(subtotalElement.getText().replace("$", "")); //getting the total without $ sign and converting to double type 
        }

        double actualTotal = Double.parseDouble(driver.findElement(By.id("total")).getText().replace("Total: $", "")); //getting the actualTotal  without $ sign and converting to double type 
        }
        softAssert.assertEquals(actualTotal, total, "Total should be the sum of subtotals");
    }

    @AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
