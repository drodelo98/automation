package XtendoPruebas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class functions {
	
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

	public static void inconcert_call(WebDriver driver, String rider_phone) {
		boolean segunda_llamada = false;
		for(int i=0;i<2;) {
			if(!segunda_llamada) {
				driver.findElement(By.className("butQuickCall")).click();
				esperar_time(5000);
				segunda_llamada = true;
			}

			if(driver.findElements(By.tagName("iframe")).size()>0) {
				driver.switchTo().frame(0);
				esperar_time(1500);
				
				try {
					WebElement cuadro_llamadas = driver.findElement(By.id("digitsDisplay"));
					cuadro_llamadas.click();
					cuadro_llamadas.sendKeys(rider_phone, Keys.ENTER);
					i++;
				}catch(Exception e) {
					System.out.print("No se pudo llamar");
					driver.switchTo().defaultContent();
					System.out.println("Se ejecuta el cambio al parent frame");
					if(driver.findElements(By.tagName("iframe")).size()==0) {
						esperar_time(1000);
						driver.findElement(By.className("butQuickCall")).click();
					}
				}
			}
		}	
		
		while(true) {
			if(driver.findElements(By.tagName("iframe")).size()==0) {
				break;
			}
		}
	}
	
	public static void esperar_time(int time) {
		try {
		    Thread.sleep(time);
		} catch (InterruptedException ie) {
		    Thread.currentThread().interrupt();
		}
	}

	public static void logs_write(String order_id,String checkwadTime) {
	      try {
	          String data = "Se procesa orden: " + order_id +" a la hora: "+ checkwadTime +";\n";
	          File f1 = new File("C:\\x_logs\\logs.txt");
	          if(!f1.exists()) {
	             f1.createNewFile();
	          }

	          FileWriter fileWritter = new FileWriter("C:\\x_logs\\logs.txt",true);
	          BufferedWriter bw = new BufferedWriter(fileWritter);
	          bw.write(data);
	          bw.close();
	          System.out.println("Done");
	       } catch(IOException e){
	          e.printStackTrace();
	       }
	}
	
	public static String inconcert_login(WebDriver driver) {
		//-----------------------------------------------------------------------<ABRE INCONCERT>-------------------------------------------------------------------------------------------------------------
		driver.get("https://pedidosya.i6.inconcertcc.com/inconcert/apps/agent/login/");
		driver.findElement(By.id("username")).sendKeys("sebastian.rodelo_ndo.ext@pedidosya");
		driver.findElement(By.id("password")).sendKeys("Pedidosya2022");
		driver.findElement(By.id("submitButton")).click();
		
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.elementToBeClickable(By.className("loadingWaitingContainer"))).click();
		
		WebElement loadingDisplay = driver.findElement(By.className("loadingWaitingContainer"));
		
		
		while(true) {
			if (!loadingDisplay.isDisplayed()) {
				System.out.println("Inconcert loaded.");
				break;
			}
		}	
		//----------------------------------------------------------------------<CLICK EN ESTADO Y SELECCIONA NO ACD>--------------------------------------------------------------------------------------------
		driver.findElement(By.className("userStatus")).click();
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[7]/div[1]/div[3]/div[2]/div[2]"))).click();
		
		return driver.getWindowHandle();
	}
	
	public static String hurrier_getWHandle(WebDriver driver) {
		driver.switchTo().newWindow(WindowType.TAB);
		return  driver.getWindowHandle();
	}
	
	public static void hurrier_OpenOrder(WebDriver driver, String country_id) {
		driver.get("https://"+country_id+".us.logisticsbackoffice.com/dashboard/v2/hurrier/issues");
		functions.esperar_time(1000);
		driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[1]/div/div/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div[3]/div/div[1]")).click();						
		driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[2]/div[1]/div/div")).click();
		driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[5]")).click();
		driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[5]")).click();
		System.out.println("Organized by 'plazo'");
	}
	
	public static boolean hurrier_pickOrder(WebDriver driver,String order_id, String country_id,SimpleDateFormat dateFormat) {
		String currentTime = dateFormat.format(new Date());
		int wait_time = 5;
		if(!currentTime.contains("12:")) {
			wait_time = 20;
		}
		try {
			System.out.println("Buscando orden en: " + country_id);
			//-----------------------------------------------------------------------<PICKEA LA ORDEN>------------------------------------------------------------------------------------------------------------------
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait_time));
			driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[7]/div/button")).click();
			System.out.println("Puede pickear");
			esperar_time(500);
			//-----------------------------------------------------------------------<ALMACENA EL NUMERO DE LA ORDEN>--------------------------------------------------------------------------------------------------
			driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[6]/div")).click();
			order_id =driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[1]/div/div/a")).getText();
			System.out.println("Orden "+ order_id + " pickeada");				
			return false;
		}catch(Exception e) {
			return true;
		}

	}
	
	public static int hurrier_changeCountry(WebDriver driver, int country_iterator) {
		driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[2]/div[2]/div/div")).click();
		esperar_time(500);					
		if(country_iterator < 14) {
			country_iterator++;
		}else {
			country_iterator = 0;
		}		
		return country_iterator;
	}
	
	public static int send_slack_message(SimpleDateFormat dateFormat, int processed_orders2,String slackWindow,WebDriver driver) {
		String pro_check_string = dateFormat.format(new Date());
		if(pro_check_string.equals("12:30 AM")||
		   pro_check_string.equals("1:00 AM")||
		   pro_check_string.equals("1:30 AM")||
		   pro_check_string.equals("2:00 AM")||
		   pro_check_string.equals("2:30 AM")||
		   pro_check_string.equals("3:00 AM")||
		   pro_check_string.equals("3:30 AM")||
		   pro_check_string.equals("4:00 AM")){
			System.out.println("Ordenes pickeadas: " + processed_orders2);
			if(processed_orders2 < 1) {
				driver.switchTo().window(slackWindow);
				String slack_text = "";
				System.out.print("La hora en este momento es: "+pro_check_string);
				switch(pro_check_string) {
				case "12:30 AM":
					slack_text = "Reporto media hora muerta entre 12:00 AM Y 12:30 AM, WAD, todos los paises sebastian.rodelo_ndo.ext@pedidosya.com";
					break;
				case "1:00 AM":
					slack_text = "Reporto media hora muerta entre 12:30 AM Y 1:00 AM, WAD, todos los paises sebastian.rodelo_ndo.ext@pedidosya.com";
					break;
				case "1:30 AM":
					slack_text = "Reporto media hora muerta entre 1:00 AM Y 1:30 AM, WAD, todos los paises sebastian.rodelo_ndo.ext@pedidosya.com";
					break;
				case "2:00 AM":
					slack_text = "Reporto media hora muerta entre 1:30 AM Y 2:00 AM, WAD, todos los paises sebastian.rodelo_ndo.ext@pedidosya.com";
					break;
				case "2:30 AM":
					slack_text = "Reporto media hora muerta entre 2:00 AM Y 2:30 AM, WAD, todos los paises sebastian.rodelo_ndo.ext@pedidosya.com";
					break;
				case "3:00 AM":
					slack_text = "Reporto media hora muerta entre 2:30 AM Y 3:00 AM, WAD, todos los paises sebastian.rodelo_ndo.ext@pedidosya.com";
					break;
				case "3:30 AM":
					slack_text = "Reporto media hora muerta entre 3:00 AM Y 3:30 AM, WAD, todos los paises sebastian.rodelo_ndo.ext@pedidosya.com";
					break;
				case "4:00 AM":
					slack_text = "Reporto media hora muerta entre 3:30 AM Y 4:00 AM, WAD, todos los paises sebastian.rodelo_ndo.ext@pedidosya.com";
					break;	
				}
				esperar_time(1000);
				try {
					driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[4]/div/div/div[3]/div/div[2]/div/div/div/div[2]/div/div/div/div[2]/div/div[1]")).sendKeys(slack_text,Keys.ENTER);					
					processed_orders2 = 0;
				}catch(Exception e) {
					System.out.print("No se pudo enviar mensaje en slack");
				}
				esperar_time(60000);
			}
		}
		return processed_orders2;
	}
	
	public static String slack_open(WebDriver driver) {
		driver.switchTo().newWindow(WindowType.TAB);
		driver.get("https://app.slack.com/client/T052P4KCD/C01QS64Q0AK");
		return driver.getWindowHandle();
	}
	
	public static String turnos_login(WebDriver driver) {
		driver.switchTo().newWindow(WindowType.TAB);
		driver.get("https://www.rs.servicesjw.com/index");
		esperar_time(2000);
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div[1]/div/div/div[2]/form/button[1]")).click();
		return driver.getWindowHandle();
	}
	
	public static List<WebElement> bo_getchats(WebDriver driver, String order_id){
		driver.get("https://backoffice-app.pedidosya.com/#/orders/"+ order_id );
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.findElement(By.xpath("//*[@id=\"top\"]/div[1]/div/div[1]/div/div[3]/div[2]/div[1]/div/div[2]/button")).click(); //ABRE BO CHAT
		return driver.findElements(By.className("p-l-10")); //RETORNA EL LISTADO DE CHATS
	}
	
	public static void turnos_logout(WebDriver driver, String turnosWindow) {
		driver.switchTo().window(turnosWindow);
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div[1]/div/div/div[4]/div/div/div[2]/form/div[1]/div[2]/label")).click();
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div[1]/div/div/div[4]/div/div/div[2]/form/div[1]/div[4]/label")).click();
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div[1]/div/div/div[4]/div/div/div[2]/form/div[1]/div[7]/input")).sendKeys("20","07","2022",Keys.TAB,"00","00");
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div[1]/div/div/div[4]/div/div/div[2]/form/div[1]/div[8]/input")).sendKeys("20","07","2022",Keys.TAB,"04","00");
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div[1]/div/div/div[4]/div/div/div[2]/form/div[1]/div[9]/label")).click();
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div[1]/div/div/div[4]/div/div/div[2]/form/div[1]/label[4]/h5/div/input")).sendKeys("4");
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div[1]/div/div/div[4]/div/div/div[2]/form/div[2]/textarea")).sendKeys("4 horas dispatch service");
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div[1]/div/div/div[4]/div/div/div[2]/form/div[3]/button[2]")).click();
		System.out.println("Se cierra sesión en turnos");
		driver.close();		
	}
	
	public static void freschat_session(WebDriver driver, int option) {
		driver.switchTo().newWindow(WindowType.TAB);
		driver.get("https://dh-latam.freshchat.com/a/133969877319489/people");
		esperar_time(5000);
		driver.findElement(By.xpath("/html/body/div[8]/div[5]/div[1]/div[1]/div/span/span/span[1]")).click();
		driver.findElement(By.xpath("/html/body/div[8]/div[5]/div[1]/div[1]/ul/div[2]/div[1]/div/span[3]")).click();
		driver.findElement(By.xpath("/html/body/div[8]/div[7]/div/ul/li["+option+"]")).click();
		if(option == 2) {
			System.out.println("Se cierra sesión en freschat");
		}else {
			System.out.println("Se abre sesión en freschat");
		}
		driver.close();
	}
	
	public static void inconcert_logout(WebDriver driver,String inconcertWindow) {
		driver.switchTo().window(inconcertWindow);
		esperar_time(2000);
		driver.findElement(By.xpath("/html/body/div[7]/div[4]/div/i")).click();
		driver.findElement(By.xpath("/html/body/div[7]/div[5]/div[2]")).click();
		System.out.print("Se cierra sesión en inconcert");
		driver.close();
	}
}






