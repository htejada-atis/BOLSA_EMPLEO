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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorContratacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaContratacion;

/** test controlador inicio.
 * @author jlopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorContratacion {
	
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_ERROR_ESPERADO = "El mensaje de error debe coincidir";
	private static final String MENSAJE_PLAZAS_OFERTADAS_DEVUELTAS = "Debe devolver plazas ofertadas";
	private static final String MENSAJE_PLAZA_OFERTADA_DEVUELTA = "Debe devolver plaza ofertada";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
	/** Prepara la bd con los datos iniciales.
	 * @throws SQLException si error en bd
	 * @throws IOException si error en ficheros
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
	
	private VistaContratacion obtenerPlazasOfertadas() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_DATATABLE_PLAZAS_OFERTADAS);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		return (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
	}
	
	/** Obtener plazas ofertadas, sin parametro definido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA01Obtener() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		VistaContratacion bean = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Acción index .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA02Obtener() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		VistaContratacion bean = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable plazas ofertadas .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA03ObtenerDataTablePlazasOfertadas() throws IOException {
		VistaContratacion bean = obtenerPlazasOfertadas();
		
		assertNotEquals(MENSAJE_PLAZAS_OFERTADAS_DEVUELTAS, 0, bean.getDatatablePlazasOfertadas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar una plaza ofertada .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA04SeleccionarPlazaOfertada() throws IOException {
		VistaContratacion bean = obtenerPlazasOfertadas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_SELECCIONAR_PLAZA_OFERTADA);
		peticion.setParameter(ControladorContratacion.PARAM_PLAZA_OFERTADA, bean.getDatatablePlazasOfertadas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		VistaContratacion bean2 = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertNotNull(MENSAJE_PLAZA_OFERTADA_DEVUELTA, bean2.getPlazaOfertada());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** acción no válida .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE01AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, "accionnovalida");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);

		VistaContratacion bean = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorContratacion.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	/** Error de permiso .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE02errorUsuarioRol() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doPost(peticion, respuesta);
		
		VistaContratacion bean = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(ControladorContratacion.MENSAJE_ERROR_SIN_PERMISO, bean.getMensajesDeError().get(0).toString());
	}
	
}