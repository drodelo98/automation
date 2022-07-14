package com.qualitystream.tutorial;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class GoogleSearchTests {
	private WebDriver driver;
	
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe");
		ChromeOptions opt = new ChromeOptions();
		opt.setExperimentalOption("debuggerAddress","localhost:9898");
		driver = new ChromeDriver(opt);
		
	}
	
	
	@Test
	public void dispacher_notes() {
		driver.get("https://ar.us.logisticsbackoffice.com/dispatcher/order_details/585961635");
		driver.findElement(By.xpath("//*[@id=\"edit-dispatcher-notes\"]")).click();
		driver.findElement(By.id("dispatcher-notes")).sendKeys("Waiting at Dropoff // no chat en freschat, presenta comunicación activa y asertiva con el cliente // doy processing.");
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		driver.get("https://www.rs.servicesjw.com/index");
		driver.get("https://ar.us.logisticsbackoffice.com/dashboard/v2/hurrier/issues");
		driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[1]/div/div/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div[3]/div/div[1]")).click();
		driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[5]")).click();
		driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/thead/tr/th[5]")).click();
		driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/div[3]/table/tbody/tr[2]/td[1]/div/div/a")).click();
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}
}




/*WebElement searchbox = driver.findElement(By.name("q"));
searchbox.clear();
searchbox.sendKeys("Como hago esto en un navegador existente");
searchbox.submit();
driver.manage().window().maximize();

assertEquals("Como hago esto en un navegador existente",driver.getTitle());*/