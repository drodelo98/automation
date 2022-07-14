package XtendoPruebas;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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


public class XtendoTest {

	public static void main(String[] args) {
		
		int system_state = 0;
		WebDriver driver = chrome_handle.chrome_h();
		String hurrierWindow="";
		String inconcertWindow="";
		
		int processed_orders=0;
	
		while(true) {
			String rider_phone = "";
			String order_id="";
			String rider_id;
			List<WebElement> chat_texts;
			List<WebElement> ar_chats; //almacena los chas del rider con agente
			List<WebElement> ar_chats_times;
			List<String> timeLastRChats = new ArrayList(); //store rider message time
			SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
			boolean rider_chat_check = true;			
			int user_chats_count;
			int rider_chats_count;
			int conversacion = 0;
		
			
			
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
				
				//----------------------------------------------------------------------<ABRE TURNOS, LO DEJA ABIERTO>----------------------------------------------------------------------------------------------------
				driver.switchTo().newWindow(WindowType.TAB);
				driver.get("https://www.rs.servicesjw.com/index");
				//----------------------------------------------------------------------<ABRE HURRIER>---------------------------------------------------------------------------------------------------------------------
				driver.switchTo().newWindow(WindowType.TAB);
				hurrierWindow = driver.getWindowHandle();
				
				driver.get("https://ar.us.logisticsbackoffice.com/dashboard/v2/hurrier/issues");
				driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[1]/div/div/div/div/input")).click();
				driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div[3]/div/div[1]")).click();
				system_state=1;
			}

			
			//----------------------------------------------------------------------<FILTRA POR DROPOFF Y ORDENA POR PLAZO>--------------------------------------------------------------------------------------------

			while(true) {
				driver.switchTo().window(hurrierWindow);
				driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[2]/div[1]/div/div")).click();
				driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[5]")).click();
				driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[5]")).click();
				
				try {
					//-----------------------------------------------------------------------<PICKEA LA ORDEN>------------------------------------------------------------------------------------------------------------------
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[7]/div/button")).click();
					espera.esperar_time(2000);
					//-----------------------------------------------------------------------<ALMACENA EL NUMERO DE LA ORDEN>--------------------------------------------------------------------------------------------------
					new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[1]/div/div/a"))).getText();
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[6]/div")).click();
					order_id =driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[1]/div/div/a")).getText();
					System.out.println("Orden "+ order_id + " pickeada");
					
					break;
				}catch(Exception e) {
					System.out.println("Buscando orden");
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[2]/div[2]/div/div")).click();
					espera.esperar_time(2000);
				}	
			}		

			
			//-----------------------------------------------------------------------<ABRE BACKOFFICE DE LA ORDEN>---------------------------------------------------------------------------------------------------
			driver.switchTo().newWindow(WindowType.TAB);
			String operationWindow = driver.getWindowHandle();
			driver.get("https://backoffice-app.pedidosya.com/#/orders/"+ order_id );
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);

			//-----------------------------------------------------------------------<ABRE EL CHAT DE BO>--------------------------------------------------------------------------------------------------------------
			driver.findElement(By.xpath("//*[@id=\"top\"]/div[1]/div/div[1]/div/div[3]/div[2]/div[1]/div/div[2]/button")).click();
			
			//----------------------------------------------------------------------<ALMACENA MENSAJES DE LA ORDEN EN UN LISTADO DE WebElements>---------------------------------------------------
			chat_texts = driver.findElements(By.className("p-l-10"));
			
