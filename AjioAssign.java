package ajio.assignment;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AjioAssign {

	public static void main(String[] args) throws InterruptedException {
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");

		ChromeDriver driver = new ChromeDriver(options);
		driver.get("https://www.ajio.com/shop/sale");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		// Enter Bags in the Search field and Select Women Handbags
		driver.findElementByXPath("//input[@placeholder='Search AJIO']").sendKeys("Bags");
		Actions builder = new Actions(driver);
		WebElement womenBags = driver.findElementByXPath("(//span [text()='handbags for women'])[1]");
		builder.moveToElement(womenBags).click().perform();
		// Click on five grid and Select SORT BY as "What's New"
		driver.findElementByXPath("//div [@class='five-grid']").click();
		WebElement selDropdown = driver.findElementByXPath("//div [@class='filter-dropdown']/select");
		Select dropdown = new Select(selDropdown);
		dropdown.selectByVisibleText("What's New");
		// Click Price on the side menu and Enter Price Range Min as 2000 and Max as
		// 5000
		driver.findElementByXPath("//span [text()='price']").click();
		driver.findElementById("minPrice").sendKeys("2000");
		driver.findElementById("maxPrice").sendKeys("5000");
		driver.findElementByXPath("(//button [@type='submit'])[2]").click();
		// Click on the first product
		driver.findElementByXPath("(//div [@class='ReactVirtualized__Grid__innerScrollContainer'])/div[1]").click();
		// Windows Handle to handle new window
		Set<String> windowHandles = driver.getWindowHandles();
		List<String> listHandles = new ArrayList<String>(windowHandles);
		String secondWin = listHandles.get(1);
		driver.switchTo().window(secondWin);
		// Get the actual price, coupon code and discount price
		String actPrice = driver.findElementByXPath("//div[@class='prod-sp']").getText();
		System.out.println("Actual Price of Bag:   " + actPrice);
		String couponCode = driver.findElementByXPath("//div [@class='promo-title']").getText();
		couponCode = couponCode.replaceAll("Use Code", " ");
		couponCode = couponCode.trim();
		System.out.println("Coupon Code:   " + couponCode);
		String discPrice = driver.findElementByXPath("//div [@class='promo-discounted-price']/span").getText();
		System.out.println("Discounted Price of Bag:   " + discPrice);
		// Check the availability of the product for pincode 560043, print the expected
		// delivery date if it is available
		driver.findElementByXPath("//span[contains (text(), 'Enter Pin-code')]").click();
		driver.findElementByXPath("//input [@name='pincode']").sendKeys("560043");
		driver.findElementByXPath("(//button [@type='submit'])[2]").click();
		String expDelDate = driver.findElementByXPath("//span [@class='edd-message-success-details-highlighted']")
				.getText();
		System.out.println("Expected Date of Deliver:    " + expDelDate);
		// Click on Other Informations under Product Details and Print the Customer Care
		// details
		driver.findElementByXPath("//div [@class='other-info-toggle']").click();
		String custCareDetails = driver.findElementByXPath("//div [text()='Customer Care Address']//following::div[2]")
				.getText();
		System.out.println("Customer Care Address:    " + custCareDetails);
		// Click on ADD TO BAG and then GO TO BAG
		driver.findElementByXPath("//span [text()='ADD TO BAG']").click();
		Thread.sleep(4000);
		// Check the Order Total before apply coupon
		driver.findElementByXPath("//div[@class='ic-cart ']").click();
		// Enter Coupon Code and Click Apply
		String totalAmount = driver.findElementByXPath("//span [@class='price-value bold-font']").getText();
		System.out.println("Total Amount before Discount:   " + totalAmount);
		driver.findElementById("couponCodeInput").sendKeys(couponCode);
		driver.findElementByXPath("//button [text()='Apply']").click();
		// Print the warning message if the product is not eligible discount
		boolean displayed = driver.findElementByXPath("//span [@class='voucher-error error-message info']")
				.isDisplayed();
		if (displayed) {
			String warningmsg = driver.findElementByXPath("//span [@class='voucher-error error-message info']")
					.getText();
			System.out.println("The product is not eligible for discount as"     + warningmsg);
		}
		//Verify the bill amount is matching with the discount price or not 
		else {
			String totalAmountafterDiscount = driver.findElementByXPath("//span [@class='price-value bold-font']").getText();
			SoftAssert softAssert = new SoftAssert();
			softAssert.assertEquals(totalAmountafterDiscount, discPrice);
			System.out.println("The Total Amount matches with discounted amount");
			}
		 //Click on Delete and Delete the item from Bag 
		driver.findElementByXPath("//div [@class='delete-btn']").click();
		System.out.println("The item is successfully deleted");
       driver.quit();
	}
}
