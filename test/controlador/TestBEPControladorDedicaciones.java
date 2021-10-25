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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorDedicaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaDedicacion;


/** test controlador dedicaciones .
 * @author ATISoluciones
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorDedicaciones {
	
	private static final String MENSAJE_DEDICACIONES_DEVUELTAS = "Debe devolver dedicaciones";
	private static final String MENSAJE_DEDICACION_DEVUELTA = "Debe devolver dedicacion";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_ERROR_ESPERADO = "El mensaje de error debe coincidir";
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de exito debe coincidir";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd .
     * @throws IOException si error en io .
     */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
	
	// método para obtener la vista con una lista de dedicaciones .
	private VistaDedicacion obtenerDedicaciones() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorDedicaciones.PARAM_ACCION, ControladorDedicaciones.ACCION_DATATABLE_DEDICACIONES);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorDedicaciones controlador = new ControladorDedicaciones();
		controlador.doGet(peticion, respuesta);
		
		return (VistaDedicacion) peticion.getUVDatos().getVistas().get(VistaDedicacion.class.getName());
	}
	
	/** Obtener sin parametro definido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA01() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorDedicaciones controlador = new ControladorDedicaciones();
		controlador.doGet(peticion, respuesta);
		
		VistaDedicacion bean = (VistaDedicacion) peticion.getUVDatos().getVistas().get(VistaDedicacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Acción index .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA02() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorDedicaciones.PARAM_ACCION, ControladorDedicaciones.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorDedicaciones controlador = new ControladorDedicaciones();
		controlador.doGet(peticion, respuesta);
		
		VistaDedicacion bean = (VistaDedicacion) peticion.getUVDatos().getVistas().get(VistaDedicacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable dedicaciones .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA03ObtenerDedicaciones() throws IOException {
		VistaDedicacion bean = obtenerDedicaciones();
		
		assertNotEquals(MENSAJE_DEDICACIONES_DEVUELTAS, 0, bean.getDatatableDedicaciones().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Agregar dedicación .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA04AgregarDedicacion() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorDedicaciones.PARAM_ACCION, ControladorDedicaciones.ACCION_NUEVA_DEDICACION);
		peticion.setParameter(ControladorDedicaciones.PARAM_TEXTO_DEDICACION, "Tiempo parcial (5 horas)");
		peticion.setParameter(ControladorDedicaciones.PARAM_SUELDO, "5000");
		peticion.setParameter(ControladorDedicaciones.PARAM_ENVIAR, "true");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorDedicaciones controlador = new ControladorDedicaciones();
		controlador.doGet(peticion, respuesta);
		
		VistaDedicacion bean = (VistaDedicacion) peticion.getUVDatos().getVistas().get(VistaDedicacion.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorDedicaciones.MENSAJE_EXITO_AGREGAR, bean.getMensajesDeExito().get(0));
	}
	
	/** Agregar dedicación .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA05EditarDedicacion() throws IOException {
		VistaDedicacion bean = obtenerDedicaciones();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorDedicaciones.PARAM_ACCION, ControladorDedicaciones.ACCION_EDITAR_DEDICACION);
		peticion.setParameter(ControladorDedicaciones.PARAM_DEDICACION, 
				bean.getDatatableDedicaciones().getData().get(bean.getDatatableDedicaciones().getData().size() - 1).getCodNum().toString());
		peticion.setParameter(ControladorDedicaciones.PARAM_TEXTO_DEDICACION, "Tiempo parcial (4 horas)");
		peticion.setParameter(ControladorDedicaciones.PARAM_SUELDO, "5000");
		peticion.setParameter(ControladorDedicaciones.PARAM_ENVIAR, "true");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorDedicaciones controlador = new ControladorDedicaciones();
		controlador.doGet(peticion, respuesta);
		
		VistaDedicacion bean2 = (VistaDedicacion) peticion.getUVDatos().getVistas().get(VistaDedicacion.class.getName());
		
		assertNotNull(MENSAJE_DEDICACION_DEVUELTA, bean2.getDedicacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorDedicaciones.MENSAJE_EXITO_EDITAR, bean2.getMensajesDeExito().get(0));
	}
	
	/** Eliminar dedicación .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA06EliminarDedicacion() throws IOException {
		VistaDedicacion bean = obtenerDedicaciones();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorDedicaciones.PARAM_ACCION, ControladorDedicaciones.ACCION_ELIMINAR_DEDICACION);
		peticion.setParameter(ControladorDedicaciones.PARAM_DEDICACION, 
				bean.getDatatableDedicaciones().getData().get(bean.getDatatableDedicaciones().getData().size() - 1).getCodNum().toString());
		peticion.setParameter(ControladorDedicaciones.PARAM_ACTIVA, "false");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorDedicaciones controlador = new ControladorDedicaciones();
		controlador.doGet(peticion, respuesta);
		
		VistaDedicacion bean2 = (VistaDedicacion) peticion.getUVDatos().getVistas().get(VistaDedicacion.class.getName());
		
		assertNotNull(MENSAJE_DEDICACION_DEVUELTA, bean2.getDedicacion());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorDedicaciones.MENSAJE_EXITO_ELIMINAR, bean2.getMensajesDeExito().get(0));
	}
	
	/** acción no válida .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE01AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorDedicaciones.PARAM_ACCION, "accionnovalida");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorDedicaciones controlador = new ControladorDedicaciones();
		controlador.doGet(peticion, respuesta);

		VistaDedicacion bean = (VistaDedicacion) peticion.getUVDatos().getVistas().get(VistaDedicacion.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorDedicaciones.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	/** Error de permiso .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE02errorUsuarioRol() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorDedicaciones.PARAM_ACCION, ControladorDedicaciones.ACCION_INDEX);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorDedicaciones controlador = new ControladorDedicaciones();
		controlador.doPost(peticion, respuesta);
		
		VistaDedicacion bean = (VistaDedicacion) peticion.getUVDatos().getVistas().get(VistaDedicacion.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(ControladorDedicaciones.MENSAJE_ERROR_SIN_PERMISO, bean.getMensajesDeError().get(0).toString());
	}
	
	/** Obtener datatable dedicaciones parámetro no válido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE03ObtenerDedicacionesParametroNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorDedicaciones.PARAM_ACCION, ControladorDedicaciones.ACCION_DATATABLE_DEDICACIONES);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorDedicaciones controlador = new ControladorDedicaciones();
		controlador.doGet(peticion, respuesta);
		
		VistaDedicacion bean = (VistaDedicacion) peticion.getUVDatos().getVistas().get(VistaDedicacion.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
}