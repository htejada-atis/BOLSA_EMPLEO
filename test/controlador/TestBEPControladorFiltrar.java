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
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaFiltrar;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorFiltrar;
import es.ujaen.uvirtual.utilidades.UVException;

/** test controlador filtrar.
 * @author jlopez
 *
 */
public class TestBEPControladorFiltrar {
	
	private static final String MENSAJE_CANDIDATO_DEVUELTO = "Debe devolver candidato";
	private static final String MENSAJE_CANDIDATOS_DEVUELTOS = "Debe devolver candida";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de exito debe coincidir";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	private static final String MENSAJE_TITULACIONES_DEVUELTAS = "Debe devolver titulaciones";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en io
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    // método para obtener la vista con una lista de méritos .
    private VistaFiltrar obtenerCandidatos() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_DATATABLE_CANDIDATOS);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		return (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
    }
    
    /** Obtener candidatos, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener candidatos, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA02ObtenerCandidatos() throws SQLException, ServletException, IOException {
		VistaFiltrar bean = obtenerCandidatos();
		
		assertNotEquals(MENSAJE_CANDIDATOS_DEVUELTOS, 0, bean.getDatatableCandidatos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar un candidato .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA02SeleccionarCandidato() throws SQLException, ServletException, IOException {
		VistaFiltrar bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_CANDIDATO_SELECCIONADO);
		peticion.setParameter(ControladorFiltrar.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean2 = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener las titulaciones de un candidato .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA03ObtenerTitulacionesCandidato() throws SQLException, ServletException, IOException {
		VistaFiltrar bean = obtenerCandidatos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorFiltrar.PARAM_ACCION, ControladorFiltrar.ACCION_DATATABLE_TITULACIONES_CANDIDATO);
		peticion.setParameter(ControladorFiltrar.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorFiltrar controlador = new ControladorFiltrar();
		controlador.doGet(peticion, respuesta);
		
		VistaFiltrar bean2 = (VistaFiltrar) peticion.getUVDatos().getVistas().get(VistaFiltrar.class.getName());
		
		assertNotEquals(MENSAJE_TITULACIONES_DEVUELTAS, 0, bean2.getDatatableTitulaciones().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
}
