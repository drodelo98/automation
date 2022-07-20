package XtendoPruebas;

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
		SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
				
		while(true) {
			String init_check_string = dateFormat.format(new Date());			
			if(init_check_string.contains("12:")||init_check_string.contains("1:")||init_check_string.contains("2:")||init_check_string.contains("3:")) {
				System.out.println("Init at: "+init_check_string);
				break;
			}else {
				System.out.println("Not init time yet: "+init_check_string);
				functions.esperar_time(30000);
			}
		}
		
		int system_state = 0; //SI ES 0 ABRE LAS APLICACIONES SI ES 1 IGNORA
		
		WebDriver driver = functions.chrome_h();
		
		String hurrierWindow="";
		String inconcertWindow="";
		String slackWindow ="";
		String turnosWindow="";
		
		int processed_orders = 0;
		int processed_orders2 = 0;
		
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
			add("ve");
			add("do");
			add("bo");
			add("sv");
			add("cl");
			add("ni");
			add("hn");
			add("gt");
		}};
		
		while(true) {
		
			String rider_phone ="";
			String order_id ="";
			String rider_id ="";
			List<WebElement> chat_texts; //STORE BO MESSAGES
			List<WebElement> ar_chats_times; //STORE RIDER'S AND AGENT'S MESSAGES TIMES IN FRESCHAT
			List<String> timeLastRChats = new ArrayList<String>(); //STORE ONLY RIDER'S MESSAGE TIME IN FRESCHAT

			boolean rider_chat_check = true;			
			int user_chats_count;
			int rider_chats_count;
			int conversacion = 0;
		
			if(system_state==0) {
				inconcertWindow = functions.inconcert_login(driver);
				slackWindow = functions.slack_open(driver);				
				hurrierWindow = functions.hurrier_getWHandle(driver);
				turnosWindow = functions.turnos_login(driver);
				system_state=1;
				functions.freschat_session(driver,3);
			}			
			
			boolean keep_searching=true;
			while(true) {
				processed_orders2 = functions.send_slack_message(dateFormat, processed_orders2, slackWindow, driver);
				String currentTime = dateFormat.format(new Date());
				if(currentTime.contains("4:")){
					functions.turnos_logout(driver, turnosWindow);
					functions.freschat_session(driver,2);
					functions.inconcert_logout(driver, inconcertWindow);
					System.exit(1);
				}
				driver.switchTo().window(hurrierWindow);
				driver.switchTo().window(inconcertWindow);
				driver.switchTo().window(hurrierWindow);
				country_id = country_ids.get(country_iterator);
//				functions.hurrier_OpenOrder(driver, country_id);
				
				while(true) {
					try {
						driver.get("https://"+country_id+".us.logisticsbackoffice.com/dashboard/v2/hurrier/issues");
						break;
					}catch(Exception e) {				
						for(String country : country_ids) {
							try {
								driver.get("https://"+country+".us.logisticsbackoffice.com/dashboard/v2/hurrier/login");
								functions.esperar_time(5000);
								driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[4]/button")).click();
								System.out.println("Se abre"+ country);
								functions.esperar_time(5000);
							}catch(Exception u) {
								System.out.println("Ya está abierto "+ country);
							}
						}
					}
				}
				
				functions.esperar_time(1000);
				driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[1]/div/div/div/div/input")).click();
				driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div[3]/div/div[1]")).click();						
				driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[2]/div[1]/div/div")).click();
				driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[5]")).click();
				driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[5]")).click();
				System.out.println("Organized by 'plazo'");
				
				
				try {
//					keep_searching = functions.hurrier_pickOrder(driver, order_id, country_id,dateFormat);
					System.out.println("Buscando orden en: " + country_id);
					
					String currentTime2 = dateFormat.format(new Date());
					int wait_time = 5;
					if(!currentTime2.contains("12:")) {
						wait_time = 10;
					}
					//-----------------------------------------------------------------------<PICKEA LA ORDEN>------------------------------------------------------------------------------------------------------------------
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait_time));
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[7]/div/button")).click();
					System.out.println("Puede pickear");
					functions.esperar_time(500);
					//-----------------------------------------------------------------------<ALMACENA EL NUMERO DE LA ORDEN>--------------------------------------------------------------------------------------------------
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[6]/div")).click();
					order_id =driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[1]/div/div/a")).getText();
					System.out.println("Orden "+ order_id + " pickeada");
					break;
				}catch(Exception e) {					
					//country_iterator = functions.hurrier_changeCountry(driver,country_iterator);
					driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[2]/div[2]/div/div")).click();
					functions.esperar_time(500);					
					if(country_iterator < (country_ids.size()-1)) {
						country_iterator++;
					}else {
						country_iterator = 0;
					}		
				}
			}		
