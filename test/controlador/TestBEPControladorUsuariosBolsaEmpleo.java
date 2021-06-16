package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo;

/**
 * Test controlador usuarios bolsa empleo.
 * 
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorUsuariosBolsaEmpleo {
	
	private static final String MENSAJE_AREAS_DEVUELTAS = "Debe devolver áreas";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_ERROR_ESPERADO = "El mensaje de error debe coincidir";
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de exito debe coincidir";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	private static final String MENSAJE_USUARIOS_DEVUELTOS = "Debe devolver usuarios";
	
	private static final String CODCUENTA = "test";
	private static final String CODCUENTAPERSONAL1 = "personal1";
	private static final String ROL = ModeloRol.ID_ROL_SERVICIO_PERSONAL.toString();
	private static final String LISTADIST = "S";
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";
	private static final String EXCLUIDO_USUARIO = "true";
	
	/** prepara la bd con los datos iniciales .
	 * @throws SQLException si error en bd .
	 * @throws IOException  si error en io .
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
    
	// método para obtener la vista con una lista de apartados .
	private VistaUsuarioBolsaEmpleo obtenerUsuarios() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();

		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doGet(peticion, respuesta);
		return (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
	}
    
	private VistaUsuarioBolsaEmpleo obtenerUsuariosBorrados() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS);
		peticion.setParameter("filter", "{\"4\":\"true\"}");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doGet(peticion, respuesta);
		return (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
	}
	
	private UsuarioBolsaEmpleo obtenerUsuarioComision() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_FILTER, "{\"3\":\"1051\"}");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doGet(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		return bean.getDatatableUsuarios().getData().get(0);
	}
    
    /** Obtener items usuarios, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doGet(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable apartados .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA02ObtenerUsuarios() throws IOException {
		VistaUsuarioBolsaEmpleo bean = obtenerUsuarios();
		
		assertNotEquals(MENSAJE_USUARIOS_DEVUELTOS, 0, bean.getDatatableUsuarios().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable usuarios borrados .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA03ObtenerUsuariosBorrados() throws IOException {
		VistaUsuarioBolsaEmpleo bean = obtenerUsuariosBorrados();
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** seleccionar usuario .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException . 
	 */
	@Test
	public void testA04seleccionarUsuario() throws IOException {
		VistaUsuarioBolsaEmpleo bean = obtenerUsuarios();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_SELECCIONAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIO, bean.getDatatableUsuarios().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** buscar usuario .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA05BuscarUsuario() {
		buscarUsuario(CODCUENTAPERSONAL1);
	}
	
	/** Agrega un usuario .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA06AgregarUsuario() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_AGREGAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_LISTA, LISTADIST);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO, "N");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO, "EJEMPLO");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ROLE, ROL);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO, CODCUENTA);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIO, CODCUENTA);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorUsuarioBolsaEmpleo.MENSAJE_EXITO_AGREGAR, bean.getMensajesDeExito().get(0));
	}
	
	/** editar usuario .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA07EditarUsuario() throws IOException {
		UsuarioBolsaEmpleo usu = buscarUsuario(CODCUENTA);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_LISTA, LISTADIST);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO, "N");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO, "EJEMPLO");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ROLE, ROL);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO, usu.getCodCuenta());
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIO, usu.getCodNum().toString());
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_GUARDAR, "");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Guardar datos del usuario con excluido a true tipo temporal .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA08ExcluirTemporalUsuario() throws IOException {
		UsuarioBolsaEmpleo usu = buscarUsuario(CODCUENTA);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_LISTA, LISTADIST);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO, EXCLUIDO_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO, "EJEMPLO");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO, "T");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO, "12/05/2021");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO_INICIO, "12/05/2021");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO_FIN, "12/05/2021");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ROLE, ROL);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO, usu.getCodCuenta());
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIO, usu.getCodNum().toString());
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_GUARDAR, "");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Guardar datos del usuario con excluido a false .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA09IncluirUsuario() throws IOException {
		VistaUsuarioBolsaEmpleo bean = obtenerUsuarios();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIO, bean.getDatatableUsuarios().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO, null);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ROLE, ROL);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_GUARDAR, "");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** eliminar usuario .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA10EliminarUsuario() throws IOException {
		UsuarioBolsaEmpleo usu = buscarUsuario(CODCUENTA);

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_ELIMINAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIO, usu.getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);

		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO,
				ControladorUsuarioBolsaEmpleo.MENSAJE_EXITO_ELIMINAR,
				bean2.getMensajesDeExito().get(0));
	}
	
	/** recuperar usuario .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA11RecuperarUsuario() throws IOException {
		UsuarioBolsaEmpleo usu = buscarUsuario(CODCUENTA);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_RECUPERAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIO, usu.getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);

		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorUsuarioBolsaEmpleo.MENSAJE_EXITO_RESTAURAR, bean2.getMensajesDeExito().get(0));
	}
	
	/** acción volver atrás .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA12VolverFormulario() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_VOLVER_USUARIO);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Acción áreas evaluables .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA13AccionAreasEvaluables() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_AREAS_EVALUABLES);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIO, obtenerUsuarioComision().getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable áreas evaluables .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA14AccionAreasEvaluables() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_AREAS_EVALUABLES_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIO, obtenerUsuarioComision().getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getDatatableAreasEvaluables().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	
	/** Buscar usuario erróneo .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE01BuscarUsuarioErroneo() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION,
				ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO, "prueba");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Obtener datatable usuarios con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE02ObtenerUsuariosParametroNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Obtener datatable áreas con parámetro no válido para forzar el error .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE03ObtenerAreasEvaluablesParametroNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_AREAS_EVALUABLES_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIO, obtenerUsuarioComision().getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** acción no válida .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testE04AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, "accionnovalida");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorUsuarioBolsaEmpleo.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	private UsuarioBolsaEmpleo buscarUsuario(String codcuenta) {
		try {
			PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
			peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO);
			peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO, codcuenta);
			RespuestaHttp respuesta = new RespuestaHttp();
			ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
			controlador.doPost(peticion, respuesta);
			
			VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
			UsuarioBolsaEmpleo usu = bean.getUsuario();
			
			assertEquals(usu.getCodCuenta(), codcuenta);
			assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
			assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
			
			return usu;
		} catch (IOException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
		
		return null;
	}
}
