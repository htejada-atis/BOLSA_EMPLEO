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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorContratacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloPlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloTitulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaContratacion;
import es.ujaen.uvirtual.utilidades.UVException;

/** test controlador contratación.
 * @author jlopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorContratacion {
	
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_ERROR_ESPERADO = "El mensaje de error debe coincidir";
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de exito debe coincidir";
	private static final String MENSAJE_CANDIDATOS_DEVUELTOS = "Debe devolver candidatos";
	private static final String MENSAJE_PLAZAS_OFERTADAS_DEVUELTAS = "Debe devolver plazas ofertadas";
	private static final String MENSAJE_PLAZA_OFERTADA_DEVUELTA = "Debe devolver plaza ofertada";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
	/** Prepara la bd con los datos iniciales.
	 * @throws SQLException si error en bd
	 * @throws IOException si error en ficheros
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
	
	private VistaContratacion obtenerPlazasOfertadas(String filter) throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_DATATABLE_PLAZAS_OFERTADAS);
		
		if (filter != null) {
			peticion.setParameter(BolsaEmpleoDataTable.PARAM_FILTER, filter);
		}
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		return (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
	}
	
	private VistaContratacion obtenerCandidatos() throws IOException {
		VistaContratacion bean = obtenerPlazasOfertadas("{" + "'" + ModeloPlazaOfertada.ORDER_COLUMN_INDEX_ESTADO + "':'" 
				+ ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "'}");
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_DATATABLE_CANDIDATOS);
		peticion.setParameter(ControladorContratacion.PARAM_PLAZA_OFERTADA, bean.getDatatablePlazasOfertadas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		return (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
	}
	
	/** Obtener plazas ofertadas, sin parametro definido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA01Obtener() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		VistaContratacion bean = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Acción index .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA02Obtener() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		VistaContratacion bean = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable plazas ofertadas .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA03ObtenerDataTablePlazasOfertadas() throws IOException {
		VistaContratacion bean = obtenerPlazasOfertadas(null);
		
		assertNotEquals(MENSAJE_PLAZAS_OFERTADAS_DEVUELTAS, 0, bean.getDatatablePlazasOfertadas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Agregar una plaza ofertada .
	 * @throws IOException si error io .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	@Test
	public void testA04AgregarPlazaOfertada() throws IOException, SQLException, UVException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_NUEVA_PLAZA_OFERTADA);
		peticion.setParameter(ControladorContratacion.PARAM_AREA, ModeloBolsa.obtenerInstancia().getBolsaById(2).getArea().getCodNum().toString());
		peticion.setParameter(ControladorContratacion.PARAM_DEDICACION, "1");
		peticion.setParameter(ControladorContratacion.PARAM_JUSTIFICACION, "JUSTIFICACION");
		peticion.setParameter(ControladorContratacion.PARAM_CENTRO_DESTINO, ModeloPlazaOfertada.CENTRO_DESTINO_JAEN);
		peticion.setParameter(ControladorContratacion.PARAM_CUATRIMESTRE, ModeloPlazaOfertada.CUATRIMESTRE_TODO_EL_CURSO);
		peticion.setParameter(ControladorContratacion.PARAM_DURACION_PREVISTA, "DURACION PREVISTA");
		peticion.setParameter(ControladorContratacion.PARAM_FECHA_FIN_OFERTA, "12/08/2021 23:59:59");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		VistaContratacion bean = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorContratacion.MENSAJE_EXITO_AGREGAR, bean.getMensajesDeExito().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Editar una plaza ofertada .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA05EditarPlazaOfertada() throws IOException {
		VistaContratacion bean = obtenerPlazasOfertadas(null);
		
		PlazaOfertada plaza = bean.getDatatablePlazasOfertadas().getData().get(0);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_EDITAR_PLAZA_OFERTADA);
		peticion.setParameter(ControladorContratacion.PARAM_PLAZA_OFERTADA, plaza.getCodNum().toString());
		peticion.setParameter(ControladorContratacion.PARAM_AREA, plaza.getArea().getCodNum().toString());
		peticion.setParameter(ControladorContratacion.PARAM_JUSTIFICACION, plaza.getJustificacion());
		peticion.setParameter(ControladorContratacion.PARAM_CENTRO_DESTINO, plaza.getCentroDestino());
		peticion.setParameter(ControladorContratacion.PARAM_CUATRIMESTRE, ModeloPlazaOfertada.CUATRIMESTRE_TODO_EL_CURSO);
		peticion.setParameter(ControladorContratacion.PARAM_DURACION_PREVISTA, plaza.getDuracionPrevista());
		peticion.setParameter(ControladorContratacion.PARAM_FECHA_FIN_OFERTA, "12/08/2021 23:59:59");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		VistaContratacion bean2 = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertNotNull(MENSAJE_PLAZA_OFERTADA_DEVUELTA, bean2.getPlazaOfertada());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorContratacion.MENSAJE_EXITO_EDITAR, bean2.getMensajesDeExito().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar una plaza ofertada .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA06SeleccionarPlazaOfertada() throws IOException {
		VistaContratacion bean = obtenerPlazasOfertadas(null);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_SELECCIONAR_PLAZA_OFERTADA);
		peticion.setParameter(ControladorContratacion.PARAM_PLAZA_OFERTADA, bean.getDatatablePlazasOfertadas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		VistaContratacion bean2 = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertNotNull(MENSAJE_PLAZA_OFERTADA_DEVUELTA, bean2.getPlazaOfertada());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Abrir una plaza ofertada .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA07AbrirPlazaOfertada() throws IOException {
		VistaContratacion bean = obtenerPlazasOfertadas("{" + "'" + ModeloPlazaOfertada.ORDER_COLUMN_INDEX_ESTADO + "':'" 
				+ ModeloPlazaOfertada.PLAZA_ESTADO_TRAMITACION + "'}");
		
		PeticionHttp peticion2 = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion2.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_ABRIR_PLAZA);
		peticion2.setParameter(ControladorContratacion.PARAM_PLAZA_OFERTADA, bean.getDatatablePlazasOfertadas().getData().get(0).getCodNum().toString());
		peticion2.setParameter(ControladorContratacion.PARAM_FECHA_FIN_OFERTA, "12/08/2021 23:59:59");
		
		RespuestaHttp respuesta2 = new RespuestaHttp();
		ControladorContratacion controlador2 = new ControladorContratacion();
		controlador2.doGet(peticion2, respuesta2);
		
		VistaContratacion bean2 = (VistaContratacion) peticion2.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertNotNull(MENSAJE_PLAZA_OFERTADA_DEVUELTA, bean2.getPlazaOfertada());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorContratacion.MENSAJE_EXITO_ABRIR_PLAZA, bean2.getMensajesDeExito().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Datatable de candidatos que han aceptado una oferta .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA08ObtenerDatatableCandidatos() throws IOException {
		VistaContratacion bean = obtenerCandidatos();
		
		assertNotEquals(MENSAJE_CANDIDATOS_DEVUELTOS, 0, bean.getDatatableCandidatos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** acción no válida .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE01AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, "accionnovalida");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);

		VistaContratacion bean = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorContratacion.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	/** Error de permiso .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE02errorUsuarioRol() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doPost(peticion, respuesta);
		
		VistaContratacion bean = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(ControladorContratacion.MENSAJE_ERROR_SIN_PERMISO, bean.getMensajesDeError().get(0).toString());
	}
	
	/** obtener datatable plazas ofertadas con parámetro erróneo de la tabla para forzar error .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE03ObtenerPlazasOfertadasParametroErroneo() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_DATATABLE_PLAZAS_OFERTADAS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
		
		VistaContratacion bean = (VistaContratacion) peticion.getUVDatos().getVistas().get(VistaContratacion.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** obtener datatable candidatos con parámetro erróneo de la tabla para forzar error .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE04ObtenerCandidatosParametroErroneo() throws IOException {
		VistaContratacion bean = obtenerPlazasOfertadas("{" + "'" + ModeloPlazaOfertada.ORDER_COLUMN_INDEX_ESTADO + "':'" 
				+ ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "'}");
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorContratacion.PARAM_ACCION, ControladorContratacion.ACCION_DATATABLE_CANDIDATOS);
		peticion.setParameter(ControladorContratacion.PARAM_PLAZA_OFERTADA, bean.getDatatablePlazasOfertadas().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorContratacion controlador = new ControladorContratacion();
		controlador.doGet(peticion, respuesta);
	}
	
}