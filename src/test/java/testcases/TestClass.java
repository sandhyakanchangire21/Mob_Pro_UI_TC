package testcases;

import java.io.File;
import java.util.Scanner;

import org.openqa.selenium.WindowType;
import org.testng.annotations.Test;
import pages.PageClass;

public class TestClass extends PageClass {

	@Test
	public void test_1() {
		// Get user,email and pass
		String email = getProperty("email");
		String password = getProperty("password");
		String username = getProperty("username");
		// Test Data
		String searchWord = "amazon";
		String expUrl = "https://www.amazon.in";
		String productCategory = "Electronics";
		String productKeyword = "dell computers";
		int minValue = 30000;
		int maxValue = 50000;
		int checkPricePageCount = 2;
        int checkFiveStarProductsPageCount = 2;
		// Search keyword on google
		googleSearch(searchWord, expUrl);
		// Select and open expected url from results 
		loginToAmazonPortal(email, password, username);
		// Select product category on amazon home page
		selectCategory(productCategory);
		// Search product name in searchbar
		seachProduct(productKeyword);
		// Apply price filter to the result
		applyPriceFilter(minValue, maxValue);
		// Validate product prices are within range
		checkPricesAreWithinRangeOnPages(checkPricePageCount, minValue, maxValue);
		// Collect five star products from pages
		allFiveStarProductsListOnPages(checkFiveStarProductsPageCount);
		// Add first five star product encountered into wishlist and validate
		addFirstFiveStarProductToWishlist();
	}
}
