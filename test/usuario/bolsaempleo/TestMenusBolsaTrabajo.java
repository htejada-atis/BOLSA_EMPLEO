package usuario.bolsaempleo;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorBolsas;
import es.ujaen.uvirtual.utilidades.DataTable;

/** Clase para probar solicitudCrud.
 * @author usig
 *
 */
public class TestMenusBolsaTrabajo {
	private static final String NOMBREDEESTACLASE = TestMenusBolsaTrabajo.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	private static final Integer WAIT_ELEMENT = 5; // segundos
	
	private static final String DIV_MAIN_BOLSAS = "bolsas";
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
		DriverUv.getDriver().get("http://localhost:8080/srv/es/informacionadministrativa/bolsaempleo/bolsas");
	}
	
	/** Listado de bolsas de empleo.
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
		WebElement th = table.findElement(By.xpath("//th[@class='area']"));
		th.click();
		WebElement img = th.findElement(By.className("order"));
		assertTrue(img.getAttribute("src").indexOf("down.png") != -1);
		th.click();
		img = th.findElement(By.className("order"));
		assertTrue(img.getAttribute("src").indexOf("up.png") != -1);
		
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
	
	/**
	 * Probamos petición datatable .
	 * @throws MalformedURLException .
	 * @throws IOException .
	 */
	@Test
	public void testA2() throws MalformedURLException, IOException {
		String url = "http://localhost:8080/srv/es/informacionadministrativa/bolsaempleo/bolsas?a=" + ControladorBolsas.ACCION_DATATABLE;
		String json = DriverUv.getAjaxRequestJson(url);
		
		Type typeDt = new TypeToken<DataTable<Bolsa>>() { }.getType();
		DataTable<Bolsa> dt = new Gson().fromJson(json, typeDt);
		
		assertTrue(dt.getData().size() > 0);
		
		//DriverUv.getDriver().get();
		//String json = DriverUv.getDriver().getPageSource();
		
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
}
