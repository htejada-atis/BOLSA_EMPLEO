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
public class TestBEPMenusBolsaTrabajo extends UtilsTestBEP {
	private static final String NOMBREDEESTACLASE = TestBEPMenusBolsaTrabajo.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	private static final String DIV_MAIN_BOLSAS = "bolsa-empleo";
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
		WebDriverWait wait = new WebDriverWait(DriverUv.getDriver(), WAIT_ELEMENT);

		// esperamos div principal
		WebElement main = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DIV_MAIN_BOLSAS)));
		WebElement h2 = main.findElement(By.tagName("h2"));
		assertTrue(h2.getText().equals("Estado de las bolsas"));

		// esperamos a que se renderice la table
		WebElement table = main.findElement(By.id(ID_TABLE));
		WebElement total = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(table, By.className("total")));

		// comprobamos que el número de bolsas es mayor que cero
		Integer numElementos = Integer.parseInt(total.getText().split(" ")[1]);
		assertTrue(numElementos > 0);

		// click sobre ordenación y comprobamos que existe icono
		WebElement th = table.findElement(By.className("area"));
		th.click();
		WebElement img = th.findElement(By.className("order"));
		assertTrue(img.getAttribute("src").indexOf("up.png") != -1);
		th.click();
		img = th.findElement(By.className("order"));
		assertTrue(img.getAttribute("src").indexOf("down.png") != -1);

		// paginacion
		WebElement pagination = table.findElement(By.className(CLASS_PAGINATION));
		Integer totalPages = Integer.parseInt(pagination.getText().split("/")[1].trim().split(" ")[0].trim());
		assertTrue(totalPages > 0);

		// paginacion botones
		WebElement btnNext = pagination.findElement(By.className(CLASS_PAGINATION_NEXT));
		btnNext.click();
		pagination = table.findElement(By.className(CLASS_PAGINATION));
		assertTrue(pagination.getText().indexOf("Página 2") != -1);

		WebElement btnLast = pagination.findElement(By.className(CLASS_PAGINATION_LAST));
		btnLast.click();
		pagination = table.findElement(By.className(CLASS_PAGINATION));
		Integer firstPage = Integer.parseInt(pagination.getText().split("/")[0].trim().split(" ")[2].trim());
		assertTrue(firstPage == totalPages);

		WebElement btnBack = pagination.findElement(By.className(CLASS_PAGINATION_BACK));
		btnBack.click();
		pagination = table.findElement(By.className(CLASS_PAGINATION));
		Integer penultimatePage = Integer.parseInt(pagination.getText().split("/")[0].trim().split(" ")[2].trim());
		assertTrue(penultimatePage == totalPages - 1);

		WebElement btnFist = pagination.findElement(By.className(CLASS_PAGINATION_FIRST));
		btnFist.click();
		pagination = table.findElement(By.className(CLASS_PAGINATION));
		Integer firstPageAgain = Integer.parseInt(pagination.getText().split("/")[0].trim().split(" ")[2].trim());
		assertTrue(firstPageAgain == 1);

		// alert si no hay filas seleccionadas
		WebElement actions = table.findElement(By.className(CLASS_ACTIONS));
		WebElement btnBloquear = actions.findElement(By.xpath("button[1]"));
		btnBloquear.click();
		String alert = this.getDialogText();
		assertTrue(alert.equals(MENSAJE_DIALOGO_SELECCIONAR_FILAS));
	}

	private String getDialogText() {
		WebElement dialogo = DriverUv.getDriver().findElement(By.className(CLASS_DIALOGO));
		WebElement content = dialogo.findElement(By.className(CLASS_DIALOGO_CONTENT));

		return content.findElement(By.tagName("h6")).getText();
	}


}
