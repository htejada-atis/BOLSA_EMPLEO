package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.configuracion.ControladorAfinidades;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAfinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaAfinidades;

/**
 * test controlador afinidades .
 * 
 * @author ATISoluciones
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorAfinidades {
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";
	private static final String DESCRIPCION = "--NUEVA AFINDIAD--";
	private static final String DESCRIPCION_EDIT = "--NUEVA AFINDIAD--EDITADA--";
	private static final String CODIGO = "BEP";
	private static final String CODIGO_EDIT = "BEP2";
	private static final String MODULACION = "0.45";
	private static final String MODULACION_EDIT = "0.55";
	
	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException si error en bd
	 * @throws IOException  si error en ficheros
	 */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}

	/**
	 * Opción por defecto y errores comunes del controlador.
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA01() {
		// accion por defecto
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();		
		VistaAfinidades bean = post(peticion);
		assertTrue(bean.getMensajesDeError().isEmpty());
		assertTrue(bean.getMensajesDeAdvertencia().isEmpty());
		
		// accion desconocida
		peticion.setParameter(ControladorAfinidades.PARAM_ACCION, "dummy");
		bean = post(peticion);
		assertFalse(bean.getMensajesDeError().isEmpty());
		assertEquals(bean.getMensajesDeError().get(0), ControladorAfinidades.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA);
		
		// rol erroneo
		peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();		
		bean = post(peticion);
		assertFalse(bean.getMensajesDeError().isEmpty());
		assertEquals(bean.getMensajesDeError().get(0), ControladorAfinidades.MENSAJE_ERROR_SIN_PERMISOS);		
	}

	/**
	 * Listado de afinidades.
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA02() {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorAfinidades.PARAM_ACCION, ControladorAfinidades.ACCION_DATATABLE);
		VistaAfinidades bean = post(peticion);
		assertFalse(bean.getDatatableAfinidades().getData().isEmpty());
		assertTrue(bean.getMensajesDeError().isEmpty());
		assertTrue(bean.getMensajesDeAdvertencia().isEmpty());
	}
	
	/**
	 * Creamos afinidad.
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA03() {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		
		// formulario
		peticion.setParameter(ControladorAfinidades.PARAM_ACCION, ControladorAfinidades.ACCION_FORMULARIO_AFINIDAD);
		VistaAfinidades bean = post(peticion);
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
		
		// confirmación create
		peticion.setParameter(ControladorAfinidades.PARAM_ACCION, ControladorAfinidades.ACCION_AGREGAR_AFINIDAD);
		peticion.setParameter(ControladorAfinidades.PARAM_DESCRIPCION, DESCRIPCION);
		peticion.setParameter(ControladorAfinidades.PARAM_CODIGO, CODIGO);
		peticion.setParameter(ControladorAfinidades.PARAM_MODULACION, MODULACION);		
		bean = post(peticion);
		assertTrue(bean.getMensajesDeError().isEmpty());
		assertTrue(bean.getMensajesDeAdvertencia().isEmpty());
		assertFalse(bean.getMensajesDeExito().isEmpty());
		assertEquals(bean.getMensajesDeExito().get(0), ControladorAfinidades.MENSAJE_INFO_AFINIDAD_INSERTADA_CORRECTAMENTE);
		
		Afinidad creada = getAfinidadListadoByDescripcion(DESCRIPCION); 
		assertNotNull(creada);
		assertEquals(creada.getDescripcion(), DESCRIPCION);
		assertEquals(creada.getCodigo(), CODIGO);
		assertEquals(creada.getModulacion().toString(), MODULACION);
	}
	
	/**
	 * Editamos.
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA04() {
		// leemos afinidad
		Afinidad afinidad = getAfinidadListadoByDescripcion(DESCRIPCION);
		assertNotNull(afinidad);
		
		// edit
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorAfinidades.PARAM_ACCION, ControladorAfinidades.ACCION_MODIFICAR_AFINIDAD);
		peticion.setParameter(ControladorAfinidades.PARAM_ID, afinidad.getCodNum().toString());
		peticion.setParameter(ControladorAfinidades.PARAM_DESCRIPCION, DESCRIPCION_EDIT);
		peticion.setParameter(ControladorAfinidades.PARAM_CODIGO, CODIGO_EDIT);
		peticion.setParameter(ControladorAfinidades.PARAM_MODULACION, MODULACION_EDIT);		
		VistaAfinidades bean = post(peticion);
		assertTrue(bean.getMensajesDeError().isEmpty());
		assertTrue(bean.getMensajesDeAdvertencia().isEmpty());
		assertFalse(bean.getMensajesDeExito().isEmpty());
		assertEquals(bean.getMensajesDeExito().get(0), ControladorAfinidades.MENSAJE_INFO_AFINIDAD_ACTUALIZADA_CORRECTAMENTE);
		
		Afinidad editada = getAfinidadListadoByDescripcion(DESCRIPCION_EDIT); 
		assertNotNull(editada);
		assertEquals(editada.getDescripcion(), DESCRIPCION_EDIT);
		assertEquals(editada.getCodigo(), CODIGO_EDIT);
		assertEquals(editada.getModulacion().toString(), MODULACION_EDIT);
	}
	
	/**
	 * Borramos.
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	public void testA05() {
		// leemos afinidad
		Afinidad afinidad = getAfinidadListadoByDescripcion(DESCRIPCION_EDIT);
		assertNotNull(afinidad);
		
		// borrar
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorAfinidades.PARAM_ACCION, ControladorAfinidades.ACCION_BORRAR_AFINIDAD);
		peticion.setParameter(ControladorAfinidades.PARAM_AFINIDADES_SELECCIONADAS, "[" + afinidad.getCodNum() + "]");
		VistaAfinidades bean = post(peticion);
		assertTrue(bean.getMensajesDeError().isEmpty());
		assertTrue(bean.getMensajesDeAdvertencia().isEmpty());
		assertFalse(bean.getMensajesDeExito().isEmpty());
		assertEquals(bean.getMensajesDeExito().get(0), ControladorAfinidades.MENSAJE_EXITO_ELIMINAR);
		
		Afinidad editada = getAfinidadListadoByDescripcion(DESCRIPCION_EDIT); 
		assertNull(editada);
	}
	
	/**
	 * Errores validación.
	 * 
	 * @throws SQLException     si fallo bd .
	 * @throws IOException      si error io .
	 * @throws ServletException si error servlet .
	 */
	@Test
	@SuppressWarnings({"checkstyle:ExecutableStatementCount"})
	public void testE01() {		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorAfinidades.PARAM_ACCION, ControladorAfinidades.ACCION_AGREGAR_AFINIDAD);
		
		// codigo
		peticion.setParameter(ControladorAfinidades.PARAM_DESCRIPCION, "aa");
		peticion.setParameter(ControladorAfinidades.PARAM_CODIGO, null);		
		VistaAfinidades bean = post(peticion);
		assertFalse(bean.getMensajesDeError().isEmpty());
		assertEquals(bean.getMensajesDeError().get(0), ControladorAfinidades.MENSAJE_ERROR_CODIGO_REQUERIDO);
		
		peticion.setParameter(ControladorAfinidades.PARAM_DESCRIPCION, "aa");
		peticion.setParameter(ControladorAfinidades.PARAM_CODIGO, " ");
		bean = post(peticion);
		assertFalse(bean.getMensajesDeError().isEmpty());
		assertEquals(bean.getMensajesDeError().get(0), ControladorAfinidades.MENSAJE_ERROR_CODIGO_REQUERIDO);
		
		peticion.setParameter(ControladorAfinidades.PARAM_DESCRIPCION, "aa");
		peticion.setParameter(ControladorAfinidades.PARAM_CODIGO, UtilsTestBolsaEmpleo.generateRandomString(ModeloAfinidad.COLUMN_CODIGO_MAXLENGTH + 1));
		bean = post(peticion);
		assertFalse(bean.getMensajesDeError().isEmpty());
		assertEquals(bean.getMensajesDeError().get(0), String.format(ControladorAfinidades.MENSAJE_ERROR_CODIGO_LARGA, ModeloAfinidad.COLUMN_CODIGO_MAXLENGTH));
		
		// descripcion
		peticion.setParameter(ControladorAfinidades.PARAM_CODIGO, "AA");
		peticion.setParameter(ControladorAfinidades.PARAM_DESCRIPCION, "  ");		
		bean = post(peticion);
		assertFalse(bean.getMensajesDeError().isEmpty());
		assertEquals(bean.getMensajesDeError().get(0), ControladorAfinidades.MENSAJE_ERROR_DESCRIPCION_REQUERIDA);
		
		peticion.setParameter(ControladorAfinidades.PARAM_CODIGO, "AA");
		peticion.setParameter(ControladorAfinidades.PARAM_DESCRIPCION, UtilsTestBolsaEmpleo.generateRandomString(ModeloAfinidad.COLUMN_DESCRIPCION_MAXLENGTH + 1));
		bean = post(peticion);
		assertFalse(bean.getMensajesDeError().isEmpty());
		assertEquals(bean.getMensajesDeError().get(0), String.format(ControladorAfinidades.MENSAJE_ERROR_DESCRIPCION_LARGA, ModeloAfinidad.COLUMN_DESCRIPCION_MAXLENGTH));
		
		// modulacion
		peticion.setParameter(ControladorAfinidades.PARAM_CODIGO, "AA");
		peticion.setParameter(ControladorAfinidades.PARAM_DESCRIPCION, "aa");
		peticion.setParameter(ControladorAfinidades.PARAM_MODULACION, "dummy");
		bean = post(peticion);
		assertFalse(bean.getMensajesDeError().isEmpty());
		assertEquals(bean.getMensajesDeError().get(0), ControladorAfinidades.MENSAJE_ERROR_MODULACION_VACIA);		
	}
	
	// método para obtener la vista con listado de afinidades .
	private VistaAfinidades post(PeticionHttp peticion) {
		try {
			RespuestaHttp respuesta = new RespuestaHttp();
			ControladorAfinidades controlador = new ControladorAfinidades();
			controlador.doPost(peticion, respuesta);
			return (VistaAfinidades) peticion.getUVDatos().getVistas().get(VistaAfinidades.class.getName());			
		} catch (Exception ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
		return null;
	}

	private Afinidad getAfinidadListadoByDescripcion(String descripcion) {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorAfinidades.PARAM_ACCION, ControladorAfinidades.ACCION_DATATABLE);
		VistaAfinidades bean = post(peticion);
		assertFalse(bean.getDatatableAfinidades().getData().isEmpty());
		assertEquals(0, bean.getMensajesDeError().size());
		assertEquals(0, bean.getMensajesDeAdvertencia().size());
		
		for (Afinidad a : bean.getDatatableAfinidades().getData()) {
			if (a.getDescripcion().equals(descripcion)) {
				return a;
			}
		}
		return null;
	}
	
	 
}
