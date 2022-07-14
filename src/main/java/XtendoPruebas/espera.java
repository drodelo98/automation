package XtendoPruebas;

public class espera {
	public static void esperar_time(int time) {
		try {
		    Thread.sleep(time);
		} catch (InterruptedException ie) {
		    Thread.currentThread().interrupt();
		}
	}

	public static void main(String[] args) {

	}


}
