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
import org.openqa.selenium.support.ui.Select;

import bbdd.UtilsTestBolsaEmpleo;

/**
 * Clase para probar solicitudCrud.
 * 
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPBolsasBaremar extends UtilsTestUsuarioBase {
	private static final String NOMBREDEESTACLASE = TestBEPBolsasBaremar.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	private static final String DIV_MAIN = "bolsa-empleo";	
	private static final String ID_TABLE = "table";
	private static final int FILTER_BAREMABLE_INDEX = 7;
	
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
		UtilsTestBolsaEmpleo.assertTitlePage(UtilsTestBolsaEmpleo.waitVisibility(By.className(DIV_MAIN)), "Estado de las bolsas");
		WebElement table = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE));
		Select filtro = UtilsTestBolsaEmpleo.getFilterSelectByIndex(table, FILTER_BAREMABLE_INDEX);
		filtro.selectByValue("0");
		table = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE));
		
		assertTrue(UtilsTestBolsaEmpleo.getTotalTable(table) > 0);		
	}
}
