package usuario.bolsaempleo;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Clase para probar solicitudCrud.
 * @author usig
 *
 */
public class TestMenusBolsaTrabajo {
	private static final String NOMBREDEESTACLASE = TestMenusBolsaTrabajo.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	private static final String DIV_MAIN_BOLSAS = "bolsas";
	private static final String ID_TABLE = "table";
	private static final Integer WAIT_AJAX = 10; // segundos
		
	/** Se ejecuta una vez al inicio de la clase.
	 * @throws SQLException Si se produce error en bbdd
	 * @throws IOException Si no se puede cargar los ficheros
	 */
	@BeforeClass
	public static void setUp() throws IOException, SQLException {
		LOGGER.log(Level.INFO, "inicio");
		DriverUv.inicializaBd();
		DriverUv.inicializaDriver();
		DriverUv.login();
	}

	/** Se ejecuta antes de cada test.
	 * Aqui navegamos hasta la opcion que vamos a probar
	 */
	@Before
	public void navegaOpcion() {
		DriverUv.getDriver().get("http://localhost:8080/srv/es/index");
		DriverUv.getDriver().get("http://localhost:8080/srv/es/informacionadministrativa");
		DriverUv.getDriver().get("http://localhost:8080/srv/es/informacionadministrativa/bolsaempleo");		
		DriverUv.getDriver().get("http://localhost:8080/srv/es/informacionadministrativa/bolsaempleo/bolsas");
	}
	
	/** Listado de bolsas de empleo.
	 */
	@Test
	public void testA1() {
		WebElement div = DriverUv.getDriver().findElement(By.className(DIV_MAIN_BOLSAS));
		WebElement h2 = div.findElement(By.tagName("h2"));
		assertTrue(h2.getText().equals("Estado de las bolsas"));
		
		// esperamos a que se renderice la table
		WebElement table = div.findElement(By.id(ID_TABLE));		
		WebDriverWait wait = new WebDriverWait(DriverUv.getDriver(), WAIT_AJAX);
		WebElement total = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(table, By.className("total")));
		
		// comprobamos que el número de bolsas es mayor que cero
		Integer numElementos = Integer.parseInt(total.getText().split(" ")[1]);
		assertTrue(numElementos > 0);
	}

	/** Cierre de este unittest.
	 */
	@AfterClass
	public static void cerrar() {
		LOGGER.log(Level.INFO, "final");
		DriverUv.getDriver().quit();
	}
}