//ABRE BACK OFFICE	
			driver.switchTo().newWindow(WindowType.TAB);
			String operationWindow = driver.getWindowHandle();			
			chat_texts = functions.bo_getchats(driver,order_id);			
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
						functions.esperar_time(2000);
						driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[2]/div[1]/div/div"));
					}
					
					driver.switchTo().window(operationWindow);
					driver.get("https://"+country_id+".us.logisticsbackoffice.com/dispatcher/order_details/"+ order_id);
					driver.findElement(By.xpath("//*[@id=\"edit-dispatcher-notes\"]")).click();
					functions.esperar_time(1000);
					driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // no chat en freschat, presenta comunicación activa y asertiva con el cliente // doy processing.");
					driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div/div[2]/span")).click();
					processed_orders++;
					processed_orders2++;
					System.out.println("Ordenes procesadas: " + processed_orders);
					functions.esperar_time(1000);
					 
					String checkwadTime = dateFormat.format(new Date());
					System.out.print("Se procesa orden: " + order_id +"a la hora: "+ checkwadTime);
					functions.logs_write(order_id, checkwadTime);

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
				functions.esperar_time(2000);
				
				driver.switchTo().window(operationWindow);
				System.out.println("No hay mensajes en BO.");
				//-----------------------------------------------------------< BUSCA Y ALMACENA CODIGO Y TELEFONO DEL DRIVER
				
				while(true) {
					try {
						driver.get("https://"+country_id+".us.logisticsbackoffice.com/dispatcher/order_details/"+ order_id);
						driver.findElement(By.xpath("/html/body/div[3]/div/div[6]/div/div/table/tbody/tr[1]/td[9]/div/a")).click();
						break;
					}catch(Exception e){
						for(String country : country_ids) {
							try {
								driver.get("https://"+country+".usehurrier.com/users/sign_in");
								driver.findElement(By.xpath("/html/body/div[3]/div/button")).click();
								System.out.println("Se abre"+ country);
								functions.esperar_time(5000);
							}catch(Exception i) {
								System.out.println("Ya está abierto "+ country);
							}
						}
					}
				}


				
				functions.esperar_time(2500);
				
				rider_id = driver.findElement(By.id("infoFormCourierUserId")).getText();
				rider_phone = driver.findElement(By.id("riderPhone")).getText().replaceAll("\\s+","");
				
				functions.esperar_time(1000);
				
				System.out.println("El telefono del rider es: " +rider_phone);
				System.out.println("El id del rider es: " + rider_id);
								
				///ENTRA A FRESCHAT
				driver.get("https://dh-latam.freshchat.com/a/133969877319489/people");

				//CLICK EN BUSQUEDA DE CADETE
				functions.esperar_time(5000);
			    driver.findElement(By.xpath("/html/body/div[8]/div[5]/div[4]/div/div/div[1]/div[2]/div/div[1]/div/div/div[2]/div/ul/li/a/span")).click();
			    
			    //CLICK EN FILTER
				functions.esperar_time(2000);
			    driver.findElement(By.className("segment-filter")).click();
			    functions.esperar_time(2000);
			    
			    driver.findElement(By.className("rc-value")).click();//click en input ID
			    driver.findElement(By.className("ember-power-select-search-input")).sendKeys(rider_id,Keys.ENTER);//Pasa rider_id
			    driver.findElement(By.xpath("/html/body/div[8]/div[6]/div/div/div/div[3]/span/button[2]")).click();//Click en Apply
			    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			    try {
				    driver.findElement(By.xpath("/html/body/div[8]/div[5]/div[4]/div/div/div[2]/div[2]/div[2]/div[1]/div[2]/a")).click();//Entra al chat del rider.    
				     rider_chat_check = true;
			    }catch(Exception e) {
			    	System.out.println("Cadete sin chat");
			    	rider_chat_check = false;
			    } 
			    		    
			    try {
			    	functions.esperar_time(4000);
				    
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
							driver.get("https://"+country_id+".us.logisticsbackoffice.com/dispatcher/order_details/"+ order_id);
							functions.esperar_time(700);
							driver.findElement(By.xpath("//*[@id=\"edit-dispatcher-notes\"]")).click();
							functions.esperar_time(700);
							driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // chat activo en freschat menor a 10min relacionado al ISSUE // doy processing.");
							driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div[1]/div[2]/span")).click();
							processed_orders++;
							processed_orders2++;
							System.out.println("Ordenes procesadas: " + processed_orders);
							functions.esperar_time(1000);
							String checkwadTime = dateFormat.format(new Date());
							System.out.println("Se procesa orden: " + order_id +"a la hora: "+ checkwadTime);
							functions.logs_write(order_id, checkwadTime);
							driver.close();
							driver.switchTo().window(hurrierWindow);							
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
					functions.esperar_time(3000);
				     rider_chat_check = true;
			    }catch(Exception e) {
			    	System.out.println("Cadete sin chat");
			    	rider_chat_check = false;
			    } 
						
				driver.switchTo().window(inconcertWindow);
								
