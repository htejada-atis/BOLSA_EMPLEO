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
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaSolicitudes;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorMisSolicitudes;

/** test controlador mis solicitudes.
 * @author ATISoluciones
 *
 */
public class TestBEPControladorMisSolicitudes {
	
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de exito debe coincidir";
	private static final String MENSAJE_MERITOS_DEVUELTOS = "Debe devolver méritos";
	private static final String MENSAJE_MERITO_DEVUELTO = "Debe devolver mérito";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en io
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    // método para obtener la vista con una lista de solicitudes .
    private VistaSolicitudes obtenerSolicitudes() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DATATABLE_SOLICITUDES);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		return (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
    }
    
    // método para obtener la vista con una lista de solicitudes del usuario .
    private VistaSolicitudes obtenerBolsas() throws ServletException, IOException {
    	VistaSolicitudes bean = obtenerSolicitudes();
    	
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DATATABLE_BOLSAS_SELECCIONADAS);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		return (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
    }
    
    // método para obtener la vista con una lista de méritos de una bolsa .
    private VistaSolicitudes obtenerMeritosBolsa() throws ServletException, IOException {
    	VistaSolicitudes bean = obtenerSolicitudes();
    	VistaSolicitudes bean2 = obtenerBolsas();
    	
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_DATATABLE_MERITOS_BOLSA);
		peticion.setParameter(ControladorMisSolicitudes.PARAM_SOLICITUD_ID, bean.getDatatableSolicitudes().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorMisSolicitudes.PARAM_BOLSA, bean2.getDatatableBolsasSolicitud().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		return (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
    }
    
    
    
    /** Obtener mis solicitudes, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		
		VistaSolicitudes bean = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener mis solicitudes, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA02() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorMisSolicitudes.PARAM_ACCION, ControladorMisSolicitudes.ACCION_CONSULTAR_SOLICITUD);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorMisSolicitudes controlador = new ControladorMisSolicitudes();
		controlador.doGet(peticion, respuesta);
		VistaSolicitudes bean = (VistaSolicitudes) peticion.getUVDatos().getVistas().get(VistaSolicitudes.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	
	
	/** obtener datatable méritos de una bolsa .
	 * @throws SQLException .
	 * @throws ServletException .
	 * @throws IOException .
	 */
	@Test
	public void testA03ObtenerMeritos() throws SQLException, ServletException, IOException {
		VistaSolicitudes bean = obtenerMeritosBolsa();
		
		assertNotEquals(MENSAJE_MERITOS_DEVUELTOS, 0, bean.getDataTableMeritos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
}
