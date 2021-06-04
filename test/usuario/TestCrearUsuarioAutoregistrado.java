package usuario;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import bbdd.UtilsTestAutoregistrado;
import es.ujaen.uvirtual.utilidades.UVException;

/** Clase para probar solicitudCrud.
 * @author usig
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCrearUsuarioAutoregistrado extends UtilsTestUsuarioBase {
	private static final String NOMBREDEESTACLASE = TestCrearUsuarioAutoregistrado.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	private static final Integer WAITTIME = 5; 
	
	private static final String TEXTO = "texto prueba";
	private static final String CORREO = "juanmoralcardenas@gmail.com";
	/** Se ejecuta una vez al inicio de la clase.
	 * @throws SQLException Si se produce error en bbdd
	 * @throws IOException Si no se puede cargar los ficheros
	 * @throws UVException si error uv
	 */
	@BeforeClass
	public static void setUp() throws IOException, SQLException {
		LOGGER.log(Level.INFO, "inicio");
		UtilsTestAutoregistrado.inicializaDb();	
		DriverUv.inicializaDriver();
	}

	/** Se ejecuta antes de cada test.
	 * Aqui navegamos hasta la opcion que vamos a probar
	 */
	@Before
	public void navegaOpcion() {
		DriverUv.getDriver().get(DriverUv.RUTA + "/pub/es/operaciones/autoregistrado/usuarioautoresgistrado");
	}
	
	/** Inserta una convocatoria.
	 */
	@Test
	public void testA01() {
		DriverUv.getDriver().get(DriverUv.RUTA + "/pub/es/operaciones/autoregistrado/usuarioautoresgistrado?a=mostrarcrear");
		DriverUv.getDriver().findElement(By.name("documento")).sendKeys(TEXTO);
		DriverUv.getDriver().findElement(By.name("nombre")).sendKeys(TEXTO);
		DriverUv.getDriver().findElement(By.name("apellido1")).sendKeys(TEXTO);
		DriverUv.getDriver().findElement(By.name("apellido2")).sendKeys(TEXTO);
		DriverUv.getDriver().findElement(By.name("email")).sendKeys(CORREO);
		DriverUv.getDriver().findElement(By.name("emailRepetido")).sendKeys(CORREO);
		DriverUv.getDriver().findElement(By.name("emailRepetido")).submit();

		WebDriverWait wait = new WebDriverWait(DriverUv.getDriver(), WAITTIME);
		WebElement codigoTemporal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("codigoTemporal")));
		assertNotNull(codigoTemporal);
		assertTrue(codigoTemporal.isDisplayed());
	}
	
	/** Cierre de este unittest.
	 */
	@AfterClass
	public static void cerrar() {
		LOGGER.log(Level.INFO, "final");
		DriverUv.getDriver().quit();
	}
}
