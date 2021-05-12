package usuario;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/** Clase para obtener el driver de firefox. */
public class DriverUv {
	private static WebDriver driver;
	private static final int TIEMPO_MAXIMO_ESPERA = 10;
	private static int contador = 1;
	
	public static final String RUTA = "http://localhost:8888";
	
	private DriverUv() { }
	
	private static String getTipoSistema() {
		return System.getProperty("os.name").toLowerCase();
	}

	/** inicializa el driver sin opciones.
	 */
	public static void inicializaDriver() {
		inicializaDriver(null);
	}
	
	/** inicializa el driver. 
	 * @param options opciones del navegador
	 * */
	public static void inicializaDriver(FirefoxOptions options) {
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		if (options != null) {
			firefoxOptions = options;
		}
		if (getTipoSistema().contains("win")) {
			System.setProperty("webdriver.gecko.driver", "Documentos/selenium/drivers/geckodriver.exe");
		} else {
			System.setProperty("webdriver.gecko.driver", "Documentos/selenium/drivers/geckodriver");
			firefoxOptions.setHeadless(true);
		}
		driver = new FirefoxDriver(firefoxOptions);
		driver.manage().timeouts().pageLoadTimeout(TIEMPO_MAXIMO_ESPERA, TimeUnit.SECONDS);
	}
	
	/** realiza el login del usuario. 
	 * @param userLogin usuario para logarse
	 */
	public static void login(String userLogin) {
		driver.get(RUTA + "/srv/es/index");
		capturaPantalla("index");
		WebElement username = driver.findElement(By.name("usuario"));
		username.sendKeys(userLogin);
		username.submit();
	}
	
	/** hace loguot del usuario.
	 */
	public static void logoutUser() {
		driver.get(RUTA + "/salir");
	}
	
	/** realiza una captura de pantalla.
	 * @param nombre nombre del fichero de la captura
	 */
	public static void capturaPantalla(String nombre) {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    String directorio = "/tmp/";
		if (getTipoSistema().contains("win")) {
			directorio = System.getProperty("java.io.tmpdir");
		}
	    try {
	    	String nombreFichero = directorio + contador++ + "-" + nombre + ".png";
			Files.copy(scrFile.toPath(), (new File(nombreFichero)).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** obtiene el driver.
	 * @return driver
	 */
	public static WebDriver getDriver() {
		capturaPantalla("");
		return driver;
	}
}
