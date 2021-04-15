package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import org.junit.BeforeClass;
import org.junit.Test;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFicheros;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionFicheros;

/** test controlador noticias.
 * @author jmoral
 *
 */
public class TestBEPControladorGestionFicheros {
	
	private static final String MENSAJE_FICHERO_DEVUELTO = "Debe devolver fichero";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String TITULO_FICHERO = "titulo";
	private static final String PUBLICO_FICHERO = "N";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    
    private VistaFicheros obtenerFicheros(String action) throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionFicheros.PARAM_ACCION, action);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionFicheros controlador = new ControladorGestionFicheros();
		controlador.doGet(peticion, respuesta);
		return (VistaFicheros) peticion.getUVDatos().getVistas().get(VistaFicheros.class.getName());
    }
    
    /** Obtener ficheros, sin parametro definido .
	 * @throws SQLException si fallo bd  .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		VistaFicheros bean = obtenerFicheros(null);
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
    
	/** obtener listado de ficheros.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA02() throws SQLException, ServletException, IOException {
		VistaFicheros bean = obtenerFicheros(ControladorGestionFicheros.ACCION_LISTAR_FICHEROS);

		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable ficheros .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA03() throws SQLException, ServletException, IOException {
		VistaFicheros bean = obtenerFicheros(ControladorGestionFicheros.ACCION_DATATABLE);
		
		assertNotEquals(0, bean.getDatatableFicheros().getData().size());
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** eliminar fichero.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA04Eliminar() throws ServletException, IOException {
		VistaFicheros bean = obtenerFicheros(ControladorGestionFicheros.ACCION_DATATABLE);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionFicheros.PARAM_ACCION, ControladorGestionFicheros.ACCION_BORRAR_FICHEROS);
		peticion.setParameter(ControladorGestionFicheros.PARAM_FICHEROS, "[" + bean.getDatatableFicheros().getData().get(0).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionFicheros controlador = new ControladorGestionFicheros();
		controlador.doPost(peticion, respuesta);
		VistaFicheros bean2 = (VistaFicheros) peticion.getUVDatos().getVistas().get(VistaFicheros.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** subir fichero.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA05Agregar() throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionFicheros.PARAM_ACCION, ControladorGestionFicheros.ACCION_SUBIR_FICHERO);
		peticion.setParameter(ControladorGestionFicheros.PARAM_FICHERO, "");
		peticion.setParameter(ControladorGestionFicheros.PARAM_TITULO, TITULO_FICHERO);
		peticion.setParameter(ControladorGestionFicheros.PARAM_PUBLICO, PUBLICO_FICHERO);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionFicheros controlador = new ControladorGestionFicheros();
		controlador.doPost(peticion, respuesta);
		VistaFicheros bean = (VistaFicheros) peticion.getUVDatos().getVistas().get(VistaFicheros.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** descargar fichero.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA06Descargar() throws ServletException, IOException {
		VistaFicheros bean = obtenerFicheros(ControladorGestionFicheros.ACCION_DATATABLE);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionFicheros.PARAM_ACCION, ControladorGestionFicheros.ACCION_DESCARGAR_FICHERO);
		peticion.setParameter(ControladorGestionFicheros.PARAM_FICHERO, bean.getDatatableFicheros().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionFicheros controlador = new ControladorGestionFicheros();
		controlador.doPost(peticion, respuesta);
		VistaFicheros bean2 = (VistaFicheros) peticion.getUVDatos().getVistas().get(VistaFicheros.class.getName());
		
		assertNotNull(MENSAJE_FICHERO_DEVUELTO, bean2.getFichero());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** hacer publico fichero.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA07HacerPublico() throws ServletException, IOException {
		VistaFicheros bean = obtenerFicheros(ControladorGestionFicheros.ACCION_DATATABLE);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionFicheros.PARAM_ACCION, ControladorGestionFicheros.ACCION_HACER_FICHEROS_PUBLICOS);
		peticion.setParameter(ControladorGestionFicheros.PARAM_FICHEROS, "[" + bean.getDatatableFicheros().getData().get(0).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionFicheros controlador = new ControladorGestionFicheros();
		controlador.doPost(peticion, respuesta);
		VistaFicheros bean2 = (VistaFicheros) peticion.getUVDatos().getVistas().get(VistaFicheros.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** hacer privado fichero.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA08HacerPrivado() throws ServletException, IOException {
		VistaFicheros bean = obtenerFicheros(ControladorGestionFicheros.ACCION_DATATABLE);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionFicheros.PARAM_ACCION, ControladorGestionFicheros.ACCION_HACER_FICHEROS_PRIVADOS);
		peticion.setParameter(ControladorGestionFicheros.PARAM_FICHEROS, "[" + bean.getDatatableFicheros().getData().get(0).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionFicheros controlador = new ControladorGestionFicheros();
		controlador.doPost(peticion, respuesta);
		VistaFicheros bean2 = (VistaFicheros) peticion.getUVDatos().getVistas().get(VistaFicheros.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
}
