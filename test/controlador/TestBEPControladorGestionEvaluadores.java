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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorGestionEvaluadores;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaEvaluadores;


/** test controlador titulacion.
 * @author ATISoluciones 
 */
public class TestBEPControladorGestionEvaluadores {
	
	private static final String MENSAJE_AREAS_DEVUELTAS = "Debe devolver areas";
	private static final String MENSAJE_EVALUADOR_DEVUELTO = "Debe devolver evaluador";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de exito debe coincidir";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	private static final String MENSAJE_USUARIOS_DEVUELTOS = "Debe devolver usuarios";
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd .
     * @throws IOException si error en ficheros .
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    // método para obtener la vista con una lista de áreas .
    private VistaEvaluadores getVistaConAreas() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
    	peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_LISTAR_AREAS);	
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doGet(peticion, respuesta);
		
		return (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
    }
    
    
    // método para obtener la vista con una lista de usuarios .
    private VistaEvaluadores getVistaConUsuarios(String accion) throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, accion);
		
		VistaEvaluadores bean = getVistaConAreas();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean.getAreas().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		
		return (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
    }
    
                
	/** Obtener areas, sin parametro definido .
	 * @throws SQLException si fallo bd  .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA01Obtener() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doGet(peticion, respuesta);
		
		VistaEvaluadores bean = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getAreas().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener listado de areas .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA02Obtener() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConAreas();
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getAreas().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable evaluadores .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA03ObtenerEvaluadores() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConUsuarios(ControladorGestionEvaluadores.ACCION_DATATABLE_EVALUADORES);
		
		assertNotEquals(MENSAJE_USUARIOS_DEVUELTOS, 0, bean.getDatatableUsuarios().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable usuarios .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA04ObtenerUsuarios() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConUsuarios(ControladorGestionEvaluadores.ACCION_DATATABLE_USUARIOS);
		
		assertNotEquals(MENSAJE_USUARIOS_DEVUELTOS, 0, bean.getDatatableUsuarios().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** agregar evaluadores dentro de un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA05AgregarEvaluadores() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConUsuarios(ControladorGestionEvaluadores.ACCION_DATATABLE_USUARIOS);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_AGREGAR_EVALUADORES);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_USUARIOS, 
				"[" + bean.getDatatableUsuarios().getData().get(0).getCodNum().toString() + "]");
		
		VistaEvaluadores bean2 = getVistaConAreas();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean2.getAreas().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		VistaEvaluadores bean3 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
	/** elimina un evaluador dentro de un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA06EliminarEvaluador() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConUsuarios(ControladorGestionEvaluadores.ACCION_DATATABLE_EVALUADORES);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_ELIMINAR_EVALUADOR);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_USUARIO, bean.getDatatableUsuarios().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean.getArea().getCodNum().toString());
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACTIVO, "false");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		VistaEvaluadores bean2 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertNotNull(MENSAJE_EVALUADOR_DEVUELTO, bean2.getEvaluador());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorGestionEvaluadores.MENSAJE_EXITO_BORRAR, bean2.getMensajesDeExito().get(0));
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** restaura un evaluador dentro de un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA07RestauraEvaluador() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConUsuarios(ControladorGestionEvaluadores.ACCION_DATATABLE_EVALUADORES);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_ELIMINAR_EVALUADOR);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_USUARIO, bean.getDatatableUsuarios().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean.getArea().getCodNum().toString());
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACTIVO, "true");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		VistaEvaluadores bean2 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertNotNull(MENSAJE_EVALUADOR_DEVUELTO, bean2.getEvaluador());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorGestionEvaluadores.MENSAJE_EXITO_RESTAURAR, bean2.getMensajesDeExito().get(0));
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable evaluadores con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE01ObtenerEvaluadoresParametroNoValido() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConAreas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_DATATABLE_EVALUADORES);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean.getAreas().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		
		VistaEvaluadores bean2 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Obtener datatable usuarios con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE02ObtenerUsuariosParametroNoValido() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConAreas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_DATATABLE_USUARIOS);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean.getAreas().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		
		VistaEvaluadores bean2 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
}
