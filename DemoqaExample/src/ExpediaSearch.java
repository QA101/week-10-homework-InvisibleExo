import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.google.common.io.Files;



public class ExpediaSearch {
	static WebDriver driver;
	static WebElement search;
	static WebElement datePicker;
	WebElement enter;
	Select price;
	

	public static void main(String[] args) {
		//Set up webdriver
		System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver_win32\\chromedriver.exe");
		//Set up webdriver variable.
		driver = new ChromeDriver();
		
		//Set up Expedia website.
		driver.get("http://expedia.com");
		driver.manage().window().maximize();
		
		search = driver.findElement(By.id("package-origin-hp-package"));
		search.sendKeys("Seattle, WA, United States");
		search = driver.findElement(By.id("package-destination-hp-package"));
		search.sendKeys("Mexico City, Mexico");
		
		//Entering a Departing and Returning Time Dates
		search = driver.findElement(By.id("package-departing-hp-package"));
		/*
		 * Failed attempt to select the dropdates, but could be useful...
		DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
		Date date = new Date();
		String currDate = dateFormat.format(date); */
		search.click();
		datePicker = driver.findElement(By.className("datepicker-cal"));
		List<WebElement> specificDate = datePicker.findElements(By.tagName("td"));
		//Picking the date for Departing
		for (WebElement cell : specificDate) {
			String cellDate = cell.getText();
			if (cellDate.equals("10")) {
				cell.click();
				break;
			}
		}
		
		//Now for returning
		search = driver.findElement(By.id("package-returning-hp-package"));
		search.click();
		datePicker = driver.findElement(By.className("datepicker-cal"));
		specificDate = datePicker.findElements(By.tagName("td"));
		for (WebElement cell : specificDate) {
			String cellDate = cell.getText();
			if (cellDate.equals("15")) {
				cell.click();
				break;
			}
		}
		//Now search the results
		driver.findElement(By.id("search-button-hp-package")).click();
		//Give enough time until the results show up.
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		
		
		//Sort by Price
		List<WebElement> sortFilter = driver.findElements(By.className("option"));
		File expediaResults = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			Files.copy(expediaResults, new File("C:\\Users\\Rigat\\Documents\\Java2017\\week-10-homework-InvisibleExo\\expediaBefore.png"));
			} catch (IOException io) {
				io.printStackTrace();
			}
		//Select Price filter
		for(WebElement option : sortFilter) {
			String type = option.getText();
			if (type.equals("Price")) {
				option.click();
				break;
			}
		}
		
		
		
		synchronized (driver) {
			try {
				driver.wait(3600);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//In case results aren't scrolled to the top.
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("window.scroll(0,-200)", "");
		
		expediaResults = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			Files.copy(expediaResults, new File("C:\\Users\\Rigat\\Documents\\Java2017\\week-10-homework-InvisibleExo\\expediaAfter.png"));
			} catch (IOException io) {
				io.printStackTrace();
			}
		
		//Close driver
		driver.close();

	}

}