//				functions.inconcert_call(driver, rider_phone);
						
				driver.switchTo().window(operationWindow);

			    if(conversacion ==0) {
			    	try {
				    	functions.esperar_time(2000);
					    
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
								driver.get("https://"+country_id+".us.logisticsbackoffice.com/dispatcher/order_details/"+ order_id);
								functions.esperar_time(700);
								driver.findElement(By.xpath("//*[@id=\"edit-dispatcher-notes\"]")).click();
								functions.esperar_time(700);
								driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // chat activo en freschat menor a 10min relacionado al ISSUE // doy processing.");
								driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div[1]/div[2]/span")).click();
								processed_orders++;
								processed_orders2++;
								System.out.println("Ordenes procesadas: " + processed_orders);
								functions.esperar_time(1000);
								String checkwadTime = dateFormat.format(new Date());
								System.out.println("Se procesa orden: " + order_id +"a la hora: "+ checkwadTime);
								functions.logs_write(order_id, checkwadTime);
								driver.close();
								driver.switchTo().window(hurrierWindow);
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
				    }

			    	if(conversacion == 0) {
						try {
							functions.esperar_time(500);
							driver.findElement(By.xpath("/html/body/div[8]/div[5]/div[4]/div[2]/div[2]/div/div/div[3]/div[1]/div[5]/div/div/button")).click();
						}catch(Exception e){
							System.out.print("No se pudo dar click a enviar");
						}
						
						functions.esperar_time(500);
						
						driver.get("https://"+country_id+".us.logisticsbackoffice.com/dispatcher/order_details/"+order_id);
						driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div[1]/div[2]/span")).click();
						if(rider_chat_check) {
							//driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // no freschat, no chat con cliente. Se llama dos veces sin exito('La conversación fue cancelada'), se envía template // doy processing.");
							driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // no freschat, no chat con cliente. Se intenta llamar sin embargo inconcert me muestra el telfono rojo y no permite llamar, se envía template // doy processing.");
						}else {
							//driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // no freschat, no chat con cliente. Se llama dos veces sin exito('La conversación fue cancelada'), no se envía /WAD ya que no se encuentra chat con ID // doy processing.");
							driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // no freschat, no chat con cliente. Se intenta llamar sin embargo inconcert me muestra el telfono rojo y no permite llamar, no se envía /WAD ya que no se encuentra chat con ID // doy processing.");

						}
						driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[2]/div/div[1]/div[2]/span")).click();
						processed_orders++;
						processed_orders2++;
						System.out.println("Ordenes procesadas: " + processed_orders);
						functions.esperar_time(1000);
						String checkwadTime = dateFormat.format(new Date());
						System.out.println("Se procesa orden: " + order_id +"a la hora: "+ checkwadTime);
						functions.logs_write(order_id, checkwadTime);
						driver.close();
						
						driver.switchTo().window(hurrierWindow);

						conversacion = 1;
			    	}	
			    }
			}
		}		
	}
}



	
