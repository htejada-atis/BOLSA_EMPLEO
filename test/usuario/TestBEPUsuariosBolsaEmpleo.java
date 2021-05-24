package usuario;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
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

/** Clase para probar configuracion.usuarios BEP .
 * @author fcampos
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPUsuariosBolsaEmpleo extends UtilsTestUsuarioBase {
	private static final String NOMBREDEESTACLASE = TestBEPUsuariosBolsaEmpleo.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	private static final Integer WAIT_ELEMENT = 5; // segundos
	
	private static final String DIV_MAIN_USUARIOS = "usuarios";
	private static final String DIV_MAIN_USUARIOS_BUSQUEDA = "usuarios-buscar";
	private static final String DIV_MAIN_USUARIOS_FORM = "usuarios-form";
	private static final String ID_TABLE = "table_usuarios";
	private static final String CLASS_PAGINATION = "pagination";
	private static final String CLASS_PAGINATION_LAST = "last";
	private static final String CLASS_PAGINATION_FIRST = "first";
	private static final String CLASS_ACTIONS = "actions";
	
	private static final String CLASS_DIALOGO = "ui-dialog";
	private static final String CLASS_DIALOGO_CONTENT = "ui-dialog-content";
		
	private static final String MENSAJE_DIALOGO_SELECCIONAR_FILAS = "Seleccione al menos un usuario.";
		
	/** Se ejecuta una vez al inicio de la clase.
	 * @throws SQLException Si se produce error en bbdd
	 * @throws IOException Si no se puede cargar los ficheros
	 */
	@BeforeClass
	public static void setUp() throws IOException, SQLException {
		LOGGER.log(Level.INFO, "inicio");
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
		DriverUv.inicializaDriver();
		DriverUv.login("personal1");
	}

	/** Se ejecuta antes de cada test.
	 * Aqui navegamos hasta la opcion que vamos a probar
	 */
	@Before
	public void navegaOpcion() {
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/index");
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa");
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/bolsaempleo");		
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/bolsaempleo/configuracion");
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/bolsaempleo/configuracion/usuarios");
	}
	
	/** Listado de usuarios.
	 */
	@Test
	public void testA1() {
		WebDriverWait wait = new WebDriverWait(DriverUv.getDriver(), WAIT_ELEMENT);

		// esperamos div principal
		WebElement main = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DIV_MAIN_USUARIOS)));		
		WebElement h2 = main.findElement(By.tagName("h2"));
		assertTrue(h2.getText().equals("Usuarios del sistema"));
		
		// esperamos a que se renderice la table
		WebElement total = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(getTables(main, ID_TABLE), By.className("total")));
		
		comprobarNumUsu(total);
		
		comprobarOrdenacion(getTables(main, ID_TABLE));

		comprobarPaginacion(getTables(main, ID_TABLE));
		
		comprobarSeleccionTabla(getTables(main, ID_TABLE));
	}
	
	/**
	 * Probamos a crear un nuevo usuario .
	 * @throws MalformedURLException .
	 * @throws IOException .
	 */
	@Test
	public void testA3() {
		WebDriverWait wait = new WebDriverWait(DriverUv.getDriver(), WAIT_ELEMENT);
		WebElement pmain = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DIV_MAIN_USUARIOS)));	
		WebElement btnNuevo = pmain.findElement(By.id("nuevo_usuario"));
		btnNuevo.click();
		
		WebElement pmain2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DIV_MAIN_USUARIOS_BUSQUEDA)));	

		WebElement username = pmain2.findElement(By.name("nombre"));
		username.sendKeys("usig");
		WebElement btnBuscar = pmain2.findElement(By.id("usuario_buscar"));
		btnBuscar.click();
		
		WebElement pmain3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DIV_MAIN_USUARIOS_FORM)));
		WebElement btnVolver = pmain3.findElement(By.id("usuario_enviar"));
		btnVolver.click();
	}
	
	private String getDialogText() {
		WebElement dialogo = DriverUv.getDriver().findElement(By.className(CLASS_DIALOGO));
		WebElement content = dialogo.findElement(By.className(CLASS_DIALOGO_CONTENT));
		
		return content.findElement(By.tagName("h6")).getText();
	}

	/** Cierre de este unittest.
	 */
	@AfterClass
	public static void cerrar() {
		LOGGER.log(Level.INFO, "final");
		DriverUv.getDriver().quit();
	}
	
	/** GetTables.
	 * @param main .
	 * @param tabla .
	 * @return Web.
	 */
	public WebElement getTables(WebElement main, String tabla) {
		return main.findElement(By.id(tabla));
	}
	
	
	
	/** Comprueba que el numero de usuarios es mayor a 0.
	 * @param total .
	 */
	public void comprobarNumUsu(WebElement total) {
		Integer numElementos = Integer.parseInt(total.getText().split(" ")[1]);
		assertTrue(numElementos > 0);
	}
	
	/** click sobre ordenación y comprobamos que existe icono.
	 * @param tabla .
	 */
	public void comprobarOrdenacion(WebElement tabla) {
		WebElement th = tabla.findElement(By.className("user"));
		th.click();
		WebElement img = th.findElement(By.className("order"));
		assertTrue(img.getAttribute("src").indexOf("down.png") != -1);
		th.click();
		img = th.findElement(By.className("order"));
		assertTrue(img.getAttribute("src").indexOf("up.png") != -1);
	}
	
	
	
	/** comprobacion de la paginación.
	 * @param tabla .
	 */
	public void comprobarPaginacion(WebElement tabla) {
		WebElement pagination = tabla.findElement(By.className(CLASS_PAGINATION));
		Integer totalPages = Integer.parseInt(pagination.getText().split("/")[1].trim().split(" ")[0].trim());
		assertTrue(totalPages > 0);
		
		WebElement btnLast = pagination.findElement(By.className(CLASS_PAGINATION_LAST));
		btnLast.click();
		pagination = tabla.findElement(By.className(CLASS_PAGINATION));
		Integer firstPage = Integer.parseInt(pagination.getText().split("/")[0].trim().split(" ")[2].trim());
		assertTrue(firstPage == totalPages);
		
		WebElement btnFirst = pagination.findElement(By.className(CLASS_PAGINATION_FIRST));
		btnFirst.click();
		pagination = tabla.findElement(By.className(CLASS_PAGINATION));
		Integer firstPageAgain = Integer.parseInt(pagination.getText().split("/")[0].trim().split(" ")[2].trim());
		assertTrue(firstPageAgain == 1);
	}
	
	
	/** alert si no hay filas seleccionadas.
	 * @param tabla .
	 */
	public void comprobarSeleccionTabla(WebElement tabla) {
		WebElement actions = tabla.findElement(By.className(CLASS_ACTIONS));
		WebElement btnBorrar = actions.findElement(By.xpath("button[1]"));
		btnBorrar.click();
		String alert = this.getDialogText();
		assertTrue(alert.equals(MENSAJE_DIALOGO_SELECCIONAR_FILAS));
	}
	
}
