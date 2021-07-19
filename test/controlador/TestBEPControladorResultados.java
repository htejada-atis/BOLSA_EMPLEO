package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaResultados;
import es.ujaen.uvirtual.utilidades.UVException;

/** test controlador resultados .
 * @author ATISoluciones
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorResultados {
	
	private static final String MENSAJE_BOLSAS_DEVUELTAS = "Debe devolver bolsas";
	private static final String MENSAJE_BOLSA_DEVUELTA = "Debe devolver bolsa";
	private static final String MENSAJE_CANDIDATOS_DEVUELTOS = "Debe devolver candidatos";
	private static final String MENSAJE_CANDIDATO_DEVUELTO = "Debe devolver candidato";
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
	
	// método para obtener la vista con una lista de bolsas .
	private VistaResultados obtenerBolsas() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorResultados.PARAM_ACCION, ControladorResultados.ACCION_DATATABLE_BOLSAS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, String.valueOf(ModeloBolsa.ORDER_COLUMN_INDEX_FECHA_BAREMACION_RESULTADOS));
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorResultados controlador = new ControladorResultados();
		controlador.doGet(peticion, respuesta);
		
		return (VistaResultados) peticion.getUVDatos().getVistas().get(VistaResultados.class.getName());
	}
	
	// método para obtener la vista con una lista de candidatos .
	private VistaResultados obtenerCandidatos() throws IOException {
		VistaResultados bean = obtenerBolsas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorResultados.PARAM_ACCION, ControladorResultados.ACCION_DATATABLE_CANDIDATOS);
		peticion.setParameter(ControladorResultados.PARAM_BOLSA, bean.getDataTableBolsas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorResultados controlador = new ControladorResultados();
		controlador.doGet(peticion, respuesta);
		
		return (VistaResultados) peticion.getUVDatos().getVistas().get(VistaResultados.class.getName());
	}
	
	/** Obtener sin parametro definido .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA01() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorResultados controlador = new ControladorResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaResultados bean = (VistaResultados) peticion.getUVDatos().getVistas().get(VistaResultados.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Acción index .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA02() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorResultados.PARAM_ACCION, ControladorResultados.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorResultados controlador = new ControladorResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaResultados bean = (VistaResultados) peticion.getUVDatos().getVistas().get(VistaResultados.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable bolsa .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA03ObtenerBolsas() throws IOException {
		VistaResultados bean = obtenerBolsas();
		
		assertNotEquals(MENSAJE_BOLSAS_DEVUELTAS, 0, bean.getDataTableBolsas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar bolsa .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA04SeleccionarBolsa() throws IOException {
		VistaResultados bean = obtenerBolsas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorResultados.PARAM_ACCION, ControladorResultados.ACCION_SELECCIONAR_BOLSA);
		peticion.setParameter(ControladorResultados.PARAM_BOLSA, bean.getDataTableBolsas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorResultados controlador = new ControladorResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaResultados bean2 = (VistaResultados) peticion.getUVDatos().getVistas().get(VistaResultados.class.getName());
		
		assertNotNull(MENSAJE_BOLSA_DEVUELTA, bean2.getBolsa());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable candidatos .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA05ObtenerCandidatos() throws IOException {
		VistaResultados bean = obtenerCandidatos();
		
		assertNotEquals(MENSAJE_CANDIDATOS_DEVUELTOS, 0, bean.getDataTableCandidatos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA06SeleccionarCandidato() throws IOException {
		VistaResultados bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorResultados.PARAM_ACCION, ControladorResultados.ACCION_SELECCIONAR_CANDIDATO);
		peticion.setParameter(ControladorResultados.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorResultados.PARAM_CANDIDATO, bean.getDataTableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorResultados controlador = new ControladorResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaResultados bean2 = (VistaResultados) peticion.getUVDatos().getVistas().get(VistaResultados.class.getName());
		
		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** acción no válida .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE01AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorResultados.PARAM_ACCION, "accionnovalida");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorResultados controlador = new ControladorResultados();
		controlador.doGet(peticion, respuesta);

		VistaResultados bean = (VistaResultados) peticion.getUVDatos().getVistas().get(VistaResultados.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorResultados.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	/** Error de permiso .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE02errorUsuarioRol() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorResultados.PARAM_ACCION, ControladorResultados.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorResultados controlador = new ControladorResultados();
		controlador.doPost(peticion, respuesta);
		
		VistaResultados bean = (VistaResultados) peticion.getUVDatos().getVistas().get(VistaResultados.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(ControladorResultados.MENSAJE_ERROR_SIN_PERMISO, bean.getMensajesDeError().get(0).toString());
	}
	
	/** Obtener datatable bolsas parámetro no válido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE03ObtenerBolsasParametroNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorResultados.PARAM_ACCION, ControladorResultados.ACCION_DATATABLE_BOLSAS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorResultados controlador = new ControladorResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaResultados bean = (VistaResultados) peticion.getUVDatos().getVistas().get(VistaResultados.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Obtener datatable candidatos parámetro no válido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE04ObtenerCandidatosParametroNoValido() throws IOException {
		VistaResultados bean = obtenerBolsas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorResultados.PARAM_ACCION, ControladorResultados.ACCION_DATATABLE_CANDIDATOS);
		peticion.setParameter(ControladorResultados.PARAM_BOLSA, bean.getDataTableBolsas().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorResultados controlador = new ControladorResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaResultados bean2 = (VistaResultados) peticion.getUVDatos().getVistas().get(VistaResultados.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
}
