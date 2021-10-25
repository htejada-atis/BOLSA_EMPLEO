package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorGestionTitulaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloTitulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaTitulaciones;


/** test controlador gestión titulaciones.
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorGestionTitulaciones {
	
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_TITULACIONES_DEVUELTAS = "Debe devolver titulaciones";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	private static final String NOMBRE_TITULACION = "titulacion";
	
    /** prepara la bd con los datos iniciales .
     * @throws SQLException si error en bd .
     * @throws IOException si error en ficheros .
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    private VistaTitulaciones getVistaTitulaciones(String action) throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
    	
    	if (action != null) {
    		peticion.setParameter(ControladorGestionTitulaciones.PARAM_ACCION, action);	
    	}
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulaciones controlador = new ControladorGestionTitulaciones();
		controlador.doGet(peticion, respuesta);
		
		return (VistaTitulaciones) peticion.getUVDatos().getVistas().get(VistaTitulaciones.class.getName());
    }
                
	/** Obtener titulaciones, sin parametro definido .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA01Obtener() throws ServletException, IOException {
		VistaTitulaciones bean = getVistaTitulaciones(null);
		
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener listado de titulaciones .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA02ObtenerTitulaciones() throws ServletException, IOException {
		VistaTitulaciones bean = getVistaTitulaciones(ControladorGestionTitulaciones.ACCION_INDEX);
		
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable titulaciones .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA03ObtenerTitulaciones() throws ServletException, IOException {
		VistaTitulaciones bean = getVistaTitulaciones(ControladorGestionTitulaciones.ACCION_DATATABLE_TITULACIONES);
		
		assertNotEquals(MENSAJE_TITULACIONES_DEVUELTAS, 0, bean.getDatatableTitulaciones().getData().size());	
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** insertar titulación .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA04InsertarTitulacion() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionTitulaciones.PARAM_ACCION, ControladorGestionTitulaciones.ACCION_AGREGAR_TITULACION);
		peticion.setParameter(ControladorGestionTitulaciones.PARAM_NOMBRE, NOMBRE_TITULACION);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulaciones controlador = new ControladorGestionTitulaciones();
		controlador.doPost(peticion, respuesta);
		VistaTitulaciones bean = (VistaTitulaciones) peticion.getUVDatos().getVistas().get(VistaTitulaciones.class.getName());
		
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** eliminar titulación .
	 * @throws ServletException si error de servlet .
	 * @throws IOException si error de io .
	 */
	@Test
	public void testA06EliminarTitulacion() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionTitulaciones.PARAM_ACCION, ControladorGestionTitulaciones.ACCION_DATATABLE_TITULACIONES);	
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_FILTER, "{" + "'" + ModeloTitulacion.ORDER_COLUMN_INDEX_NOMBRE + "':'" + NOMBRE_TITULACION + "'}");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulaciones controlador = new ControladorGestionTitulaciones();
		controlador.doPost(peticion, respuesta);
		VistaTitulaciones bean = (VistaTitulaciones) peticion.getUVDatos().getVistas().get(VistaTitulaciones.class.getName());
		
		PeticionHttp peticion2 = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionTitulaciones.PARAM_ACCION, ControladorGestionTitulaciones.ACCION_BORRAR_TITULACION);
		peticion.setParameter(ControladorGestionTitulaciones.PARAM_ID, bean.getDatatableTitulaciones().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta2 = new RespuestaHttp();
		ControladorGestionTitulaciones controlador2 = new ControladorGestionTitulaciones();
		controlador2.doPost(peticion2, respuesta2);
		VistaTitulaciones bean2 = (VistaTitulaciones) peticion2.getUVDatos().getVistas().get(VistaTitulaciones.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable titulaciones con parámetro erróneo de la tabla para forzar error .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE01ObtenerTitulacionesParametroErroneo() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionTitulaciones.PARAM_ACCION, ControladorGestionTitulaciones.ACCION_DATATABLE_TITULACIONES);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulaciones controlador = new ControladorGestionTitulaciones();
		controlador.doPost(peticion, respuesta);
		VistaTitulaciones bean = (VistaTitulaciones) peticion.getUVDatos().getVistas().get(VistaTitulaciones.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** insertar titulación nombre vacío .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE02InsertarTitulacionNombreVacio() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionTitulaciones.PARAM_ACCION, ControladorGestionTitulaciones.ACCION_AGREGAR_TITULACION);
		peticion.setParameter(ControladorGestionTitulaciones.PARAM_NOMBRE, "");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulaciones controlador = new ControladorGestionTitulaciones();
		controlador.doPost(peticion, respuesta);
		VistaTitulaciones bean = (VistaTitulaciones) peticion.getUVDatos().getVistas().get(VistaTitulaciones.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** eliminar titulación ya asignada a un area .
	 * @throws ServletException si error de servlet .
	 * @throws IOException si error de io .
	 */
	@Test
	public void testE03EliminarTitulacionConArea() throws ServletException, IOException {
		VistaTitulaciones bean = getVistaTitulaciones(ControladorGestionTitulaciones.ACCION_DATATABLE_TITULACIONES);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionTitulaciones.PARAM_ACCION, ControladorGestionTitulaciones.ACCION_BORRAR_TITULACION);
		peticion.setParameter(ControladorGestionTitulaciones.PARAM_ID, bean.getDatatableTitulaciones().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulaciones controlador = new ControladorGestionTitulaciones();
		controlador.doPost(peticion, respuesta);
		VistaTitulaciones bean2 = (VistaTitulaciones) peticion.getUVDatos().getVistas().get(VistaTitulaciones.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
}
