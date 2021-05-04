package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import org.junit.BeforeClass;
import org.junit.Test;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaMeritos;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisMeritos;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;

/** test controlador mis méritos.
 * @author jlopez
 *
 */
public class TestBEPControladorMisMeritos {
	
	private static final String MENSAJE_APARTADO_DEVUELTO = "Debe devolver apartado";
	private static final String MENSAJE_APARTADOS_DEVUELTOS = "Debe devolver apartados";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_ITEMS_DEVUELTOS = "Debe devolver items";
	private static final String MENSAJE_MERITOS_DEVUELTOS = "Debe devolver méritos";
	private static final String MENSAJE_MERITO_DEVUELTO = "Debe devolver mérito";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en io
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    // método para obtener la vista con una lista de méritos .
    private VistaMeritos obtenerMeritos() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisMeritos.PARAM_ACCION, ControladorMisMeritos.ACCION_DATATABLE);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisMeritos controlador = new ControladorMisMeritos();
		controlador.doGet(peticion, respuesta);
		return (VistaMeritos) peticion.getUVDatos().getVistas().get(VistaMeritos.class.getName());
    }
    
    // método para obtener la vista con el formulario de agregar .
    private VistaMeritos obtenerFormularioAgregar(Integer apartado) throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisMeritos.PARAM_ACCION, ControladorMisMeritos.ACCION_AGREGAR_MERITO);
		
		if (apartado != null) {
			peticion.setParameter(ControladorMisMeritos.PARAM_APARTADO, apartado.toString());
		}
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisMeritos controlador = new ControladorMisMeritos();
		controlador.doPost(peticion, respuesta);
		
		return (VistaMeritos) peticion.getUVDatos().getVistas().get(VistaMeritos.class.getName());
    }
    
    /** Obtener items baremación, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisMeritos controlador = new ControladorMisMeritos();
		controlador.doGet(peticion, respuesta);
		
		VistaMeritos bean = (VistaMeritos) peticion.getUVDatos().getVistas().get(VistaMeritos.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable méritos .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA02ObtenerMeritos() throws SQLException, ServletException, IOException {
		VistaMeritos bean = obtenerMeritos();
		
		assertNotEquals(MENSAJE_MERITOS_DEVUELTOS, 0, bean.getDatatable().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** borrar méritos .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA03BorrarMeritos() throws SQLException, ServletException, IOException {
		VistaMeritos bean = obtenerMeritos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisMeritos.PARAM_ACCION, ControladorMisMeritos.ACCION_ELIMINAR_MERITOS);
		peticion.setParameter(ControladorMisMeritos.PARAM_MERITOS, "[" + bean.getDatatable().getData().get(2).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisMeritos controlador = new ControladorMisMeritos();
		controlador.doPost(peticion, respuesta);
		VistaMeritos bean2 = (VistaMeritos) peticion.getUVDatos().getVistas().get(VistaMeritos.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** carga formulario de agregar mérito.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA04AgregarMerito() throws ServletException, IOException {
		VistaMeritos bean = obtenerFormularioAgregar(null);
		
		assertNotEquals(MENSAJE_APARTADOS_DEVUELTOS, 0, bean.getApartados().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** carga formulario de agregar mérito con apartado seleccionado.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA05AgregarMeritoApartadoSeleccionado() throws ServletException, IOException {
		VistaMeritos bean = obtenerFormularioAgregar(null);
		VistaMeritos bean2 = obtenerFormularioAgregar(bean.getApartados().get(0).getCodNum());
		
		assertNotNull(MENSAJE_APARTADO_DEVUELTO, bean2.getApartado());
		assertNotEquals(MENSAJE_ITEMS_DEVUELTOS, 0, bean2.getItems().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** eliminar ids de méritos no válidos  .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE01EliminarMeritosNoValidos() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisMeritos.PARAM_ACCION, ControladorMisMeritos.ACCION_ELIMINAR_MERITOS);
		peticion.setParameter(ControladorMisMeritos.PARAM_MERITOS, "");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisMeritos controlador = new ControladorMisMeritos();
		controlador.doPost(peticion, respuesta);
		VistaMeritos bean = (VistaMeritos) peticion.getUVDatos().getVistas().get(VistaMeritos.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** carga formulario de agregar mérito con apartado seleccionado no válido.
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE02AgregarMeritoApartadoSeleccionadoNoValido() throws SQLException, ServletException, IOException {
		VistaMeritos bean = obtenerFormularioAgregar(-1);
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** descargar fichero mérito error.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testE03DescargarError() throws ServletException, IOException {
		VistaMeritos bean = obtenerMeritos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisMeritos.PARAM_ACCION, ControladorMisMeritos.ACCION_DESCARGAR_FICHERO);
		peticion.setParameter(ControladorMisMeritos.PARAM_ID, bean.getDatatable().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisMeritos controlador = new ControladorMisMeritos();
		controlador.doGet(peticion, respuesta);
		VistaMeritos bean2 = (VistaMeritos) peticion.getUVDatos().getVistas().get(VistaMeritos.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** obtener datatable méritos con parámetro no válido .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testE04ObtenerMeritos() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisMeritos.PARAM_ACCION, ControladorMisMeritos.ACCION_DATATABLE);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisMeritos controlador = new ControladorMisMeritos();
		controlador.doGet(peticion, respuesta);
		VistaMeritos bean = (VistaMeritos) peticion.getUVDatos().getVistas().get(VistaMeritos.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
}
