package controlador.bolsaempleo;

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
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaNoticias;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionNoticias;
import es.ujaen.uvirtual.utilidades.UVException;

/** test controlador noticias.
 * @author jmoral
 *
 */
public class TestControladorGestionNoticias {
	
	private static final String MENSAJE_NOTICIAS_DEVUELTAS = "Debe devolver noticias";
	private static final String MENSAJE_NOTICIA_DEVUELTA = "Debe devolver noticia";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String ACTIVA_NOTICIA = "S";
	private static final String ENLACE_NOTICIA = "enlace";
	private static final String FECHA_NOTICIA = "01/01/2030";
	private static final String PUBLICA_NOTICIA = "N";
	private static final String TEXTO_NOTICIA = "texto";
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    
    private VistaNoticias obtenerNoticias(String action) throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionNoticias.PARAM_ACCION, action);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionNoticias controlador = new ControladorGestionNoticias();
		controlador.doGet(peticion, respuesta);
		return (VistaNoticias) peticion.getUVDatos().getVistas().get(VistaNoticias.class.getName());
    }
    
    /** Obtener noticias, sin parametro definido .
	 * @throws SQLException si fallo bd  .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA01Obtener() throws SQLException, ServletException, IOException {
		VistaNoticias bean = obtenerNoticias(null);
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
    
	/** Obtener listado de noticias.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA02Obtener() throws SQLException, ServletException, IOException {
		VistaNoticias bean = obtenerNoticias(ControladorGestionNoticias.ACCION_LISTAR_NOTICIAS);

		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable noticias .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA03Obtener() throws SQLException, ServletException, IOException {
		VistaNoticias bean = obtenerNoticias(ControladorGestionNoticias.ACCION_DATATABLE);
		
		assertNotEquals(MENSAJE_NOTICIAS_DEVUELTAS, 0, bean.getDatatableNoticias().getData().size());
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** insertar noticia.
	 * @throws SQLException si fallo bd 
	 * @throws UVException si error uv
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA04Insertar() throws SQLException, UVException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionNoticias.PARAM_ACCION, ControladorGestionNoticias.ACCION_AGREGAR_NOTICIA);
		peticion.setParameter(ControladorGestionNoticias.PARAM_ENLACE, ENLACE_NOTICIA);
		peticion.setParameter(ControladorGestionNoticias.PARAM_FECHA, FECHA_NOTICIA);
		peticion.setParameter(ControladorGestionNoticias.PARAM_PUBLICA, PUBLICA_NOTICIA);
		peticion.setParameter(ControladorGestionNoticias.PARAM_TEXTO, TEXTO_NOTICIA);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionNoticias controlador = new ControladorGestionNoticias();
		controlador.doGet(peticion, respuesta);
		VistaNoticias bean = (VistaNoticias) peticion.getUVDatos().getVistas().get(VistaNoticias.class.getName());
		
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** editar noticia.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA05Editar() throws ServletException, IOException {
		VistaNoticias bean = obtenerNoticias(ControladorGestionNoticias.ACCION_DATATABLE);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionNoticias.PARAM_ACCION, ControladorGestionNoticias.ACCION_EDITAR_NOTICIA);
		peticion.setParameter(ControladorGestionNoticias.PARAM_ID, bean.getDatatableNoticias().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorGestionNoticias.PARAM_ENLACE, ENLACE_NOTICIA);
		peticion.setParameter(ControladorGestionNoticias.PARAM_FECHA, FECHA_NOTICIA);
		peticion.setParameter(ControladorGestionNoticias.PARAM_PUBLICA, PUBLICA_NOTICIA);
		peticion.setParameter(ControladorGestionNoticias.PARAM_TEXTO, TEXTO_NOTICIA);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionNoticias controlador = new ControladorGestionNoticias();
		controlador.doPost(peticion, respuesta);
		VistaNoticias bean2 = (VistaNoticias) peticion.getUVDatos().getVistas().get(VistaNoticias.class.getName());
		
		assertNotNull(MENSAJE_NOTICIA_DEVUELTA, bean2.getNoticia());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** eliminar noticia.
	 * @throws ServletException si error de servlet
	 * @throws IOException si error de io
	 */
	@Test
	public void testA06Eliminar() throws ServletException, IOException {
		VistaNoticias bean = obtenerNoticias(ControladorGestionNoticias.ACCION_DATATABLE);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionNoticias.PARAM_ACCION, ControladorGestionNoticias.ACCION_ELIMINAR_NOTICIA);
		peticion.setParameter(ControladorGestionNoticias.PARAM_ID, bean.getDatatableNoticias().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionNoticias controlador = new ControladorGestionNoticias();
		controlador.doPost(peticion, respuesta);
		VistaNoticias bean2 = (VistaNoticias) peticion.getUVDatos().getVistas().get(VistaNoticias.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
}
