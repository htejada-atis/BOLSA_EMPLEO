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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorFiltrarAcreditaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaFiltrarAcreditaciones;

/**
 * test controlador filtrar.
 * 
 * @author ATISoluciones 2021
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorFiltrarAcreditaciones {

	private static final String MENSAJE_ACREDITACIONES_DEVUELTAS = "Debe devolver acreditaciones";
	private static final String MENSAJE_ACREDITACION_DEVUELTA = "Debe devolver acreditación";
	private static final String MENSAJE_CANDIDATO_DEVUELTO = "Debe devolver candidato";
	private static final String MENSAJE_CANDIDATOS_DEVUELTOS = "Debe devolver candida";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_ERROR_ESPERADO = "El mensaje de error debe coincidir";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";

	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException si error en bd
	 * @throws IOException  si error en io
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}

	// método para obtener la vista con una lista de candidatos .
	private VistaFiltrarAcreditaciones obtenerCandidatos() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACCION, ControladorFiltrarAcreditaciones.ACCION_DATATABLE_CANDIDATOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doPost(peticion, respuesta);
		return (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());
	}

	// método para obtener la vista con una lista de acreditaciones de un candidato
	// .
	private VistaFiltrarAcreditaciones obtenerAcreditaciones() throws IOException {
		VistaFiltrarAcreditaciones bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACCION, ControladorFiltrarAcreditaciones.ACCION_DATATABLE_ACREDITACIONES_CANDIDATO);
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doPost(peticion, respuesta);
		return (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());
	}

	/** Obtener candidatos, sin parametro definido .
	 * @throws IOException      si error io
	 */
	@Test
	public void testA01() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doGet(peticion, respuesta);

		VistaFiltrarAcreditaciones bean = (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}

	/** Obtener candidatos, sin parametro definido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA02ObtenerCandidatos() throws IOException {
		VistaFiltrarAcreditaciones bean = obtenerCandidatos();

		assertNotEquals(MENSAJE_CANDIDATOS_DEVUELTOS, 0, bean.getDatatableCandidatos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}

	/** Seleccionar un candidato .
	 * @throws IOException      si error io .
	 */
	@Test
	public void testA03SeleccionarCandidato() throws IOException {
		VistaFiltrarAcreditaciones bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACCION, ControladorFiltrarAcreditaciones.ACCION_CANDIDATO_SELECCIONADO);
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doGet(peticion, respuesta);

		VistaFiltrarAcreditaciones bean2 = (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());

		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}

	/** Obtener las acreditaciones de un candidato .
	 * @throws IOException      si error io .
	 */
	@Test
	public void testA04ObtenerAcreditacionesCandidato() throws IOException {
		VistaFiltrarAcreditaciones bean = obtenerAcreditaciones();
		
		assertNotEquals(MENSAJE_ACREDITACIONES_DEVUELTAS, 0, bean.getDataTableAcreditaciones().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}

	/** Selecciona una acreditación para validarla .
	 * @throws IOException      si error io .
	 */
	@Test
	public void testA05SeleccionarAcreditacion() throws IOException {
		VistaFiltrarAcreditaciones bean = obtenerCandidatos();
		VistaFiltrarAcreditaciones bean2 = obtenerAcreditaciones();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACCION, ControladorFiltrarAcreditaciones.ACCION_ACREDITACION_SELECCIONADA);
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACREDITACION, bean2.getDataTableAcreditaciones().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doGet(peticion, respuesta);

		VistaFiltrarAcreditaciones bean3 = (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());

		assertNotNull(MENSAJE_ACREDITACION_DEVUELTA, bean3.getAcreditacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}

	/** Deselecciona una acreditación para desvalidarla .
	 * @throws IOException      si error io .
	 */
	@Test
	public void testA06DeseleccionarAcreditacion() throws IOException {
		VistaFiltrarAcreditaciones bean = obtenerCandidatos();
		VistaFiltrarAcreditaciones bean2 = obtenerAcreditaciones();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACCION, ControladorFiltrarAcreditaciones.ACCION_ACREDITACION_DESELECCIONADA);
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACREDITACION, bean2.getDataTableAcreditaciones().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doGet(peticion, respuesta);

		VistaFiltrarAcreditaciones bean3 = (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());

		assertNotNull(MENSAJE_ACREDITACION_DEVUELTA, bean3.getAcreditacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}

	/** Obtener datatable candidatos con parámetro no válido para forzar el error .
	 * @throws IOException      si error de io
	 */
	@Test
	public void testE01ObtenerCandidatosParametroNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACCION,
				ControladorFiltrarAcreditaciones.ACCION_DATATABLE_CANDIDATOS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doPost(peticion, respuesta);

		VistaFiltrarAcreditaciones bean = (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}

	/** Obtener datatable acreditaciones con parámetro no válido para forzar el error .
	 * @throws IOException      si error de io
	 */
	@Test
	public void testE02ObtenerAcreditacionesParametroNoValido() throws IOException {
		VistaFiltrarAcreditaciones bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACCION, ControladorFiltrarAcreditaciones.ACCION_DATATABLE_ACREDITACIONES_CANDIDATO);
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doPost(peticion, respuesta);

		VistaFiltrarAcreditaciones bean2 = (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}

	/** Seleccionar candidato no válido .
	 * @throws IOException      si error de io
	 */
	@Test
	public void testE03SeleccionarCandidatoNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACCION, ControladorFiltrarAcreditaciones.ACCION_CANDIDATO_SELECCIONADO);
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_CANDIDATO, "0");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doGet(peticion, respuesta);

		VistaFiltrarAcreditaciones bean = (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}

	/** Seleccionar acreditación no válida .
	 * @throws IOException      si error de io
	 */
	@Test
	public void testE04SeleccionarAcreditacionNoValida() throws IOException {
		VistaFiltrarAcreditaciones bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACCION, ControladorFiltrarAcreditaciones.ACCION_ACREDITACION_SELECCIONADA);
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACREDITACION, "0");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doGet(peticion, respuesta);

		VistaFiltrarAcreditaciones bean2 = (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}

	/** Seleccionar acreditación sin el parametro acreditación .
	 * @throws IOException      si error de io
	 */
	@Test
	public void testE05SeleccionarAcreditacionSinAcreditacion() throws IOException {
		VistaFiltrarAcreditaciones bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACCION, ControladorFiltrarAcreditaciones.ACCION_ACREDITACION_SELECCIONADA);
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doGet(peticion, respuesta);

		VistaFiltrarAcreditaciones bean2 = (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}

	/** acción no válida .
	 * @throws IOException      si error io .
	 */
	@Test
	public void testE06AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrarAcreditaciones.PARAM_ACCION, "accionnovalida");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrarAcreditaciones controlador = new ControladorFiltrarAcreditaciones();
		controlador.doGet(peticion, respuesta);

		VistaFiltrarAcreditaciones bean = (VistaFiltrarAcreditaciones) peticion.getUVDatos().getVistas().get(VistaFiltrarAcreditaciones.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorFiltrarAcreditaciones.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
}
