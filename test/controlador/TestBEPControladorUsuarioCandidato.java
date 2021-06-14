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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatos;

/**
 * Test controlador usuarios candidatos bolsa empleo.
 * 
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorUsuarioCandidato {
	
	private static final String MENSAJE_ACREDITACIONES_DEVUELTAS = "Debe devolver acreditaciones";
	private static final String MENSAJE_AREAS_DEVUELTAS = "Debe devolver áreas";
	private static final String MENSAJE_CANDIDATOS_DEVUELTOS = "Debe devolver candidatos";
	private static final String MENSAJE_CANDIDATO_DEVUELTO = "Debe devolver candidato";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_ERROR_ESPERADO = "El mensaje de error debe coincidir";
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de exito debe coincidir";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SOLICITUDES_DEVUELTAS = "Debe devolver solicitudes";
	private static final String MENSAJE_SOLICITUD_DEVUELTA = "Debe devolver solicitud";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	private static final String MENSAJE_TITULACIONES_DEVUELTAS = "Debe devolver titulaciones";
	
	private static final String EXCLUIDO_CANDIDATO = "true";
	private static final String EXCLUIDO_TIPO_INDEFINIDO = "I";
	private static final String EXCLUIDO_TIPO_TEMPORAL = "T";
	private static final String EXCLUIDO_FECHA_INICIO = "11/06/2021";
	private static final String EXCLUIDO_FECHA_FIN = "11/07/2021";
	private static final String LISTA_CANDIDATO = "true";
	private static final String RAZON_EXCLUSION_CANDIDATO = "test";
	
	
	/** prepara la bd con los datos iniciales .
	 * @throws SQLException si error en bd .
	 * @throws IOException  si error en io .
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
    
	// método para obtener la vista con una lista de candidatos .
	private VistaCandidatos obtenerCandidatos() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_USUARIOS_CANDIDATOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		return (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
	}
	
	// método para obtener la vista con una lista de áreas excluidas o no excluidas .
	private VistaCandidatos obtenerAreasExcluidas(boolean excluidas) throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		if (excluidas) {
			peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_AREAS_EXCLUIDAS_CANDIDATO);
		} else {
			peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_AREAS_NO_EXCLUIDAS_CANDIDATO);
		}
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		return (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
	}
	
	// método para obtener la vista con una lista de áreas excluidas o no excluidas .
	private VistaCandidatos obtenerSolicitudesCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_SOLICITUDES);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		return (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
	}
    
    /** Obtener candidatos, sin parametro definido .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA01Obtener() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doGet(peticion, respuesta);
		
		VistaCandidatos bean = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable candidatos .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA02ObtenerCandidatos() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		assertNotEquals(MENSAJE_CANDIDATOS_DEVUELTOS, 0, bean.getDatatableCandidatos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA03SeleccionarCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_SELECCIONAR_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Acción areas excluidas candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA04AreasExcluidasCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_AREAS_EXCLUIDAS_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(true, bean2.getApartadoAreasExcluidas());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Acción solicitudes candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA05SolicitudesCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_SOLICITUDES_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(true, bean2.getApartadoSolicitudes());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable áreas no excluidas candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA06ObtenerAreasNoExcluidasCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerAreasExcluidas(false);
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getDatatableAreas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Excluir área para un candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA07ExcluirAreaCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerAreasExcluidas(false);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_EXCLUIR_USUARIO_AREA);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_AREAS_SELECCIONADAS, "[" + bean.getDatatableAreas().getData().get(0).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean2.getListaAreas().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, String.format(ControladorUsuarioCandidato.MENSAJE_EXITO_AREA_EXCLUIDA, bean2.getCandidato().getCodCuenta()),
				bean2.getMensajesDeExito().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable áreas excluidas candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA08ObtenerAreasExcluidasCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerAreasExcluidas(true);
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getDatatableAreas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Borra área excluida para un candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA09IncluirAreaCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerAreasExcluidas(true);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_INCLUIR_USUARIO_AREA);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_AREAS_SELECCIONADAS, "[" + bean.getDatatableAreas().getData().get(0).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean2.getListaAreas().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, String.format(ControladorUsuarioCandidato.MENSAJE_EXITO_AREA_INCLUIDA, bean2.getCandidato().getCodCuenta()),
				bean2.getMensajesDeExito().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable solicitudes de un candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA10ObtenerSolicitudes() throws ServletException, IOException {
		VistaCandidatos bean = obtenerSolicitudesCandidato();
		
		assertNotEquals(MENSAJE_SOLICITUDES_DEVUELTAS, 0, bean.getDataTableSolicitudes().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar solicitud .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA11SeleccionarSolicitud() throws ServletException, IOException {
		VistaCandidatos bean = obtenerSolicitudesCandidato();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_SELECCIONAR_SOLICITUD);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_SOLICITUD, bean.getDataTableSolicitudes().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertNotNull(MENSAJE_SOLICITUD_DEVUELTA, bean2.getSolicitud());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Guardar datos del candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA12EditarCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_EDITAR_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_LISTA, LISTA_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_GUARDAR, "");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorUsuarioCandidato.MENSAJE_EXITO_EDITAR, bean2.getMensajesDeExito().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Guardar datos del candidato con excluido a true tipo indefinido .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA13ExcluirIndefinidoCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_EDITAR_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_EXCLUIDO, EXCLUIDO_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_EXCLUIDO_TIPO, EXCLUIDO_TIPO_INDEFINIDO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_RAZON_EXCLUIDO, RAZON_EXCLUSION_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_GUARDAR, "");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorUsuarioCandidato.MENSAJE_EXITO_EDITAR, bean2.getMensajesDeExito().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Guardar datos del candidato con excluido a true tipo temporal .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA14ExcluirTemporalCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_EDITAR_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_EXCLUIDO, EXCLUIDO_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_EXCLUIDO_TIPO, EXCLUIDO_TIPO_TEMPORAL);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_FECHA_EXCLUIDO_INICIO, EXCLUIDO_FECHA_INICIO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_FECHA_EXCLUIDO_FIN, EXCLUIDO_FECHA_FIN);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_RAZON_EXCLUIDO, RAZON_EXCLUSION_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_GUARDAR, "");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorUsuarioCandidato.MENSAJE_EXITO_EDITAR, bean2.getMensajesDeExito().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Guardar datos del candidato con excluido a false .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA15IncluirCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_EDITAR_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_EXCLUIDO, null);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_GUARDAR, "");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorUsuarioCandidato.MENSAJE_EXITO_EDITAR, bean2.getMensajesDeExito().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Eliminar candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA16EliminarCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_ELIMINAR_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorUsuarioCandidato.MENSAJE_EXITO_ELIMINAR, bean2.getMensajesDeExito().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Recuperar candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA17RestaurarCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_RECUPERAR_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorUsuarioCandidato.MENSAJE_EXITO_RESTAURAR, bean2.getMensajesDeExito().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener titulaciones .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA18ObtenerTitulaciones() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_TITULACIONES_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertNotEquals(MENSAJE_TITULACIONES_DEVUELTAS, 0, bean2.getDataTableTitulaciones().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener acreditaciones .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA19ObtenerAcreditaciones() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_ACREDITACIONES_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertNotEquals(MENSAJE_ACREDITACIONES_DEVUELTAS, 0, bean2.getDataTableAcreditaciones().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Acción titulaciones candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA20TitulacionesCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_TITULACIONES_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(true, bean2.getApartadoTitulaciones());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Acción acreditaciones candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA21AcreditacionesCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_ACREDITACIONES_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(true, bean2.getApartadoAcreditaciones());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Acción volver candidato .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA22VolverCandidato() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_VOLVER_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable candidatos con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE01ObtenerCandidatosParametroNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_USUARIOS_CANDIDATOS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Obtener datatable áreas excluidas del candidato con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE02ObtenerAreasExcluidasParametroNoValido() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_AREAS_EXCLUIDAS_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Obtener datatable áreas no excluidas del candidato con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE03ObtenerAreasNoExcluidasParametroNoValido() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_AREAS_NO_EXCLUIDAS_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Obtener datatable solicitudes del candidato con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE04ObtenerSolicitudesCandidatoParametroNoValido() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_SOLICITUDES);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doPost(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** acción no válida .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE05AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, "accionnovalida");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doGet(peticion, respuesta);
		
		VistaCandidatos bean = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorUsuarioCandidato.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	/** Obtener titulaciones con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE06ObtenerTitulacionesParametroNoValido() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_TITULACIONES_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doGet(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Obtener acreditaciones con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE07ObtenerAcreditacionesParametroNoValido() throws ServletException, IOException {
		VistaCandidatos bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_ACREDITACIONES_CANDIDATO);
		peticion.setParameter(ControladorUsuarioCandidato.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doGet(peticion, respuesta);
		
		VistaCandidatos bean2 = (VistaCandidatos) peticion.getUVDatos().getVistas().get(VistaCandidatos.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
}
