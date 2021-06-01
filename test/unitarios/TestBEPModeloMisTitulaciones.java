package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMisTitulaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloTitulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * test modelo mis titulaciones.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloMisTitulaciones {
	private static final Integer CODNUM = 1;
	private static final Integer CODNUM_NOEXISTE = 111_111_111;
	private static final Integer CODNUM_CANDITATO = 5;
	private static final Integer TITULACION_CODNUM = 1;
	private static final Integer TITULACION_USUARIO_1 = 1;
	private static final Integer TITULACION_USUARIO_2 = 2;
	private static final Integer TITULACION_USUARIO_3 = 3;
	private static final String DESCRIPCION = "mi titulación";
	private static final String DESCRIPCION_OTRATITULACION = "OTRA TITULAC";
	private static final byte[] CONTENIDO_ARCHIVO = "archivo de prueba".getBytes();	
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";
    
	/** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
	 * @throws ParseException si error fecha
     */
	@BeforeClass
	public static void preparaBd() throws SQLException, IOException {
		DataSource ds = BbddRunner.obtenerDataSourceUv();
		DataSource dsArcos = BbddRunner.obtenerDataSourceArcos();
		Conexion.setConexionUvirtual(ds);
		Conexion.setConexionArcos(dsArcos);
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
	
	/**
	 * getTitulacionById.
	 */
	@Test
	public void testA01getTitulacionById() {
		try {
			TitulacionUsuario item = ModeloMisTitulaciones.obtenerInstancia().getTitulacionUsuarioById(CODNUM);
			assertEquals(item.getCodNum(), CODNUM);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listaTitulacionesDatatable.
	 */
	@Test
	public void testA02listaTitulacionesDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloMisTitulaciones.ORDER_COLUMN_INDEX_NOMBRE)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(CODNUM_CANDITATO);
			
			BolsaEmpleoDataTable<Titulacion> dt = ModeloMisTitulaciones.obtenerInstancia().listaTitulacionesDatatable(params, usuario.getCodNum());
			assertFalse(dt.getData().isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listaTitulacionesUsuarioDatatable.
	 */
	@Test
	public void testA03listaTitulacionesUsuarioDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloMisTitulaciones.ORDER_COLUMN_INDEX_NOMBRE_USUARIO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(CODNUM_CANDITATO);
			
			BolsaEmpleoDataTable<TitulacionUsuario> dt = ModeloMisTitulaciones.obtenerInstancia().listaTitulacionesUsuarioDatatable(params, usuario.getCodNum());
			assertFalse(dt.getData().isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * getTitulacionesUsuarios.
	 */
	@Test
	public void testA04getTitulacionesUsuarios() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloMisTitulaciones.ORDER_COLUMN_INDEX_NOMBRE_USUARIO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			int[] ids = new int[] {TITULACION_USUARIO_1, TITULACION_USUARIO_2, TITULACION_USUARIO_3};
			
			List<TitulacionUsuario> titulaciones = ModeloMisTitulaciones.obtenerInstancia().getTitulacionesUsuarioByIds(ids);
			assertEquals(titulaciones.size(), ids.length);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * insertaTitulacionUsuario.
	 */
	@Test
	public void testA05insertaTitulacionUsuario() {
		try {		
			// insertamos titulacion
			Titulacion t = ModeloTitulacion.obtenerInstancia().getTitulacionById(TITULACION_CODNUM);
			assertEquals(t.getCodNum(), TITULACION_CODNUM);
			TitulacionUsuario tu = insertarTitulacion(t);
			
			// la borramos
			borrarTitulacion(tu);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}

	
	
	/**
	 * insertaTitulacionUsuario.
	 */
	@Test
	public void testA06insertaTitulacionUsuario() {
		try {
			TitulacionUsuario tu = insertarTitulacion(null);			
			borrarTitulacion(tu);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listaTitulacionesValidadasCandidato.
	 */
	@Test
	public void testA07listaTitulacionesValidadasCandidato() {
		try {
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(CODNUM_CANDITATO);
			assertEquals(usuario.getCodNum(), CODNUM_CANDITATO);
			
			List<TitulacionUsuario> listado = ModeloMisTitulaciones.obtenerInstancia().listaTitulacionesValidadasCandidato(usuario.getCodNum());
			assertFalse(listado.isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * validaTitulacion.
	 */
	@Test
	public void testA08validaTitulacion() {
		try {
			Titulacion t = ModeloTitulacion.obtenerInstancia().getTitulacionById(TITULACION_CODNUM);
			TitulacionUsuario tu = insertarTitulacion(t);
			
			Date now = BolsaEmpleoUtils.getCurrentDateTime();
			ModeloMisTitulaciones.obtenerInstancia().validaTitulacion(tu, tu.getUsuario(), now, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
			TitulacionUsuario tuValidado = ModeloMisTitulaciones.obtenerInstancia().getTitulacionUsuarioById(tu.getCodNum());			
			assertEquals(tuValidado.getCodNum(), tu.getCodNum());
			assertTrue(tuValidado.getValidada());
			assertEquals(Formateador.formatoFecha(tuValidado.getFechaValidada(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS), 
					Formateador.formatoFecha(now, Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS));
			
			borrarTitulacion(tu);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * desvalidaTitulacion.
	 */
	@Test
	public void testA09desvalidaTitulacion() {
		try {
			Titulacion t = ModeloTitulacion.obtenerInstancia().getTitulacionById(TITULACION_CODNUM);
			TitulacionUsuario tu = insertarTitulacion(t);
			
			ModeloMisTitulaciones.obtenerInstancia().desvalidaTitulacion(tu, tu.getUsuario(), UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
			TitulacionUsuario tuNew = ModeloMisTitulaciones.obtenerInstancia().getTitulacionUsuarioById(tu.getCodNum());			
			assertEquals(tuNew.getCodNum(), tu.getCodNum());
			assertFalse(tuNew.getValidada());
			assertNull(tuNew.getFechaValidada());
			
			borrarTitulacion(tu);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	// ERRORES
	
	/**
	 * getTitulacionById.
	 */
	@Test
	public void testE01getTitulacionById() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().getTitulacionUsuarioById(CODNUM_NOEXISTE));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_NO_EXISTE_TITULACION, throwable.getMessage());
	}
	
	/**
	 * listaTitulacionesUsuarioDatatable.
	 */
	@Test
	public void testE02listaTitulacionesUsuarioDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloMisTitulaciones.ORDER_COLUMN_INDEX_NOMBRE_USUARIO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});
		
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().listaTitulacionesUsuarioDatatable(params, null));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_EL_USUARIO_ES_REQUERIDO, throwable.getMessage());
	}
	
	/**
	 * insertaTitulacionUsuario.
	 */
	@Test
	public void testE03insertaTitulacionUsuario() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().insertaTitulacionUsuario(null, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_TITULACION_VACIA, throwable.getMessage());
		
		throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().insertaTitulacionUsuario(new TitulacionUsuario(), UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_TITULACION_SIN_ARCHIVO, throwable.getMessage());
	}
	
	/**
	 * validaTitulacion.
	 */
	@Test
	public void testE04validaTitulacion() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().validaTitulacion(null, null, null, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_TITULACION_OBLIGATORIA, throwable.getMessage());
		
		TitulacionUsuario t = new TitulacionUsuario();		
		throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().validaTitulacion(t, null, null, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_ID_TITULACION_OBLIGATORIO, throwable.getMessage());
		
		t.setCodNum(CODNUM);
		throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().validaTitulacion(t, null, null, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_CANDIDATO_OBLIGATORIO, throwable.getMessage());
		
		UsuarioBolsaEmpleo u = new UsuarioBolsaEmpleo();
		throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().validaTitulacion(t, u, null, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_ID_USUARIO_NO_VALIDO, throwable.getMessage());
		
		u.setCodNum(CODNUM);
		throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().validaTitulacion(t, u, null, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_SIN_TITULACION, throwable.getMessage());
	}
	
	/**
	 * desvalidaTitulacion.
	 */
	@Test
	public void testE05validaTitulacion() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().desvalidaTitulacion(null, null, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_TITULACION_OBLIGATORIA, throwable.getMessage());
		
		TitulacionUsuario t = new TitulacionUsuario();		
		throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().desvalidaTitulacion(t, null, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_ID_TITULACION_OBLIGATORIO, throwable.getMessage());
		
		t.setCodNum(CODNUM);
		throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().desvalidaTitulacion(t, null, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_CANDIDATO_OBLIGATORIO, throwable.getMessage());
		
		UsuarioBolsaEmpleo u = new UsuarioBolsaEmpleo();
		throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().desvalidaTitulacion(t, u, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_ID_USUARIO_NO_VALIDO, throwable.getMessage());
		
		u.setCodNum(CODNUM);
		throwable = assertThrows(Throwable.class,
				() -> ModeloMisTitulaciones.obtenerInstancia().desvalidaTitulacion(t, u, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado()));		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMisTitulaciones.ERROR_SIN_TITULACION, throwable.getMessage());
	}
	
	private TitulacionUsuario insertarTitulacion(Titulacion t) throws SQLException, UVException {			
		ByteArrayInputStream archivo = new ByteArrayInputStream(CONTENIDO_ARCHIVO);
		
		TitulacionUsuario tusuario = new TitulacionUsuario();
		tusuario.setArchivo(archivo);
		tusuario.setBorrado(false);
		tusuario.setDescripcion(DESCRIPCION);
		
		if (t != null) {
			tusuario.setTitulacion(t);
			tusuario.setOtraTitulacion(null);	
		} else {
			tusuario.setTitulacion(null);
			tusuario.setOtraTitulacion(DESCRIPCION_OTRATITULACION);
		}
		
		UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(CODNUM_CANDITATO);
		assertEquals(usuario.getCodNum(), CODNUM_CANDITATO);
		tusuario.setUsuario(usuario);
		
		Integer codNum = ModeloMisTitulaciones.obtenerInstancia().insertaTitulacionUsuario(tusuario, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		TitulacionUsuario tusuarioNew = ModeloMisTitulaciones.obtenerInstancia().getTitulacionUsuarioById(codNum);
		
		assertEquals(BolsaEmpleoUtils.inputStreamToString(tusuario.getArchivo()), BolsaEmpleoUtils.inputStreamToString(archivo));
		assertEquals(tusuarioNew.getBorrado(), false);
		assertEquals(tusuarioNew.getDescripcion(), DESCRIPCION);
		
		if (t != null) {
			assertEquals(tusuarioNew.getTitulacion(), t);
			assertEquals(tusuarioNew.getOtraTitulacion(), null);
		} else {
			assertEquals(tusuarioNew.getTitulacion(), null);
			assertEquals(tusuarioNew.getOtraTitulacion(), DESCRIPCION_OTRATITULACION);
		}
		
		return tusuarioNew;
	}

	private void borrarTitulacion(TitulacionUsuario tu) throws SQLException, UVException {
		ArrayList<TitulacionUsuario> titulacionesABorrar = new ArrayList<>();
		titulacionesABorrar.add(tu);
		ModeloMisTitulaciones.obtenerInstancia().borraTitulacionUsuario(titulacionesABorrar, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		TitulacionUsuario tusuarioDel = ModeloMisTitulaciones.obtenerInstancia().getTitulacionUsuarioById(tu.getCodNum());
		assertTrue(tusuarioDel.getBorrado());
		assertNotNull(tusuarioDel.getFechaBorrado());
	}
}
