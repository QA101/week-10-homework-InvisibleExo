import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.google.common.io.Files;



public class DemoqaExample {
	static Random rand = new Random();

	public static void main(String[] args) {
		//Setting up to system property based on where chromedriver is located on Hard Drive
		System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver_win32\\chromedriver.exe");
		//Setting a Webdriver variable as sub-class ChromeDriver
		WebDriver driver = new ChromeDriver();
		
		driver.get("http://demoqa.com");
		WebElement w = driver.findElement(By.id("menu-item-374"));
		w.click();
		//or alternative
		//driver.findElement(By.id("menu-item-374")).click();
		
		if(driver.getCurrentUrl().contains("http://demoqa.com/registration/")) {
			System.out.println("Destination confirmed.");
		}
		
		//Fill in first name and last name
		driver.findElement(By.id("name_3_firstname")).sendKeys("Tony");
		driver.findElement(By.id("name_3_lastname")).sendKeys("Lawrence");
		
		
		
		//Filling our relational status (if only there was engaged option.)
		List<WebElement> status = driver.findElements(By.name("radio_4[]"));
		for(WebElement m : status) {
			if(m.getAttribute("value").equals("single")) {
				m.click();
			}
		}
		// Selecting our hobby
		List<WebElement> hobby = driver.findElements(By.name("checkbox_5[]"));
		for(WebElement m : hobby) {
			if(m.getAttribute("value").equalsIgnoreCase("reading")) {
				m.click();
			}
		}
		
		//Select our country
		Select country = new Select(driver.findElement(By.id("dropdown_7")));
		country.selectByValue("United States");
		
		// Selecting our date of birth
		Select birthMonth = new Select(driver.findElement(By.id("mm_date_8")));
		birthMonth.selectByValue("3");
		Select birthDay = new Select(driver.findElement(By.id("dd_date_8")));
		birthDay.selectByValue("23");
		Select birthYear = new Select(driver.findElement(By.id("yy_date_8")));
		birthYear.selectByValue("1989");
		
		//Filling out phone number (fake number to avoid accidental use innocent user, unless provided with a test number )
		driver.findElement(By.id("phone_9")).sendKeys("12025550159");
		
		//Filling out a username and email.
		driver.findElement(By.id("username")).sendKeys("PickleRick17Rat");
		driver.findElement(By.id("email_1")).sendKeys("ratpickles@gmail.com");
		
		//Entering picture profile
		w = driver.findElement(By.id("profile_pic_10"));
		w.sendKeys("C:\\Users\\Rigat\\\\Documents\\Java2017/week-10-homework-InvisibleExo/PicklePic.jpg");
		
		//Entering info for About Yourself
		driver.findElement(By.id("description")).sendKeys("I'm Pickle Rick and I'm someone who"
				+ " isn't satisfied with something in life I just make it. And I know some people like"
				+ " to say more about themselves to help relate with others, which is a state of mind we "
				+ " all like to be in, but that's not for "
				+ " someone like me, because I'm a pickle......when I feel like it.");
		
		//Entering a random generated legitmate PW
		int pwLength = rand.nextInt(32 - 17) + 17;
		String pw = pWGenerator(pwLength);
		driver.findElement(By.id("password_2")).sendKeys(pw);
		//To confirm on console that the password cells match.
		
		System.out.println("Password: " + driver.findElement(By.id("password_2")).getAttribute("value"));
		driver.findElement(By.id("confirm_password_password_2")).sendKeys(pw);
		//To confirm on console that the password cells match.
		System.out.println("Confirm Password: " + driver.findElement(By.id("confirm_password_password_2")).getAttribute("value"));
		
		//Scroll up to get top portion of page
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("window.scroll(0,-150)", "");
		
		
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			Files.copy(srcFile, new File("C:\\Users\\Rigat\\Documents\\Java2017\\week-10-homework-InvisibleExo\\DemoQaScreenshot.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Capture lower half of webpage.
		js.executeScript("scroll(0,500);");
		
		srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			Files.copy(srcFile, new File("C:\\Users\\Rigat\\Documents\\Java2017\\week-10-homework-InvisibleExo\\DemoQaScreenshotPt2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//States that username exists.
		/*driver.findElement(By.name("pie_submit")).click();
		synchronized (driver) {
		
				try {
					driver.wait(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		} */
		
		
		//To close the browser
		driver.quit();

	}
	
	//Do some research on Static overflow for the best use of a random-generator(consider, code that could have the best against attackers, until type into the input block.)

	public static String pWGenerator(int len) {
		SecureRandom rnd = new SecureRandom();
		String pwInput = letterAndNumCatalog();
		String pwInput2 = specialChar();
		int specialCount = 0;
		String pwBuilder = "";
		for (int i = 0; i < len; i++) {
			if(specialCount < 2 && (rand.nextInt(4) + 1 == 1  || rand.nextInt(4) + 1 == 3)) {
				pwBuilder += pwInput2.charAt(rnd.nextInt(pwInput2.length()));
				specialCount++;
			}
			else {
			pwBuilder += pwInput.charAt(rnd.nextInt(pwInput.length()));
			}
		}
		return pwBuilder;
	}
	//Given more time and cooled down from thinking, maybe lookup more solutions to store character, letters and numbers
	//And when to ensure at least one upper-case letter, special character, and number are always included.
	public static String letterAndNumCatalog() {
		String catalog = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		return catalog;
	}
	//Could have it in the method above, but wish to avoid the chance of over-use of special characters
	public static String specialChar () {
		String catalog = "!@#%$*";
		return catalog;
	}

}
