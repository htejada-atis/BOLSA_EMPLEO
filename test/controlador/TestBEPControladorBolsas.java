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
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorBolsas;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaEstadoBolsas;


/** test controlador convocatoria crud.
 * @author ATISoluciones 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorBolsas {	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    private VistaEstadoBolsas getVistaBolsasEmpleo(String action) throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
    	
    	if (action != null) {
    		peticion.setParameter(ControladorBolsas.PARAM_ACCION, action);
    	}
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorBolsas controlador = new ControladorBolsas();
		controlador.doGet(peticion, respuesta);
		
		return (VistaEstadoBolsas) peticion.getUVDatos().getVistas().get(VistaEstadoBolsas.class.getName());
    }
                
	/** Obtener bolsas, sin parametro definido .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA01() throws SQLException, ServletException, IOException {
		VistaEstadoBolsas bean = getVistaBolsasEmpleo(null);
		
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener listado de bolsas.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA02() throws SQLException, ServletException, IOException {
		VistaEstadoBolsas bean = getVistaBolsasEmpleo(ControladorBolsas.ACCION_INDEX);
		
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA03() throws SQLException, ServletException, IOException {
		VistaEstadoBolsas bean = getVistaBolsasEmpleo(ControladorBolsas.ACCION_DATATABLE);
		
		assertNotEquals(0, bean.getDatatableBolsas().getData().size());
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Acción sobre una bolsa.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA04() throws SQLException, ServletException, IOException {
		this.cambiarEstadoBolsa(ControladorBolsas.ACCION_BOLSAS_BLOQUEAR, ModeloBolsa.BOLSA_ESTADO_BLOQUEADA);		
	}
	
	/** Acción sobre una bolsa.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA05() throws SQLException, ServletException, IOException {
		this.cambiarEstadoBolsa(ControladorBolsas.ACCION_BOLSAS_REVISION, ModeloBolsa.BOLSA_ESTADO_REVISION);		
	}
	
	/** Acción sobre una bolsa.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA06() throws SQLException, ServletException, IOException {
		this.cambiarEstadoBolsa(ControladorBolsas.ACCION_BOLSAS_BAREMACION, ModeloBolsa.BOLSA_ESTADO_BAREMACION);		
	}
	
	/** Acción sobre una bolsa.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA07() throws SQLException, ServletException, IOException {
		this.cambiarEstadoBolsa(ControladorBolsas.ACCION_BOLSAS_ALEGACION, ModeloBolsa.BOLSA_ESTADO_ALEGACIONES);		
	}
	
	/** Acción sobre una bolsa.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA08() throws SQLException, ServletException, IOException {
		this.cambiarEstadoBolsa(ControladorBolsas.ACCION_BOLSAS_DESBLOQUEAR, ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA);		
	}
	
	/** Baremar bolsas.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA09() throws SQLException, ServletException, IOException {
		// leemos algunos ids devueltos
		VistaEstadoBolsas bean = getVistaBolsasEmpleo(ControladorBolsas.ACCION_DATATABLE);
		String selected = "[" 
				+ bean.getDatatableBolsas().getData().get(0).getCodNum() + "," 
				+ bean.getDatatableBolsas().getData().get(1).getCodNum() + "]";
								
		// petición para bloquear bolsas
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorBolsas.PARAM_ACCION, ControladorBolsas.ACCION_BOLSA);		
		peticion.setParameter(ControladorBolsas.PARAM_ACCION_BOLSA, ControladorBolsas.ACCION_BOLSAS_BAREMAR);
    	peticion.setParameter(ControladorBolsas.PARAM_BOLSAS_SELECCIONADAS, selected);    	
    	RespuestaHttp respuesta = new RespuestaHttp();
    	ControladorBolsas controlador = new ControladorBolsas();
    	controlador.doPost(peticion, respuesta);
    	bean = (VistaEstadoBolsas) peticion.getUVDatos().getVistas().get(VistaEstadoBolsas.class.getName());
    	assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
		assertEquals(ControladorBolsas.MENSAJE_EXITO_BOLSA_MODIFICADA_CORRECTAMENTE, bean.getMensajesDeExito().get(0));
    	
		// volvemos a pedir los datos
		bean = getVistaBolsasEmpleo(ControladorBolsas.ACCION_DATATABLE);
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Id de bolsas no validas .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA10() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorBolsas.PARAM_ACCION, ControladorBolsas.ACCION_BOLSA);		
		peticion.setParameter(ControladorBolsas.PARAM_ACCION_BOLSA, ControladorBolsas.ACCION_BOLSAS_BLOQUEAR);
		peticion.setParameter(ControladorBolsas.PARAM_BOLSAS_SELECCIONADAS, "");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	ControladorBolsas controlador = new ControladorBolsas();
    	controlador.doPost(peticion, respuesta);    	
    	VistaEstadoBolsas bean = (VistaEstadoBolsas) peticion.getUVDatos().getVistas().get(VistaEstadoBolsas.class.getName());
    	assertNotEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
		assertNotEquals(-1, bean.getMensajesDeError().get(0).indexOf(ControladorBolsas.MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS));
	}
	
	/** Acción sobre bolsa no válida .
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA11() throws SQLException, ServletException, IOException {
		// leemos algunos ids devueltos
		VistaEstadoBolsas bean = getVistaBolsasEmpleo(ControladorBolsas.ACCION_DATATABLE);
		String selected = "[" 
				+ bean.getDatatableBolsas().getData().get(0).getCodNum() + "," 
				+ bean.getDatatableBolsas().getData().get(1).getCodNum() + "]";
				
		bean = getVistaBolsasEmpleo(ControladorBolsas.ACCION_DATATABLE);
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorBolsas.PARAM_ACCION, ControladorBolsas.ACCION_BOLSA);		
		peticion.setParameter(ControladorBolsas.PARAM_ACCION_BOLSA, "accionmal");
		peticion.setParameter(ControladorBolsas.PARAM_BOLSAS_SELECCIONADAS, selected);
    	RespuestaHttp respuesta = new RespuestaHttp();
    	ControladorBolsas controlador = new ControladorBolsas();
    	controlador.doPost(peticion, respuesta);
    	bean = (VistaEstadoBolsas) peticion.getUVDatos().getVistas().get(VistaEstadoBolsas.class.getName());
    	assertNotEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
		assertEquals(ControladorBolsas.MENSAJE_ERROR_ACCION_BOLSA_NO_VALIDA, bean.getMensajesDeError().get(0));
	}
	
	/** Error al pedir listado.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA12() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorBolsas.PARAM_ACCION, ControladorBolsas.ACCION_DATATABLE);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "ddd");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	ControladorBolsas controlador = new ControladorBolsas();
    	controlador.doGet(peticion, respuesta);
    	VistaEstadoBolsas bean = (VistaEstadoBolsas) peticion.getUVDatos().getVistas().get(VistaEstadoBolsas.class.getName());
    	assertNotEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
		assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, bean.getMensajesDeError().get(0));		
	}
	
	/** Ids de bolsas no válidos.
	 * @throws SQLException si fallo bd 
	 * @throws IOException si error io
	 * @throws ServletException  si error servlet
	 */
	@Test
	public void testA13() throws SQLException, ServletException, IOException {
		// leemos algunos ids devueltos
		VistaEstadoBolsas bean = getVistaBolsasEmpleo(ControladorBolsas.ACCION_DATATABLE);
		String selected = "[111111111111]";
				
		bean = getVistaBolsasEmpleo(ControladorBolsas.ACCION_DATATABLE);
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorBolsas.PARAM_ACCION, ControladorBolsas.ACCION_BOLSA);		
		peticion.setParameter(ControladorBolsas.PARAM_ACCION_BOLSA, "accionmal");
		peticion.setParameter(ControladorBolsas.PARAM_BOLSAS_SELECCIONADAS, selected);
    	RespuestaHttp respuesta = new RespuestaHttp();
    	ControladorBolsas controlador = new ControladorBolsas();
    	controlador.doPost(peticion, respuesta);
    	bean = (VistaEstadoBolsas) peticion.getUVDatos().getVistas().get(VistaEstadoBolsas.class.getName());
    	assertNotEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
		assertNotEquals(-1, bean.getMensajesDeError().get(0).indexOf(ControladorBolsas.MENSAJE_ERROR_BOLSAS_SELECCIONADAS_INCORRECTAS));
	}
	
	private void cambiarEstadoBolsa(String accionSobreBolsas, String estadoFinalBolsa) throws SQLException, ServletException, IOException {
		// leemos algunos ids devueltos
		VistaEstadoBolsas bean = getVistaBolsasEmpleo(ControladorBolsas.ACCION_DATATABLE);
		String selected = "[" 
				+ bean.getDatatableBolsas().getData().get(0).getCodNum() + "," 
				+ bean.getDatatableBolsas().getData().get(1).getCodNum() + "]";
								
		// petición para bloquear bolsas
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorBolsas.PARAM_ACCION, ControladorBolsas.ACCION_BOLSA);
		peticion.setParameter(ControladorBolsas.PARAM_ACCION_BOLSA, accionSobreBolsas);
    	peticion.setParameter(ControladorBolsas.PARAM_BOLSAS_SELECCIONADAS, selected);
    	RespuestaHttp respuesta = new RespuestaHttp();
    	ControladorBolsas controlador = new ControladorBolsas();
    	controlador.doPost(peticion, respuesta);
    	bean = (VistaEstadoBolsas) peticion.getUVDatos().getVistas().get(VistaEstadoBolsas.class.getName());
    	assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
		assertEquals(ControladorBolsas.MENSAJE_EXITO_BOLSA_MODIFICADA_CORRECTAMENTE, bean.getMensajesDeExito().get(0));
    	
		// volvemos a pedir los datos y comprobamos estado ha cambiado
		bean = getVistaBolsasEmpleo(ControladorBolsas.ACCION_DATATABLE);
		assertEquals(estadoFinalBolsa, bean.getDatatableBolsas().getData().get(0).getEstado());
		assertEquals(estadoFinalBolsa, bean.getDatatableBolsas().getData().get(1).getEstado());
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
}
