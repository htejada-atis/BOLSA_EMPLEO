package usuario;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/** prueba de concepto de test de usuario. */
public class Prueba {
	
	private Prueba() { }

	/** programa principal.
	 * @param args argumentos
	 */
	public static void main(String[] args) {
		WebDriver driver;
		final int timeout = 10;
		System.setProperty("webdriver.gecko.driver", "drivers/geckodriver");
		System.out.println("debug 1");
		driver = new FirefoxDriver();
		driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
		System.out.println("debug 2");
		//fire.manage().window().maximize();

		System.out.println("debug 3");
		driver.get("http://localhost:8080/srv/es/index");
		System.out.println("debug 4");
		WebElement username = driver.findElement(By.name("usuario"));
		username.sendKeys("usig");
		username.submit();
		driver.get("http://localhost:8080/srv/es/informacionadministrativa");
		driver.get("http://localhost:8080/srv/es/informacionadministrativa/docentia");
		driver.get("http://localhost:8080/srv/es/informacionadministrativa/docentia/convocatoriacrud");
		
		WebElement botonEliminar = driver.findElement(By.
				cssSelector("html body#micrositeA div#wrapper div#content div#mainContent div.convocatorias div form table.bluetable tr td input"));
		botonEliminar.click();
		//assertTrue(driver.findElement(By.className("error")).getText().contains("ORA-02292"));
		
		driver.findElement(By.cssSelector("#nombreCampoFormulario")).sendKeys("prueba2");
	}

}
