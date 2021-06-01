package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * test usuarios bolsa empleo.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloUsuarios {

	private static final Integer CODNUM = 1;
	private static final Integer CODNUM_OTHER = 2;
	private static final Integer CODNUM_NOEXISTE = 111_111_111;
	private static final String DOCUMENTO_USUARIO_UJA_NO_EXISTE = "51992686J";
	private static final String DOCUMENTO_USUARIO_BOLSA_NO_EXISTE = "XXXXXXXXX";
	private static final String ATISOLUCIONES_DOCUMENTO = "B18865253";
	private static final String ATISOLUCIONES_CODCUENTA = "atisoluciones";
	private static final String CANDIDATO1_CODCUENTA = "candidato1";
	private static final String CANDIDATO1_DOCUMENTO = "43396488L";
	private static final String CODCUENTA_INEXISTENTE = "DUMMY";
	private static final String RAZON_EXCLUSION = "razon de exclusión";
	private static final Date FECHA_EXCLUSION = BolsaEmpleoUtils.getCurrentDateTime();
	
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";

	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException   si error en bd
	 * @throws IOException    si error en ficheros
	 * @throws ParseException si error fecha
	 */
	@BeforeClass
	public static void preparaBd() throws SQLException, IOException {
		Conexion.setConexionUvirtual(BbddRunner.obtenerDataSourceUv());
		Conexion.setConexionArcos(BbddRunner.obtenerDataSourceArcos());
		Conexion.setConexionUxxiRrhh(BbddRunner.obtenerDataSourceRh());
		Conexion.setConexionUxxiAc(BbddRunner.obtenerDataSourceAc());
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}

	/**
	 * getUsuarioByPrsnif.
	 */
	@Test
	public void testA01GetUsuarioByPrsnif() {
		try {
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByNumeroDocumento(ATISOLUCIONES_DOCUMENTO);
			assertEquals(usuario.getCodCuenta(), ATISOLUCIONES_CODCUENTA);
			assertEquals(usuario.getNumDocumento(), ATISOLUCIONES_DOCUMENTO);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}

	/**
	 * getUsuarioById.
	 */
	@Test
	public void testA02GetUsuarioById() {
		try {
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(CODNUM);
			assertEquals(usuario.getCodNum(), CODNUM);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}

	/**
	 * getUsuarioLogeado.
	 */
	@Test
	public void testA03GetUsuarioLogeado() {
		try {
			Usuario usu = CrearUsuario.usuario(ATISOLUCIONES_CODCUENTA);
			UVDatos datos = new UVDatos();
			datos.setUsuario(usu);

			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioLogeado(datos);
			assertEquals(usuario.getCodCuenta(), ATISOLUCIONES_CODCUENTA);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}

	/**
	 * getUsuarioLogeado borrado.
	 */
	@Test
	public void testA04GetUsuarioLogeadoBorrado() {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		try {
			Usuario usu = CrearUsuario.usuario(ATISOLUCIONES_CODCUENTA);
			UVDatos datos = new UVDatos();
			datos.setUsuario(usu);
			
			// marcamos usuario como borrado
			ArrayList<UsuarioBolsaEmpleo> usuariosBorrar = marcarUsuarioComoBorrado(ATISOLUCIONES_DOCUMENTO);

			// obtenemos usuario logeado borrado
			Throwable throwable = assertThrows(Throwable.class, () -> modelo.getUsuarioLogeado(datos));
			assertEquals(UVException.class, throwable.getClass());
			assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_BORRADO, throwable.getMessage());

			// marcamos usuario como no borrado
			marcarUsuarioComoNoBorrado(usuariosBorrar);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}

	/**
	 * getUsuarioLogeado excluido.
	 */
	@Test
	public void testA05GetUsuarioLogeadoExcluido() {
		ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		try {
			Usuario usu = CrearUsuario.usuario(ATISOLUCIONES_CODCUENTA);
			UVDatos datos = new UVDatos();
			datos.setUsuario(usu);
			
			// marcamos usuario como excluido
			UsuarioBolsaEmpleo usuario = marcarUsuarioComoExcluido(ATISOLUCIONES_DOCUMENTO);

			// obtenemos usuario logeado excluido
			Throwable throwable = assertThrows(Throwable.class, () -> modelo.getUsuarioLogeado(datos));
			assertEquals(UVException.class, throwable.getClass());
			assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_EXCLUIDO, throwable.getMessage());
			
			// marcamos usuario como no excluido
			marcarUsuarioComoNoExcluido(usuario);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * getUsuarioCandidato.
	 */
	@Test
	public void testA06getUsuarioCandidato() {
		Usuario usu = CrearUsuario.usuario(CANDIDATO1_CODCUENTA);
		UVDatos datos = new UVDatos();
		datos.setUsuario(usu);
				
		try {
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioCandidato(datos);
			assertEquals(usuario.getCodCuenta(), CANDIDATO1_CODCUENTA);
			assertEquals(usuario.getNumDocumento(), CANDIDATO1_DOCUMENTO);
			assertTrue(usuario.isCandidato());			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * 	getUsuariosByIds.
	 */
	@Test
	public void testA07getUsuariosByIds() {
		Usuario usu = CrearUsuario.usuario(CANDIDATO1_CODCUENTA);
		UVDatos datos = new UVDatos();
		datos.setUsuario(usu);
				
		try {
			int[] ids = new int[] {CODNUM, CODNUM_OTHER};
			
			List<UsuarioBolsaEmpleo> usuarios = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuariosByIds(ids);			
			assertFalse(usuarios.isEmpty());			
			assertTrue(usuarios.size() == ids.length);
			
			assertEquals(usuarios.get(0).getCodNum(), CODNUM);
			assertEquals(usuarios.get(1).getCodNum(), CODNUM_OTHER);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test datatable.
	 */
	@Test
	public void testA08listaUsuarioBolsaEmpleoDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_TIPO_DOCUMENTO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dt = ModeloUsuarioBolsaEmpleo.obtenerInstancia().listaUsuarioBolsaEmpleoDatatable(params);
			assertFalse(dt.getData().isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test datatable.
	 */
	@Test
	public void testA09listaUsuarioCandidatosBolsaEmpleoDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_TIPO_DOCUMENTO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dt = ModeloUsuarioBolsaEmpleo.obtenerInstancia().listaUsuarioCandidatosBolsaEmpleoDatatable(params);
			assertFalse(dt.getData().isEmpty());
			
			for (UsuarioBolsaEmpleo usuario : dt.getData()) {
				assertTrue(usuario.isCandidato());
			}
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test datatable.
	 */
	@Test
	public void testA10listaUsuarioBorradoBolsaEmpleoDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_TIPO_DOCUMENTO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			// marcamos usuario como borrado
			ArrayList<UsuarioBolsaEmpleo> usuariosBorrar = marcarUsuarioComoBorrado(ATISOLUCIONES_DOCUMENTO);
			
			// listamos usuarios borrados
			BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dt = ModeloUsuarioBolsaEmpleo.obtenerInstancia().listaUsuarioBorradoBolsaEmpleoDatatable(params);
			assertFalse(dt.getData().isEmpty());
			for (UsuarioBolsaEmpleo usuarioDelete : dt.getData()) {
				assertTrue(usuarioDelete.getBorrado());
			}
						
			// marcamos usuario como no borrado
			marcarUsuarioComoNoBorrado(usuariosBorrar);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test datatable.
	 */
	@Test
	public void testA11listaUsuarioExcluidoBolsaEmpleoDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_TIPO_DOCUMENTO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			// marcamos usuario como excluido
			UsuarioBolsaEmpleo usuario = marcarUsuarioComoExcluido(ATISOLUCIONES_DOCUMENTO);

			// listamos usuarios excluidos
			BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dt = ModeloUsuarioBolsaEmpleo.obtenerInstancia().listaUsuarioExcluidoBolsaEmpleoDatatable(params);
			assertFalse(dt.getData().isEmpty());
			for (UsuarioBolsaEmpleo usuarioExclu : dt.getData()) {
				assertTrue(usuarioExclu.getExcluido());
			}
			
			// marcamos usuario como no excluido
			marcarUsuarioComoNoExcluido(usuario);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test datatable.
	 */
	@Test
	public void testA12listaAreasExcluidasPorUsuarioDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloArea.ORDER_COLUMN_INDEX_ID)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			// marcamos usuario como excluido
			UsuarioBolsaEmpleo usuario = marcarUsuarioComoExcluido(ATISOLUCIONES_DOCUMENTO);

			// listamos usuarios excluidos
			BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dt = ModeloUsuarioBolsaEmpleo.obtenerInstancia().listaUsuarioExcluidoBolsaEmpleoDatatable(params);
			assertFalse(dt.getData().isEmpty());
			for (UsuarioBolsaEmpleo usuarioExclu : dt.getData()) {
				assertTrue(usuarioExclu.getExcluido());
			}
			
			// marcamos usuario como no excluido
			marcarUsuarioComoNoExcluido(usuario);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	
	/**
	 * Test insertaUsuario.
	 */
	@Test
	public void testA13insertaUsuario() {
		try {
			UsuarioBolsaEmpleo nuevo = new UsuarioBolsaEmpleo();
			nuevo.setRol(ModeloRol.obtenerInstancia().getRoleById(ModeloRol.ID_ROL_CANDIDATO));
			nuevo.setCodCuenta("candidato3");
			nuevo.setNumDocumento("51906000Z");
			
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().insertaUsuario(nuevo, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
			UsuarioBolsaEmpleo nuevoRead = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByNumeroDocumento("51906000Z");
			
			assertEquals(nuevo.getCodCuenta(), nuevoRead.getCodCuenta());
			assertEquals(nuevo.getNumDocumento(), nuevoRead.getNumDocumento());
			assertEquals(nuevo.getRol(), nuevoRead.getRol());
			assertFalse(nuevoRead.getExcluido());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test insertaUsuario excluido.
	 */
	@Test
	public void testA13insertaUsuarioExcluido() {
		try {
			Date fechaExclusion = BolsaEmpleoUtils.getCurrentDateTime();
			Date fechaInicioExclusion = BolsaEmpleoUtils.getCurrentDateTime();
			Date fechaFinExclusion = BolsaEmpleoUtils.getCurrentDateTime();
			
			UsuarioBolsaEmpleo nuevo = new UsuarioBolsaEmpleo();
			nuevo.setRol(ModeloRol.obtenerInstancia().getRoleById(ModeloRol.ID_ROL_CANDIDATO));
			nuevo.setCodCuenta("candidato4");
			nuevo.setNumDocumento("00834172P");
			nuevo.setExcluido(true);
			nuevo.setExcluidoTipo(ModeloUsuarioBolsaEmpleo.EXCLUSION_TIPO_TEMPORAL);
			nuevo.setRazonExcluido("test");
			nuevo.setFechaExclusion(fechaExclusion);
			nuevo.setFechaExclusionInicio(fechaInicioExclusion);
			nuevo.setFechaExclusionFin(fechaFinExclusion);
			
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().insertaUsuario(nuevo, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
			UsuarioBolsaEmpleo nuevoRead = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByNumeroDocumento("00834172P");
			
			assertEquals(nuevo.getCodCuenta(), nuevoRead.getCodCuenta());
			assertEquals(nuevo.getNumDocumento(), nuevoRead.getNumDocumento());
			assertEquals(nuevo.getRol(), nuevoRead.getRol());
			assertTrue(nuevoRead.getExcluido());
			
			assertEquals(Formateador.formatoFecha(nuevoRead.getFechaExclusion(), Formateador.FORMATO_FECHA_DDMMYYYY), 
					Formateador.formatoFecha(fechaExclusion, Formateador.FORMATO_FECHA_DDMMYYYY));
			
			assertEquals(Formateador.formatoFecha(nuevoRead.getFechaExclusionInicio(), Formateador.FORMATO_FECHA_DDMMYYYY), 
					Formateador.formatoFecha(fechaInicioExclusion, Formateador.FORMATO_FECHA_DDMMYYYY));
			
			assertEquals(Formateador.formatoFecha(nuevoRead.getFechaExclusionFin(), Formateador.FORMATO_FECHA_DDMMYYYY), 
					Formateador.formatoFecha(fechaFinExclusion, Formateador.FORMATO_FECHA_DDMMYYYY));
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}

	/**
	 * GetUsuarioByPrsnif.
	 */
	@Test
	public void testE01GetUsuarioByPrsnif() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByNumeroDocumento(null));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_DOCUMENTO_REQUERIDO, throwable.getMessage());

		throwable = assertThrows(Throwable.class, () -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().
				getUsuarioByNumeroDocumento(DOCUMENTO_USUARIO_BOLSA_NO_EXISTE));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_UJA_CON_DOCUMENTO_NO_EXISTE,
				throwable.getMessage());

		throwable = assertThrows(Throwable.class, () -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().
				getUsuarioByNumeroDocumento(DOCUMENTO_USUARIO_UJA_NO_EXISTE));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_CON_DOCUMENTO_NO_EXISTE, throwable.getMessage());
	}

	/**
	 * getUsuarioById.
	 */
	@Test
	public void testE02GetUsuarioById() {
		Throwable throwable;

		throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(CODNUM_NOEXISTE));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_CON_ID_NO_EXISTE, throwable.getMessage());
	}

	/**
	 * getUsuarioLogeado.
	 */
	@Test
	public void testE03getUsuarioLogeadoNull() {
		Throwable throwable;
		UVDatos datos = new UVDatos();
		
		throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioLogeado(datos));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_NO_HAY_USUARIO_LOGEADO, throwable.getMessage());
	}
	
	/**
	 * getUsuarioLogeado no es candidato.
	 */
	@Test
	public void testE04getUsuarioCandidatoNoEsCandidato() {
		Usuario usu = CrearUsuario.usuario(ATISOLUCIONES_CODCUENTA);
		UVDatos datos = new UVDatos();
		datos.setUsuario(usu);
		
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioCandidato(datos));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_NO_ES_CANDIDATO, throwable.getMessage());
	}
	
	/**
	 * getUsuarioByCodCuenta inexsitente.
	 */
	@Test
	public void testE05getUsuarioByCodCuenta() {
		Usuario usu = CrearUsuario.usuario(ATISOLUCIONES_CODCUENTA);
		UVDatos datos = new UVDatos();
		datos.setUsuario(usu);
		
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(CODCUENTA_INEXISTENTE));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_CON_DOCUMENTO_NO_EXISTE, throwable.getMessage());
	}
	
	private void marcarUsuarioComoNoExcluido(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoNoExcluido(usuario, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByNumeroDocumento(usuario.getNumDocumento());
		assertFalse(usuario.getExcluido());
	}

	private UsuarioBolsaEmpleo marcarUsuarioComoExcluido(String documento) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByNumeroDocumento(documento);
		usuario.setExcluidoTipo(ModeloUsuarioBolsaEmpleo.EXCLUSION_TIPO_INDEFINIDO);
		usuario.setRazonExcluido(RAZON_EXCLUSION);
		usuario.setFechaExclusion(FECHA_EXCLUSION);
		
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoExcluido(usuario, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		
		usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByNumeroDocumento(usuario.getNumDocumento());
		assertTrue(usuario.getExcluido());
		assertEquals(usuario.getRazonExcluido(), RAZON_EXCLUSION);
		assertEquals(Formateador.formatoFecha(usuario.getFechaExclusion(), Formateador.FORMATO_FECHA_DDMMYYYY), 
				Formateador.formatoFecha(FECHA_EXCLUSION, Formateador.FORMATO_FECHA_DDMMYYYY));
		
		return usuario;
	}
	
	private ArrayList<UsuarioBolsaEmpleo> marcarUsuarioComoBorrado(String documento) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByNumeroDocumento(documento);
		assertFalse(usuario.getBorrado());
		ArrayList<UsuarioBolsaEmpleo> usuariosBorrar = new ArrayList<>();
		usuariosBorrar.add(usuario);
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoBorrado(usuariosBorrar, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByNumeroDocumento(usuario.getNumDocumento());
		assertTrue(usuario.getBorrado());
		return usuariosBorrar;
	}
	
	private void marcarUsuarioComoNoBorrado(ArrayList<UsuarioBolsaEmpleo> usuariosBorrar) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario;
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoNoBorrado(usuariosBorrar, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByNumeroDocumento(ATISOLUCIONES_DOCUMENTO);
		assertFalse(usuario.getBorrado());
	}
}
