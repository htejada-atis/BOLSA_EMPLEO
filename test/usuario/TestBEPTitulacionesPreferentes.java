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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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
	private static final Integer WAIT_ELEMENT = 5; // segundos

	private static final String DIV_MAIN_TITULACIONES = "titulacionespreferentes";
	private static final String ID_TABLE = "table_titulaciones_preferentes_area";
	private static final String ID_TABLE_DISPONIBLES = "table_titulaciones";
	private static final String CLASS_PAGINATION = "pagination";
	private static final String CLASS_PAGINATION_LAST = "last";
	private static final String CLASS_PAGINATION_FIRST = "first";
	private static final String CLASS_ACTIONS = "actions";

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
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/bolsaempleo/configuracion/titulacionespreferentesarea");
	}

	/**
	 * Listado de usuarios.
	 */
	@Test
	public void testA1() {
		WebDriverWait wait = new WebDriverWait(DriverUv.getDriver(), WAIT_ELEMENT);

		// esperamos div principal
		WebElement main = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DIV_MAIN_TITULACIONES)));
		WebElement h2 = main.findElement(By.tagName("h2"));
		assertTrue(h2.getText().equals("Titulaciones preferentes por área"));

		// seleccionamos en select
		Select area = new Select(DriverUv.getDriver().findElement(By.id("select_area")));
		area.selectByIndex(1);

		// esperamos a que se renderice la table
		WebElement total = wait.until(
				ExpectedConditions.presenceOfNestedElementLocatedBy(getTables(main, ID_TABLE), By.className("total")));
		WebElement totalDisponibles = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(getTables(main, ID_TABLE_DISPONIBLES), By.className("total")));

		comprobarNumUsu(total);
		comprobarNumUsu(totalDisponibles);

		comprobarOrdenacion(getTables(main, ID_TABLE));
		comprobarOrdenacion(getTables(main, ID_TABLE_DISPONIBLES));

		comprobarPaginacion(getTables(main, ID_TABLE));
		comprobarPaginacion(getTables(main, ID_TABLE_DISPONIBLES));
	}

	/**
	 * Probamos a crear un nuevo usuario .
	 * 
	 * @throws MalformedURLException .
	 * @throws IOException           .
	 */
	@Test
	public void testA3() {
		// seleccionamos en select
		Select area = new Select(DriverUv.getDriver().findElement(By.id("select_area")));
		area.selectByIndex(1);

		WebDriverWait wait = new WebDriverWait(DriverUv.getDriver(), WAIT_ELEMENT);
		WebElement pmain = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DIV_MAIN_TITULACIONES)));
		WebElement table = getTables(pmain, ID_TABLE);
		WebElement th = table.findElement(By.cssSelector("input[type='checkbox']"));
		th.click();
		WebElement actions = table.findElement(By.className(CLASS_ACTIONS));
		WebElement btnNoBaremable = actions.findElement(By.xpath("button[1]"));
		btnNoBaremable.click();

		WebDriverWait wait2 = new WebDriverWait(DriverUv.getDriver(), WAIT_ELEMENT);
		WebElement pmain2 = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.className(DIV_MAIN_TITULACIONES)));
		WebElement table2 = getTables(pmain2, ID_TABLE_DISPONIBLES);
		WebElement th2 = table2.findElement(By.cssSelector("input[type='checkbox']"));
		th2.click();
		WebElement actions2 = table2.findElement(By.className(CLASS_ACTIONS));
		WebElement btnNoBaremable2 = actions2.findElement(By.xpath("button[1]"));
		btnNoBaremable2.click();
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
	 * GetTables.
	 * 
	 * @param main  .
	 * @param tabla .
	 * @return Web.
	 */
	public WebElement getTables(WebElement main, String tabla) {
		return main.findElement(By.id(tabla));
	}

	/**
	 * Comprueba que el numero de usuarios es mayor a 0.
	 * 
	 * @param total .
	 */
	public void comprobarNumUsu(WebElement total) {
		Integer numElementos = Integer.parseInt(total.getText().split(" ")[1]);
		assertTrue(numElementos > 0);
	}

	/**
	 * click sobre ordenación y comprobamos que existe icono.
	 * 
	 * @param tabla .
	 */
	public void comprobarOrdenacion(WebElement tabla) {
		WebElement th = tabla.findElement(By.className("nombre"));
		th.click();
		WebElement img = th.findElement(By.className("order"));
		assertTrue(img.getAttribute("src").indexOf("down.png") != -1);
		th.click();
		img = th.findElement(By.className("order"));
		assertTrue(img.getAttribute("src").indexOf("up.png") != -1);
	}

	/**
	 * comprobacion de la paginación.
	 * 
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
}
