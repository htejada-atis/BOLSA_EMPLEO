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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.candidato.ControladorPlazasOfertadas;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaPlazasOfertadas;
import es.ujaen.uvirtual.utilidades.UVException;

/** test controlador plazas ofertadas .
 * @author ATISoluciones
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorPlazasOfertadas {
	
	private static final String MENSAJE_PLAZAS_OFERTADAS_DEVUELTAS = "Debe devolver plazas ofertadas";
	private static final String MENSAJE_PLAZA_OFERTADA_DEVUELTA = "Debe devolver plaza ofertada";
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
	
	// método para obtener la vista con una lista de plazas ofertadas .
	private VistaPlazasOfertadas obtenerBolsas() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorPlazasOfertadas.PARAM_ACCION, ControladorPlazasOfertadas.ACCION_DATATABLE_PLAZAS_OFERTADAS);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlazasOfertadas controlador = new ControladorPlazasOfertadas();
		controlador.doGet(peticion, respuesta);
		
		return (VistaPlazasOfertadas) peticion.getUVDatos().getVistas().get(VistaPlazasOfertadas.class.getName());
	}
	
	/** Obtener sin parametro definido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA01() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlazasOfertadas controlador = new ControladorPlazasOfertadas();
		controlador.doGet(peticion, respuesta);
		
		VistaPlazasOfertadas bean = (VistaPlazasOfertadas) peticion.getUVDatos().getVistas().get(VistaPlazasOfertadas.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Acción index .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA02() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorPlazasOfertadas.PARAM_ACCION, ControladorPlazasOfertadas.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlazasOfertadas controlador = new ControladorPlazasOfertadas();
		controlador.doGet(peticion, respuesta);
		
		VistaPlazasOfertadas bean = (VistaPlazasOfertadas) peticion.getUVDatos().getVistas().get(VistaPlazasOfertadas.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable plazas ofertadas .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA03ObtenerPlazasOfertadas() throws IOException {
		VistaPlazasOfertadas bean = obtenerBolsas();
		
		assertNotEquals(MENSAJE_PLAZAS_OFERTADAS_DEVUELTAS, 0, bean.getDatatableOfertasCandidatos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** acción no válida .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE01AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorPlazasOfertadas.PARAM_ACCION, "accionnovalida");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlazasOfertadas controlador = new ControladorPlazasOfertadas();
		controlador.doGet(peticion, respuesta);

		VistaPlazasOfertadas bean = (VistaPlazasOfertadas) peticion.getUVDatos().getVistas().get(VistaPlazasOfertadas.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorPlazasOfertadas.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	/** Error de permiso .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE02errorUsuarioRol() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorPlazasOfertadas.PARAM_ACCION, ControladorPlazasOfertadas.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlazasOfertadas controlador = new ControladorPlazasOfertadas();
		controlador.doPost(peticion, respuesta);
		
		VistaPlazasOfertadas bean = (VistaPlazasOfertadas) peticion.getUVDatos().getVistas().get(VistaPlazasOfertadas.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(ControladorPlazasOfertadas.MENSAJE_ERROR_SIN_PERMISO, bean.getMensajesDeError().get(0).toString());
	}
	
	/** Obtener datatable plazas ofertadas parámetro no válido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE03ObtenerBolsasParametroNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorPlazasOfertadas.PARAM_ACCION, ControladorPlazasOfertadas.ACCION_DATATABLE_PLAZAS_OFERTADAS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorPlazasOfertadas controlador = new ControladorPlazasOfertadas();
		controlador.doGet(peticion, respuesta);
		
		VistaPlazasOfertadas bean = (VistaPlazasOfertadas) peticion.getUVDatos().getVistas().get(VistaPlazasOfertadas.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
}
