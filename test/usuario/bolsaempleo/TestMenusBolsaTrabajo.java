package usuario.bolsaempleo;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import usuario.bolsaempleo.DriverUv;

/** Clase para probar solicitudCrud.
 * @author usig
 *
 */
public class TestMenusBolsaTrabajo {
	private static final String NOMBREDEESTACLASE = TestMenusBolsaTrabajo.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
		
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
	}
	
	/** Comprueba correcta navegación
	 */
	@Test
	public void testA1() {				
		assertTrue(false);
	}

	/** Cierre de este unittest.
	 */
	@AfterClass
	public static void cerrar() {
		LOGGER.log(Level.INFO, "final");
		DriverUv.getDriver().quit();
	}
}
