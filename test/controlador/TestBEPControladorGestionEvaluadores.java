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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorGestionEvaluadores;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaEvaluadores;


/** test controlador titulacion.
 * @author ATISoluciones 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorGestionEvaluadores {
	
	private static final String MENSAJE_AREAS_DEVUELTAS = "Debe devolver areas";
	private static final String MENSAJE_AREA_DEVUELTA = "Debe devolver area";
	private static final String MENSAJE_DEPARTAMENTOS_DEVUELTOS = "Debe devolver departamentos";
	private static final String MENSAJE_EVALUADOR_DEVUELTO = "Debe devolver evaluador";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_ERROR_ESPERADO = "El mensaje de error debe coincidir";
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
    
    // método para obtener la vista con una lista de departamentos .
    private VistaEvaluadores getVistaConDepartamentos() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
    	peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_INDEX);
    	
    	RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doGet(peticion, respuesta);
		
		return (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
    }
    
    // método para obtener la vista con una lista de áreas .
    private VistaEvaluadores getVistaConAreas() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
    	peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_DATATABLE_AREAS);
    	
    	VistaEvaluadores bean = getVistaConDepartamentos();
    	peticion.setParameter(ControladorGestionEvaluadores.PARAM_DEPARTAMENTO, bean.getDepartamentos().get(1).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doGet(peticion, respuesta);
		
		return (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
    }
    
    
    // método para obtener la vista con una lista de evaluadores .
    private VistaEvaluadores getVistaConEvaluadores() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_DATATABLE_EVALUADORES);
		
		VistaEvaluadores bean = getVistaConAreas();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean.getDatatableAreas().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		
		return (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
    }
    
                
	/** Obtener departamentos, sin parametro definido .
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
		
		assertNotEquals(MENSAJE_DEPARTAMENTOS_DEVUELTOS, 0, bean.getDepartamentos().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener listado de departamentos .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA02ObtenerDepartamentos() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConDepartamentos();
		
		assertNotEquals(MENSAJE_DEPARTAMENTOS_DEVUELTOS, 0, bean.getDepartamentos().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable áreas .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA03ObtenerAreas() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConAreas();
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getDatatableAreas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}

	/** agregar evaluador dentro de un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA04AgregarEvaluador() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_AGREGAR_EVALUADORES);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_NOMBRE_EVALUADOR, "comision2");
		
		VistaEvaluadores bean = getVistaConDepartamentos();
		VistaEvaluadores bean2 = getVistaConAreas();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREAS, "[" + bean2.getDatatableAreas().getData().get(0).getCodNum().toString() + "]");
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_DEPARTAMENTO, bean.getDepartamentos().get(1).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		VistaEvaluadores bean3 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable evaluadores .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA05ObtenerEvaluadores() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConEvaluadores();
		
		assertNotEquals(MENSAJE_USUARIOS_DEVUELTOS, 0, bean.getDatatableEvaluadores().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** elimina un evaluador dentro de un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA06EliminarEvaluador() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConDepartamentos();
		VistaEvaluadores bean2 = getVistaConEvaluadores();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_ELIMINAR_EVALUADOR);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_USUARIO, bean2.getDatatableEvaluadores().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean2.getArea().getCodNum().toString());
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACTIVO, "false");
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_DEPARTAMENTO, bean.getDepartamentos().get(1).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		VistaEvaluadores bean3 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertNotNull(MENSAJE_EVALUADOR_DEVUELTO, bean3.getEvaluador());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorGestionEvaluadores.MENSAJE_EXITO_BORRAR, bean3.getMensajesDeExito().get(0));
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
	/** restaura un evaluador dentro de un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA07RestauraEvaluador() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConDepartamentos();
		VistaEvaluadores bean2 = getVistaConEvaluadores();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_ELIMINAR_EVALUADOR);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_USUARIO, bean2.getDatatableEvaluadores().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean2.getArea().getCodNum().toString());
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACTIVO, "true");
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_DEPARTAMENTO, bean.getDepartamentos().get(1).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		VistaEvaluadores bean3 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertNotNull(MENSAJE_EVALUADOR_DEVUELTO, bean3.getEvaluador());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorGestionEvaluadores.MENSAJE_EXITO_RESTAURAR, bean3.getMensajesDeExito().get(0));
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
	/** selecciona un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA08SeleccionarArea() throws SQLException, ServletException, IOException {
		VistaEvaluadores bean = getVistaConDepartamentos();
		VistaEvaluadores bean2 = getVistaConAreas();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_AREA_SELECCIONADA);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean2.getDatatableAreas().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_DEPARTAMENTO, bean.getDepartamentos().get(1).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		VistaEvaluadores bean3 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertNotEquals(MENSAJE_AREA_DEVUELTA, 0, bean3.getArea());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
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
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean.getDatatableAreas().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		
		VistaEvaluadores bean2 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Obtener datatable áreas con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE02ObtenerAreasParametroNoValido() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
    	peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_DATATABLE_AREAS);
    	
    	VistaEvaluadores bean = getVistaConDepartamentos();
    	peticion.setParameter(ControladorGestionEvaluadores.PARAM_DEPARTAMENTO, bean.getDepartamentos().get(1).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		
		VistaEvaluadores bean2 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** agregar evaluador dentro de un área con rol no válido .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testE03AgregarEvaluadorRolNoValido() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_AGREGAR_EVALUADORES);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_NOMBRE_EVALUADOR, "candidato2");
		
		VistaEvaluadores bean = getVistaConDepartamentos();
		VistaEvaluadores bean2 = getVistaConAreas();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean2.getDatatableAreas().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_DEPARTAMENTO, bean.getDepartamentos().get(1).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		VistaEvaluadores bean3 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean3.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorGestionEvaluadores.MENSAJE_ERROR_ROL_COMISION, bean3.getMensajesDeError().get(0));
	}
	
	/** agregar evaluador dentro de un área usuario no válido .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testE04AgregarEvaluadorNoValido() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_AGREGAR_EVALUADORES);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_NOMBRE_EVALUADOR, "usuarionovalido");
		
		VistaEvaluadores bean = getVistaConDepartamentos();
		VistaEvaluadores bean2 = getVistaConAreas();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREA, bean2.getDatatableAreas().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_DEPARTAMENTO, bean.getDepartamentos().get(1).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		VistaEvaluadores bean3 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean3.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ModeloUsuarioBolsaEmpleo.MENSAJE_USUARIO_NO_EXISTE, bean3.getMensajesDeError().get(0));
	}
	
	/** agregar evaluador dentro de un área que ya existe .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testE05AgregarEvaluadorYaExistente() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, ControladorGestionEvaluadores.ACCION_AGREGAR_EVALUADORES);
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_NOMBRE_EVALUADOR, "comision2");
		
		VistaEvaluadores bean = getVistaConDepartamentos();
		VistaEvaluadores bean2 = getVistaConAreas();
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_AREAS, "[" + bean2.getDatatableAreas().getData().get(0).getCodNum().toString() + "]");
		peticion.setParameter(ControladorGestionEvaluadores.PARAM_DEPARTAMENTO, bean.getDepartamentos().get(1).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doPost(peticion, respuesta);
		VistaEvaluadores bean3 = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean3.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, String.format(ControladorGestionEvaluadores.MENSAJE_ERROR_EVALUADOR_YA_EXISTE,
				bean2.getDatatableAreas().getData().get(0).getArea().getDescripcion()), bean3.getMensajesDeError().get(0));
	}
	
	/** acción no válida .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE06AccionNoValida() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
    	peticion.setParameter(ControladorGestionEvaluadores.PARAM_ACCION, "accionnovalida");
    	
    	RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionEvaluadores controlador = new ControladorGestionEvaluadores();
		controlador.doGet(peticion, respuesta);
		
		VistaEvaluadores bean = (VistaEvaluadores) peticion.getUVDatos().getVistas().get(VistaEvaluadores.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorGestionEvaluadores.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
}
