package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorInicio;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaInicio;

/** test controlador inicio.
 * @author jlopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorInicio {
	
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_DOCUMENTOS_DEVUELTOS = "Debe devolver documentos";
	private static final String MENSAJE_FICHERO_PUBLICO = "Fichero debe ser público";
	private static final String MENSAJE_NOTICIAS_DEVUELTAS = "Debe devolver noticias";
	private static final String MENSAJE_NOTICIA_PUBLICA = "Noticia debe ser pública";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
		
    /** Prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    private VistaInicio obtenerInicio(String accion) throws IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorInicio.PARAM_ACCION, accion);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorInicio controlador = new ControladorInicio();
		controlador.doGet(peticion, respuesta);
		return (VistaInicio) peticion.getUVDatos().getVistas().get(VistaInicio.class.getName());
    }
    
    private VistaInicio obtenerInicioPublico(String accion) throws IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAnonima();
		peticion.setParameter(ControladorInicio.PARAM_ACCION, accion);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorInicio controlador = new ControladorInicio();
		controlador.doGet(peticion, respuesta);
		return (VistaInicio) peticion.getUVDatos().getVistas().get(VistaInicio.class.getName());
    }
    
    /** Obtener noticias, sin parametro definido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA01Obtener() throws IOException {
		VistaInicio bean = obtenerInicio(null);
		
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
    
	/** obtener noticias.
	 * @throws IOException si error io
	 */
	@Test
	public void testA02ObtenerNoticias() throws IOException {
		VistaInicio bean = obtenerInicio(ControladorInicio.ACCION_INDEX);

		assertNotEquals(MENSAJE_NOTICIAS_DEVUELTAS, 0, bean.getNoticias().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertNotEquals("texto no vacio", "", bean.getNoticias().get(0).getTexto());
	}
	
	/** post ayuda.
	 * @throws IOException si error de io
	 */
	@Test
	public void testA03Post() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorInicio.PARAM_ACCION, ControladorInicio.ACCION_AYUDA);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorInicio controlador = new ControladorInicio();
		controlador.doPost(peticion, respuesta);
		VistaInicio bean = (VistaInicio) peticion.getUVDatos().getVistas().get(VistaInicio.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** post faq.
	 * @throws IOException si error de io
	 */
	@Test
	public void testA04Post() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorInicio.PARAM_ACCION, ControladorInicio.ACCION_FAQ);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorInicio controlador = new ControladorInicio();
		controlador.doPost(peticion, respuesta);
		VistaInicio bean = (VistaInicio) peticion.getUVDatos().getVistas().get(VistaInicio.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener noticias restantes .
	 * @throws IOException si error de io
	 */
	@Test
	public void testA05Obtener() throws IOException {
		VistaInicio bean = obtenerInicio(ControladorInicio.ACCION_INDEX);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorInicio.PARAM_ACCION, ControladorInicio.ACCION_LISTAR_TODAS_NOTICIAS);
		peticion.setParameter(ControladorInicio.PARAM_NOTICIAS, 
				"[" + bean.getNoticias().get(0).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorInicio controlador = new ControladorInicio();
		controlador.doPost(peticion, respuesta);
		VistaInicio bean2 = (VistaInicio) peticion.getUVDatos().getVistas().get(VistaInicio.class.getName());
		
		assertNotEquals(MENSAJE_NOTICIAS_DEVUELTAS, 0, bean2.getNoticias().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
		assertNotEquals("texto no vacio", "", bean2.getNoticias().get(0).getTexto());
	}
	
	/** post documentos.
	 * @throws IOException si error de io
	 */
	@Test
	public void testA06Post() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorInicio.PARAM_ACCION, ControladorInicio.ACCION_DOCUMENTOS);
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorInicio controlador = new ControladorInicio();
		controlador.doPost(peticion, respuesta);
		VistaInicio bean = (VistaInicio) peticion.getUVDatos().getVistas().get(VistaInicio.class.getName());
		
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener documentos.
	 * @throws IOException si error de io
	 */
	@Test
	public void testA07ObtenerDocumentos() throws IOException {
		VistaInicio bean = obtenerInicio(ControladorInicio.ACCION_DOCUMENTOS);

		assertNotEquals(MENSAJE_DOCUMENTOS_DEVUELTOS, 0, bean.getFicheros().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertNotEquals("titulo no vacio", "", bean.getFicheros().get(0).getTitulo());
	}
	
	/** Obtener noticias, sin parametro definido con usuario anónimo .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA08ObtenerPublico() throws IOException {
		VistaInicio bean = obtenerInicioPublico(null);
		
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** obtener noticia con usuario anónimo .
	 * @throws IOException si error io
	 */
	@Test
	public void testA09ObtenerNoticiaPublica() throws IOException {
		VistaInicio bean = obtenerInicioPublico(ControladorInicio.ACCION_INDEX);

		assertNotEquals(MENSAJE_NOTICIAS_DEVUELTAS, 0, bean.getNoticias().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_NOTICIA_PUBLICA, true, bean.getNoticias().get(0).isPublica());
	}
	
	/** obtener documento con usuario anónimo .
	 * @throws IOException si error de io
	 */
	@Test
	public void testA07ObtenerDocumentoPublico() throws IOException {
		VistaInicio bean = obtenerInicioPublico(ControladorInicio.ACCION_DOCUMENTOS);

		assertNotEquals(MENSAJE_DOCUMENTOS_DEVUELTOS, 0, bean.getFicheros().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
		assertEquals(MENSAJE_FICHERO_PUBLICO, true, bean.getFicheros().get(0).isPublico());
	}
	
	/** obtener todas las noticias con parámetro 'noticias' no válido .
	 * @throws IOException .
	 */
	@Test
	public void testE01ObtenerTodasNoticiasParametroNoValido() throws IOException {
		VistaInicio bean = obtenerInicio(ControladorInicio.ACCION_INDEX);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorInicio.PARAM_ACCION, ControladorInicio.ACCION_LISTAR_TODAS_NOTICIAS);
		peticion.setParameter(ControladorInicio.PARAM_NOTICIAS, bean.getNoticias().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorInicio controlador = new ControladorInicio();
		controlador.doPost(peticion, respuesta);
		VistaInicio bean2 = (VistaInicio) peticion.getUVDatos().getVistas().get(VistaInicio.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
}
