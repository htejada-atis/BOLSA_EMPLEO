package controlador;

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
public class TestBEPControladorGestionTitulacionesPreferentesArea {
	
	private static final String MENSAJE_AREAS_DEVUELTAS = "Debe devolver areas";
	private static final String MENSAJE_TITULACIONES_DEVUELTAS = "Debe devolver titulaciones";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd .
     * @throws IOException si error en ficheros .
     */
    @BeforeClass
    public static void preparaBd() throws IOException, SQLException {
    	BbddRunner.conectarBd();
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    // método para obtener la vista con una lista de áreas .
    private VistaTitulacionesArea getVistaConAreas() throws ServletException, IOException {
    	PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
    	peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_ACCION, ControladorGestionTitulacionesPreferentesArea.ACCION_LISTAR_AREAS);	
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulacionesPreferentesArea controlador = new ControladorGestionTitulacionesPreferentesArea();
		controlador.doGet(peticion, respuesta);
		
		return (VistaTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaTitulacionesArea.class.getName());
    }
    
    // método para obtener la vista con una lista de las titulaciones .
    private VistaTitulacionesArea getVistaConTitulaciones(String accion, boolean area) throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_ACCION, accion);
		
		if (area) {
			VistaTitulacionesArea bean = getVistaConAreas();
			peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_AREA, bean.getAreas().get(0).getCodNum().toString());
		}
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulacionesPreferentesArea controlador = new ControladorGestionTitulacionesPreferentesArea();
		controlador.doPost(peticion, respuesta);
		
		return (VistaTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaTitulacionesArea.class.getName());
    }
    
    // método para obtener la vista con la respuesta de incluir o eliminar una titulación preferente
    private VistaTitulacionesArea getVistaCambiarTitulacionesPreferentes(String accion, boolean area, String listaTitulaciones) throws ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_ACCION, accion);
		peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_TITULACIONES, listaTitulaciones);
		
		if (area) {
			VistaTitulacionesArea bean = getVistaConAreas();
			peticion.setParameter(ControladorGestionTitulacionesPreferentesArea.PARAM_AREA, bean.getAreas().get(0).getCodNum().toString());
		}
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulacionesPreferentesArea controlador = new ControladorGestionTitulacionesPreferentesArea();
		controlador.doPost(peticion, respuesta);
		return (VistaTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaTitulacionesArea.class.getName());
    }
    
                
	/** Obtener areas, sin parametro definido .
	 * @throws SQLException si fallo bd  .
	 * @throws IOException si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA01Obtener() throws SQLException, ServletException, IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorGestionTitulacionesPreferentesArea controlador = new ControladorGestionTitulacionesPreferentesArea();
		controlador.doGet(peticion, respuesta);
		
		VistaTitulacionesArea bean = (VistaTitulacionesArea) peticion.getUVDatos().getVistas().get(VistaTitulacionesArea.class.getName());
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getAreas().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener listado de areas .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA02Obtener() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaConAreas();
		
		assertNotEquals(MENSAJE_AREAS_DEVUELTAS, 0, bean.getAreas().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable titulaciones .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA03ObtenerTitulaciones() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaConTitulaciones(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES, true);
		
		assertNotEquals(MENSAJE_TITULACIONES_DEVUELTAS, 0, bean.getDatatableTitulaciones().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable titulaciones preferentes por área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA04ObtenerTitulacionesPreferentesArea() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaConTitulaciones(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES_PREFERENTES_AREA, true);
		
		assertNotEquals(MENSAJE_TITULACIONES_DEVUELTAS, 0, bean.getDatatableTitulaciones().getData().size());	
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** eliminar una titulaciones preferentes dentro de un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA05EliminarTitulacionesArea() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaConTitulaciones(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES, true);
		VistaTitulacionesArea bean2 = getVistaCambiarTitulacionesPreferentes(ControladorGestionTitulacionesPreferentesArea.ACCION_ELIMINAR_TITULACION_AREA,
				true, "[" + bean.getDatatableTitulaciones().getData().get(0).getCodNum().toString() + "]");
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** incluir titulaciones preferentes dentro de un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testA06IncluirTitulacionesArea() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaConTitulaciones(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES, true);
		VistaTitulacionesArea bean2 = getVistaCambiarTitulacionesPreferentes(ControladorGestionTitulacionesPreferentesArea.ACCION_INCLUIR_TITULACION_AREA,
				true, "[" + bean.getDatatableTitulaciones().getData().get(0).getCodNum().toString() + "]");
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** eliminar ids de titulaciones no válidas  .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE01EliminarTitulacionesNoValidas() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaCambiarTitulacionesPreferentes(ControladorGestionTitulacionesPreferentesArea.ACCION_ELIMINAR_TITULACION_AREA, true, "");
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** incluir ids de titulaciones no válidas  .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE02IncluirTitulacionesNoValidas() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaCambiarTitulacionesPreferentes(ControladorGestionTitulacionesPreferentesArea.ACCION_INCLUIR_TITULACION_AREA, true, "");
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** eliminar titulaciones con área nula .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE03EliminarTitulacionesAreaNula() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaConTitulaciones(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES, true);
		VistaTitulacionesArea bean2 = getVistaCambiarTitulacionesPreferentes(ControladorGestionTitulacionesPreferentesArea.ACCION_ELIMINAR_TITULACION_AREA,
				false, "[" + bean.getDatatableTitulaciones().getData().get(0).getCodNum().toString() + "]");
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** incluir titulaciones con área nula .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE04IncluirTitulacionesAreaNula() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaConTitulaciones(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES, true);
		VistaTitulacionesArea bean2 = getVistaCambiarTitulacionesPreferentes(ControladorGestionTitulacionesPreferentesArea.ACCION_INCLUIR_TITULACION_AREA,
				false, "[" + bean.getDatatableTitulaciones().getData().get(0).getCodNum().toString() + "]");
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** incluir titulaciones ya incluidas en un área .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE05RepetirIncluirTitulacionesArea() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaConTitulaciones(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES, true);
		getVistaCambiarTitulacionesPreferentes(ControladorGestionTitulacionesPreferentesArea.ACCION_INCLUIR_TITULACION_AREA,
				true, "[" + bean.getDatatableTitulaciones().getData().get(0).getCodNum().toString() + "]");
		VistaTitulacionesArea bean3 = getVistaCambiarTitulacionesPreferentes(ControladorGestionTitulacionesPreferentesArea.ACCION_INCLUIR_TITULACION_AREA,
				true, "[" + bean.getDatatableTitulaciones().getData().get(0).getCodNum().toString() + "]");
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean3.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean3.getMensajesDeExito().size());
	}
	
	/** datatable titulaciones sin área definida .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE06ObtenerTitulacionesSinArea() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaConTitulaciones(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES, false);
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** datatable titulaciones preferentes a un área sin área definida .
	 * @throws SQLException si fallo bd .
	 * @throws IOException si error io .
	 * @throws ServletException  si error servlet .
	 */
	@Test
	public void testE07ObtenerTitulacionesPreferentesSinArea() throws SQLException, ServletException, IOException {
		VistaTitulacionesArea bean = getVistaConTitulaciones(ControladorGestionTitulacionesPreferentesArea.ACCION_DATATABLE_TITULACIONES_PREFERENTES_AREA, false);
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
}