			//----------------------------------------------------------------------<VERIFICA SI HAY ALMENOS UN MENSAJE EN LA ORDEN.>-------------------------------------------------------------------------------------
			if(chat_texts.size() > 0) {
				user_chats_count = 0;
				rider_chats_count = 0;
				System.out.print("Hay mensajes");
				for(WebElement e: chat_texts) {
					if(e.getText().equals("Rider:") || e.getText().equals("Rider :")) {
						rider_chats_count++;
					}else if(e.getText().equals("User:") || e.getText().equals("User :")){
						user_chats_count++;
					}
				}
			//----------------------------------------------------------------------<CODIGO SÍ SÍ HAY CONVERSACIÓN>-------------------------------------------------------------------------------------	
				if(user_chats_count > 0 && rider_chats_count > 0) {
					System.out.print(" y hay conversción");
					driver.switchTo().window(hurrierWindow);
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[2]/div[3]/div/div")).click(); //Click en Mis issues
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[6]/div")).click();//Ordena por pickeado
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[6]/div")).click();//Ordena por pickeados
					try{
						driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[7]/div/button[2]")).click();//Procesa
					}catch(Exception e) {
						System.out.println("Ya se procesó tomar otra");
						espera.esperar_time(2000);
						driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[2]/div[1]/div/div"));
					}
					
					driver.switchTo().window(operationWindow);
					driver.get("https://ar.us.logisticsbackoffice.com/dispatcher/order_details/"+ order_id);
					driver.findElement(By.xpath("//*[@id=\"edit-dispatcher-notes\"]")).click();
					espera.esperar_time(1000);
					driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // no chat en freschat, presenta comunicación activa y asertiva con el cliente // doy processing.");
					driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div/div[2]/span")).click();
					processed_orders++;
					System.out.println("Ordenes procesadas: " + processed_orders);
					espera.esperar_time(1000);
					Date current_time = new Date();
					String checkwadTime = dateFormat.format(current_time);
					System.out.print("Se procesa orden: " + order_id +"a la hora: "+ checkwadTime);
					 try {
				          String data = "Se procesa orden: " + order_id +" a la hora: "+ checkwadTime+";\n";
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

					driver.close();
					driver.switchTo().window(hurrierWindow);
					conversacion = 1;
			//-----------------------------------------------------------------------<FIN DEL CODIGO SI SÍ HAY CONVERSACION>----------------------------------------------------------------------------------------		
				}else {
					System.out.println(" pero no hay conversacion");
				}
			}
			//------------------------------------------------------------------------<SE EJECUTA SI NO HAY CONVERSACION>--------------------------------------------------------------------------------------
			if (conversacion == 0){
				driver.switchTo().window(hurrierWindow);
				driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[2]/div[3]/div/div")).click(); //Click en Mis issues
				try{
					driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[7]/div/button[2]")).click();//Procesa
				}catch(Exception e) {
					System.out.print("Ya se procesó tomar otra");
					conversacion = 1;
				}
				
				espera.esperar_time(2000);
				
				
				driver.switchTo().window(operationWindow);
				System.out.println("No hay mensajes en BO.");
				//-----------------------------------------------------------< BUSCA Y ALMACENA CODIGO Y TELEFONO DEL DRIVER
				driver.get("https://ar.us.logisticsbackoffice.com/dispatcher/order_details/"+ order_id);
				driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/div/table/tbody/tr[1]/td[9]/div/a")).click();
				
				espera.esperar_time(2500);
				
				rider_id = driver.findElement(By.id("infoFormCourierUserId")).getText();
				rider_phone = driver.findElement(By.id("riderPhone")).getText().replaceAll("\\s+","");
				
				espera.esperar_time(1000);
				
				System.out.println("El telefono del rider es: " +rider_phone);
				System.out.println("El id del rider es: " + rider_id);
				
				
				///ENTRA A FRESCHAT
				driver.get("https://dh-latam.freshchat.com/a/133969877319489/people");

				//CLICK EN BUSQUEDA DE CADETE
				espera.esperar_time(5000);
			    driver.findElement(By.xpath("/html/body/div[8]/div[5]/div[4]/div/div/div[1]/div[2]/div/div[1]/div/div/div[2]/div/ul/li/a/span")).click();
			    
			    //CLICK EN FILTER
				espera.esperar_time(2000);
			    driver.findElement(By.className("segment-filter")).click();
			    espera.esperar_time(2000);
			    
			    driver.findElement(By.className("rc-value")).click();//click en input ID
			    driver.findElement(By.className("ember-power-select-search-input")).sendKeys(rider_id,Keys.ENTER);//Pasa rider_id
			    driver.findElement(By.xpath("/html/body/div[8]/div[6]/div/div/div/div[3]/span/button[2]")).click();//Click en Apply
			    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
			    try {
				    driver.findElement(By.xpath("/html/body/div[8]/div[5]/div[4]/div/div/div[2]/div[2]/div[2]/div[1]/div[2]/a")).click();//Entra al chat del rider.    
				     rider_chat_check = true;
			    }catch(Exception e) {
			    	System.out.println("Cadete sin chat");
			    	rider_chat_check = false;
			    } 
			    
			    
			    try {
			    	espera.esperar_time(2000);
				    
				    ar_chats = driver.findElements(By.className("insta-user-message"));
				    ar_chats_times = driver.findElements(By.className("time"));

				    for(WebElement e : ar_chats_times) {
				    	String c_time= e.getText();
				    	if(!c_time.startsWith(" ")) {
				    		timeLastRChats.add(c_time);
				    	}
				    
				    }
				    String lastChatTime = timeLastRChats.get(timeLastRChats.size()-1);

					SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");	
					Calendar calendario = Calendar.getInstance();
					long timeInSecs = calendario.getTimeInMillis();
					String checkTime;
					
					for(int i = 0; i <= 11; i++) {
						Date afterAdding10Mins = new Date(timeInSecs - (i * 60 * 1000));
						checkTime = formatter.format(afterAdding10Mins);
						if(lastChatTime.equals(checkTime)) {
							System.out.println("Si hay chat reciente en : " + checkTime);
							driver.get("https://ar.us.logisticsbackoffice.com/dispatcher/order_details/"+ order_id);
							espera.esperar_time(700);
							driver.findElement(By.xpath("//*[@id=\"edit-dispatcher-notes\"]")).click();
							espera.esperar_time(700);
							driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // chat activo en freschat menor a 10min relacionado al ISSUE // doy processing.");
							driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div[1]/div[2]/span")).click();
							processed_orders++;
							System.out.println("Ordenes procesadas: " + processed_orders);
							espera.esperar_time(1000);
							Date current_time = new Date();
							String checkwadTime = dateFormat.format(current_time);
							String log_text = "Se procesa orden: " + order_id +"a la hora: "+ checkwadTime;
							driver.close();
							driver.switchTo().window(hurrierWindow);
							 try {
						          String data = "Se procesa orden: " + order_id +" a la hora: "+ checkwadTime+";\n";
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
							conversacion = 1;
							break;
						}else {
							System.out.println("No hay chat reciente en : " + checkTime);
						}
					}
			    	
			    }catch(Exception e) {
			    	e.getStackTrace()[0].getLineNumber();
			    	System.out.println(e.getStackTrace());
			    	System.out.println("No hay ningún mensaje en freschat");
			    	conversacion = 0;
			    	//System.exit(1);
			    }

			}
	//---------------------------------------------------------------------------------------------------<entra si no hay chat en freschat---------------------------------------------------------------->

			
			
			if (conversacion == 0) {
			    try {
			    	driver.findElement(By.xpath("/html/body/div[8]/div[5]/div[4]/div[2]/div[2]/div/div/div[3]/div[1]/div[1]")).click(); //Click en Mis issues
					driver.findElement(By.xpath("/html/body/div[8]/div[5]/div[4]/div[2]/div[2]/div/div/div[3]/div[1]/div[1]/div")).sendKeys("{{user.firstName}}, intentamos contactarte, pero no obtuvimos respuesta. Verificamos en el sistema que retiraste un pedido, pero no se ha podido concretar la entrega.\r\n"
							+ "\r\n"
							+ "Si tienes algún problema para comunicarte con el cliente, puedes escribirle mediante el chat de la App.\r\n"
							+ "\r\n"
							+ "En caso de que presentes algún inconveniente, puedes ponerte en contacto con nosotros para ayudarte a resolverlo, de lo contrario, no es necesario que respondas a este mensaje. ¡Muchas gracias!\r\n"
							+ "");
					

					
					espera.esperar_time(3000);
				     rider_chat_check = true;
			    }catch(Exception e) {
			    	System.out.println("Cadete sin chat");
			    	rider_chat_check = false;
			    } 
						
				driver.switchTo().window(inconcertWindow);
								
//				boolean segunda_llamada = false;
//				for(int i=0;i<2;) {
//					if(!segunda_llamada) {
//						driver.findElement(By.className("butQuickCall")).click();
//						espera.esperar_time(5000);
//						segunda_llamada = true;
//					}
//
//					if(driver.findElements(By.tagName("iframe")).size()>0) {
//						driver.switchTo().frame(0);
//						espera.esperar_time(1500);
//						
//						try {
//							WebElement cuadro_llamadas = driver.findElement(By.id("digitsDisplay"));
//							cuadro_llamadas.click();
//							cuadro_llamadas.sendKeys(rider_phone, Keys.ENTER);
//							i++;
//						}catch(Exception e) {
//							System.out.print("No se pudo llamar");
//							driver.switchTo().defaultContent();
//							System.out.println("Se ejecuta el cambio al parent frame");
//							if(driver.findElements(By.tagName("iframe")).size()==0) {
//								espera.esperar_time(1000);
//								driver.findElement(By.className("butQuickCall")).click();
//							}
//						}
//					}
//				}	
//				
//				while(true) {
//					if(driver.findElements(By.tagName("iframe")).size()==0) {
//						break;
//					}
//				}
						
				driver.switchTo().window(operationWindow);
				try {
					espera.esperar_time(1000);
					driver.findElement(By.xpath("/html/body/div[8]/div[5]/div[4]/div[2]/div[2]/div/div/div[3]/div[1]/div[5]/div/div/button")).click();
				}catch(Exception e){
					System.out.print("No se pudo dar click a enviar");
				}
				
				espera.esperar_time(500);
				
				driver.get("https://ar.us.logisticsbackoffice.com/dispatcher/order_details/"+order_id);
				driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div[1]/div[2]/span")).click();
				if(rider_chat_check) {
					driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // no freschat, no chat con cliente. Se llama dos veces sin exito('La conversación fue cancelada'), se envía template // doy processing.");
					//driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // no freschat, no chat con cliente. Se intenta llamar sin embargo inconcert me muestra el telfono rojo y no permite llamar, se envía template // doy processing.");
				}else {
					driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // no freschat, no chat con cliente. Se llama dos veces sin exito('La conversación fue cancelada'), no se envía /WAD ya que no se encuentra chat con ID // doy processing.");
					//driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // no freschat, no chat con cliente. Se intenta llamar sin embargo inconcert me muestra el telfono rojo y no permite llamar, se envía template // doy processing.");

				}
				driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div[1]/div[2]/span")).click();
				processed_orders++;
				System.out.println("Ordenes procesadas: " + processed_orders);
				espera.esperar_time(1000);
				Date current_time = new Date();
				String checkwadTime = dateFormat.format(current_time);
				System.out.println("Se procesa orden: " + order_id +"a la hora: "+ checkwadTime);
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
				driver.close();
				
				driver.switchTo().window(hurrierWindow);

				conversacion = 1;
			}
		}
		
	}
}



	
