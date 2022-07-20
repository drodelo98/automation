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
import java.util.ArrayList;
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
//		try {
//			Runtime.getRuntime().exec("cmd /c start cmd.exe /K \" cd C:\\Program Files\\Google\\Chrome\\Application && chrome.exe --remote-debugging-port=9898 &&exit");
//		}
//		catch(IOException e){}

//		System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe");
//		ChromeOptions opt = new ChromeOptions();
//		opt.setExperimentalOption("debuggerAddress","localhost:9898");
//		WebDriver driver = new ChromeDriver(opt);



		
		String country_id = "";
		int country_iterator = 0;
		List<String> country_ids = new ArrayList<String>() {{
			add("ar");
			add("pe");
			add("py");
			add("ec");
			add("uy");
			add("pa");
			add("cr");
//			add("ve");
			add("do");
			add("bo");
			add("sv");
			add("cl");
			add("ni");
			add("hn");
			add("gt");
		}};
		
//		for(String country : country_ids) {
//			try {
//				driver.get("https://"+country+".usehurrier.com/users/sign_in");
//				driver.findElement(By.xpath("/html/body/div[3]/div/button")).click();
//				System.out.println("Se abre"+ country);
//				functions.esperar_time(5000);
//			}catch(Exception e) {
//				System.out.println("Ya está abierto "+ country);
//			}
//
//		}
//		
//		
//		for(String country : country_ids) {
//			try {
//				driver.get("https://"+country+".us.logisticsbackoffice.com/dashboard/v2/hurrier/login");
//				functions.esperar_time(5000);
//				driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[4]/button")).click();
//				System.out.println("Se abre"+ country);
//				functions.esperar_time(5000);
//			}catch(Exception e) {
//				System.out.println("Ya está abierto "+ country);
//			}
//
//		}
		
		System.out.print(country_ids.size()-1);
		
		

}
}
