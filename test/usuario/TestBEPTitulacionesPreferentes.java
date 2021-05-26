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
		UtilsTestBEP.assertTitlePage(UtilsTestBEP.waitVisibility(By.className(DIV_MAIN)), "Titulaciones preferentes por área");
		seleccionarArea(2);
		
		// seleccionamos la primera fila
		WebElement tableTitulaciones = UtilsTestBEP.waitVisibility(By.id(ID_TABLE_TITULACIONES_AREA));
		UtilsTestBEP.assertTotalTable(tableTitulaciones, 5);
		UtilsTestBEP.selectRowTable(tableTitulaciones, 0);
		UtilsTestBEP.assertTotalSelectedTable(tableTitulaciones, 1);
		
		// leemos la titulación de la primera fila
		String titulacion = UtilsTestBEP.getTextCellTable(tableTitulaciones, 0, 1);
		assertNotNull(titulacion);
		
		// eliminamos
		WebElement btn = UtilsTestBEP.getActionTableByText(tableTitulaciones, "Eliminar");
		btn.click();		
		tableTitulaciones = UtilsTestBEP.waitVisibility(By.id(ID_TABLE_TITULACIONES_AREA));
		UtilsTestBEP.assertTotalTable(tableTitulaciones, 4);
		
		// restauramos titulacion
		WebElement tableDisponibles = UtilsTestBEP.waitVisibility(By.id(ID_TABLE_TITULACIONES_DISPONIBLES));
		WebElement input = UtilsTestBEP.getFilterInputByIndex(tableDisponibles, 1);
		input.sendKeys(titulacion);
		input.sendKeys(Keys.ENTER);
		UtilsTestBEP.assertTotalTable(tableDisponibles, 1);
		UtilsTestBEP.selectRowTable(tableDisponibles, 0);
		btn = UtilsTestBEP.getActionTableByText(tableDisponibles, "Incluir");
		btn.click();
		
		tableTitulaciones = UtilsTestBEP.waitVisibility(By.id(ID_TABLE_TITULACIONES_AREA));
		UtilsTestBEP.assertTotalTable(tableTitulaciones, 5);
	}

	private void seleccionarArea(int index) {
		Select sel = new Select(UtilsTestBEP.waitVisibility(By.id("select_area")));
		sel.selectByIndex(index);
		sel = new Select(UtilsTestBEP.waitVisibility(By.id("select_area")));
		String textoSelect = sel.getFirstSelectedOption().getText();
		
		WebElement tableTitulaciones = UtilsTestBEP.waitVisibility(By.id(ID_TABLE_TITULACIONES_AREA));
		UtilsTestBEP.assertTitleTable(tableTitulaciones, "Titulaciones Preferentes al Área: " + textoSelect);
		
		WebElement tableDisponibles = UtilsTestBEP.waitVisibility(By.id(ID_TABLE_TITULACIONES_DISPONIBLES));
		UtilsTestBEP.assertTitleTable(tableDisponibles, "Titulaciones Disponibles");
		assertTrue(UtilsTestBEP.getTotalTable(tableDisponibles) > 0);
	}
}
