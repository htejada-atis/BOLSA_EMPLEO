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
import org.openqa.selenium.WebElement;

import bbdd.UtilsTestDocentia;
import es.ujaen.uvirtual.utilidades.UVException;

/** Clase para probar solicitudCrud.
 * @author usig
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConvocatoriaCrud extends UtilsTestUsuarioBase {
	private static final String NOMBREDEESTACLASE = TestConvocatoriaCrud.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	private static final String ID_NOMBRE_FORMULARIO = "#nombreCampoFormulario";
	private static final String ID_FECHA_FORMULARIO = "#fechaCampoFormulario";
	private static final String ID_FECHA_COMISION_FORMULARIO = "#fechaComisionCampoFormulario";
	
	private static final String ID_OBSERVACIONES_FORMULARIO = "#observacionesCampoFormulario";
	private static final String ID_BOTON_INSERTAR = "botonInsertaConvocatoria";
	private static final String CLASS_ERROR = "error";
	private static final String CLASS_BLUETABLE = "bluetable";
	private static final String FECHA_EJEMPLO = "01/01/2020";
	private static final String OBSERVACIONES_EJEMPLO = "observaciones";

	/** Se ejecuta una vez al inicio de la clase.
	 * @throws SQLException Si se produce error en bbdd
	 * @throws IOException Si no se puede cargar los ficheros
	 * @throws UVException si error uv
	 */
	@BeforeClass
	public static void setUp() throws IOException, SQLException, UVException {
		LOGGER.log(Level.INFO, "inicio");
		UtilsTestDocentia.inicializaDocentia();	
		DriverUv.inicializaDriver();
		DriverUv.login("usig");
	}

	/** Se ejecuta antes de cada test.
	 * Aqui navegamos hasta la opcion que vamos a probar
	 */
	@Before
	public void navegaOpcion() {
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/index");
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa");
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/docentia");
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/docentia/convocatoriacrud");
	}
	
	/** Inserta una convocatoria.
	 */
	@Test
	public void testA01() {
		DriverUv.getDriver().findElement(By.cssSelector(ID_NOMBRE_FORMULARIO)).sendKeys("prueba 1");
		DriverUv.getDriver().findElement(By.cssSelector(ID_FECHA_FORMULARIO)).sendKeys(FECHA_EJEMPLO);
		DriverUv.getDriver().findElement(By.cssSelector(ID_FECHA_COMISION_FORMULARIO)).sendKeys(FECHA_EJEMPLO);
		DriverUv.getDriver().findElement(By.cssSelector(ID_OBSERVACIONES_FORMULARIO)).sendKeys(OBSERVACIONES_EJEMPLO);
		DriverUv.getDriver().findElement(By.id(ID_BOTON_INSERTAR)).click();
		assertTrue(DriverUv.getDriver().findElements(By.className(CLASS_ERROR)).isEmpty());
		assertNotNull(DriverUv.getDriver().findElement(By.className(CLASS_BLUETABLE)));
	}
	
	/** Inserta una convocatoria sin observaciones.
	 */
	@Test
	public void testA02() {
		DriverUv.getDriver().findElement(By.cssSelector(ID_NOMBRE_FORMULARIO)).sendKeys("prueba2");
		DriverUv.getDriver().findElement(By.cssSelector(ID_FECHA_FORMULARIO)).sendKeys(FECHA_EJEMPLO);
		DriverUv.getDriver().findElement(By.cssSelector(ID_FECHA_COMISION_FORMULARIO)).sendKeys(FECHA_EJEMPLO);
		DriverUv.getDriver().findElement(By.id(ID_BOTON_INSERTAR)).click();
		assertTrue(DriverUv.getDriver().findElements(By.className(CLASS_ERROR)).isEmpty());
		assertNotNull(DriverUv.getDriver().findElement(By.className(CLASS_BLUETABLE)));
	}
	
	/** Edita una convocatoria.
	 */
	@Test
	public void testA03() {
		WebElement botonEditar = DriverUv.getDriver().findElement(By.id("botonEditar3"));
		botonEditar.click();
		assertTrue(DriverUv.getDriver().findElements(By.className(CLASS_ERROR)).isEmpty());
		assertNotNull(DriverUv.getDriver().findElement(By.className(CLASS_BLUETABLE)));
		DriverUv.getDriver().findElement(By.cssSelector(ID_NOMBRE_FORMULARIO)).clear();
		DriverUv.getDriver().findElement(By.cssSelector(ID_NOMBRE_FORMULARIO)).sendKeys("update 1");
		DriverUv.getDriver().findElement(By.cssSelector("#estadoCampoFormulario")).sendKeys("CERRADA");
		DriverUv.getDriver().findElement(By.cssSelector(ID_FECHA_FORMULARIO)).clear();
		DriverUv.getDriver().findElement(By.cssSelector(ID_FECHA_FORMULARIO)).sendKeys("02/02/2020");
		DriverUv.getDriver().findElement(By.cssSelector(ID_OBSERVACIONES_FORMULARIO)).clear();
		DriverUv.getDriver().findElement(By.cssSelector(ID_OBSERVACIONES_FORMULARIO)).sendKeys("update 1");
		WebElement botonGuardarCambios = DriverUv.getDriver().findElement(By.id("botonGuardar"));
		botonGuardarCambios.click();
		assertTrue(DriverUv.getDriver().findElements(By.className(CLASS_ERROR)).isEmpty());
		assertNotNull(DriverUv.getDriver().findElement(By.className(CLASS_BLUETABLE)));
	}

	/** Borrar una convocatoria.
	 */
	@Test
	public void testA04() {
		WebElement botonEditar = DriverUv.getDriver().findElement(By.id("botonEliminar5"));
		botonEditar.click();
		assertTrue(DriverUv.getDriver().findElements(By.className(CLASS_ERROR)).isEmpty());
		assertNotNull(DriverUv.getDriver().findElement(By.className(CLASS_BLUETABLE)));
	}

	/** Inserta una convocatoria sin nombre.
	 * La pagina debe mostrar un error
	 */
	@Test
	public void testE01() {
		DriverUv.getDriver().findElement(By.cssSelector(ID_FECHA_FORMULARIO)).sendKeys(FECHA_EJEMPLO);
		DriverUv.getDriver().findElement(By.cssSelector(ID_OBSERVACIONES_FORMULARIO)).sendKeys(OBSERVACIONES_EJEMPLO);
		DriverUv.getDriver().findElement(By.id(ID_BOTON_INSERTAR)).click();
		assertNotNull(DriverUv.getDriver().findElement(By.className(CLASS_ERROR)));
	}

	/** Inserta una convocatoria sin fecha.
	 * La pagina debe mostrar un error
	 */
	@Test
	public void testE02() {
		DriverUv.getDriver().findElement(By.cssSelector(ID_NOMBRE_FORMULARIO)).sendKeys("pruebaE02");
		DriverUv.getDriver().findElement(By.cssSelector(ID_OBSERVACIONES_FORMULARIO)).sendKeys(OBSERVACIONES_EJEMPLO);
		DriverUv.getDriver().findElement(By.id(ID_BOTON_INSERTAR)).click();
		assertNotNull(DriverUv.getDriver().findElement(By.className(CLASS_ERROR)));
	}

	/** Borra una convocatoria que tiene solicitudes.
	 * No debe dejar borrarla por integridad referencial 
	 * comentada para la base, las pruebas de error tambien deben de hacerse
	@Test
	public void testE11BorrarConvocatoriaConSolicitudes() {
		WebElement botonEliminar = DriverUv.getDriver().findElement(By.id("botonEliminar1"));
		botonEliminar.click();
		assertTrue(DriverUv.getDriver().findElement(By.className("error")).getText().contains("ORA-02292"));
	}
	 */

	/** Inserta una convocatoria con fecha con formato erroneo.
	 * La pagina debe mostrar un error
	 */
	@Test
	public void testE21() {
		DriverUv.getDriver().findElement(By.cssSelector(ID_NOMBRE_FORMULARIO)).sendKeys("pruebaE21");
		DriverUv.getDriver().findElement(By.cssSelector(ID_FECHA_FORMULARIO)).sendKeys("01/01-2010");
		DriverUv.getDriver().findElement(By.cssSelector(ID_OBSERVACIONES_FORMULARIO)).sendKeys(OBSERVACIONES_EJEMPLO);
		DriverUv.getDriver().findElement(By.id(ID_BOTON_INSERTAR)).click();
		assertNotNull(DriverUv.getDriver().findElement(By.className(CLASS_ERROR)));
	}

	/** Cierre de este unittest.
	 */
	@AfterClass
	public static void cerrar() {
		LOGGER.log(Level.INFO, "final");
		DriverUv.getDriver().quit();
	}
}
