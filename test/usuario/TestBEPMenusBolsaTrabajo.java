package usuario;

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

import bbdd.UtilsTestBolsaEmpleo;

/**
 * Clase para probar solicitudCrud.
 * 
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPMenusBolsaTrabajo extends UtilsTestUsuarioBase {
	private static final String NOMBREDEESTACLASE = TestBEPMenusBolsaTrabajo.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	private static final String DIV_MAIN = "bolsa-empleo";	
	private static final String ID_TABLE = "table";
	
	private static final String CLASS_PAGINATION = "pagination";
	private static final String CLASS_PAGINATION_NEXT = "next";
	private static final String CLASS_PAGINATION_LAST = "last";
	private static final String CLASS_PAGINATION_BACK = "back";
	private static final String CLASS_PAGINATION_FIRST = "first";
	private static final String CLASS_ACTIONS = "actions";

	private static final String CLASS_DIALOGO = "ui-dialog";
	private static final String CLASS_DIALOGO_CONTENT = "ui-dialog-content";

	private static final String MENSAJE_DIALOGO_SELECCIONAR_FILAS = "Seleccione al menos una bolsa para cambiar su estado.";

	/**
	 * Se ejecuta una vez al inicio de la clase.
	 * 
	 * @throws SQLException Si se produce error en bbdd
	 * @throws IOException  Si no se puede cargar los ficheros
	 */
	@BeforeClass
	public static void setUp() throws IOException, SQLException {
		LOGGER.log(Level.INFO, "inicio");
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
		DriverUv.inicializaDriver();
		DriverUv.login("personal1");
	}
	
	/**
	 * Cierre de este unittest.
	 */
	@AfterClass
	public static void cerrar() {
		LOGGER.log(Level.INFO, "final");
		DriverUv.getDriver().quit();
	}

	/**
	 * Se ejecuta antes de cada test. Aqui navegamos hasta la opcion que vamos a
	 * probar
	 */
	@Before
	public void navegaOpcion() {
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/index");
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa");
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/bolsaempleo");
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/bolsaempleo/bolsas");
	}

	/**
	 * Listado de bolsas de empleo.
	 */
	@Test
	public void testA1() {
		UtilsTestBEP.assertTitlePage(UtilsTestBEP.waitVisibility(By.className(DIV_MAIN)), "Estado de las bolsas");
		WebElement table = UtilsTestBEP.waitVisibility(By.id(ID_TABLE));
		assertTrue(UtilsTestBEP.getTotalTable(table) > 0);		
	}
}
