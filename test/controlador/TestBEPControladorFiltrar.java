package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import org.junit.BeforeClass;
import org.junit.Test;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFiltrar;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorFiltrar;

/** test controlador filtrar.
 * @author jlopez
 *
 */
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
    private VistaFiltrar obtenerCandidatos() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_DATATABLE_CANDIDATOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doPost(peticion, respuesta);
		return (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
    }
    
    // método para obtener la vista con una lista de titulaciones de un candidato .
    private VistaFiltrar obtenerTitulaciones() throws ServletException, IOException {
    	VistaFiltrar bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_DATATABLE_TITULACIONES_CANDIDATO);
		peticion.setParameter(ControladorFiltrar.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doPost(peticion, respuesta);
		return (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
    }
    
    /** Obtener candidatos, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener candidatos, sin parametro definido .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA02ObtenerCandidatos() throws SQLException, ServletException, IOException {
		VistaFiltrar bean = obtenerCandidatos();
		
		assertNotEquals(MENSAJE_CANDIDATOS_DEVUELTOS, 0, bean.getDatatableCandidatos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar un candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA03SeleccionarCandidato() throws SQLException, ServletException, IOException {
		VistaFiltrar bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_CANDIDATO_SELECCIONADO);
		peticion.setParameter(ControladorFiltrar.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean2 = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener las titulaciones de un candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA04ObtenerTitulacionesCandidato() throws SQLException, ServletException, IOException {
		VistaFiltrar bean = obtenerTitulaciones();
		
		assertNotEquals(MENSAJE_TITULACIONES_DEVUELTAS, 0, bean.getDatatableTitulaciones().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Selecciona una titulación para validarla .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA05SeleccionarTitulacion() throws SQLException, ServletException, IOException {
		VistaFiltrar bean = obtenerCandidatos();
		VistaFiltrar bean2 = obtenerTitulaciones();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_TITULACION_SELECCIONADA);
		peticion.setParameter(ControladorFiltrar.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorFiltrar.PARAM_TITULACION, bean2.getDatatableTitulaciones().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean3 = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertNotNull(MENSAJE_TITULACION_DEVUELTA, bean3.getTitulacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener las titulaciones validadas de un candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA06ObtenerTitulacionesValidadasCandidato() throws SQLException, ServletException, IOException {
		VistaFiltrar bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_CANDIDATO_SELECCIONADO);
		peticion.setParameter(ControladorFiltrar.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean2 = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertNotEquals(MENSAJE_TITULACIONES_DEVUELTAS, 0, bean2.getValidadas().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Deselecciona una titulación para validarla .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA07DeseleccionarTitulacion() throws SQLException, ServletException, IOException {
		VistaFiltrar bean = obtenerCandidatos();
		VistaFiltrar bean2 = obtenerTitulaciones();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_TITULACION_DESELECCIONADA);
		peticion.setParameter(ControladorFiltrar.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorFiltrar.PARAM_TITULACION, bean2.getDatatableTitulaciones().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean3 = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertNotNull(MENSAJE_TITULACION_DEVUELTA, bean3.getTitulacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
	/** descargar fichero mérito.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA08Descargar() throws ServletException, IOException {
		VistaFiltrar bean = obtenerTitulaciones();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_DESCARGAR_FICHERO);
		peticion.setParameter(ControladorFiltrar.PARAM_TITULACION, bean.getDatatableTitulaciones().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		VistaFiltrar bean2 = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertNotNull(MENSAJE_TITULACION_DEVUELTA, bean2.getTitulacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar candidato no válido .
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testE01SeleccionarCandidatoNoValido() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_CANDIDATO_SELECCIONADO);
		peticion.setParameter(ControladorFiltrar.PARAM_CANDIDATO, "0");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Seleccionar titulación no válida .
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testE02SeleccionarTitulacionNoValida() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_TITULACION_SELECCIONADA);
		peticion.setParameter(ControladorFiltrar.PARAM_TITULACION, "0");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Lista titulaciones de un candidato nulo .
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testE03ObtenerTitulacionesCandidatoNulo() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_DATATABLE_TITULACIONES_CANDIDATO);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doPost(peticion, respuesta);
		
		VistaFiltrar bean = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
}
