package controlador.bolsaempleo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import org.junit.BeforeClass;
import org.junit.Test;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaTitulacionesArea;
import es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo.ControladorGestionTitulacionesPreferentesArea;


/** test controlador titulacion.
 * @author ATISoluciones 
 */
public class TestControladorGestionTitulacionesPreferentesArea {
	
	private static final String MENSAJE_AREAS_DEVUELTAS = "Debe devolver areas";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd .
     * @throws IOException si error en ficheros .
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    private VistaTitulacionesArea getVistaTitulacionesArea(String action) throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
    	
    	if (action != null) {
    		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_ACCION, action);	
    	}
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulacionesPreferentesArea controlador = new ControladorGestionTitulacionesPreferentesArea();
		controlador.doGet(peticion, respuesta);
		
		return (VistaTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaTitulacionesArea.class.getName());
    }
                
	/** Obtener areas, sin parametro definido .
	 * @throws SQLException si fallo bd  .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA01Obtener() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaTitulacionesArea(null);
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getAreas().size());
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener listado de areas .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA02Obtener() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaTitulacionesArea(ControladorGestionTitulacionesPreferentesArea.ACCION_LISTAR_AREAS);
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getAreas().size());
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable titulaciones .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA03Obtener() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaTitulacionesArea(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES);
		
		assertNotEquals(0, bean.getDatatableTitulaciones().getData().size());	
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable titulaciones preferentes por área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA04Obtener() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaTitulacionesArea(ControladorGestionTitulacionesPreferentesArea.ACCION_LISTAR_AREAS);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_ACCION, ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES_AREA);
		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_AREA, bean.getAreas().get(0).getCodNum().toString());
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulacionesPreferentesArea controlador = new ControladorGestionTitulacionesPreferentesArea();
		controlador.doPost(peticion, respuesta);
		VistaTitulacionesArea bean2 = (VistaTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaTitulacionesArea.class.getName());
		
		assertNotEquals(0, bean2.getDatatableTitulaciones().getData().size());	
		assertEquals(0, bean2.getMensajesDeError().size());
		assertEquals(0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** incluir una titulación preferente dentro de un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA05IncluirTitulacionArea() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaTitulacionesArea(ControladorGestionTitulacionesPreferentesArea.ACCION_LISTAR_AREAS);
		VistaTitulacionesArea bean2 = getVistaTitulacionesArea(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_ACCION, ControladorGestionTitulacionesPreferentesArea.ACCION_INCLUIR_TITULACION_AREA);
		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_AREA, bean.getAreas().get(0).getCodNum().toString());
		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_TITULACIONES,
				"[" + bean2.getDatatableTitulaciones().getData().get(0).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulacionesPreferentesArea controlador = new ControladorGestionTitulacionesPreferentesArea();
		controlador.doPost(peticion, respuesta);
		VistaTitulacionesArea bean3 = (VistaTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaTitulacionesArea.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
	/** eliminar una titulación preferente dentro de un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA06EliminarTitulacionArea() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaTitulacionesArea(ControladorGestionTitulacionesPreferentesArea.ACCION_LISTAR_AREAS);
		VistaTitulacionesArea bean2 = getVistaTitulacionesArea(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticada();
		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_ACCION, ControladorGestionTitulacionesPreferentesArea.ACCION_ELIMINAR_TITULACION_AREA);
		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_AREA, bean.getAreas().get(0).getCodNum().toString());
		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_TITULACIONES, 
				"[" + bean2.getDatatableTitulaciones().getData().get(0).getCodNum().toString() + "]");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulacionesPreferentesArea controlador = new ControladorGestionTitulacionesPreferentesArea();
		controlador.doPost(peticion, respuesta);
		VistaTitulacionesArea bean3 = (VistaTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaTitulacionesArea.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean3.getMensajesDeAdvertencia().size());
	}
	
}
