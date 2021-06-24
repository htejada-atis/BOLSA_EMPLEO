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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidarNoAfines;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidarNoAfines;

/** test controlador validar no afines .
 * @author ATISoluciones
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorValidarNoAfines {
	
	private static final String MENSAJE_BOLSAS_DEVUELTAS = "Debe devolver bolsas";
	private static final String MENSAJE_BOLSA_DEVUELTA = "Debe devolver bolsa";
	private static final String MENSAJE_CANDIDATOS_DEVUELTOS = "Debe devolver candidatos";
	private static final String MENSAJE_CANDIDATO_DEVUELTO = "Debe devolver candidato";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_ERROR_ESPERADO = "El mensaje de error debe coincidir";
	private static final String MENSAJE_MERITOS_DEVUELTOS = "Debe devolver méritos";
	private static final String MENSAJE_MERITO_DEVUELTO = "Debe devolver mérito";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
	private static final String OBSERVACIONES = "observaciones";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd .
     * @throws IOException si error en io .
     */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
    
	// método para obtener la vista con una lista de áreas .
	private VistaValidarNoAfines obtenerAreas() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_DATATABLE_BOLSAS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "5");
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_DESC);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);
		return (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());
	}
    
    // método para obtener la vista con una lista de candidatos .
	private VistaValidarNoAfines obtenerCandidatos() throws ServletException, IOException {
		VistaValidarNoAfines bean = obtenerAreas();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_DATATABLE_CANDIDATOS);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getDatatableBolsas().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);
		return (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());
	}
    
	// método para obtener la vista con una lista de méritos .
	private VistaValidarNoAfines obtenerMeritos() throws ServletException, IOException {
		VistaValidarNoAfines bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_DATATABLE_MERITOS);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);
		return (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());
	}
    
    /** Obtener áreas, sin parametro definido .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doGet(peticion, respuesta);

		VistaValidarNoAfines bean = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable áreas .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA02ObtenerAreas() throws SQLException, ServletException, IOException {
		VistaValidarNoAfines bean = obtenerAreas();
		
		assertNotEquals(MENSAJE_BOLSAS_DEVUELTAS, 0, bean.getDatatableBolsas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA03SeleccionarArea() throws SQLException, ServletException, IOException {
		VistaValidarNoAfines bean = obtenerAreas();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_BOLSA_SELECCIONADA);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA,
				bean.getDatatableBolsas().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertNotNull(MENSAJE_BOLSA_DEVUELTA, bean2.getBolsa());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable candidatos .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA04ObtenerCandidatos() throws ServletException, IOException {
		VistaValidarNoAfines bean = obtenerCandidatos();
		
		assertNotEquals(MENSAJE_CANDIDATOS_DEVUELTOS, 0, bean.getDatatableCandidatos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar candidato .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA05SeleccionarCandidato() throws ServletException, IOException {
		VistaValidarNoAfines bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_CANDIDATO_SELECCIONADO);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_CANDIDATO,
				bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable méritos .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA06ObtenerMeritos() throws ServletException, IOException {
		VistaValidarNoAfines bean = obtenerMeritos();
		
		assertNotEquals(MENSAJE_MERITOS_DEVUELTOS, 0, bean.getDatatableMeritos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar mérito .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA07SeleccionarMerito() throws ServletException, IOException {
		VistaValidarNoAfines bean = obtenerMeritos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_MERITO_SELECCIONADO);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertNotNull(MENSAJE_MERITO_DEVUELTO, bean2.getMerito());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable bolsas en las que está apuntado actualmente el candidato .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA08ObtenerBolsasCandidato() throws ServletException, IOException {
		VistaValidarNoAfines bean = obtenerMeritos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_DATATABLE_BOLSAS_CANDIDATO);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertNotEquals(MENSAJE_BOLSAS_DEVUELTAS, bean2.getDatatableBolsasCandidato().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable de valores actuales de un mérito en distintas bolsas a las que se ha apuntado el candidato .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA09ObtenerValoresMeritoBolsas() throws ServletException, IOException {
		VistaValidarNoAfines bean = obtenerMeritos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_DATATABLE_VALORES_MERITO_BOLSA);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertNotEquals(MENSAJE_MERITOS_DEVUELTOS, 0, bean2.getDatatableValoresMeritoBolsa().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Mérito aceptado .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA10AceptarMerito() throws ServletException, IOException {
		VistaValidarNoAfines bean = obtenerMeritos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_VALIDAR_MERITO);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACEPTAR_MERITO, "");
		peticion.setParameter(ControladorValidarNoAfines.PARAM_OBSERVACION_CANDIDATO, OBSERVACIONES);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSAS,
				"[" + bean.getBolsa().getCodNum().toString() + "]");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertNotNull(MENSAJE_MERITO_DEVUELTO, bean2.getMerito());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Mérito excluido .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA11ExcluirMerito() throws ServletException, IOException {
		VistaValidarNoAfines bean = obtenerMeritos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_VALIDAR_MERITO);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_EXCLUIR_MERITO, "");
		peticion.setParameter(ControladorValidarNoAfines.PARAM_OBSERVACION_CANDIDATO, OBSERVACIONES);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSAS, "[" + bean.getBolsa().getCodNum().toString() + "]");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertNotNull(MENSAJE_MERITO_DEVUELTO, bean2.getMerito());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** acción no válida .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE01AccionNoValida() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, "accionnovalida");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doGet(peticion, respuesta);

		VistaValidarNoAfines bean = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorValidarNoAfines.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	/** Obtener datatable áreas con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE02ObtenerAreasParametroNoValido() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_DATATABLE_BOLSAS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);
		
		VistaValidarNoAfines bean = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Obtener datatable candidatos con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE03ObtenerCandidatosParametroNoValido() throws SQLException, ServletException, IOException {
		VistaValidarNoAfines bean = obtenerAreas();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_DATATABLE_CANDIDATOS);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getDatatableBolsas().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Obtener datatable méritos con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE03ObtenerMeritosParametroNoValido() throws SQLException, ServletException, IOException {
		VistaValidarNoAfines bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_DATATABLE_MERITOS);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Obtener datatable bolsas candidato con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE04ObtenerBolsasCandidatoParametroNoValido() throws SQLException, ServletException, IOException {
		VistaValidarNoAfines bean = obtenerMeritos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_DATATABLE_BOLSAS_CANDIDATO);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Obtener datatable bolsas candidato con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE05ObtenerValoresMeritoBolsasParametroNoValido() throws SQLException, ServletException, IOException {
		VistaValidarNoAfines bean = obtenerMeritos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_DATATABLE_VALORES_MERITO_BOLSA);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Mérito aceptado con parámetro bolsas a 0 .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testE06AceptarMeritoSinBolsas() throws ServletException, IOException {
		VistaValidarNoAfines bean = obtenerMeritos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACCION, ControladorValidarNoAfines.ACCION_VALIDAR_MERITO);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_ACEPTAR_MERITO, "");
		peticion.setParameter(ControladorValidarNoAfines.PARAM_OBSERVACION_CANDIDATO, OBSERVACIONES);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSAS, "[" + "]");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidarNoAfines controlador = new ControladorValidarNoAfines();
		controlador.doPost(peticion, respuesta);

		VistaValidarNoAfines bean2 = (VistaValidarNoAfines) peticion.getUVDatos().getVistas().get(VistaValidarNoAfines.class.getName());

		assertNotNull(MENSAJE_MERITO_DEVUELTO, bean2.getMerito());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
}
