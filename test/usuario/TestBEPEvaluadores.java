package usuario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * Clase para probar configuracion.evaluadores BEP .
 * 
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPEvaluadores extends UtilsTestBEP {
	private static final String NOMBREDEESTACLASE = TestBEPAreasBaremar.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	private static final String DIV_MAIN_EVALUADORES = "evaluadores-listar";
	private static final String ID_TABLE_AREAS = "tableAreas";	
	private static final String ID_TABLE_EVALUADORES = "tableEvaluadoresArea";

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
		assertTitlePage(waitVisibility(By.className(DIV_MAIN_EVALUADORES)), "Evaluadores de un departamento");

		// seleccionamos el departamento (refresh)
		WebElement tableAreas = seleccionarDepartamento(1);
				
		// añadimos evaluador (refresh)
		WebElement input = waitVisibility(By.id("nombre_evaluador"));
		input.sendKeys("comision1");
		WebElement btn = waitClickable(By.id("nuevo_evaluador"));
		btn.click();
		tableAreas = waitVisibility(By.id(ID_TABLE_AREAS));
		
		// mensaje 			
		List<String> mensajes = getMensajesDeExito();
		assertEquals(getTotalTable(tableAreas), mensajes.size());
		
		// seleccionamos el primer item de la tabla
		tableAreas = waitVisibility(By.id(ID_TABLE_AREAS));
		WebElement trArea = getRowByIndex(tableAreas, 0);
		WebElement tdId = getColumnByIndex(trArea, 1);
		tdId.click();
		
		// comprobamos tabla de evaluadores del area
		WebElement tableEvaluadores = waitVisibility(By.id(ID_TABLE_EVALUADORES));
		assertTotalTable(tableEvaluadores, 1);
	}
	
	/**
	 * Agregar evaluador a ciertas areas del departamento.
	 */
	@Test
	public void testA2() {
		// seleccionamos el departamento (refresh)
		WebElement tableAreas = seleccionarDepartamento(2);
		
		// seleccionamos las dos primeras areas
		selectRowTable(tableAreas, 0);
		selectRowTable(tableAreas, 1);
		assertTotalSelectedTable(tableAreas, 2);
		
		// añadimos evaluador (refresh)
		WebElement input = waitVisibility(By.id("nombre_evaluador"));
		input.sendKeys("comision1");
		WebElement btn = waitClickable(By.id("nuevo_evaluador"));
		btn.click();
		
		// mensaje 
		List<String> mExito = getMensajesDeExito();
		List<String> mError = getMensajesDeError();
		assertEquals(1, mExito.size());
		assertEquals(1, mError.size());
		
		// seleccionamos el primer item de la tabla
		tableAreas = waitVisibility(By.id(ID_TABLE_AREAS));
		WebElement trArea = getRowByIndex(tableAreas, 0);
		WebElement tdId = getColumnByIndex(trArea, 1);
		tdId.click();
		WebElement tableEvaluadores = waitVisibility(By.id(ID_TABLE_EVALUADORES));
		assertTotalTable(tableEvaluadores, 1);
		assertEquals(getTextCellTable(tableEvaluadores, 0, 0), "36393760D");
		
		// seleccionamos el segundo item de la tabla
		tableAreas = waitVisibility(By.id(ID_TABLE_AREAS));
		trArea = getRowByIndex(tableAreas, 0);
		tdId = getColumnByIndex(trArea, 2);
		tdId.click();
		tableEvaluadores = waitVisibility(By.id(ID_TABLE_EVALUADORES));
		assertTotalTable(tableEvaluadores, 1);
		assertEquals(getTextCellTable(tableEvaluadores, 0, 0), "36393760D");
	}
	
	/**
	 * Borrar evaluador.
	 */
	@Test
	public void testA3() {
		// seleccionamos el departamento (refresh)
		WebElement tableAreas = seleccionarDepartamento(1);
		
		// seleccionamos la primera area
		WebElement trArea = getRowByIndex(tableAreas, 0);
		WebElement tdId = getColumnByIndex(trArea, 1);
		tdId.click();	
		
		// borramos el primer item
		WebElement tableEvaluadores = waitVisibility(By.id(ID_TABLE_EVALUADORES));
		assertTotalTable(tableEvaluadores, 1);
		assertEquals(getTextCellTable(tableEvaluadores, 0, 0), "36393760D");
		WebElement trEvaluador = getRowByIndex(tableEvaluadores, 0);
		WebElement btnBorrar = trEvaluador.findElement(By.className("btn-borrar")); 
		btnBorrar.click();
		
		// dialogo
		WebElement dialog = getDialog();
		assertTitleDialgo(dialog, "Borrar evaluador");
		WebElement btnSi = getButtonDialog(dialog, "Si");
		btnSi.click();
		
		tableEvaluadores = waitVisibility(By.id(ID_TABLE_EVALUADORES));
		assertEquals(getTextCellTable(tableEvaluadores, 0, 0), "Sin resultados");
	}
	
	/**
	 * Restaurar evaluador.
	 */
	@Test
	public void testA4() {
		// seleccionamos el departamento (refresh)
		WebElement tableAreas = seleccionarDepartamento(1);
		
		// seleccionamos la primera area
		WebElement trArea = getRowByIndex(tableAreas, 0);
		WebElement tdId = getColumnByIndex(trArea, 1);
		tdId.click();	
		
		// seleccionamos el filtro
		WebElement tableEvaluadores = waitVisibility(By.id(ID_TABLE_EVALUADORES));
		Select filtro = getFilterSelectByIndex(tableEvaluadores, 2);
		filtro.selectByValue("false");
		
		// restauramos
		tableEvaluadores = waitVisibility(By.id(ID_TABLE_EVALUADORES));
		assertTotalTable(tableEvaluadores, 1);
		assertEquals(getTextCellTable(tableEvaluadores, 0, 0), "36393760D");
		WebElement trEvaluador = getRowByIndex(tableEvaluadores, 0);
		WebElement btn = trEvaluador.findElement(By.className("btn-restaurar")); 
		btn.click();
		
		// dialogo
		WebElement dialog = getDialog();
		assertTitleDialgo(dialog, "Restaurar evaluador");
		WebElement btnSi = getButtonDialog(dialog, "Si");
		btnSi.click();
		
		tableEvaluadores = waitVisibility(By.id(ID_TABLE_EVALUADORES));
		assertTotalTable(tableEvaluadores, 1);
		assertEquals(getTextCellTable(tableEvaluadores, 0, 0), "36393760D");
	}
	
	/**
	 * Error no existe el usuario.
	 */
	@Test
	public void testE1() {
		// seleccionamos el departamento (refresh)
		seleccionarDepartamento(1);
		
		// añadimos evaluador (refresh)
		WebElement input = waitVisibility(By.id("nombre_evaluador"));
		input.sendKeys("pepe");
		WebElement btn = waitClickable(By.id("nuevo_evaluador"));
		btn.click();
		
		List<String> mensajes = getMensajesDeError();
		assertEquals(1, mensajes.size());
		assertEquals(mensajes.get(0), "El usuario no existe en el sistema");		
	}
	
	/**
	 * Error usuario no evaluador.
	 */
	@Test
	public void testE2() {
		// seleccionamos el departamento (refresh)
		seleccionarDepartamento(1);
		
		// añadimos evaluador (refresh)
		WebElement input = waitVisibility(By.id("nombre_evaluador"));
		input.sendKeys("personal1");
		WebElement btn = waitClickable(By.id("nuevo_evaluador"));
		btn.click();
		
		List<String> mensajes = getMensajesDeError();
		assertEquals(1, mensajes.size());
		assertEquals(mensajes.get(0), "El usuario no tiene rol de comisión");		
	}
	
	private WebElement seleccionarDepartamento(int index) {
		Select dep = new Select(waitVisibility(By.id("select_departamento")));
		dep.selectByIndex(index);
		dep = new Select(waitVisibility(By.id("select_departamento")));
		String textoSelect = dep.getFirstSelectedOption().getText();
		
		WebElement tableAreas = waitVisibility(By.id(ID_TABLE_AREAS));
		assertTitleTable(tableAreas, "Áreas del Departamento: " + textoSelect);
		int totalAreas = getTotalTable(tableAreas); 
		assertTrue(totalAreas > 0);
		
		return tableAreas;
	}
}
