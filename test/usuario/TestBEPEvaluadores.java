package usuario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;

/**
 * Clase para probar configuracion.evaluadores BEP .
 * 
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPEvaluadores extends UtilsTestUsuarioBase {
	private static final String NOMBREDEESTACLASE = TestBEPAreasBaremar.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	private static final String DIV_MAIN = "evaluadores-listar";
	private static final String ID_TABLE_AREAS = "tableAreas";	
	private static final String ID_TABLE_EVALUADORES = "tableEvaluadoresArea";
	private static final String DEP1 = "CIENCIAS DE LA SALUD";
	private static final String DEP2 = "ECONOMIA";
	
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
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/bolsaempleo/configuracion/evaluadores");
	}

	/**
	 * Agregar evaluador a todas las areas del departamento.
	 */
	@Test
	public void testA1() {
		// comprobamos titulo de la página
		UtilsTestBolsaEmpleo.assertTitlePage(UtilsTestBolsaEmpleo.waitVisibility(By.className(DIV_MAIN)), "Evaluadores de un departamento");

		// seleccionamos el departamento (refresh)
		WebElement tableAreas = seleccionarDepartamento(DEP1);
				
		// añadimos evaluador (refresh)
		WebElement input = UtilsTestBolsaEmpleo.waitVisibility(By.id("nombre_evaluador"));
		input.sendKeys("comision1");
		WebElement btn = UtilsTestBolsaEmpleo.waitClickable(By.id("nuevo_evaluador"));
		btn.click();
		tableAreas = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_AREAS));
		
		// mensaje 			
		List<String> mensajes = UtilsTestBolsaEmpleo.getMensajesDeExito();
		assertEquals(UtilsTestBolsaEmpleo.getTotalTable(tableAreas), mensajes.size());
		
		// seleccionamos el primer item de la tabla
		tableAreas = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_AREAS));
		WebElement trArea = UtilsTestBolsaEmpleo.getRowByIndex(tableAreas, 0);
		WebElement tdId = UtilsTestBolsaEmpleo.getColumnByIndex(trArea, 1);
		tdId.click();
		
		// comprobamos tabla de evaluadores del area
		WebElement tableEvaluadores = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_EVALUADORES));
		UtilsTestBolsaEmpleo.assertTotalTable(tableEvaluadores, 1);
	}
	
	/**
	 * Agregar evaluador a ciertas areas del departamento.
	 */
	@Test
	public void testA2() {
		// seleccionamos el departamento (refresh)
		WebElement tableAreas = seleccionarDepartamento(DEP2);
		
		UtilsTestBolsaEmpleo.assertTotalSelectedTable(tableAreas, UtilsTestBolsaEmpleo.getTotalTable(tableAreas));
		
		// añadimos evaluador (refresh)
		WebElement input = UtilsTestBolsaEmpleo.waitVisibility(By.id("nombre_evaluador"));
		input.sendKeys("comision1");
		WebElement btn = UtilsTestBolsaEmpleo.waitClickable(By.id("nuevo_evaluador"));
		btn.click();
		
		// mensaje 
		List<String> mExito = UtilsTestBolsaEmpleo.getMensajesDeExito();
		assertEquals(3, mExito.size());
		
		// seleccionamos el primer item de la tabla
		tableAreas = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_AREAS));
		WebElement trArea = UtilsTestBolsaEmpleo.getRowByIndex(tableAreas, 0);
		WebElement tdId = UtilsTestBolsaEmpleo.getColumnByIndex(trArea, 1);
		tdId.click();
		WebElement tableEvaluadores = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_EVALUADORES));
		UtilsTestBolsaEmpleo.assertTotalTable(tableEvaluadores, 1);
		assertEquals(UtilsTestBolsaEmpleo.getTextCellTable(tableEvaluadores, 0, 0), "36393760D");
		
		// seleccionamos el segundo item de la tabla
		tableAreas = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_AREAS));
		trArea = UtilsTestBolsaEmpleo.getRowByIndex(tableAreas, 0);
		tdId = UtilsTestBolsaEmpleo.getColumnByIndex(trArea, 2);
		tdId.click();
		tableEvaluadores = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_EVALUADORES));
		UtilsTestBolsaEmpleo.assertTotalTable(tableEvaluadores, 1);
		assertEquals(UtilsTestBolsaEmpleo.getTextCellTable(tableEvaluadores, 0, 0), "36393760D");
	}
	
	/**
	 * Borrar evaluador.
	 */
	@Test
	public void testA3() {
		// seleccionamos el departamento (refresh)
		WebElement tableAreas = seleccionarDepartamento(DEP1);
		
		// seleccionamos la primera area
		WebElement trArea = UtilsTestBolsaEmpleo.getRowByIndex(tableAreas, 0);
		WebElement tdId = UtilsTestBolsaEmpleo.getColumnByIndex(trArea, 1);
		tdId.click();	
		
		// borramos el primer item
		WebElement tableEvaluadores = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_EVALUADORES));
		UtilsTestBolsaEmpleo.assertTotalTable(tableEvaluadores, 1);
		assertEquals(UtilsTestBolsaEmpleo.getTextCellTable(tableEvaluadores, 0, 0), "36393760D");
		WebElement trEvaluador = UtilsTestBolsaEmpleo.getRowByIndex(tableEvaluadores, 0);
		WebElement btnBorrar = trEvaluador.findElement(By.className("btn-borrar")); 
		btnBorrar.click();
		
		// dialogo
		WebElement dialog = UtilsTestBolsaEmpleo.getDialog();
		UtilsTestBolsaEmpleo.assertTitleDialgo(dialog, "Borrar evaluador");
		WebElement btnSi = UtilsTestBolsaEmpleo.getButtonDialog(dialog, "Si");
		btnSi.click();
		
		tableEvaluadores = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_EVALUADORES));
		assertEquals(UtilsTestBolsaEmpleo.getTextCellTable(tableEvaluadores, 0, 0), "Sin resultados");
	}
	
	/**
	 * Restaurar evaluador.
	 */
	@Test
	public void testA4() {
		// seleccionamos el departamento (refresh)
		WebElement tableAreas = seleccionarDepartamento(DEP1);
		
		// seleccionamos la primera area
		WebElement trArea = UtilsTestBolsaEmpleo.getRowByIndex(tableAreas, 0);
		WebElement tdId = UtilsTestBolsaEmpleo.getColumnByIndex(trArea, 1);
		tdId.click();	
		
		// seleccionamos el filtro
		WebElement tableEvaluadores = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_EVALUADORES));
		Select filtro = UtilsTestBolsaEmpleo.getFilterSelectByIndex(tableEvaluadores, 2);
		filtro.selectByValue("false");
		
		// restauramos
		tableEvaluadores = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_EVALUADORES));
		UtilsTestBolsaEmpleo.assertTotalTable(tableEvaluadores, 1);
		assertEquals(UtilsTestBolsaEmpleo.getTextCellTable(tableEvaluadores, 0, 0), "36393760D");
		WebElement trEvaluador = UtilsTestBolsaEmpleo.getRowByIndex(tableEvaluadores, 0);
		WebElement btn = trEvaluador.findElement(By.className("btn-restaurar")); 
		btn.click();
		
		// dialogo
		WebElement dialog = UtilsTestBolsaEmpleo.getDialog();
		UtilsTestBolsaEmpleo.assertTitleDialgo(dialog, "Restaurar evaluador");
		WebElement btnSi = UtilsTestBolsaEmpleo.getButtonDialog(dialog, "Si");
		btnSi.click();
		
		tableEvaluadores = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_EVALUADORES));
		UtilsTestBolsaEmpleo.assertTotalTable(tableEvaluadores, 1);
		assertEquals(UtilsTestBolsaEmpleo.getTextCellTable(tableEvaluadores, 0, 0), "36393760D");
	}
	
	/**
	 * Error no existe el usuario.
	 */
	@Test
	public void testE1() {
		// seleccionamos el departamento (refresh)
		seleccionarDepartamento(DEP1);
		
		// añadimos evaluador (refresh)
		WebElement input = UtilsTestBolsaEmpleo.waitVisibility(By.id("nombre_evaluador"));
		input.sendKeys("pepe");
		WebElement btn = UtilsTestBolsaEmpleo.waitClickable(By.id("nuevo_evaluador"));
		btn.click();
		
		List<String> mensajes = UtilsTestBolsaEmpleo.getMensajesDeError();
		assertEquals(1, mensajes.size());
		assertEquals(mensajes.get(0), ModeloUsuarioBolsaEmpleo.MENSAJE_BUSCAR_USUARIO_NO_EXISTE);		
	}
	
	/**
	 * Error usuario no evaluador.
	 */
	@Test
	public void testE2() {
		// seleccionamos el departamento (refresh)
		seleccionarDepartamento(DEP1);
		
		// añadimos evaluador (refresh)
		WebElement input = UtilsTestBolsaEmpleo.waitVisibility(By.id("nombre_evaluador"));
		input.sendKeys("personal1");
		WebElement btn = UtilsTestBolsaEmpleo.waitClickable(By.id("nuevo_evaluador"));
		btn.click();
		
		List<String> mensajes = UtilsTestBolsaEmpleo.getMensajesDeError();
		assertEquals(1, mensajes.size());
		assertEquals(mensajes.get(0), "El usuario no tiene rol de comisión");		
	}
	
	private WebElement seleccionarDepartamento(String value) {
		Select dep = new Select(UtilsTestBolsaEmpleo.waitVisibility(By.id("select_departamento")));
		// dep.selectByValue(value);	
		dep.selectByVisibleText(value);
		dep = new Select(UtilsTestBolsaEmpleo.waitVisibility(By.id("select_departamento")));
		String textoSelect = dep.getFirstSelectedOption().getText();
		
		WebElement tableAreas = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE_AREAS));
		UtilsTestBolsaEmpleo.assertTitleTable(tableAreas, "ÁREAS DEL DEPARTAMENTO: " + textoSelect);
		int totalAreas = UtilsTestBolsaEmpleo.getTotalTable(tableAreas); 
		assertTrue(totalAreas > 0);
		
		return tableAreas;
	}
}
