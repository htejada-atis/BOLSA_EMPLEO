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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorFiltrarTitulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaFiltrar;

/** test controlador filtrar.
 * @author jlopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorFiltrar {
	
	private static final String MENSAJE_CANDIDATO_DEVUELTO = "Debe devolver candidato";
	private static final String MENSAJE_CANDIDATOS_DEVUELTOS = "Debe devolver candida";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	private static final String MENSAJE_TITULACION_DEVUELTA = "Debe devolver titulación";
	private static final String MENSAJE_TITULACIONES_DEVUELTAS = "Debe devolver titulaciones";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en io
     */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
    
	// método para obtener la vista con una lista de candidatos .
	private VistaFiltrar obtenerCandidatos() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_ACCION, ControladorFiltrarTitulacion.ACCION_DATATABLE_CANDIDATOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarTitulacion controlador = new ControladorFiltrarTitulacion();
		controlador.doPost(peticion, respuesta);
		return (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
	}
    
	// método para obtener la vista con una lista de titulaciones de un candidato .
	private VistaFiltrar obtenerTitulaciones() throws IOException {
		VistaFiltrar bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_ACCION, ControladorFiltrarTitulacion.ACCION_DATATABLE_TITULACIONES_CANDIDATO);
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarTitulacion controlador = new ControladorFiltrarTitulacion();
		controlador.doPost(peticion, respuesta);
		return (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
	}
	
	/** Obtener candidatos, sin parametro definido .
	 * @throws IOException si error io
	 */
	@Test
	public void testA01() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarTitulacion controlador = new ControladorFiltrarTitulacion();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener candidatos, sin parametro definido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA02ObtenerCandidatos() throws IOException {
		VistaFiltrar bean = obtenerCandidatos();
		
		assertNotEquals(MENSAJE_CANDIDATOS_DEVUELTOS, 0, bean.getDatatableCandidatos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar un candidato .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA03SeleccionarCandidato() throws IOException {
		VistaFiltrar bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_ACCION, ControladorFiltrarTitulacion.ACCION_CANDIDATO_SELECCIONADO);
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarTitulacion controlador = new ControladorFiltrarTitulacion();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean2 = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener las titulaciones de un candidato .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA04ObtenerTitulacionesCandidato() throws IOException {
		VistaFiltrar bean = obtenerTitulaciones();
		
		assertNotEquals(MENSAJE_TITULACIONES_DEVUELTAS, 0, bean.getDatatableTitulaciones().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Selecciona una titulación para validarla .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA05SeleccionarTitulacion() throws IOException {
		VistaFiltrar bean = obtenerCandidatos();
		VistaFiltrar bean2 = obtenerTitulaciones();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_ACCION, ControladorFiltrarTitulacion.ACCION_TITULACION_SELECCIONADA);
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_TITULACION_USUARIO, bean2.getDatatableTitulaciones().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarTitulacion controlador = new ControladorFiltrarTitulacion();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean3 = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertNotNull(MENSAJE_TITULACION_DEVUELTA, bean3.getTitulacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener las titulaciones validadas de un candidato .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA06ObtenerTitulacionesValidadasCandidato() throws IOException {
		VistaFiltrar bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_ACCION, ControladorFiltrarTitulacion.ACCION_CANDIDATO_SELECCIONADO);
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarTitulacion controlador = new ControladorFiltrarTitulacion();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean2 = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertNotEquals(MENSAJE_TITULACIONES_DEVUELTAS, 0, bean2.getValidadas().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Deselecciona una titulación para validarla .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA07DeseleccionarTitulacion() throws IOException {
		VistaFiltrar bean = obtenerCandidatos();
		VistaFiltrar bean2 = obtenerTitulaciones();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_ACCION, ControladorFiltrarTitulacion.ACCION_TITULACION_DESELECCIONADA);
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_TITULACION_USUARIO, bean2.getDatatableTitulaciones().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarTitulacion controlador = new ControladorFiltrarTitulacion();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean3 = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertNotNull(MENSAJE_TITULACION_DEVUELTA, bean3.getTitulacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar candidato no válido .
	 * @throws IOException si error de io
	 */
	@Test
	public void testE02SeleccionarCandidatoNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_ACCION, ControladorFiltrarTitulacion.ACCION_CANDIDATO_SELECCIONADO);
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_CANDIDATO, "0");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarTitulacion controlador = new ControladorFiltrarTitulacion();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Seleccionar titulación no válida .
	 * @throws IOException si error de io
	 */
	@Test
	public void testE03SeleccionarTitulacionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_ACCION, ControladorFiltrarTitulacion.ACCION_TITULACION_SELECCIONADA);
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_TITULACION_USUARIO, "0");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarTitulacion controlador = new ControladorFiltrarTitulacion();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Lista titulaciones de un candidato nulo .
	 * @throws IOException si error de io
	 */
	@Test
	public void testE04ObtenerTitulacionesCandidatoNulo() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarTitulacion.PARAM_ACCION, ControladorFiltrarTitulacion.ACCION_DATATABLE_TITULACIONES_CANDIDATO);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarTitulacion controlador = new ControladorFiltrarTitulacion();
		controlador.doPost(peticion, respuesta);
		
		VistaFiltrar bean = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
}
