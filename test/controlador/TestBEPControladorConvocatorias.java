package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorConvocatorias;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaConvocatorias;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;
import unitarios.TestBEPModeloConvocatoria;

/** test controlador contratación.
 * @author jlopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorConvocatorias {
	
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_ERROR_ESPERADO = "El mensaje de error debe coincidir";
	private static final String MENSAJE_CONVOCATORIAS_DEVUELTAS = "Debe devolver convocatorias";
	private static final String MENSAJE_CONVOCATORIA_DEVUELTA = "Debe devolver convocatoria";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
	/** Prepara la bd con los datos iniciales .
	 * @throws SQLException si error en bd .
	 * @throws IOException si error en ficheros .
	 * @throws UVException .
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
	
	private VistaConvocatorias obtenerConvocatorias(String filter) throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, ControladorConvocatorias.ACCION_DATATABLE);
		
		if (filter != null) {
			peticion.setParameter(BolsaEmpleoDataTable.PARAM_FILTER, filter);
		}
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		return (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
	}
	
	/** Obtener convocatorias, sin parametro definido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA01Obtener() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		VistaConvocatorias bean = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Acción index .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA02Obtener() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, ControladorConvocatorias.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		VistaConvocatorias bean = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable convocatorias .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA03ObtenerDataTableConvocatorias() throws IOException {
		VistaConvocatorias bean = obtenerConvocatorias(null);
		
		assertNotEquals(MENSAJE_CONVOCATORIAS_DEVUELTAS, 0, bean.getDatatableConvocatorias().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar convocatoria .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA04SeleccionarConvocatoria() throws IOException {
		VistaConvocatorias bean = obtenerConvocatorias("{" + "'" + ModeloConvocatoria.ORDER_COLUMN_INDEX_ESTADO + "':'" 
				+ ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA + "'}");
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, ControladorConvocatorias.ACCION_SELECCIONAR_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatorias.PARAM_CONVOCATORIA_ID, bean.getDatatableConvocatorias().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		VistaConvocatorias bean2 = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertNotNull(MENSAJE_CONVOCATORIA_DEVUELTA, bean2.getConvocatoria());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** acción no válida .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE01AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, "accionnovalida");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		VistaConvocatorias bean = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorConvocatorias.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	/** Error de permiso .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE02errorUsuarioRol() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, ControladorConvocatorias.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doPost(peticion, respuesta);
		
		VistaConvocatorias bean = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(ControladorConvocatorias.MENSAJE_ERROR_SIN_PERMISO_PERSONAL, bean.getMensajesDeError().get(0).toString());
	}
	
	/** obtener datatable convocatorias con parámetro erróneo de la tabla para forzar error .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE03ObtenerConvocatoriasParametroErroneo() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, ControladorConvocatorias.ACCION_DATATABLE);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		VistaConvocatorias bean = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Nueva convocatoria error existe convocatoria no finalizada .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE04NuevaConvocatoriaError() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, ControladorConvocatorias.ACCION_AGREGAR_CONVOCATORIA);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		VistaConvocatorias bean = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(ControladorConvocatorias.MENSAJE_ERROR_EXISTE_CONVOCATORIA_NO_FINALIZADA, bean.getMensajesDeError().get(0).toString());
	}
	
	/** Nueva convocatoria error existe convocatoria no finalizada .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE05FinalizaConvocatoriaError() throws IOException {
		VistaConvocatorias bean = obtenerConvocatorias("{" + "'" + ModeloConvocatoria.ORDER_COLUMN_INDEX_ESTADO + "':'" 
				+ ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA + "'}");
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, ControladorConvocatorias.ACCION_FINALIZAR_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatorias.PARAM_CONVOCATORIA_ID, bean.getDatatableConvocatorias().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		VistaConvocatorias bean2 = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
		assertEquals(ControladorConvocatorias.MENSAJE_ERROR_ESTADO_CERRADA_REQUERIDO, bean2.getMensajesDeError().get(0).toString());
	}
	
	/** Inserta una nueva convocatoria y la edita .
	 * @throws IOException si error io .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	@Test
	@SuppressWarnings({"checkstyle:magicnumber"})
	public void testI01EditarNuevaConvocatoria() throws IOException, SQLException, UVException {
		TestBEPModeloConvocatoria testModelo = new TestBEPModeloConvocatoria();
		testModelo.testA01InsertaConvocatoria();
		
		Convocatoria convocatoria = obtenerConvocatorias("{" + "'" + ModeloConvocatoria.ORDER_COLUMN_INDEX_ID + "':'3'}").getDatatableConvocatorias().getData().get(0);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, ControladorConvocatorias.ACCION_MODIFICAR_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatorias.PARAM_CONVOCATORIA_ID, convocatoria.getCodNum().toString());
		peticion.setParameter(ControladorConvocatorias.PARAM_CONVOCATORIA_DESCRIPCION, "test");
		peticion.setParameter(ControladorConvocatorias.PARAM_CONVOCATORIA_FECHACIERRE,
				Formateador.formatoFecha(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)), Formateador.FORMATO_FECHA_DDMMYYYY));
		peticion.setParameter(ControladorConvocatorias.PARAM_CONVOCATORIA_NUMEROBOLSASMAXIMO, convocatoria.getNumBolsasMaximo().toString());
		peticion.setParameter(ControladorConvocatorias.PARAM_CONVOCATORIA_NUMEROMERITOSPORBLOQUE, convocatoria.getNumMeritosPorBloque().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		VistaConvocatorias bean2 = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Abrir convocatoria .
	 * @throws IOException si error io .
	 */
	@Test
	public void testI02AbrirConvocatoria() throws IOException {
		VistaConvocatorias bean = obtenerConvocatorias("{" + "'" + ModeloConvocatoria.ORDER_COLUMN_INDEX_ID + "':'3'}");
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, ControladorConvocatorias.ACCION_ABRIR_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatorias.PARAM_CONVOCATORIA_ID, bean.getDatatableConvocatorias().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		VistaConvocatorias bean2 = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertNotNull(MENSAJE_CONVOCATORIA_DEVUELTA, bean2.getConvocatoria());
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
		assertEquals(ControladorConvocatorias.MENSAJE_ERROR_BOLSAS_BLOQUEADAS, bean2.getMensajesDeError().get(0).toString());
	}
	
	/** Cerrar convocatoria .
	 * @throws IOException si error io .
	 */
	@Test
	public void testI03CerrarConvocatoria() throws IOException {
		VistaConvocatorias bean = obtenerConvocatorias("{" + "'" + ModeloConvocatoria.ORDER_COLUMN_INDEX_ID + "':'3'}");
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, ControladorConvocatorias.ACCION_CERRAR_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatorias.PARAM_CONVOCATORIA_ID, bean.getDatatableConvocatorias().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		VistaConvocatorias bean2 = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertNotNull(MENSAJE_CONVOCATORIA_DEVUELTA, bean2.getConvocatoria());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Borrar convocatoria .
	 * @throws IOException si error io .
	 */
	@Test
	public void testI04BorrarConvocatoria() throws IOException {
		VistaConvocatorias bean = obtenerConvocatorias("{" + "'" + ModeloConvocatoria.ORDER_COLUMN_INDEX_ESTADO + "':'" 
				+ ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA + "'}");
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorConvocatorias.PARAM_ACCION, ControladorConvocatorias.ACCION_BORRAR_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatorias.PARAM_CONVOCATORIA_ID, bean.getDatatableConvocatorias().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatorias controlador = new ControladorConvocatorias();
		controlador.doGet(peticion, respuesta);
		
		VistaConvocatorias bean2 = (VistaConvocatorias) peticion.getUVDatos().getVistas().get(VistaConvocatorias.class.getName());
		
		assertNotNull(MENSAJE_CONVOCATORIA_DEVUELTA, bean2.getConvocatoria());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
}