package usuario;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import bbdd.UtilsTestDocentia;

/** Clase para obtener el driver de firefox. */
public class DriverUv {
	private static WebDriver driver;
	private static final int TIEMPO_MAXIMO_ESPERA = 10;
	
	private DriverUv() { }
	
	/** inicializa el driver. */
	public static void inicializaDriver() {
		System.setProperty("webdriver.gecko.driver", "Documentos/selenium/drivers/geckodriver");
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setHeadless(true);
		driver = new FirefoxDriver(firefoxOptions);
		driver.manage().timeouts().pageLoadTimeout(TIEMPO_MAXIMO_ESPERA, TimeUnit.SECONDS);
	}
	
	/** realiza el login del usuario. */
	public static void login() {
		driver.get("http://localhost:8888/srv/es/index");
		capturaPantalla("index");
		WebElement username = driver.findElement(By.name("usuario"));
		username.sendKeys("usig");
		username.submit();
	}
	
	/** realiza una captura de pantalla.
	 * @param nombre nombre del fichero de la captura
	 */
	public static void capturaPantalla(String nombre) {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    String directorio = "/tmp/";
	    try {
			Files.copy(scrFile.toPath(), (new File(directorio + nombre + ".png")).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static WebDriver getDriver() {
		return driver;
	}
	
	/** inicializa la bd para realizar las pruebas.
	 * @throws IOException si error en fichero
	 * @throws SQLException si error en bd
	 */
	public static void inicializaBd() throws IOException, SQLException {
		UtilsTestDocentia.inicializaDocentia();	
	}
}
