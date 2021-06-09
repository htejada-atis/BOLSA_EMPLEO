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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorUsuarioCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaUsuarioBolsaEmpleo;

/**
 * Test controlador usuarios candidatos bolsa empleo.
 * 
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorUsuarioCandidato {
	
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
	
	/** prepara la bd con los datos iniciales.
	 * @throws SQLException si error en bd
	 * @throws IOException  si error en io
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
    
	// método para obtener la vista con una lista de usuarios .
	private VistaUsuarioBolsaEmpleo obtenerUsuarios() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();

		peticion.setParameter(ControladorUsuarioCandidato.PARAM_ACCION, ControladorUsuarioCandidato.ACCION_DATATABLE_USUARIOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
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
		ControladorUsuarioCandidato controlador = new ControladorUsuarioCandidato();
		controlador.doGet(peticion, respuesta);
		
		VistaUsuarioBolsaEmpleo bean = (VistaUsuarioBolsaEmpleo) peticion.getUVDatos().getVistas().get(VistaUsuarioBolsaEmpleo.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
}
