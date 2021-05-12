package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorAreasABaremar;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaAreasBaremar;


/** test controlador areas a baremar .
 * @author ATISoluciones 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorAreasABaremar {
	
	private static final String MENSAJE_AREAS_DEVUELTAS = "Debe devolver areas";
	private static final String MENSAJE_AREA_DEVUELTA = "Debe devolver area";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_EXITO = "Debe devolver exito";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
	private static final String ACCION_NO_VALIDA = "accionnovalida";
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    // método para obtener la vista con una lista de áreas .
    private VistaAreasBaremar obtenerAreas() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION, ControladorAreasABaremar.ACCION_DATATABLE);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorAreasABaremar controlador = new ControladorAreasABaremar();
		controlador.doPost(peticion, respuesta);
		return (VistaAreasBaremar) peticion.getUVDatos().getVistas().get(VistaAreasBaremar.class.getName());
    }
    
    /** Obtener áreas, sin parametro definido .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorAreasABaremar controlador = new ControladorAreasABaremar();
		controlador.doGet(peticion, respuesta);
		
		VistaAreasBaremar bean = (VistaAreasBaremar) peticion.getUVDatos().getVistas().get(VistaAreasBaremar.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener áreas, acción index .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA02() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION, ControladorAreasABaremar.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorAreasABaremar controlador = new ControladorAreasABaremar();
		controlador.doGet(peticion, respuesta);
		
		VistaAreasBaremar bean = (VistaAreasBaremar) peticion.getUVDatos().getVistas().get(VistaAreasBaremar.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener áreas, acción index .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA03ObtenerAreas() throws SQLException, ServletException, IOException {
		VistaAreasBaremar bean = obtenerAreas();
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getDatatableAreas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
    
	/** Acción sobre área, pasar lista de bolsas a baremables .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA04AccionAreaPasarABaremable() throws SQLException, ServletException, IOException {
		VistaAreasBaremar bean = obtenerAreas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION, ControladorAreasABaremar.ACCION_AREA);
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION_AREA, ControladorAreasABaremar.ACCION_AREA_PASAR_A_BAREMALE);
		peticion.setParameter(ControladorAreasABaremar.PARAM_AREAS_SELECCIONADAS, "[" + bean.getDatatableAreas().getData().get(0).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorAreasABaremar controlador = new ControladorAreasABaremar();
		controlador.doGet(peticion, respuesta);
		
		VistaAreasBaremar bean2 = (VistaAreasBaremar) peticion.getUVDatos().getVistas().get(VistaAreasBaremar.class.getName());
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean2.getListaBolsas().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Acción sobre área, pasar lista de bolsas a no baremables .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA05AccionAreaPasarANoBaremable() throws SQLException, ServletException, IOException {
		VistaAreasBaremar bean = obtenerAreas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION, ControladorAreasABaremar.ACCION_AREA);
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION_AREA, ControladorAreasABaremar.ACCION_AREA_PASAR_A_NO_BAREMALE);
		peticion.setParameter(ControladorAreasABaremar.PARAM_AREAS_SELECCIONADAS, 
				"[" + bean.getDatatableAreas().getData().get(bean.getDatatableAreas().getData().size() - 1).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorAreasABaremar controlador = new ControladorAreasABaremar();
		controlador.doGet(peticion, respuesta);
		
		VistaAreasBaremar bean2 = (VistaAreasBaremar) peticion.getUVDatos().getVistas().get(VistaAreasBaremar.class.getName());
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean2.getListaBolsas().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** importar áreas de uvirtual .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA06ImportarAreasUVirtual() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION, ControladorAreasABaremar.ACCION_IMPORTAR_AREAS_UVIRTUAL);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorAreasABaremar controlador = new ControladorAreasABaremar();
		controlador.doGet(peticion, respuesta);
		
		VistaAreasBaremar bean = (VistaAreasBaremar) peticion.getUVDatos().getVistas().get(VistaAreasBaremar.class.getName());
		
		assertEquals(MENSAJE_CON_EXITO, 1, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener áreas excluidas de un usuario .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA07ObtenerAreasExcluidasUsuario() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION, ControladorAreasABaremar.ACCION_DATATABLE_EXCLUIDOS);
		peticion.setParameter(ControladorAreasABaremar.PARAM_ID, "1");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorAreasABaremar controlador = new ControladorAreasABaremar();
		controlador.doGet(peticion, respuesta);
		
		VistaAreasBaremar bean = (VistaAreasBaremar) peticion.getUVDatos().getVistas().get(VistaAreasBaremar.class.getName());
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getDatatableAreas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable áreas con parámetro no válido .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testE01ObtenerAreasParametroNoValido() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION, ControladorAreasABaremar.ACCION_DATATABLE);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorAreasABaremar controlador = new ControladorAreasABaremar();
		controlador.doGet(peticion, respuesta);
		
		VistaAreasBaremar bean = (VistaAreasBaremar) peticion.getUVDatos().getVistas().get(VistaAreasBaremar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** obtener datatable áreas excluidas de un usuario con parámetro no válido .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testE02ObtenerAreasExcluidasUsuarioParametroNoValido() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION, ControladorAreasABaremar.ACCION_DATATABLE_EXCLUIDOS);
		peticion.setParameter(ControladorAreasABaremar.PARAM_ID, "1");
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorAreasABaremar controlador = new ControladorAreasABaremar();
		controlador.doGet(peticion, respuesta);
		
		VistaAreasBaremar bean = (VistaAreasBaremar) peticion.getUVDatos().getVistas().get(VistaAreasBaremar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Obtener áreas, acción no válida .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE03AccionNoValida() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION, ACCION_NO_VALIDA);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorAreasABaremar controlador = new ControladorAreasABaremar();
		controlador.doGet(peticion, respuesta);
		
		VistaAreasBaremar bean = (VistaAreasBaremar) peticion.getUVDatos().getVistas().get(VistaAreasBaremar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Acción sobre área no válida .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE04AccionAreaNoValida() throws SQLException, ServletException, IOException {
		VistaAreasBaremar bean = obtenerAreas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION, ControladorAreasABaremar.ACCION_AREA);
		peticion.setParameter(ControladorAreasABaremar.PARAM_ACCION_AREA, ACCION_NO_VALIDA);
		peticion.setParameter(ControladorAreasABaremar.PARAM_AREAS_SELECCIONADAS, 
				"[" + bean.getDatatableAreas().getData().get(bean.getDatatableAreas().getData().size() - 1).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorAreasABaremar controlador = new ControladorAreasABaremar();
		controlador.doGet(peticion, respuesta);
		
		VistaAreasBaremar bean2 = (VistaAreasBaremar) peticion.getUVDatos().getVistas().get(VistaAreasBaremar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
}
