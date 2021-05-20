package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.UtilsTestDocentia;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.beans.uvirtual.docentia.Convocatoria;
import es.ujaen.uvirtual.beans.vistas.uvirtual.docentia.VistaConvocatoriaCRUD;
import es.ujaen.uvirtual.controlador.infadministrativa.docentia.ControladorConvocatoriaCRUD;
import es.ujaen.uvirtual.utilidades.UVException;

/** test controlador convocatoria crud.
 * @author jmoral
 *
 */
public class TestControladorConvocatoriaCrud {
	private static final String MENSAJE_CONVOCATORIAS_DEVUELTAS = "Debe devolver convocatorias";
	private static final String MENSAJE_CONVOCATORIA_DEVUELTA = "Debe devolver convocatoria";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de exito debe coincidir";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	private static final String NOMBRE_CONVOCATORIA = "convocatoria";
	private static final String OBSERVACIONES_CONVOCATORIA = "observaciones";
	private static final String FECHA_CORRECTA = "01/01/2030";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     * @throws UVException si error uv
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException, UVException {
    	UtilsTestDocentia.inicializaDocentia();
    }
    
    
    private VistaConvocatoriaCRUD obtenerConvocatorias() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ACCION, ControladorConvocatoriaCRUD.ACCION_OBTENER_CONVOCATORIAS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatoriaCRUD controlador = new ControladorConvocatoriaCRUD();
		controlador.doGet(peticion, respuesta);
		return (VistaConvocatoriaCRUD) peticion.getUVDatos().getVistas().get(VistaConvocatoriaCRUD.class.getName());
    }
    
	/** obtener convocatorias.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01Obtener() throws SQLException, ServletException, IOException {
		VistaConvocatoriaCRUD bean = obtenerConvocatorias();

		assertNotEquals(MENSAJE_CONVOCATORIAS_DEVUELTAS, 0, bean.getConvocatorias().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertNotEquals("nombre no vacio", "", bean.getConvocatorias().get(0).getNombreConvocatoria());
	}

	/** insertar convocatoria.
	 * @throws SQLException si fallo bd 
	 * @throws UVException si error uv
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA02Insertar() throws SQLException, UVException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ACCION, ControladorConvocatoriaCRUD.ACCION_AGREGAR_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_NOMBRE_CONVOCATORIA, NOMBRE_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ESTADO, "ABIERTA");
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_FECHA_LIMITE, FECHA_CORRECTA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_FECHA_COMISION, FECHA_CORRECTA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_OBSERVACIONES, OBSERVACIONES_CONVOCATORIA);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatoriaCRUD controlador = new ControladorConvocatoriaCRUD();
		controlador.doGet(peticion, respuesta);
		VistaConvocatoriaCRUD bean = (VistaConvocatoriaCRUD) peticion.getUVDatos().getVistas().get(VistaConvocatoriaCRUD.class.getName());
		
		assertNotEquals(MENSAJE_CONVOCATORIAS_DEVUELTAS, 0, bean.getConvocatorias().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorConvocatoriaCRUD.MENSAJE_EXITO_AGREGAR, bean.getMensajesDeExito().get(0));
	}
	
	/** eliminar convocatoria.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA03Eliminar() throws ServletException, IOException {
		Convocatoria convocatoriaEliminar = obtenerConvocatorias().getConvocatorias().get(0);
		
		PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ACCION, ControladorConvocatoriaCRUD.ACCION_ELIMINAR_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ID, convocatoriaEliminar.getIdConvocatoria().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatoriaCRUD controlador = new ControladorConvocatoriaCRUD();
		controlador.doGet(peticion, respuesta);
		VistaConvocatoriaCRUD bean = (VistaConvocatoriaCRUD) peticion.getUVDatos().getVistas().get(VistaConvocatoriaCRUD.class.getName());
		
		assertNotEquals(MENSAJE_CONVOCATORIAS_DEVUELTAS, 0, bean.getConvocatorias().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorConvocatoriaCRUD.MENSAJE_EXITO_ELIMINAR, bean.getMensajesDeExito().get(0));
		assertTrue("convocatoria eliminada no es listada", !bean.getConvocatorias().contains(convocatoriaEliminar));		
	}
	
	/** editar convocatoria.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA04Editar() throws ServletException, IOException {
		Convocatoria convocatoriaEditar = obtenerConvocatorias().getConvocatorias().get(0);

		PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ACCION, ControladorConvocatoriaCRUD.ACCION_EDITAR_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ID, convocatoriaEditar.getIdConvocatoria().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatoriaCRUD controlador = new ControladorConvocatoriaCRUD();
		controlador.doGet(peticion, respuesta);
		VistaConvocatoriaCRUD bean = (VistaConvocatoriaCRUD) peticion.getUVDatos().getVistas().get(VistaConvocatoriaCRUD.class.getName());
		
		assertNotNull(MENSAJE_CONVOCATORIA_DEVUELTA, bean.getConvocatoria());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** cambiar convocatoria.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA05Cambiar() throws ServletException, IOException {
		Convocatoria convocatoriaCambiar = obtenerConvocatorias().getConvocatorias().get(0);
		
		PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ACCION, ControladorConvocatoriaCRUD.ACCION_CAMBIAR_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ID, convocatoriaCambiar.getIdConvocatoria().toString());
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_NOMBRE_CONVOCATORIA, NOMBRE_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ESTADO, "ABIERTA");
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_FECHA_LIMITE, FECHA_CORRECTA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_FECHA_COMISION, FECHA_CORRECTA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_OBSERVACIONES, OBSERVACIONES_CONVOCATORIA);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatoriaCRUD controlador = new ControladorConvocatoriaCRUD();
		controlador.doGet(peticion, respuesta);
		VistaConvocatoriaCRUD bean = (VistaConvocatoriaCRUD) peticion.getUVDatos().getVistas().get(VistaConvocatoriaCRUD.class.getName());
		
		assertNotEquals(MENSAJE_CONVOCATORIAS_DEVUELTAS, 0, bean.getConvocatorias().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** post.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA06Post() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatoriaCRUD controlador = new ControladorConvocatoriaCRUD();
		controlador.doPost(peticion, respuesta);
		VistaConvocatoriaCRUD bean = (VistaConvocatoriaCRUD) peticion.getUVDatos().getVistas().get(VistaConvocatoriaCRUD.class.getName());
		
		assertNotEquals(MENSAJE_CONVOCATORIAS_DEVUELTAS, 0, bean.getConvocatorias().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** accion no valida.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA07AccionNoValida() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ACCION, "accion no valida");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatoriaCRUD controlador = new ControladorConvocatoriaCRUD();
		controlador.doPost(peticion, respuesta);
		VistaConvocatoriaCRUD bean = (VistaConvocatoriaCRUD) peticion.getUVDatos().getVistas().get(VistaConvocatoriaCRUD.class.getName());
		
		assertNotEquals(MENSAJE_CONVOCATORIAS_DEVUELTAS, 0, bean.getConvocatorias().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}

	/** accion no valida.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testE01AgregarVacio() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ACCION, ControladorConvocatoriaCRUD.ACCION_AGREGAR_CONVOCATORIA);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatoriaCRUD controlador = new ControladorConvocatoriaCRUD();
		controlador.doGet(peticion, respuesta);
		VistaConvocatoriaCRUD bean = (VistaConvocatoriaCRUD) peticion.getUVDatos().getVistas().get(VistaConvocatoriaCRUD.class.getName());
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		
	}

	/** Error estado no permitido.
	 * @throws SQLException si fallo bd 
	 * @throws UVException si error uv
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testE02EstadoErroneo() throws SQLException, UVException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ACCION, ControladorConvocatoriaCRUD.ACCION_AGREGAR_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_NOMBRE_CONVOCATORIA, NOMBRE_CONVOCATORIA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_ESTADO, "estado mal de atacante");
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_FECHA_LIMITE, FECHA_CORRECTA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_FECHA_COMISION, FECHA_CORRECTA);
		peticion.setParameter(ControladorConvocatoriaCRUD.PARAM_OBSERVACIONES, OBSERVACIONES_CONVOCATORIA);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorConvocatoriaCRUD controlador = new ControladorConvocatoriaCRUD();
		controlador.doGet(peticion, respuesta);
		VistaConvocatoriaCRUD bean = (VistaConvocatoriaCRUD) peticion.getUVDatos().getVistas().get(VistaConvocatoriaCRUD.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
}
