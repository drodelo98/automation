package XtendoPruebas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class pruebita {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//try {
			//Runtime.getRuntime().exec("cmd /c start cmd.exe /K \" cd C:\\Program Files\\Google\\Chrome\\Application && chrome.exe --remote-debugging-port=9898 &&exit");
		//}
		//catch(IOException e){}

		System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe");
		ChromeOptions opt = new ChromeOptions();
		opt.setExperimentalOption("debuggerAddress","localhost:9898");
		WebDriver driver = new ChromeDriver(opt);
		
		int processed_orders=0;
		List<WebElement> courier_amount;
		String courier_state = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
		String order_id = "999999";
		
		courier_amount = driver.findElements(By.className("editable_row"));
		

		
		System.out.println(driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/div/table/tbody/tr[1]/td[9]")).getText());
		System.out.println(driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/div/table/tbody/tr[3]/td[9]")).getText());
		
	}
}
