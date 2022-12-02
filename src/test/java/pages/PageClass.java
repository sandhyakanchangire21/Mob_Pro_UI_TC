package pages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import baseUtility.CommonUtility;

public class PageClass extends CommonUtility {
	// google search page xpaths
	String searchbarFld = "//input[@title='Search']";
	String searchBtn = "//center/input[@value='Google Search']";
	// amazon login page xpaths
	String loginForm = "//div[@class='a-box-inner a-padding-extra-large']";
	String emailFld = "//input[@type='email']";
	String passwordFld = "//input[@type='password']";
	String loginBtn = "//input[@id='signInSubmit']";
	String continueBtn = "//input[@id='continue']";
	// amazon home page xpaths
	String myAccountTxt = "//div[@class='nav-line-1-container']";
	protected String searchProduct = "//input[@id='twotabsearchtextbox']";
	protected String productSearchBtn = "//input[@id='nav-search-submit-button']";
	String categoryDropdown = "//select[@id='searchDropdownBox']";
	String minPriceFilterFld = "//input[@id='low-price']";
	String maxPriceFilterFld = "//input[@id='high-price']";
	String priceFilterApplyBtn = "//input[@class='a-button-input']";
	String allProductPricesOnPage = "//a[@target='_blank']/span[@class='a-price']/span[@aria-hidden='true']/span[@class='a-price-whole']";
	String currentPage = "//span[contains(@aria-label,'Current page')]";
	String nextPageBtn = "//a[normalize-space()='Next']";
	String firstPageBtn = "//a[@aria-label='Go to page 1']";
	String fiveStarProducts = "//div[@class='a-section a-spacing-small a-spacing-top-small']//i/span[text()='5.0 out of 5 stars']/preceding::h2[1]/a/span";
	String addToCartBtn = "//input[@id='add-to-cart-button']";
	String addToCartConfirmationTxt = "//span[normalize-space()='Added to Cart']";

	// #######################################################################

	public void googleSearch(String searchString, String expectedUrl) {
		navigateTo("https://www.google.co.in/");
		typeIn(searchbarFld, searchString);
		clickOn(searchBtn);
		findExpectedUrlInSearchResults(expectedUrl);
	}

	public void loginToAmazonPortal(String email, String password, String username) {
		clickOn(myAccountTxt);
		if (isElementDisplayed(loginForm)) {
			writeToConsole("INFO", "Logging into application");
			typeIn(emailFld, email);
			clickOn(continueBtn);
			typeIn(passwordFld, password);
			clickOn(loginBtn);
		}

		if (getText(myAccountTxt).contains(username)) {
			writeToConsole("INFO", "User logged in successfully");
		} else {
			testStepFailed("User login failed");
		}

	}

	public void selectCategory(String text) {
		waitForPageToLoad();
		Select category = new Select(driver.findElement(By.xpath(categoryDropdown)));
		category.selectByVisibleText(text);
		waitForPageToLoad();
		writeToConsole("INFO", "Selected category: " + text);
	}

	public void applyPriceFilter(int minValue, int maxValue) {
		typeIn(minPriceFilterFld, String.valueOf(minValue));
		typeIn(maxPriceFilterFld, String.valueOf(maxValue));
		clickOn(priceFilterApplyBtn);
		waitForPageToLoad();
		writeToConsole("INFO", "Applied filter for --> minimun value: " + minValue + " maximum value: " + maxValue);
	}

	public void checkPricesAreWithinRangeOnPages(int pageCount, int minRange, int maxRange) {
		List<Integer> priceList = new ArrayList<>();

		waitForPageToLoad();
		while (Integer.parseInt(getText(currentPage)) <= pageCount) {
			List<WebElement> priceListElements = findWebElements(allProductPricesOnPage);
			for (WebElement i : priceListElements) {
				String priceString = "";
				String[] pricePieces = i.getText().split(",");
				for (String piece : pricePieces) {
					priceString = priceString + piece;
				}
				priceList.add(Integer.parseInt(priceString));
			}
			clickOn(nextPageBtn);
		}
		clickOn(firstPageBtn);
		// Sort list
		priceList.sort(Comparator.naturalOrder());
		if (priceList.get(0) < minRange || priceList.get(priceList.size() - 1) > maxRange) {
			writeToConsole("WARN", "Not all prices are within range");
		} else {
			writeToConsole("INFO", "All the prices are within range");
		}
	}

	public void seachProduct(String productKeyword) {
		typeIn(searchProduct, productKeyword);
		clickOn(productSearchBtn);
		writeToConsole("INFO", "Searching product: " + productKeyword);
	}

	public void allFiveStarProductsListOnPages(int pageCount) {
		List<String> productList = new ArrayList<>();
		List<WebElement> productListElements = null;
		waitForPageToLoad();
		int actualPageNo = Integer.parseInt(getText(currentPage));
		while (actualPageNo <= pageCount) {
			try {
				productListElements = findWebElements(fiveStarProducts);
				for (WebElement i : productListElements) {
					productList.add(i.getText());
				}
				clickOn(nextPageBtn);
				actualPageNo = Integer.parseInt(getText(currentPage));
			} catch (NullPointerException e) {
				writeToConsole("WARN", "No five start product found on page " + actualPageNo);
			}
		}
		clickOn(firstPageBtn);
		// print five star listed products
		writeToConsole("INFO", "Following are five star products: ");
		for (int j = 0; j < productList.size(); j++) {
			writeToConsole("INFO", j + 1 + ". " + productList.get(j));
		}
	}

	public String addFirstFiveStarProductToWishlist() {
		List<WebElement> productListElements = null;
		String productName = null;
		boolean flag = false;
		waitForPageToLoad();
		int actualPageNo;
		while (!flag) {
			actualPageNo = Integer.parseInt(getText(currentPage));
			productListElements = findWebElements(fiveStarProducts);
			if (productListElements != null) {
				productName = productListElements.get(0).getText();
				productListElements.get(0).click();
				waitForPageToLoad();
				transferControlToWindow(2, false);
				waitForPageToLoad();
				clickOn(addToCartBtn);
				if (isElementDisplayed(addToCartConfirmationTxt)) {
					writeToConsole("INFO", "Product is added to cart");
					flag = true;
					break;
				} else {
					writeToConsole("WARN", "Product is not added to cart");
				}
			} else {
				writeToConsole("WARN", "There is no five star product on page: " + actualPageNo);
			}
			clickOn(nextPageBtn);
		}
		transferControlToWindow(2, true);
		transferControlToWindow(1, false);
		if (productName != null) {
			writeToConsole("INFO", "Following is the product added: " + productName);
			return productName;
		} else {
			writeToConsole("WARN", "No product added");
			return null;
		}
	}
}
