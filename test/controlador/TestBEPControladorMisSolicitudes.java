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
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisSolicitudes;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;

/** test controlador mis solicitudes.
 * @author ATISoluciones
 *
 */
public class TestBEPControladorMisSolicitudes {
	
	private static final String MENSAJE_BOLSAS_DEVUELTAS = "Debe devolver bolsas";
	private static final String MENSAJE_BOLSA_DEVUELTA = "Debe devolver bolsa";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_MERITOS_DEVUELTOS = "Debe devolver méritos";
	private static final String MENSAJE_MERITO_DEVUELTO = "Debe devolver mérito";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	private static final String MENSAJE_SOLICITUDES_DEVUELTAS = "Debe devolver solicitudes";
	private static final String MENSAJE_SOLICITUD_DEVUELTA = "Debe devolver solicitud";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd .
     * @throws IOException si error en io .
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    // método para obtener la vista con una lista de solicitudes .
    private VistaSolicitudes obtenerSolicitudes() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DATATABLE_SOLICITUDES);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doPost(peticion, respuesta);
		return (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
    }
    
    // método para obtener la vista con una lista de bolsas del usuario .
    private VistaSolicitudes obtenerAreas() throws ServletException, IOException {
    	VistaSolicitudes bean = obtenerSolicitudes();
    	
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DATATABLE_AREAS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doPost(peticion, respuesta);
		return (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
    }
    
    // método para obtener la vista con una lista de bolsas del usuario .
    private VistaSolicitudes obtenerBolsasCandidato() throws ServletException, IOException {
    	VistaSolicitudes bean = obtenerSolicitudes();
    	
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DATATABLE_BOLSAS_SELECCIONADAS);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doPost(peticion, respuesta);
		return (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
    }
    
    // método para obtener la vista con una lista de méritos de una bolsa .
    private VistaSolicitudes obtenerMeritosBolsa() throws ServletException, IOException {
    	VistaSolicitudes bean = obtenerSolicitudes();
    	VistaSolicitudes bean2 = obtenerBolsasCandidato();
    	
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DATATABLE_MERITOS_BOLSA);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorMisSolicitudes.PARAM_BOLSA, bean2.getDatatableBolsasSolicitud().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doPost(peticion, respuesta);
		return (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
    }
    
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TESTS: SOLICITUDES
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Obtener mis solicitudes, sin parametro definido .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		
		VistaSolicitudes bean = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener mis solicitudes .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA02() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_LISTAR_SOLICITUDES);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		
		VistaSolicitudes bean = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Consultar solicitud .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA03ConsultarSolicitud() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_CONSULTAR_SOLICITUD);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		VistaSolicitudes bean2 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertNotNull(MENSAJE_SOLICITUD_DEVUELTA, bean2.getSolicitud());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Crear solicitud .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA04CrearSolicitud() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato2();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_CREAR_SOLICITUD);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_CONVOCATORIA_ID, "1");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		VistaSolicitudes bean = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertNotNull(MENSAJE_SOLICITUD_DEVUELTA, bean.getSolicitud());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable solicitudes de una convocatoria .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA05ObtenerSolicitudes() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		
		assertNotEquals(MENSAJE_SOLICITUDES_DEVUELTAS, 0, bean.getDatatableSolicitudes().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable solicitudes de una convocatoria con parámetro erróneo de la tabla para forzar error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE01ObtenerSolicitudesParametroErroneo() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DATATABLE_SOLICITUDES);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doPost(peticion, respuesta);
		VistaSolicitudes bean = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TESTS PASO 1: SELECCIÓN DE ÁREAS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** Seleccionar bolsas para la solicitud .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA06SeleccionarBolsas() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		VistaSolicitudes bean2 = obtenerAreas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_SELECCIONAR_BOLSAS);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorMisSolicitudes.PARAM_BOLSAS, "[" + bean2.getDataTableBolsasCandidato().getData().get(0).getCodNum().toString() + "]");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		VistaSolicitudes bean3 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable areas .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA07ObtenerAreas() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerAreas();
		
		assertNotEquals(MENSAJE_SOLICITUDES_DEVUELTAS, 0, bean.getDataTableBolsasCandidato().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar bolsas para la solicitud no válidas .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE02SeleccionarBolsasNoValidas() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		VistaSolicitudes bean2 = obtenerAreas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_SELECCIONAR_BOLSAS);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorMisSolicitudes.PARAM_BOLSAS, bean2.getDataTableBolsasCandidato().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		VistaSolicitudes bean3 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean3.getMensajesDeExito().size());
	}
	
	/** Obtener datatable areas con parámetro erróneo de la tabla para forzar error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE03ObtenerAreasParametroErroneo() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
    	
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DATATABLE_AREAS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doPost(peticion, respuesta);
		VistaSolicitudes bean2 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TESTS PASO 2: ASIGNACIÓN DE MÉRITOS A ÁREAS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** seleccionar bolsa .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA08SeleccionarBolsa() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		VistaSolicitudes bean2 = obtenerBolsasCandidato();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_BOLSA_SELECCIONADA);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorMisSolicitudes.PARAM_BOLSA, bean2.getDatatableBolsasSolicitud().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		VistaSolicitudes bean3 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertNotNull(MENSAJE_SOLICITUD_DEVUELTA, bean3.getSolicitud());
		assertNotNull(MENSAJE_BOLSA_DEVUELTA, bean3.getArea());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable areas de la solicitud .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA09ObtenerAreasSolicitud() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerBolsasCandidato();
		
		assertNotEquals(MENSAJE_BOLSAS_DEVUELTAS, 0, bean.getDatatableBolsasSolicitud().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable méritos de una bolsa .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA10ObtenerMeritos() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerMeritosBolsa();
		
		assertNotEquals(MENSAJE_MERITOS_DEVUELTOS, 0, bean.getDataTableMeritos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener bolsas solicitud .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA11ObtenerBolsasSolicitud() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_LISTAR_BOLSAS_SOLICITUD);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		
		VistaSolicitudes bean2 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** deselecciona merito para agregarlo a la bolsa .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA12DeseleccionarMerito() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		VistaSolicitudes bean2 = obtenerBolsasCandidato();
		VistaSolicitudes bean3 = obtenerMeritosBolsa();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_MERITO_DESELECCIONADO);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorMisSolicitudes.PARAM_BOLSA, bean2.getDatatableBolsasSolicitud().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorMisSolicitudes.PARAM_MERITO, bean3.getDataTableMeritos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		
		VistaSolicitudes bean4 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertNotNull(MENSAJE_MERITO_DEVUELTO, bean4.getMerito());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean4.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean4.getMensajesDeAdvertencia().size());
	}
	
	/** selecciona merito para agregarlo a la bolsa .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA13SeleccionarMerito() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		VistaSolicitudes bean2 = obtenerBolsasCandidato();
		VistaSolicitudes bean3 = obtenerMeritosBolsa();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_MERITO_SELECCIONADO);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorMisSolicitudes.PARAM_BOLSA, bean2.getDatatableBolsasSolicitud().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorMisSolicitudes.PARAM_MERITO, bean3.getDataTableMeritos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		
		VistaSolicitudes bean4 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertNotNull(MENSAJE_MERITO_DEVUELTO, bean4.getMerito());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean4.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean4.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable areas de la solicitud con parámetro erróneo de la tabla para forzar error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE04ObtenerBolsasSolicitudParametroErroneo() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DATATABLE_BOLSAS_SELECCIONADAS);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		
		VistaSolicitudes bean2 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Obtener datatable méritos con parámetro erróneo de la tabla para forzar error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE05ObtenerMeritosParametroErroneo() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
    	VistaSolicitudes bean2 = obtenerBolsasCandidato();
    	
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DATATABLE_MERITOS_BOLSA);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorMisSolicitudes.PARAM_BOLSA, bean2.getDatatableBolsasSolicitud().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doPost(peticion, respuesta);
		
		VistaSolicitudes bean3 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean3.getMensajesDeExito().size());
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PASO 3: RESUMEN, CONFIRMAR SOLICITUD
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** resumen de la solicitud .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA14ResumenSolicitud() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_RESUMEN_SOLICITUD);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		
		VistaSolicitudes bean2 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertNotNull(MENSAJE_SOLICITUD_DEVUELTA, bean2.getSolicitud());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** confirmar solicitud .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA15ConfirmarSolicitud() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_CONFIRMAR_SOLICITUD);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		
		VistaSolicitudes bean2 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertNotNull(MENSAJE_SOLICITUD_DEVUELTA, bean2.getSolicitud());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** descargar pdf de la solicitud error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE06ErrorPdfSolicitud() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerSolicitudes();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DESCARGAR_PDF);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		
		VistaSolicitudes bean2 = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
}
