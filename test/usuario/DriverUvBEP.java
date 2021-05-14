package usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import bbdd.UtilsTestBolsaEmpleo;

/** Clase para obtener el driver de firefox. */
public class DriverUvBEP {
	private static WebDriver driver;
	private static final int TIEMPO_MAXIMO_ESPERA = 10;
	private static final int RESPONSE_CODE_200 = 200;
	private static final int RESPONSE_CODE_201 = 201;
	
	private DriverUvBEP() { }
	
	/** inicializa el driver. */
	public static void inicializaDriver() {
		System.setProperty("webdriver.gecko.driver", "Documentos/selenium/drivers/geckodriver");
		
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("devtools.jsonview.enabled", false);
		
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);
		capabilities.setCapability(FirefoxDriver.PROFILE, profile);
		
		FirefoxOptions options = new FirefoxOptions();
		options.merge(capabilities);
				
		driver = new FirefoxDriver(options);
		driver.manage().timeouts().pageLoadTimeout(TIEMPO_MAXIMO_ESPERA, TimeUnit.SECONDS);		
	}
	
	/** realiza el login del usuario. */
	public static void login() {
		driver.get(DriverUv.RUTA + "/srv/es/index");
		WebElement username = driver.findElement(By.name("usuario"));
		username.sendKeys("personal1");
		username.submit();
	}
	
	/** realiza una petición ajax que devuelve un json.
	 * @param url . 
	 * @return deuelve un string con json leido
	 */
	@java.lang.SuppressWarnings("java:S2093") 
	public static String getAjaxRequestJson(String url) throws IOException {
	    Cookie cookie = driver.manage().getCookieNamed("JSESSIONID");
	    URL u = new URL(url);
	    HttpURLConnection c = null;
	    
		try {
			c = (HttpURLConnection) u.openConnection();

			c.setRequestMethod("GET");
			c.setRequestProperty("Content-length", "0");
			c.setRequestProperty("Cookie", cookie.getName() + "=" + cookie.getValue());
			c.setUseCaches(false);
			c.setAllowUserInteraction(false);
			c.connect();

			int status = c.getResponseCode();

			if (status == RESPONSE_CODE_200 || status == RESPONSE_CODE_201) {
				BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				return sb.toString();
			}
		} finally {
			if (c != null) {
				c.disconnect();
			}
		}
	    
	    return null;
	}
	
	public static WebDriver getDriver() {
		return driver;
	}
	
	/** inicializa la bd para realizar las pruebas.
	 * @throws SQLException si error en bd
	 */
	public static void inicializaBd() throws IOException, SQLException {
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();	
	}
}
