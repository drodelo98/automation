package XtendoPruebas;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class chrome_handle {
	
	
	public static WebDriver chrome_h() {
		try {
			Runtime.getRuntime().exec("cmd /c start cmd.exe /K \" cd C:\\Program Files\\Google\\Chrome\\Application && chrome.exe --remote-debugging-port=9898 &&exit");
		}
		catch(IOException e){
			System.out.print("No se pudo iniciar Chrome Driver");
		}

		System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe");
		ChromeOptions opt = new ChromeOptions();
		opt.setExperimentalOption("debuggerAddress","localhost:9898");
		WebDriver driver = new ChromeDriver(opt);
		
		return driver;
	}

	public static void main(String[] args) {
		

	}

}
