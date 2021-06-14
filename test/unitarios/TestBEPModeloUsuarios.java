package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.AdaptadorDocumentoIdentidad;
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
	private static final String ATISOLUCIONES_DOCUMENTO = "B18865253";
	private static final String ATISOLUCIONES_CODCUENTA = "atisoluciones";
	private static final String CANDIDATO1_CODCUENTA = "candidato1";
	private static final String CANDIDATO1_DOCUMENTO = "43396488L";
	private static final String CANDIDATO2_CODCUENTA = "candidato2";
	private static final String CANDIDATO5_CODCUENTA = "candidato5";
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
	public void testA01getUsuarioByCodCuenta() {
		try {
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(ATISOLUCIONES_CODCUENTA);
			assertEquals(usuario.getCodCuenta(), ATISOLUCIONES_CODCUENTA);
			assertEquals(usuario.getPrsNif(), ATISOLUCIONES_DOCUMENTO);
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
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(ATISOLUCIONES_CODCUENTA);
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
			ArrayList<UsuarioBolsaEmpleo> usuariosBorrar = marcarUsuarioComoBorrado(ATISOLUCIONES_CODCUENTA);

			// obtenemos usuario logeado borrado
			UsuarioBolsaEmpleo usuarioLeido = modelo.getUsuarioByCodCuenta(ATISOLUCIONES_CODCUENTA);
			assertTrue(usuarioLeido.getBorrado());

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
			UsuarioBolsaEmpleo usuario = marcarUsuarioComoExcluido(ATISOLUCIONES_CODCUENTA);

			// obtenemos usuario logeado excluido
			UsuarioBolsaEmpleo usuarioLeido = modelo.getUsuarioByCodCuenta(ATISOLUCIONES_CODCUENTA);
			assertTrue(usuarioLeido.getExcluido());
			
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
		try {
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(CANDIDATO1_CODCUENTA);					
			assertEquals(usuario.getCodCuenta(), CANDIDATO1_CODCUENTA);
			assertEquals(usuario.getPrsNif(), CANDIDATO1_DOCUMENTO);
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
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_USUARIO_DOCUMENTO)});
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
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_USUARIO_DOCUMENTO)});
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
	 * Test insertaUsuario.
	 */
	@Test
	public void testA10insertaUsuario() {
		try {
			UsuarioBolsaEmpleo nuevo = new UsuarioBolsaEmpleo();
			nuevo.setRol(ModeloRol.obtenerInstancia().getRoleById(ModeloRol.ID_ROL_CANDIDATO));
			nuevo.setCodCuenta("candidato3");
			nuevo.setIdNif("51906000Z");
			
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().insertaUsuario(nuevo, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			UsuarioBolsaEmpleo nuevoRead = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta("candidato3");
			
			assertEquals(nuevo.getCodCuenta(), nuevoRead.getCodCuenta());
			assertEquals(nuevo.getIdNif(), nuevoRead.getIdNif());
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
	public void testA11insertaUsuarioExcluido() {
		try {
			Date fechaExclusion = BolsaEmpleoUtils.getCurrentDateTime();
			Date fechaInicioExclusion = BolsaEmpleoUtils.getCurrentDateTime();
			Date fechaFinExclusion = BolsaEmpleoUtils.getCurrentDateTime();
			
			UsuarioBolsaEmpleo nuevo = new UsuarioBolsaEmpleo();
			nuevo.setRol(ModeloRol.obtenerInstancia().getRoleById(ModeloRol.ID_ROL_CANDIDATO));
			nuevo.setCodCuenta("candidato4");
			nuevo.setIdNif("00834172P");
			nuevo.setExcluido(true);
			nuevo.setExcluidoTipo(ModeloUsuarioBolsaEmpleo.EXCLUSION_TIPO_TEMPORAL);
			nuevo.setRazonExcluido("test");
			nuevo.setFechaExclusion(fechaExclusion);
			nuevo.setFechaExclusionInicio(fechaInicioExclusion);
			nuevo.setFechaExclusionFin(fechaFinExclusion);
			
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().insertaUsuario(nuevo, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			UsuarioBolsaEmpleo nuevoRead = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta("candidato4");
			
			assertEquals(nuevo.getCodCuenta(), nuevoRead.getCodCuenta());
			assertEquals(nuevo.getIdNif(), nuevoRead.getIdNif());
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
	 * Test actualizaUsuario.
	 */
	@Test
	public void testA12actualizaUsuario() {
		try {
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(CANDIDATO1_CODCUENTA);
			assertEquals(usuario.getCodCuenta(), CANDIDATO1_CODCUENTA);
			
			usuario.setNombre("Jhon");
			usuario.setPrimerApellido("Doo");
			usuario.setSegundoApellido("Foo");
			usuario.setCodigoPostal("11111");
			usuario.setDireccion("d1");
			usuario.setLocalidad("l1");
			usuario.setTelefono("123456");
			usuario.setProvincia("p1");
			usuario.setNacionalidad("n1");
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().actualizaUsuario(usuario, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			
			usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(CANDIDATO1_CODCUENTA);
			assertEquals(usuario.getCodCuenta(), CANDIDATO1_CODCUENTA);
			assertEquals(usuario.getNombre(), "Jhon");
			assertEquals(usuario.getPrimerApellido(), "Doo");
			assertEquals(usuario.getSegundoApellido(), "Foo");
			assertEquals(usuario.getCodigoPostal(), "11111");
			assertEquals(usuario.getDireccion(), "d1");
			assertEquals(usuario.getLocalidad(), "l1");
			assertEquals(usuario.getTelefono(), "123456");	
			assertEquals(usuario.getProvincia(), "p1");
			assertEquals(usuario.getNacionalidad(), "n1");
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test actualizaUsuarioMisDatos.
	 */
	@Test
	public void testA13actualizaUsuarioMisDatos() {
		try {
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(CANDIDATO1_CODCUENTA);
			assertEquals(usuario.getCodCuenta(), CANDIDATO1_CODCUENTA);
			
			usuario.setDireccion("d2");
			usuario.setCodigoPostal("22222");
			usuario.setLocalidad("l2");
			usuario.setProvincia("p2");
			usuario.setNacionalidad("n2");
			usuario.setTelefono("987654321");
			ModeloUsuarioBolsaEmpleo.obtenerInstancia().actualizaUsuarioMisDatos(usuario, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			
			usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(CANDIDATO1_CODCUENTA);
			assertEquals(usuario.getCodCuenta(), CANDIDATO1_CODCUENTA);
			assertEquals(usuario.getDireccion(), "d2");
			assertEquals(usuario.getCodigoPostal(), "22222");
			assertEquals(usuario.getLocalidad(), "l2");
			assertEquals(usuario.getProvincia(), "p2");
			assertEquals(usuario.getNacionalidad(), "n2");
			assertEquals(usuario.getTelefono(), "987654321");
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test getOrCreateUsuario.
	 */
	@Test
	public void testA14getOrCreateUsuario() {
		try {
			// sin usuario logeado
			UVDatos datos = new UVDatos();
			UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos);
			assertNull(usuario);
			
			// el usuario existe en bep
			datos.setUsuario(CrearUsuario.usuario(CANDIDATO2_CODCUENTA));
			usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos);
			assertEquals(usuario.getCodCuenta(), CANDIDATO2_CODCUENTA);
			
			// el usuario no existe en bep, se crea como candidato
			Usuario usuArcos = CrearUsuario.usuario(CANDIDATO5_CODCUENTA); 
			datos.setUsuario(usuArcos);
			usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getOrCreateUsuario(datos);
			assertEquals(usuario.getCodCuenta(), CANDIDATO5_CODCUENTA);
			assertEquals(usuario.getRol().getCodNum(), ModeloRol.ID_ROL_CANDIDATO);
			assertEquals(usuario.getTipoDocumento(), usuArcos.getDocumentoTipo());
			assertEquals(usuario.getIdNif(), AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIAC, usuArcos));
			assertEquals(usuario.getLetraNif(), AdaptadorDocumentoIdentidad.letraNIF(usuArcos.getDocumentoTipo(), usuArcos.getDocumentoNumero()));
			assertEquals(usuario.getPrsNif(), usuArcos.getDocumentoNumero());
			assertEquals(usuario.getNombre(), usuArcos.getNombre());
			assertEquals(usuario.getPrimerApellido(), usuArcos.getApellido1());
			assertEquals(usuario.getSegundoApellido(), usuArcos.getApellido2());
			assertEquals(usuario.getEmail(), usuArcos.getEmailCalculado());
			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	

	/**
	 * getUsuarioById.
	 */
	@Test
	public void testE01GetUsuarioById() {
		Throwable throwable;

		throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(CODNUM_NOEXISTE));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_CON_ID_NO_EXISTE, throwable.getMessage());
	}

	/**
	 * getUsuarioByCodCuenta inexsitente.
	 */
	@Test
	public void testE02getUsuarioByCodCuenta() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(CODCUENTA_INEXISTENTE));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_CON_DOCUMENTO_NO_EXISTE, throwable.getMessage());
	}
	
	/**
	 * insertaUsuario null.
	 */
	@Test
	public void testE03insertaUsuario() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().insertaUsuario(null, UtilsTestBolsaEmpleo.getUsuario("personal1")));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_VACIO, throwable.getMessage());
		
		UsuarioBolsaEmpleo nuevo = new UsuarioBolsaEmpleo();
		throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().insertaUsuario(nuevo, UtilsTestBolsaEmpleo.getUsuario("personal1")));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_SIN_ROL, throwable.getMessage());
	}
	
	/**
	 * actualizaUsuario.
	 */
	@Test
	public void testE04actualizaUsuario() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().actualizaUsuario(null, UtilsTestBolsaEmpleo.getUsuario("personal1")));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_VACIO, throwable.getMessage());
		
		UsuarioBolsaEmpleo nuevo = new UsuarioBolsaEmpleo();
		throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().actualizaUsuario(nuevo, UtilsTestBolsaEmpleo.getUsuario("personal1")));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_CODNUM_REQUERIDO, throwable.getMessage());
	}
	
	/**
	 * actualizaUsuarioMisDatos.
	 */
	@Test
	public void testE05actualizaUsuarioMisDatos() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().actualizaUsuarioMisDatos(null, UtilsTestBolsaEmpleo.getUsuario("personal1")));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_USUARIO_VACIO, throwable.getMessage());
		
		UsuarioBolsaEmpleo nuevo = new UsuarioBolsaEmpleo();
		throwable = assertThrows(Throwable.class,
				() -> ModeloUsuarioBolsaEmpleo.obtenerInstancia().actualizaUsuarioMisDatos(nuevo, UtilsTestBolsaEmpleo.getUsuario("personal1")));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloUsuarioBolsaEmpleo.MENSAJE_ERROR_CODNUM_REQUERIDO, throwable.getMessage());
	}
	
	private void marcarUsuarioComoNoExcluido(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoNoExcluido(usuario, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(usuario.getCodCuenta());
		assertFalse(usuario.getExcluido());
	}

	private UsuarioBolsaEmpleo marcarUsuarioComoExcluido(String codCuenta) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(codCuenta);
		usuario.setExcluidoTipo(ModeloUsuarioBolsaEmpleo.EXCLUSION_TIPO_INDEFINIDO);
		usuario.setRazonExcluido(RAZON_EXCLUSION);
		usuario.setFechaExclusion(FECHA_EXCLUSION);
		
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoExcluido(usuario, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		
		usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(usuario.getCodCuenta());
		assertTrue(usuario.getExcluido());
		assertEquals(usuario.getRazonExcluido(), RAZON_EXCLUSION);
		assertEquals(Formateador.formatoFecha(usuario.getFechaExclusion(), Formateador.FORMATO_FECHA_DDMMYYYY), 
				Formateador.formatoFecha(FECHA_EXCLUSION, Formateador.FORMATO_FECHA_DDMMYYYY));
		
		return usuario;
	}
	
	private ArrayList<UsuarioBolsaEmpleo> marcarUsuarioComoBorrado(String codCuenta) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(codCuenta);
		assertFalse(usuario.getBorrado());
		ArrayList<UsuarioBolsaEmpleo> usuariosBorrar = new ArrayList<>();
		usuariosBorrar.add(usuario);
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoBorrado(usuariosBorrar, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(usuario.getCodCuenta());
		assertTrue(usuario.getBorrado());
		return usuariosBorrar;
	}
	
	private void marcarUsuarioComoNoBorrado(ArrayList<UsuarioBolsaEmpleo> usuariosBorrar) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario;
		ModeloUsuarioBolsaEmpleo.obtenerInstancia().ponerUsuarioComoNoBorrado(usuariosBorrar, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioByCodCuenta(ATISOLUCIONES_CODCUENTA);
		assertFalse(usuario.getBorrado());
	}
}
