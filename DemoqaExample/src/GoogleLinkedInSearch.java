import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.io.Files;
//This is actually a search with FaceBook
public class GoogleLinkedInSearch {
	static WebDriver driver;
	static WebElement search;
	static WebElement enter;
	static List<WebElement> results;
	static List <WebElement> nextPage;
	static WebElement subject;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Setting up webdriver
		System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		
	}

	

	@Before
	public void setUp() throws Exception {
		driver.get("http://www.google.com");
		search =  driver.findElement(By.id("lst-ib"));
		enter = driver.findElement(By.name("btnK"));
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.close();
	}
 
	
	// Look for specific href 
	
	@Test
	public void test() throws StaleElementReferenceException {
		//Begin the search.
		search.sendKeys("Tony Lawrence Facebook");
		enter.sendKeys(Keys.RETURN);
		
		//Identify search results by class name. 
		//Find the first Facebook link containing my name then do the search through Facebook Search engine.
		
		boolean found = false;
		boolean foundInFB = false;
		int resultLink = 0;
		OUTER:
		while(found == false) {
			results = driver.findElements(By.className("g"));
			System.out.println( results.toString());
			for(WebElement r : results) {
				resultLink++;
				System.out.println("Result "+ resultLink + " : " + r.findElement(By.tagName("a")).getAttribute("href")); 
				if(r.findElement(By.tagName("a")).getAttribute("href").equals("https://www.facebook.com/public/Tony-Lawrence") || r.findElement(By.tagName("a")).getAttribute("href").equals("https://www.facebook.com/rigatoni07") ) {
					driver.get(r.findElement(By.tagName("a")).getAttribute("href"));
					found = true;
					break OUTER;
				}
			}
			//Go to next page
			try {
				driver.findElement(By.id("pnnext")).click();
			} catch (NoSuchElementException e) {
				System.out.println("End of the Search results.");
				fail("No profile under those rules matches our results or Google search engines maynot have picked it up. Please make another query.");
				break OUTER;
			}
			
		}
		
		//Look through facebook results.
		resultLink = 0;
		try {
		System.out.println("Now looking in FB results.");
		int pageCounter = 0;
		FBSearch:
		while(foundInFB == false) {
			nextPage = driver.findElements(By.className("_262a"));
			System.out.println("Web Page: " + driver.getCurrentUrl());
			results = driver.findElements(By.className("_gll"));
			for(WebElement fb : results) {
				resultLink++;
				System.out.println("Result "+ resultLink + " : " + fb.findElement(By.tagName("a")).getAttribute("href"));
				if(fb.findElement(By.tagName("a")).getAttribute("href").equals("https://www.facebook.com/public/Tony-Lawrence") || fb.findElement(By.tagName("a")).getAttribute("href").equals("https://www.facebook.com/rigatoni07")) {
					driver.get(fb.findElement(By.tagName("a")).getAttribute("href"));
					foundInFB = true;
					break FBSearch;
				}
					
			}
			//Currently, this condition doesn't go through the result pages in order, but, it gets to all of them.
			//Future research will be to sort this list in numerical order since
			//Research doesn't show a clean process for sorting with List and WebElements
			//This small block helps avoid hitting StaleReferenceExceptions for now.
			pageCounter++;
			if (nextPage.size() > pageCounter) {
				nextPage.get(pageCounter).click();
				
			}
			
			//Once the results are have been searched.	
			else if (nextPage.size() <= pageCounter) {	
				//If no results are found...
				System.out.println("No results in current query, now modifying FaceBook query.");
				resultLink = 0;
				search = driver.findElement(By.name("query"));
				search.clear();
				search.sendKeys("Tony Lawrence (rigatoni)");
				search.sendKeys(Keys.ENTER);
			}
			
		}
		assertEquals(driver.getCurrentUrl(),"https://www.facebook.com/rigatoni07");
		if(driver.getCurrentUrl().equals("https://www.facebook.com/rigatoni07")) {
			System.out.println("Found your webpage on FaceBook: " + driver.getCurrentUrl());
			File foundFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try {
			Files.copy(foundFile, new File("C:\\Users\\Rigat\\Documents\\Java2017\\week-10-homework-InvisibleExo\\MyFaceBookAccount.png"));
			} catch (IOException io) {
				io.printStackTrace();
			}
			
		}
		} catch (Exception e) {
			fail("Error ecounter during automation.");
			System.out.println(driver.getCurrentUrl());
			e.printStackTrace();
			File bugPicture = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try {
			Files.copy(bugPicture, new File("C:\\Users\\Rigat\\Documents\\Java2017\\week-10-homework-InvisibleExo\\facebookbug.png"));
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
		
		
	}

}
