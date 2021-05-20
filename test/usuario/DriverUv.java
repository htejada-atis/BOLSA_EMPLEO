package usuario;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/** Clase para obtener el driver de firefox. */
public class DriverUv {
	private static final String NOMBREDEESTACLASE = DriverUv.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	private static WebDriver driver;
	private static final int TIEMPO_MAXIMO_ESPERA = 10;
	private static int contador = 1;
	private static String directorioBase;
	
	private static boolean inicializado = false;
	
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
		if (!inicializado) {
		    directorioBase = "/tmp/";
			if (getTipoSistema().contains("win")) {
				directorioBase = System.getProperty("java.io.tmpdir");
			}
			directorioBase += "pruebasSelenium/";
			try (Stream<File> ficherosBorrar = Files.walk(Paths.get(directorioBase)).sorted(Comparator.reverseOrder()).map(Path::toFile)) {
				ficherosBorrar.forEach(File::delete);
			} catch (NoSuchFileException e) {
				//ya borrado
			} catch (IOException e) {
				e.printStackTrace();
			}
			inicializado = true;
		}
	}
	
	/** realiza el login del usuario. 
	 * @param userLogin usuario para logarse
	 */
	public static void login(String userLogin) {
		driver.get(RUTA + "/srv/es/index");
		WebElement username = driver.findElement(By.name("usuario"));
		username.sendKeys(userLogin);
		capturaPantalla("login");
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
		String rutaUrl = "";
		if (driver != null && driver.getCurrentUrl() != null) {
			rutaUrl = driver.getCurrentUrl().replace(RUTA, "");
		}
		rutaUrl = rutaUrl.replace("/", "-");
    	String nombreFichero = directorioBase + contador++ + "-" + nombre + rutaUrl + ".png";
    	if (driver != null) {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		    try {
				Files.createDirectories(Paths.get(directorioBase));
				Files.copy(scrFile.toPath(), (new File(nombreFichero)).toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	} else {
    		LOGGER.log(Level.SEVERE, "hay que inicializar driver para hacer captura");
    	}
	}
	
	/** obtiene el driver.
	 * @return driver
	 */
	public static WebDriver getDriver() {
		return driver;
	}
}
