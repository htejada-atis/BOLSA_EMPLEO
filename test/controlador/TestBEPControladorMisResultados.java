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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorMisResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados;
import es.ujaen.uvirtual.utilidades.UVException;

/** test controlador resultados .
 * @author ATISoluciones
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorMisResultados {
	
	private static final String MENSAJE_BOLSAS_DEVUELTAS = "Debe devolver bolsas";
	private static final String MENSAJE_BOLSA_DEVUELTA = "Debe devolver bolsa";
	private static final String MENSAJE_CANDIDATOS_DEVUELTOS = "Debe devolver candidatos";
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
	private VistaMisResultados obtenerBolsas() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisResultados.PARAM_ACCION, ControladorMisResultados.ACCION_DATATABLE_BOLSAS);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisResultados controlador = new ControladorMisResultados();
		controlador.doGet(peticion, respuesta);
		
		return (VistaMisResultados) peticion.getUVDatos().getVistas().get(VistaMisResultados.class.getName());
	}
	
	// método para obtener la vista con una lista de candidatos .
	private VistaMisResultados obtenerCandidatos() throws IOException {
		VistaMisResultados bean = obtenerBolsas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisResultados.PARAM_ACCION, ControladorMisResultados.ACCION_DATATABLE_CANDIDATOS);
		peticion.setParameter(ControladorMisResultados.PARAM_BOLSA, bean.getDataTableBolsas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisResultados controlador = new ControladorMisResultados();
		controlador.doGet(peticion, respuesta);
		
		return (VistaMisResultados) peticion.getUVDatos().getVistas().get(VistaMisResultados.class.getName());
	}
	
	/** Obtener sin parametro definido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA01() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisResultados controlador = new ControladorMisResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaMisResultados bean = (VistaMisResultados) peticion.getUVDatos().getVistas().get(VistaMisResultados.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Acción index .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA02() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisResultados.PARAM_ACCION, ControladorMisResultados.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisResultados controlador = new ControladorMisResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaMisResultados bean = (VistaMisResultados) peticion.getUVDatos().getVistas().get(VistaMisResultados.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable bolsas .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA03ObtenerBolsas() throws IOException {
		VistaMisResultados bean = obtenerBolsas();
		
		assertNotEquals(MENSAJE_BOLSAS_DEVUELTAS, 0, bean.getDataTableBolsas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar bolsa .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA04SeleccionarBolsa() throws IOException {
		VistaMisResultados bean = obtenerBolsas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisResultados.PARAM_ACCION, ControladorMisResultados.ACCION_SELECCIONAR_BOLSA);
		peticion.setParameter(ControladorMisResultados.PARAM_BOLSA, bean.getDataTableBolsas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisResultados controlador = new ControladorMisResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaMisResultados bean2 = (VistaMisResultados) peticion.getUVDatos().getVistas().get(VistaMisResultados.class.getName());
		
		assertNotNull(MENSAJE_BOLSA_DEVUELTA, bean2.getBolsa());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable candidatos .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA05ObtenerCandidatos() throws IOException {
		VistaMisResultados bean = obtenerCandidatos();
		
		assertNotEquals(MENSAJE_CANDIDATOS_DEVUELTOS, 0, bean.getDataTableCandidatos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener resultados del candidato .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA06MisResultados() throws IOException {
		VistaMisResultados bean = obtenerBolsas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato3();
		peticion.setParameter(ControladorMisResultados.PARAM_ACCION, ControladorMisResultados.ACCION_MIS_RESULTADOS);
		peticion.setParameter(ControladorMisResultados.PARAM_BOLSA, bean.getDataTableBolsas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisResultados controlador = new ControladorMisResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaMisResultados bean2 = (VistaMisResultados) peticion.getUVDatos().getVistas().get(VistaMisResultados.class.getName());
		
		assertNotNull(MENSAJE_BOLSA_DEVUELTA, bean2.getBolsaResultado());
		assertNotNull(MENSAJE_BOLSA_DEVUELTA, bean2.getBolsa());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** acción no válida .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE01AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisResultados.PARAM_ACCION, "accionnovalida");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisResultados controlador = new ControladorMisResultados();
		controlador.doGet(peticion, respuesta);

		VistaMisResultados bean = (VistaMisResultados) peticion.getUVDatos().getVistas().get(VistaMisResultados.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorMisResultados.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	/** Error de permiso .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE02errorUsuarioRol() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorMisResultados.PARAM_ACCION, ControladorMisResultados.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisResultados controlador = new ControladorMisResultados();
		controlador.doPost(peticion, respuesta);
		
		VistaMisResultados bean = (VistaMisResultados) peticion.getUVDatos().getVistas().get(VistaMisResultados.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(ControladorMisResultados.MENSAJE_ERROR_SIN_PERMISO_CANDIDATO, bean.getMensajesDeError().get(0).toString());
	}
	
	/** Obtener datatable bolsas parámetro no válido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE03ObtenerBolsasParametroNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisResultados.PARAM_ACCION, ControladorMisResultados.ACCION_DATATABLE_BOLSAS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisResultados controlador = new ControladorMisResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaMisResultados bean = (VistaMisResultados) peticion.getUVDatos().getVistas().get(VistaMisResultados.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Obtener datatable candidatos parámetro no válido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE04ObtenerCandidatosParametroNoValido() throws IOException {
		VistaMisResultados bean = obtenerBolsas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisResultados.PARAM_ACCION, ControladorMisResultados.ACCION_DATATABLE_CANDIDATOS);
		peticion.setParameter(ControladorMisResultados.PARAM_BOLSA, bean.getDataTableBolsas().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisResultados controlador = new ControladorMisResultados();
		controlador.doGet(peticion, respuesta);
		
		VistaMisResultados bean2 = (VistaMisResultados) peticion.getUVDatos().getVistas().get(VistaMisResultados.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
}
