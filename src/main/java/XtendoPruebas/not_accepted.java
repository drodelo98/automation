package XtendoPruebas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Driver;
import java.text.SimpleDateFormat;
import java.time.Duration;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class not_accepted {

	public static void main(String[] args) {
		int system_state = 0;
		WebDriver driver = functions.chrome_h();
		String hurrierWindow="";
		String inconcertWindow="";
		String order_id ="";
		String courier_name="";
		String operationWindow ="";
		List<WebElement> courier_amount;
		int processed_orders=0;
		String courier_state = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
		
	
		while(true) {		
			if(system_state==0) {
				

				
				//-----------------------------------------------------------------------<ABRE INCONCERT>-------------------------------------------------------------------------------------------------------------
				inconcertWindow = driver.getWindowHandle();
				driver.get("https://pedidosya.i6.inconcertcc.com/inconcert/apps/agent/login/");
				driver.findElement(By.id("username")).sendKeys("sebastian.rodelo_ndo.ext@pedidosya");
				driver.findElement(By.id("password")).sendKeys("Pedidosya2022");
				driver.findElement(By.id("submitButton")).click();
				
				new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.elementToBeClickable(By.className("loadingWaitingContainer"))).click();
				
				WebElement loadingDisplay = driver.findElement(By.className("loadingWaitingContainer"));
				
				
				while(true) {
					if (!loadingDisplay.isDisplayed()) {
						System.out.println("Ya se fue");
						break;
					}
				}	
				//----------------------------------------------------------------------<CLICK EN ESTADO Y SELECCIONA NO ACD>--------------------------------------------------------------------------------------------
				driver.findElement(By.className("userStatus")).click();
				new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[7]/div[1]/div[3]/div[2]/div[2]"))).click();
//				
//				//----------------------------------------------------------------------<ABRE TURNOS, LO DEJA ABIERTO>----------------------------------------------------------------------------------------------------
//				driver.switchTo().newWindow(WindowType.TAB);
//				driver.get("https://www.rs.servicesjw.com/index");
				//----------------------------------------------------------------------<ABRE HURRIER>---------------------------------------------------------------------------------------------------------------------
				driver.switchTo().newWindow(WindowType.TAB);
				hurrierWindow = driver.getWindowHandle();
				
				driver.get("https://ar.us.logisticsbackoffice.com/dashboard/v2/hurrier/issues");
				driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[1]/div/div/div/div/input")).click();
				driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div[3]/div/div[3]")).click();
				system_state=1;
			}

			
			//----------------------------------------------------------------------<ORDENA POR PLAZO>--------------------------------------------------------------------------------------------

			while(true) {
				driver.switchTo().window(hurrierWindow);
				driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[2]/div[1]/div/div")).click();
				driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[5]")).click();
				driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[5]")).click();
				
				try {
					//-----------------------------------------------------------------------<PICKEA LA ORDEN>------------------------------------------------------------------------------------------------------------------
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[7]/div/button")).click();
					functions.esperar_time(500);

					//-----------------------------------------------------------------------<ALMACENA EL NUMERO DE LA ORDEN>--------------------------------------------------------------------------------------------------
					new WebDriverWait(driver, Duration.ofSeconds(1)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[1]/div/div/a"))).getText();
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[6]/div")).click();
					order_id =driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[1]/div/div/a")).getText();
					Date current_time = new Date();
					String checkwadTime = dateFormat.format(current_time);
					System.out.println("Orden "+ order_id + " pickeada a la hora:" + checkwadTime);
					
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[6]/div")).click();//Ordena por pickeado
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[6]/div")).click();//Ordena por pickeados
					driver.switchTo().newWindow(WindowType.TAB);
					operationWindow = driver.getWindowHandle();			
					driver.get("https://ar.us.logisticsbackoffice.com/dispatcher/order_details/"+ order_id);
					functions.esperar_time(500);
					courier_amount = driver.findElements(By.className("editable_row"));
					
					if(courier_amount.size()<2) {
						driver.switchTo().window(hurrierWindow);
						functions.esperar_time(5000);
						driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[7]/div/button[2]")).click();//Procesa
						System.out.println("Orden "+ order_id + " procesada.");
					}else {
						System.out.println("No se procesa.");
					}
										
					
					break;
				}catch(Exception e) {
					System.out.print("Buscando orden");
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[2]/div[2]/div/div")).click();
					functions.esperar_time(300);
				}				
			}
			
			driver.switchTo().window(operationWindow);
//ALMACENA EL NOMBRE DEL CADETE QUE HAY EN HURRIER-LOGISTIC			
//			driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[1]/div/button")).click();
//			functions.esperar_time(500);
//			courier_name = driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[3]/table/tbody/tr[3]/td/div/div/div/div[2]/a")).getText();
//			functions.esperar_time(500);
//			System.out.println("El nombre del cadete es: " + courier_name);
						
//ABRE HURRIER OPERATION 			

			
			if(courier_amount.size()<2) {
				System.out.println("Hay un solo cadete");
				courier_state = driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/div/table/tbody/tr[1]/td[3]")).getText();
				System.out.print(courier_state);
				if(courier_state.equals("Queued") || courier_state.equals("Dispatched")) {
					System.out.println("La orden no ha sido aceptada");
					driver.findElement(By.className("edit_deliveries")).click();
					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/div[2]/div/a[1]")).click();
					functions.esperar_time(500);
					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/form/div/div/div/div[2]/select")).sendKeys("W");
					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/form/div/div/div/div[3]/input")).click();
					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[1]/td[1]/div[2]/input")).click();
					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[3]/td[1]/div[2]/input")).click();
					int  uti_value = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[1]/td[10]/div[2]/input")).getAttribute("value"));
					if(uti_value > 5) {
						System.out.println("Utilizacion mayor a 5");
						driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[3]/td[10]/div[2]/input")).clear();
						driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[3]/td[10]/div[2]/input")).sendKeys("5");
					}else {
						System.out.print("Utilizacion menor 5");
						driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[3]/td[10]/div[2]/input")).clear();
						driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[3]/td[10]/div[2]/input")).sendKeys(Integer.toString(uti_value));
					}
					
					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[1]/td[12]/a/i")).click();
					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/div[2]/div/input")).click();
					driver.switchTo().alert().accept();
					functions.esperar_time(1000);
					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/div/table/tbody/tr[3]/td[12]/div[1]/div/a[1]")).click();
					
					driver.findElement(By.xpath("//*[@id=\"edit-dispatcher-notes\"]")).click();
					functions.esperar_time(500);
					driver.findElement(By.id("dispatcher-notes")).sendKeys("Order was not accepted within X minutes // adiciono un nuevo envío // pongo de primario// cambio valor de utilization // processing // cancelo envío anterior // quito pending // Ds");
					driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div/div[2]/span")).click();
					processed_orders++;
					System.out.println("Ordenes procesadas: " + processed_orders);
					functions.esperar_time(1000);
					Date current_time = new Date();
					String checkwadTime = dateFormat.format(current_time);
					String data = "Se procesa orden: " + order_id +" a la hora: "+ checkwadTime+";\n";
					System.out.print(data);
					driver.close();
					 try {				          
				          File f1 = new File("C:\\x_logs\\logs_cou.txt");
				          if(!f1.exists()) {
				             f1.createNewFile();
				          }

				          FileWriter fileWritter = new FileWriter("C:\\x_logs\\logs_cou.txt",true);
				          BufferedWriter bw = new BufferedWriter(fileWritter);
				          bw.write(data);
				          bw.close();
				          System.out.println("Done");
				       } catch(IOException e){
				          e.printStackTrace();
				       }
					
				}else {
					System.out.println("La orden ya fue aceptada");
					driver.findElement(By.xpath("//*[@id=\"edit-dispatcher-notes\"]")).click();
					functions.esperar_time(500);
					driver.findElement(By.id("dispatcher-notes")).sendKeys("Order was not accepted within X minutes // Orden estado Courier Notified // Acciono processing // Ds");
					driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div/div[2]/span")).click();
					processed_orders++;
					System.out.println("Ordenes procesadas: " + processed_orders);
					functions.esperar_time(1000);
					Date current_time = new Date();
					String checkwadTime = dateFormat.format(current_time);
					String data = "Se procesa orden: " + order_id +" a la hora: "+ checkwadTime+";\n";
					System.out.print(data);
					driver.close();
					 try {
				          File f1 = new File("C:\\x_logs\\logs_cou.txt");
				          if(!f1.exists()) {
				             f1.createNewFile();
				          }

				          FileWriter fileWritter = new FileWriter("C:\\x_logs\\logs_cou.txt",true);
				          BufferedWriter bw = new BufferedWriter(fileWritter);
				          bw.write(data);
				          bw.close();
				          System.out.println("Done");
				       } catch(IOException e){
				          e.printStackTrace();
				       }
				}
				
			}else {

				System.out.println("Hay más de un cadete");
				driver.close();
//				String courier1_name = driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/div/table/tbody/tr[1]/td[9]")).getText();
//				String courier2_name = driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/div/table/tbody/tr[3]/td[9]")).getText();
//				
//				String courier_row = "";
//				if(courier1_name.equals(courier_name)) {
//					courier_row = "1";
//				}else {
//					courier_row = "3";
//				}
//				
//				
//				courier_state = driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/div/table/tbody/tr["+courier_row+"]/td[3]")).getText();
//				System.out.print(courier_state);
//				if(courier_state.equals("Queued") || courier_state.equals("Dispatched")) {
//					System.out.println("La orden no ha sido aceptada");
//					driver.findElement(By.className("edit_deliveries")).click();
//					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/div[2]/div/a[1]")).click();
//					functions.esperar_time(500);
//					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/form/div/div/div/div[2]/select")).sendKeys("W");
//					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/form/div/div/div/div[3]/input")).click();
//					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr["+courier_row+"]/td[1]/div[2]/input")).click();
//					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[5]/td[1]/div[2]/input")).click();
//					int  uti_value = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr["+courier_row+"]/td[10]/div[2]/input")).getAttribute("value"));
//					if(uti_value > 5) {
//						System.out.println("Utilizacion mayor a 5");
//						driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[5]/td[10]/div[2]/input")).clear();
//						driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[5]/td[10]/div[2]/input")).sendKeys("5");
//					}else {
//						System.out.print("Utilizacion menor 5");
//						driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[5]/td[10]/div[2]/input")).clear();
//						driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr[5]/td[10]/div[2]/input")).sendKeys(Integer.toString(uti_value));
//					}
//					
//					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/table/tbody/tr["+courier_row+"]/td[12]/a/i")).click();
//					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/form/div/div[2]/div/input")).click();
//					driver.switchTo().alert().accept();
//					functions.esperar_time(1000);
//					driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/div/table/tbody/tr[5]/td[12]/div[1]/div/a[1]")).click();
//					
//					driver.findElement(By.xpath("//*[@id=\"edit-dispatcher-notes\"]")).click();
//					functions.esperar_time(500);
//					driver.findElement(By.id("dispatcher-notes")).sendKeys("Order was not accepted within X minutes // adiciono un nuevo envío // pongo de primario// cambio valor de utilization // processing // cancelo envío anterior // quito pending // Ds");
//					driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div/div[2]/span")).click();
//					processed_orders++;
//					System.out.println("Ordenes procesadas: " + processed_orders);
//					functions.esperar_time(1000);
//					Date current_time = new Date();
//					String checkwadTime = dateFormat.format(current_time);
//					String data = "Se procesa orden: " + order_id +" a la hora: "+ checkwadTime+";\n";
//					System.out.print(data);
//					driver.close();
//					 try {				          
//				          File f1 = new File("C:\\x_logs\\logs_cou.txt");
//				          if(!f1.exists()) {
//				             f1.createNewFile();
//				          }
//
//				          FileWriter fileWritter = new FileWriter("C:\\x_logs\\logs_cou.txt",true);
//				          BufferedWriter bw = new BufferedWriter(fileWritter);
//				          bw.write(data);
//				          bw.close();
//				          System.out.println("Done");
//				       } catch(IOException e){
//				          e.printStackTrace();
//				       }
//					
//				}else {
//					System.out.println("La orden ya fue aceptada");
//					driver.findElement(By.xpath("//*[@id=\"edit-dispatcher-notes\"]")).click();
//					functions.esperar_time(500);
//					driver.findElement(By.id("dispatcher-notes")).sendKeys("Order was not accepted within X minutes // Orden estado Courier Notified // Acciono processing // Ds");
//					driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div/div[2]/span")).click();
//					processed_orders++;
//					System.out.println("Ordenes procesadas: " + processed_orders);
//					functions.esperar_time(1000);
//					Date current_time = new Date();
//					String checkwadTime = dateFormat.format(current_time);
//					String data = "Se procesa orden: " + order_id +" a la hora: "+ checkwadTime+";\n";
//					System.out.print(data);
//					driver.close();
//					 try {
//				          File f1 = new File("C:\\x_logs\\logs_cou.txt");
//				          if(!f1.exists()) {
//				             f1.createNewFile();
//				          }
//
//				          FileWriter fileWritter = new FileWriter("C:\\x_logs\\logs_cou.txt",true);
//				          BufferedWriter bw = new BufferedWriter(fileWritter);
//				          bw.write(data);
//				          bw.close();
//				          System.out.println("Done");
//				       } catch(IOException e){
//				          e.printStackTrace();
//				       }
//				}
			}			
		}
	}
}