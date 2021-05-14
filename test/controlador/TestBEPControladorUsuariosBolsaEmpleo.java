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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo;

/** test controlador usuarios bolsa empleo.
 * @author fcampos
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorUsuariosBolsaEmpleo {
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de exito debe coincidir";
	private static final String MENSAJE_APARTADOS_DEVUELTOS = "Debe devolver apartados";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
	private static final String CODNUM = "3";
	private static final String CODCUENTA = "pruebas1";
	private static final String ROL = "1050";
	private static final String EMAIL = "test@test";
	private static final String LISTADIST = "S";
	
	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException si error en bd
	 * @throws IOException  si error en io
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
    
	// método para obtener la vista con una lista de apartados .
	private VistaUsuarioBolsaEmpleo obtenerUsuarios() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();

		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION,
				ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doGet(peticion, respuesta);
		return (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
	}
    
	// método para obtener la vista con una lista de bloques .
	private VistaUsuarioBolsaEmpleo obtenerUsuariosBorrados() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION,
				ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS_BORRADOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doGet(peticion, respuesta);
		return (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
	}
    
	// método para obtener la vista con una lista de ítems .
	private VistaUsuarioBolsaEmpleo obtenerUsuariosExcluidos() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION,
				ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS_EXCLUIDOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doGet(peticion, respuesta);
		return (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
	}
    
    /** Obtener items usuarios, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
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
	public void testA02ObtenerUsuarios() throws ServletException, IOException {
		VistaUsuarioBolsaEmpleo bean = obtenerUsuarios();
		
		assertNotEquals(MENSAJE_APARTADOS_DEVUELTOS, 0, bean.getDatatable().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable bloques .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA03ObtenerUsuariosBorrados() throws ServletException, IOException {
		VistaUsuarioBolsaEmpleo bean = obtenerUsuariosBorrados();
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener datatable usuarios excluidos .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA04ObtenerUsuariosExcluidos() throws ServletException, IOException {
		VistaUsuarioBolsaEmpleo bean = obtenerUsuariosExcluidos();
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	
	/** seleccionar apartado .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException . 
	 */
	@Test
	public void testA05obtenerFormularioBusqueda() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_FORMULARIO_USUARIO);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** seleccionar bloque .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA06BuscarUsuario() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE, CODCUENTA);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** activar apartado .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA07ListaRoles() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_LISTAR_ROLES);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** activar bloque .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA08AgregarUsuario() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_AGREGAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_LISTA, LISTADIST);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO, "N");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO, "EJEMPLO");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ROLE, ROL);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ID, CODCUENTA);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_EMAIL, EMAIL);
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
	public void testA09EditarUsuario() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_LISTA, LISTADIST);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO, "N");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO, "EJEMPLO");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ROLE, ROL);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO, CODCUENTA);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ID, CODNUM);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** desactivar apartado .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA10ExcluirUsuario() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_EXCLUIR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_LISTA, LISTADIST);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO, "S");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO, "EJEMPLO");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO_TIPO, "T");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO, "12/05/2021");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO_INICIO, "12/05/2021");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_FECHA_EXCLUIDO_FIN, "12/05/2021");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ROLE, ROL);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO, CODCUENTA);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ID, CODNUM);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** desactivar bloque .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA11EliminarUsuario() throws ServletException, IOException {
		VistaUsuarioBolsaEmpleo bean = obtenerUsuarios();
		String selected = "[" + bean.getDatatable().getData().get(2).getCodNum() + ","
				+ bean.getDatatable().getData().get(3).getCodNum() + "]";

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION_USUARIO,
				ControladorUsuarioBolsaEmpleo.ACCION_ELIMINAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIOS_SELECCIONADOS, selected);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);

		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO,
				ControladorUsuarioBolsaEmpleo.MENSAJE_EXITO_USUARIO_MODIFICADO_CORRECTAMENTE,
				bean2.getMensajesDeExito().get(0));
	}
	
	/** recuperar usuario .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA12RecuperarUsuario() throws ServletException, IOException {
		VistaUsuarioBolsaEmpleo bean = obtenerUsuariosBorrados();
		String selected = "[" + bean.getDatatable().getData().get(0).getCodNum() + ","
				+ bean.getDatatable().getData().get(1).getCodNum() + "]";

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION_USUARIO,
				ControladorUsuarioBolsaEmpleo.ACCION_RECUPERAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIOS_SELECCIONADOS, selected);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);

		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO,
				ControladorUsuarioBolsaEmpleo.MENSAJE_EXITO_USUARIO_MODIFICADO_CORRECTAMENTE,
				bean2.getMensajesDeExito().get(0));
	}
	
	/** editar apartado .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA13IncluirUsuario() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_INCLUIR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ID, CODNUM);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** editar bloque .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA14VolverFormulario() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_VOLVER_USUARIO);
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	
	/** obtener bloques sin apartado definido .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE01BuscarUsuarioErroneo() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION,
				ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE, "prueba");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
}
