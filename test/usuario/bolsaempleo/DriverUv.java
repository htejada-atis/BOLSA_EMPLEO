package usuario.bolsaempleo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import bbdd.UtilsTestBolsaEmpleo;

/** Clase para obtener el driver de firefox. */
public class DriverUv {
	private static WebDriver driver;
	private static final int TIEMPO_MAXIMO_ESPERA = 10;
	
	private DriverUv() { }
	
	/** inicializa el driver. */
	public static void inicializaDriver() {
		System.setProperty("webdriver.gecko.driver", "Documentos/selenium/drivers/geckodriver");
		driver = new FirefoxDriver();
		driver.manage().timeouts().pageLoadTimeout(TIEMPO_MAXIMO_ESPERA, TimeUnit.SECONDS);
	}
	
	/** realiza el login del usuario. */
	public static void login() {
		driver.get("http://localhost:8080/srv/es/index");
		WebElement username = driver.findElement(By.name("usuario"));
		username.sendKeys("usig");
		username.submit();
	}
	
	public static WebDriver getDriver() {
		return driver;
	}
	
	/** inicializa la bd para realizar las pruebas.
	 * @throws IOException si error en fichero
	 * @throws SQLException si error en bd
	 */
	public static void inicializaBd() throws IOException, SQLException {
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();	
	}
}
