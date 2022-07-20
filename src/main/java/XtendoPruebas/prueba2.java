package XtendoPruebas;

import org.openqa.selenium.By;

public class prueba2 {
	

	public static void main(String[] args) {
		String currentTime = "12:30 AM";
		int wait_time = 5;
		if(currentTime.contains("12")) {
			wait_time = 20;
			System.out.print("Sirve");
		}
}
}
