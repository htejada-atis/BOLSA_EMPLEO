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
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo;

/**
 * Test controlador usuarios bolsa empleo.
 * 
 * @author ATISoluciones
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
	private static final String CODCUENTA = "test";
	private static final String CODCUENTAPERSONAL1 = "personal1";
	private static final String ROL = ModeloRol.ID_ROL_SERVICIO_PERSONAL.toString();
	private static final String EMAIL = "test@test";
	private static final String LISTADIST = "S";
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";
	
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

		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doGet(peticion, respuesta);
		return (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
	}
    
	private VistaUsuarioBolsaEmpleo obtenerUsuariosBorrados() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_DATATABLE_USUARIOS);
		peticion.setParameter("filter", "{\"5\":\"true\"}");

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
	public void testA06BuscarUsuario() {
		buscarUsuario(CODCUENTAPERSONAL1);
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
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO, CODCUENTA);
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
		UsuarioBolsaEmpleo usu = buscarUsuario(CODCUENTA);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_EDITAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_LISTA, LISTADIST);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_EXCLUIDO, "N");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_RAZON_EXCLUIDO, "EJEMPLO");
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ROLE, ROL);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO, usu.getCodCuenta());
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ID, usu.getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** excluir .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA10ExcluirUsuario() throws ServletException, IOException {
		UsuarioBolsaEmpleo usu = buscarUsuario(CODCUENTA);
		
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
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE_USUARIO, usu.getCodCuenta());
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ID, usu.getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** eliminar bloque .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA11EliminarUsuario() throws ServletException, IOException {
		VistaUsuarioBolsaEmpleo bean = obtenerUsuarios();
		
		StringBuilder selected = new StringBuilder();
		selected.append("[");
		for (UsuarioBolsaEmpleo usu : bean.getDatatable().getData()) {
			if (usu.getCodCuenta().equals(CODCUENTA)) {
				selected.append(usu.getCodNum());
			}
		}
		selected.append("]");

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION_USUARIO, ControladorUsuarioBolsaEmpleo.ACCION_ELIMINAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIOS_SELECCIONADOS, selected.toString());

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
		
		StringBuilder selected = new StringBuilder();
		selected.append("[");
		for (UsuarioBolsaEmpleo usu : bean.getDatatable().getData()) {
			if (usu.getCodCuenta().equals(CODCUENTA)) {
				selected.append(usu.getCodNum());
			}
		}
		selected.append("]");

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION_USUARIO, ControladorUsuarioBolsaEmpleo.ACCION_RECUPERAR_USUARIO);
		peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_USUARIOS_SELECCIONADOS, selected.toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
		controlador.doPost(peticion, respuesta);

		VistaUsuarioBolsaEmpleo bean2 = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorUsuarioBolsaEmpleo.MENSAJE_EXITO_USUARIO_MODIFICADO_CORRECTAMENTE, bean2.getMensajesDeExito().get(0));
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
	
	private UsuarioBolsaEmpleo buscarUsuario(String codcuenta) {
		try {
			PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
			peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_ACCION, ControladorUsuarioBolsaEmpleo.ACCION_BUSCAR_USUARIO);
			peticion.setParameter(ControladorUsuarioBolsaEmpleo.PARAM_NOMBRE, codcuenta);
			RespuestaHttp respuesta = new RespuestaHttp();
			ControladorUsuarioBolsaEmpleo controlador = new ControladorUsuarioBolsaEmpleo();
			controlador.doPost(peticion, respuesta);
			
			VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
			UsuarioBolsaEmpleo usu = bean.getUsuario();
			
			assertEquals(usu.getCodCuenta(), codcuenta);
			assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
			assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
			
			return usu;
		} catch (ServletException | IOException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
		
		return null;
	}
}
