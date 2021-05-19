package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteOpcion;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMeritosPreferentes;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/** test modelo meritos preferentes.
*
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloMeritosPreferentes {
	private static final Integer CODNUM = 1;
	private static final String CODIGO = "1";
	private static final String NOMBRE = "nombre";
	private static final String OBSERVACIONES = "observaciones";
	private static final String TIPO = "MERITO";
	private static final String TIPO_FACTOR = "FACTOR";
	private static final String APLICABLE = "BLOQUE";
	private static final Double FACTOR = 1.3;
	private static final Double VALORMAXIMO = 100.0;
	private static final Boolean ACTIVO = true;
	private static final Integer ID_MERITO_NO_EXISTE = 111_111_111;
	private static final String NOMBRE_OPCION = "OPCION 1";
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";
		
	private static ApartadoBaremacion apartado;
	private static BloqueBaremacion bloque;
	private static ItemBaremacion item;
	
	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException si error en bd
	 * @throws IOException  si error en ficheros
	 * @throws UVException .
	 */
	@BeforeClass
	public static void preparaBd() throws SQLException, IOException, UVException {
		DataSource ds = BbddRunner.obtenerDataSourceUv();
		Conexion.setConexionUvirtual(ds);
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
		
		item = ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(CODNUM);
		bloque = item.getBloqueBaremacion();
		apartado = bloque.getApartadoBaremacion();			
	}
    
	/**
	 * test acierto insertar usuario.
	 * 
	 * @throws SQLException   si error en bd
	 * @throws UVException    si error al validar usuario
	 * @throws ParseException si error al validar fecha
	 */
	@Test
	public void testA01InsertaMeritoPreferente() {
		MeritoPreferente merito = new MeritoPreferente();
		merito.setCodigo(CODIGO);
		merito.setNombre(NOMBRE);
		merito.setTipo(TIPO);		
		merito.setTipoCalculo(TIPO_FACTOR);
		merito.setAplicable(APLICABLE);
		merito.setFactor(FACTOR);
		merito.setBase(FACTOR);
		merito.setValorMaximo(VALORMAXIMO);
		merito.setTipoItemBaremacion(item);
		merito.setAplicableBloqueBaremacion(bloque);
		merito.setAplicableApartadoBaremacion(apartado);
		merito.setAplicableItemBaremacion(item);
		merito.setActivo(ACTIVO);
		
		try {
			ModeloMeritosPreferentes modelo = ModeloMeritosPreferentes.obtenerInstancia();
			Integer codNum = modelo.crearMeritoPreferente(merito);
			MeritoPreferente meritoNew = modelo.getMeritoPreferenteById(codNum);
			
			merito.setCodNum(codNum);
			assertEquals(merito, meritoNew);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * test acierto insertar usuario.
	 * 
	 * @throws SQLException   si error en bd
	 * @throws UVException    si error al validar usuario
	 * @throws ParseException si error al validar fecha
	 */
	@Test
	public void testA02InsertaMeritoPreferenteConValoresNull() {
		MeritoPreferente merito = new MeritoPreferente();
		merito.setCodNum(CODNUM);
		merito.setCodigo(CODIGO);
		merito.setNombre(NOMBRE);
		merito.setObservaciones(OBSERVACIONES);
		merito.setTipo(TIPO);
		merito.setTipoCalculo(TIPO_FACTOR);
		merito.setAplicable(APLICABLE);
		merito.setFactor(FACTOR);
		merito.setBase(null);
		merito.setValorMaximo(null);
		merito.setTipoItemBaremacion(null);
		merito.setAplicableBloqueBaremacion(null);
		merito.setAplicableApartadoBaremacion(null);
		merito.setAplicableItemBaremacion(null);
		merito.setActivo(ACTIVO);
		
		try {
			ModeloMeritosPreferentes modelo = ModeloMeritosPreferentes.obtenerInstancia();
			Integer codNum = modelo.crearMeritoPreferente(merito);
			MeritoPreferente meritoNew = modelo.getMeritoPreferenteById(codNum);
			
			merito.setCodNum(codNum);
			assertEquals(merito, meritoNew);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}

	/**
	 * test acierto borrar usuario.
	 * 
	 * @throws SQLException si error en bd
	 * @throws UVException  si error al validar usuario
	 */
	@Test
	public void testA03DesactivarMeritoPreferente() {
		ModeloMeritosPreferentes modelo = ModeloMeritosPreferentes.obtenerInstancia();

		try {
			MeritoPreferente merito = modelo.getMeritoPreferenteById(CODNUM);
			modelo.cambiaFlagActivoMeritoPreferente(merito, "N");
			merito = modelo.getMeritoPreferenteById(CODNUM);
			
			assertEquals(merito.getActivo(), Boolean.FALSE);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
    
	/**
	 * test acierto editar merito preferente.
	 * 
	 * @throws SQLException si error en bd
	 * @throws UVException  si error el merito preferente.
	 */
	@Test
	public void testA04EditarMerito() {
		ModeloMeritosPreferentes modelo = ModeloMeritosPreferentes.obtenerInstancia();

		try {
			MeritoPreferente merito = modelo.getMeritoPreferenteById(CODNUM);
			merito.setObservaciones(OBSERVACIONES);			
			merito.setValorMaximo(1.0);
			merito.setTipoItemBaremacion(item);
			merito.setAplicableBloqueBaremacion(bloque);
			merito.setAplicableApartadoBaremacion(apartado);
			merito.setAplicableItemBaremacion(item);			
			merito.setBase(1.0);
			merito.setValorMaximo(1.0);
			merito.setValorMaximo(1.0);
			merito.setTipoItemBaremacion(item);
			merito.setAplicableBloqueBaremacion(bloque);
			merito.setAplicableItemBaremacion(item);
			
			modelo.editarMeritoPreferente(merito);
			MeritoPreferente meritoDb = modelo.getMeritoPreferenteById(CODNUM);
						
			assertEquals(merito, meritoDb);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	} 
    
	/**
	 * test acierto editar merito preferente con null.
	 * 
	 * @throws SQLException si error en bd
	 * @throws UVException  si error el merito preferente.
	 */
	@Test
	public void testA05EditarMeritoConValoresNull() {
		ModeloMeritosPreferentes modelo = ModeloMeritosPreferentes.obtenerInstancia();

		try {
			MeritoPreferente merito = modelo.getMeritoPreferenteById(CODNUM);
			merito.setObservaciones(null);			
			merito.setValorMaximo(null);
			merito.setTipoItemBaremacion(null);
			merito.setAplicableBloqueBaremacion(null);
			merito.setAplicableApartadoBaremacion(null);
			merito.setAplicableItemBaremacion(null);			
			merito.setBase(null);
			merito.setValorMaximo(null);
			merito.setValorMaximo(null);
			merito.setTipoItemBaremacion(null);
			merito.setAplicableBloqueBaremacion(null);
			merito.setAplicableItemBaremacion(null);
			modelo.editarMeritoPreferente(merito);

			merito = modelo.getMeritoPreferenteById(merito.getCodNum());
			
			assertEquals(merito.getObservaciones(), null);
			assertEquals(merito.getValorMaximo(), null);
			assertEquals(merito.getTipoItemBaremacion(), null);
			assertEquals(merito.getAplicableBloqueBaremacion(), null);
			assertEquals(merito.getAplicableApartadoBaremacion(), null);
			assertEquals(merito.getAplicableItemBaremacion(), null);			
			assertEquals(merito.getBase(), null);
			assertEquals(merito.getValorMaximo(), null);
			assertEquals(merito.getTipoItemBaremacion(), null);
			assertEquals(merito.getAplicableBloqueBaremacion(), null);			
			assertEquals(merito.getAplicableItemBaremacion(), null);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	} 
	
	/**
	 * Test datatable.
	 * @throws SQLException .
	 * @throws UVException .
	 */
	@Test
	public void testA06ListadoDeMeritosDt() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloMeritosPreferentes.ORDER_COLUMN_INDEX_CODIGO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			BolsaEmpleoDataTable<MeritoPreferente> dt = ModeloMeritosPreferentes.obtenerInstancia().listadoMeritosPreferentes(params);
			assertFalse(dt.getData().isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test listado de merito preferentes por posesión.
	 */
	@Test
	public void testA07ListadoDeMeritosPreferentesPorPosesion() {
		try {
			List<MeritoPreferente> lista = ModeloMeritosPreferentes.obtenerInstancia().getMeritosPreferentesPorPosesion();
			if (!lista.isEmpty()) {
				MeritoPreferente mp = lista.get(0);
				assertEquals(mp.getTipo(), ModeloMeritosPreferentes.TIPO_POSESION);
			}
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test chequeo de meritos activos por codigo.
	 */
	@Test
	public void testA08CodigoActivo() {
		try {
			MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(CODNUM);
			merito.setActivo(true);
			ModeloMeritosPreferentes.obtenerInstancia().editarMeritoPreferente(merito);
			merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(CODNUM);			
			assertTrue(merito.getActivo());
			
			boolean activo = ModeloMeritosPreferentes.obtenerInstancia().isMeritoActivoConCodigo(merito);
			assertTrue(activo);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test chequeo de meritos activos por codigo.
	 */
	@Test
	public void testA09CodigoActivoSinCodNum() {
		try {
			MeritoPreferente merito = new MeritoPreferente();
			merito.setCodigo(CODIGO);
			
			boolean activo = ModeloMeritosPreferentes.obtenerInstancia().isMeritoActivoConCodigo(merito);
			assertTrue(activo);			
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test chequeo de meritos activos por codigo.
	 */
	@Test
	public void testA10CodigoInactivo() {
		try {
			// desactivamos todos los meritos preferentes		
			String sql = "UPDATE TBEP_MERITOS_PREFERENTES SET FLGACTIVO = 'N'"; 
			try (Connection con = BbddRunner.obtenerConexionUvirtual()) {
				try (PreparedStatement stmt = con.prepareStatement(sql)) {
					stmt.executeUpdate();
				}
			}
						
			MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(CODNUM);
			merito.setActivo(false);			
			ModeloMeritosPreferentes.obtenerInstancia().editarMeritoPreferente(merito);
			merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(CODNUM);			
			assertFalse(merito.getActivo());
			merito.setCodNum(null);
			
			boolean activo = ModeloMeritosPreferentes.obtenerInstancia().isMeritoActivoConCodigo(merito);
			assertFalse(activo);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Crea un opción del un mérito preferente.
	 */
	@Test
	public void testA11CrearOpcionMeritoPreferente() {
		try {
			// activamos el merito y lo ponemos como cálculo con opciones
			MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(CODNUM);
			merito.setActivo(true);
			merito.setTipoCalculo(ModeloMeritosPreferentes.TIPO_CALCULO_OPCIONES);
			ModeloMeritosPreferentes.obtenerInstancia().editarMeritoPreferente(merito);
			merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(CODNUM);
			assertEquals(merito.getTipoCalculo(), ModeloMeritosPreferentes.TIPO_CALCULO_OPCIONES);
			
			// creamos una opción
			MeritoPreferenteOpcion opcion = new MeritoPreferenteOpcion();
			opcion.setMeritoPreferenteCodNum(merito.getCodNum());
			opcion.setNombre(NOMBRE_OPCION);
			opcion.setFactor(FACTOR);			
			Integer id = ModeloMeritosPreferentes.obtenerInstancia().crearMeritoPreferenteOpcion(opcion);
			
			// leemos y chequeamos opción
			MeritoPreferenteOpcion opcionRead = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteOpcionById(id);
			assertEquals(opcionRead.getCodNum(), id);
			assertEquals(opcionRead.getMeritoPreferenteCodNum(), merito.getCodNum());
			assertEquals(opcionRead.getNombre(), opcion.getNombre());
			assertEquals(opcionRead.getFactor(), opcion.getFactor());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test datatable.
	 */
	@Test
	public void testA12ListadoDeOpcionesMeritosDt() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(CODNUM);
			BolsaEmpleoDataTable<MeritoPreferenteOpcion> dt = ModeloMeritosPreferentes.obtenerInstancia().listadoOpcionesMeritosPreferentes(params, merito);
			
			assertFalse(dt.getData().isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Test datatable.
	 */
	@Test
	public void testA13ListadoDeOpcionesMeritosDt() {
		try {
			MeritoPreferente merito = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(CODNUM);
			List<MeritoPreferenteOpcion> listado = ModeloMeritosPreferentes.obtenerInstancia().listadoOpcionesMeritosPreferentes(merito);
			
			assertEquals(listado.size(), 1);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * Desactivar opción.
	 */
	@Test
	public void testA14DesactivarOpcionMerito() {
		try {
			MeritoPreferenteOpcion opcion = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteOpcionById(1);			
			ModeloMeritosPreferentes.obtenerInstancia().desactivarMeritoPreferenteOpcion(opcion);
			MeritoPreferenteOpcion opcionRead = ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteOpcionById(1);
			
			assertTrue(opcionRead.isBorrado());
			assertNotNull(opcionRead.getFechaBorrado());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	
	
	/**
	 * test error inserta merito preferente null.
	 * 
	 * @throws SQLException si error bd
	 * @throws UVException  error experado
	 */
	@Test(expected = UVException.class)
	public void testE01InsertaMeritoPreferenteNull() throws SQLException, UVException {
		MeritoPreferente merito = null;
		ModeloMeritosPreferentes modelo = ModeloMeritosPreferentes.obtenerInstancia();
		modelo.crearMeritoPreferente(merito);
		fail();
	}

	/**
	 * Text excepción merito requerido.
	 */
	@Test
	public void testE02MeritoEsRequerito() {
		Throwable throwable = assertThrows(Throwable.class, () -> 
			ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(null)
		);

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMeritosPreferentes.ERROR_MERITO_REQUERIDO, throwable.getMessage());
	}
	
	/**
	 * Test excepción merito no existe.
	 */
	@Test
	public void testE03MeritonNoExiste() {
		Throwable throwable = assertThrows(Throwable.class, () -> 
			ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(ID_MERITO_NO_EXISTE)
		);

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMeritosPreferentes.ERROR_MERITO_NOEXITE, throwable.getMessage());
	}
	
	/**
	 * Opción mérito requerido.
	 */
	@Test
	public void testE04OpcionMeritoRequerido() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteOpcionById(null));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMeritosPreferentes.OPCION_MERITO_REQUERIDO, throwable.getMessage());
	}
	
	/**
	 * Opción mérito requerido no exixste.
	 */
	@Test
	public void testE05OpcionMeritoNoExiste() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteOpcionById(ID_MERITO_NO_EXISTE));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloMeritosPreferentes.OPCION_MERITO_NOEXISTE, throwable.getMessage());
	}
}
