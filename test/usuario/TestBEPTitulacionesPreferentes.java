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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import bbdd.UtilsTestBolsaEmpleo;

/**
 * Clase para probar titulaciones preferentes.
 * 
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPTitulacionesPreferentes extends UtilsTestUsuarioBase {
	private static final String NOMBREDEESTACLASE = TestBEPTitulacionesPreferentes.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	private static final String DIV_MAIN = "titulacionespreferentes";
	private static final String ID_TABLE_TITULACIONES_AREA = "table_titulaciones_preferentes_area";
	private static final String ID_TABLE_TITULACIONES_DISPONIBLES = "table_titulaciones";
	private static final String AREA_NAME = "DERECHO CIVIL";
	private static final int TITULACIONES_AREA = 2;

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
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/bolsaempleo/configuracion");
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/bolsaempleo/configuracion/titulacionespreferentesarea");
	}

	/**
	 * Seleccion de area y titulaciones.
	 */
	@Test
	public void testA1() {
		UtilsTestBolsaEmpleo.assertTitlePage(UtilsTestBolsaEmpleo.waitVisibility(By.className(DIV_MAIN)), "Titulaciones preferentes por área");
		seleccionarArea(AREA_NAME);
		
		// seleccionamos la primera fila
		WebElement tableTitulaciones = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_TITULACIONES_AREA));
		UtilsTestBolsaEmpleo.assertTotalTable(tableTitulaciones, TITULACIONES_AREA);
		UtilsTestBolsaEmpleo.selectRowTable(tableTitulaciones, 0);
		UtilsTestBolsaEmpleo.assertTotalSelectedTable(tableTitulaciones, 1);
		
		// leemos la titulación de la primera fila
		String titulacion = UtilsTestBolsaEmpleo.getTextCellTable(tableTitulaciones, 0, 1);
		assertNotNull(titulacion);
		
		// eliminamos
		WebElement btn = UtilsTestBolsaEmpleo.getActionTableByText(tableTitulaciones, "Eliminar");
		btn.click();		
		tableTitulaciones = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_TITULACIONES_AREA));
		UtilsTestBolsaEmpleo.assertTotalTable(tableTitulaciones, TITULACIONES_AREA - 1);
		
		// restauramos titulacion
		WebElement tableDisponibles = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_TITULACIONES_DISPONIBLES));
		WebElement input = UtilsTestBolsaEmpleo.getFilterInputByIndex(tableDisponibles, 1);
		input.sendKeys(titulacion);
		input.sendKeys(Keys.ENTER);
		UtilsTestBolsaEmpleo.assertTotalTable(tableDisponibles, 1);
		UtilsTestBolsaEmpleo.selectRowTable(tableDisponibles, 0);
		btn = UtilsTestBolsaEmpleo.getActionTableByText(tableDisponibles, "Incluir");
		btn.click();
		
		tableTitulaciones = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_TITULACIONES_AREA));
		UtilsTestBolsaEmpleo.assertTotalTable(tableTitulaciones, TITULACIONES_AREA);
	}

	private void seleccionarArea(String name) {
		Select sel = new Select(UtilsTestBolsaEmpleo.waitVisibility(By.id("select_area")));
		sel.selectByVisibleText(name);
		sel = new Select(UtilsTestBolsaEmpleo.waitVisibility(By.id("select_area")));
		String textoSelect = sel.getFirstSelectedOption().getText();
		
		WebElement tableTitulaciones = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_TITULACIONES_AREA));
		UtilsTestBolsaEmpleo.assertTitleTable(tableTitulaciones, "TITULACIONES PREFERENTES AL ÁREA: " + textoSelect);
		
		WebElement tableDisponibles = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_TITULACIONES_DISPONIBLES));
		UtilsTestBolsaEmpleo.assertTitleTable(tableDisponibles, "TITULACIONES DISPONIBLES");
		assertTrue(UtilsTestBolsaEmpleo.getTotalTable(tableDisponibles) > 0);
	}
}
