package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorPlantillas;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaPlantillas;
import es.ujaen.uvirtual.utilidades.UVException;

/** test controlador plantillas .
 * @author ATISoluciones
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorPlantillas {
	
	private static final String MENSAJE_PLANTILLAS_DEVUELTAS = "Debe devolver plantillas";
	private static final String MENSAJE_PLANTILLA_DEVUELTA = "Debe devolver plantilla";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_ERROR_ESPERADO = "El mensaje de error debe coincidir";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
	
	/** prepara la bd con los datos iniciales.
	 * @throws SQLException si error en bd .
	 * @throws IOException si error en io .
	 * @throws UVException .
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException, UVException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
		UtilsTestBolsaEmpleo.baremarBolsa(ModeloBolsa.obtenerInstancia().getBolsaById(2));
	}
	
	// método para obtener la vista con una lista de plantillas .
	private VistaPlantillas obtenerBolsas() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorPlantillas.PARAM_ACCION, ControladorPlantillas.ACCION_DATATABLE_PLANTILLAS);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlantillas controlador = new ControladorPlantillas();
		controlador.doGet(peticion, respuesta);
		
		return (VistaPlantillas) peticion.getUVDatos().getVistas().get(VistaPlantillas.class.getName());
	}
	
	/** Obtener sin parametro definido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA01() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlantillas controlador = new ControladorPlantillas();
		controlador.doGet(peticion, respuesta);
		
		VistaPlantillas bean = (VistaPlantillas) peticion.getUVDatos().getVistas().get(VistaPlantillas.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Acción index .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA02() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorPlantillas.PARAM_ACCION, ControladorPlantillas.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlantillas controlador = new ControladorPlantillas();
		controlador.doGet(peticion, respuesta);
		
		VistaPlantillas bean = (VistaPlantillas) peticion.getUVDatos().getVistas().get(VistaPlantillas.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable plantillas .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA03ObtenerPlazasOfertadas() throws IOException {
		VistaPlantillas bean = obtenerBolsas();
		
		assertNotEquals(MENSAJE_PLANTILLAS_DEVUELTAS, 0, bean.getDataTablePlantillas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar una plantilla .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA04SeleccionarPlantilla() throws IOException {
		VistaPlantillas bean = obtenerBolsas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorPlantillas.PARAM_ACCION, ControladorPlantillas.ACCION_EDITAR_PLANTILLA);
		peticion.setParameter(ControladorPlantillas.PARAM_PLANTILLA, bean.getDataTablePlantillas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlantillas controlador = new ControladorPlantillas();
		controlador.doGet(peticion, respuesta);
		
		VistaPlantillas bean2 = (VistaPlantillas) peticion.getUVDatos().getVistas().get(VistaPlantillas.class.getName());
		
		assertNotNull(MENSAJE_PLANTILLA_DEVUELTA, bean2.getPlantilla());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** acción no válida .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE01AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorPlantillas.PARAM_ACCION, "accionnovalida");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlantillas controlador = new ControladorPlantillas();
		controlador.doGet(peticion, respuesta);

		VistaPlantillas bean = (VistaPlantillas) peticion.getUVDatos().getVistas().get(VistaPlantillas.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorPlantillas.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	/** Error de permiso .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE02errorUsuarioRol() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorPlantillas.PARAM_ACCION, ControladorPlantillas.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlantillas controlador = new ControladorPlantillas();
		controlador.doPost(peticion, respuesta);
		
		VistaPlantillas bean = (VistaPlantillas) peticion.getUVDatos().getVistas().get(VistaPlantillas.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals("No tienes permiso de personal", bean.getMensajesDeError().get(0).toString());
	}
	
	/** Obtener datatable plazas ofertadas parámetro no válido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE03ObtenerBolsasParametroNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorPlantillas.PARAM_ACCION, ControladorPlantillas.ACCION_DATATABLE_PLANTILLAS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlantillas controlador = new ControladorPlantillas();
		controlador.doGet(peticion, respuesta);
		
		VistaPlantillas bean = (VistaPlantillas) peticion.getUVDatos().getVistas().get(VistaPlantillas.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
}
