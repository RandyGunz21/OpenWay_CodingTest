package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class TestCase {
    private WebDriver driver;
    private WebDriverWait wait;

    // Replace with your actual test account credentials
    private static final String LOGIN_EMAIL = "randygunawan212004@example.com";
    private static final String LOGIN_PASSWORD = "P@ssw0rd";

    @BeforeMethod
    public void setUp() {
        // Set up Chrome driver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void addProductToCartTest() {
        // Step 1: Navigate to Periplus website
        driver.get("https://www.periplus.com/");

        // Step 2: Click on login button to open login form
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@class, 'login') or contains(text(), 'Sign In')]")));
        loginButton.click();

        // Step 3: Enter login credentials
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='email' or @id='email' or @name='email']")));
        emailField.sendKeys(LOGIN_EMAIL);

        WebElement passwordField = driver.findElement(
                By.xpath("//input[@type='password' or @id='password' or @name='password']"));
        passwordField.sendKeys(LOGIN_PASSWORD);

        // Step 4: Submit login form
        WebElement signInButton = driver.findElement(
                By.xpath("//button[contains(text(), 'Sign In') or contains(@class, 'login')]"));
        signInButton.click();

        // Wait for successful login - verify username is displayed or login was successful
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'account') or contains(@class, 'user')]")),
                ExpectedConditions.urlContains("account")
        ));

        // Step 5: Search for a product (books are common on Periplus)
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='search' or @id='search' or @name='q']")));
        searchBox.sendKeys("Harry Potter");
        searchBox.submit();

        // Step 6: Wait for search results and click on the first product
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'product') or contains(@class, 'item')]//a[1]")));

        // Store product name for verification later
        WebElement productNameElement = driver.findElement(
                By.xpath("//div[contains(@class, 'product') or contains(@class, 'item')]//a[1]//div[contains(@class, 'title') or contains(@class, 'name')]"));
        String productName = productNameElement.getText().trim();
        System.out.println("Selected product: " + productName);

        // Click on the product to view details
        firstProduct.click();

        // Step 7: On the product page, add to cart
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Add to Cart') or contains(@class, 'add-to-cart')]")));
        addToCartButton.click();

        // Step 8: Wait for confirmation that product was added to cart
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'success') and contains(text(), 'added')]")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'cart-message')]"))
        ));

        // Step 9: Navigate to cart page
        WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@class, 'cart') or contains(@href, 'cart')]")));
        cartIcon.click();

        // Step 10: Verify that the product is in the cart
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'cart') or contains(@class, 'basket')]")));

        // Find all items in the cart
        WebElement cartItem = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class, 'cart-item') or contains(@class, 'item')]")));

        // Verify the cart is not empty
        Assert.assertTrue(cartItem.isDisplayed(), "The cart should not be empty");

        // Verify the product name in cart
        WebElement cartProductName = driver.findElement(
                By.xpath("//div[contains(@class, 'cart-item') or contains(@class, 'item')]//div[contains(@class, 'name') or contains(@class, 'title')]"));

        String cartItemName = cartProductName.getText().trim();
        System.out.println("Cart item: " + cartItemName);

        // This verification might need adjustment based on exact site behavior
        // Some sites might show abbreviated product names in cart
        Assert.assertTrue(cartItemName.contains(productName) || productName.contains(cartItemName),
                "Product in cart should match selected product");
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}