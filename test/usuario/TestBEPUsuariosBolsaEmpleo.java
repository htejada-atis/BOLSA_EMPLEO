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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;

/**
 * Clase para probar configuracion.usuarios BEP .
 * 
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPUsuariosBolsaEmpleo extends UtilsTestUsuarioBase {
	private static final String NOMBREDEESTACLASE = TestBEPUsuariosBolsaEmpleo.class.getName();

	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	private static final String DIV_MAIN = "usuarios";
	private static final String ID_TABLE = "table_usuarios";
	private static final String ID_BOTON_NUEVO = "nuevo_usuario";
	private static final String ID_INPUT_NUEVO = "usuario_nombre";
	private static final String ID_INPUT_BUSCAR = "usuario_buscar";
	private static final String ID_INPUT_ENVIAR_NUEVO = "usuario_enviar";
	private static final String SELECT_ROL = "select_role";
	
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
		DriverUv.getDriver().get(DriverUv.RUTA + "/srv/es/informacionadministrativa/bolsaempleo/configuracion/usuarios");
	}

	/**
	 * Listado de usuarios.
	 */
	@Test
	public void testA1() {
		UtilsTestBolsaEmpleo.assertTitlePage(UtilsTestBolsaEmpleo.waitVisibility(By.className(DIV_MAIN)), "Usuarios del sistema");
		WebElement table = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_TABLE));
		assertTrue(UtilsTestBolsaEmpleo.getTotalTable(table) > 0);
	}

	/**
	 * Probamos a crear un nuevo usuario .
	 * 
	 * @throws MalformedURLException .
	 * @throws IOException           .
	 */
	@Test
	public void testA3() {
		WebElement btnNuevo = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_BOTON_NUEVO));
		btnNuevo.click();
		
		WebElement input = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_INPUT_NUEVO));
		input.sendKeys("personal2");
		
		WebElement buscar = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_INPUT_BUSCAR));
		buscar.click();
		
		Select rol = new Select(UtilsTestBolsaEmpleo.waitVisibility(By.id(SELECT_ROL)));
		rol.selectByValue(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO.toString());
		WebElement nuevo = UtilsTestBolsaEmpleo.waitVisibility(By.id(ID_INPUT_ENVIAR_NUEVO));
		nuevo.click();
		
		List<String> mensajes = UtilsTestBolsaEmpleo.getMensajesDeExito();
		assertTrue(mensajes.size() == 1);
		assertEquals(mensajes.get(0), ControladorUsuarioBolsaEmpleo.MENSAJE_EXITO_AGREGAR);
	}
}
