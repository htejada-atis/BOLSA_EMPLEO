package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionBloques;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * test modelo bloque baremacion.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloBaremacionBloques {
	private static final Integer CODNUM = 1;
	private static final Integer CODNUM_NOEXISTE = 111_111_111;
	private static final String CODIGO = "1";
	private static final String NOMBRE = "NOMBRE BLOQUE";
	private static final Integer NUMERO_MAXIMO_MERITOS = 5;
	
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";
    
	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException   si error en bd
	 * @throws IOException    si error en ficheros
	 * @throws ParseException si error fecha
	 * @throws UVException    .
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
	 * getBloqueBaremacionById.
	 */
	@Test
	public void testA01getBloqueBaremacionById() {
		try {
			BloqueBaremacion bloque = ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(CODNUM);
			assertEquals(bloque.getCodNum(), CODNUM);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * getUltimoCodigoBloque.
	 */
	@Test
	public void testA02getUltimoCodigoBloque() {
		try {
			String codigo = ModeloBaremacionBloques.obtenerInstancia().getUltimoCodigoBloque(obtenerPrimerApartado());
			assertNotNull(codigo);
			assertNotEquals(codigo, "0");
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listadoBloquesBaremacionDatatable.
	 */
	@Test
	public void testA03listadoBloquesBaremacionDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloBaremacionBloques.ORDER_COLUMN_INDEX_BLOQUES_CODIGO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			BolsaEmpleoDataTable<BloqueBaremacion> dt = ModeloBaremacionBloques.obtenerInstancia().listadoBloquesBaremacionDatatable(params, obtenerPrimerApartado());
			assertFalse(dt.getData().isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * insertaBloque.
	 */
	@Test
	public void testA04insertaBloque() {
		try {
			ApartadoBaremacion apartado = this.obtenerPrimerApartado();
			BloqueBaremacion bloque = new BloqueBaremacion();
			bloque.setApartadoBaremacion(apartado);
			bloque.setCodigo(CODIGO);
			bloque.setNombre(NOMBRE);
			bloque.setActivo(true);
			bloque.setNumeroMaximoMeritos(NUMERO_MAXIMO_MERITOS);
			
			List<BloqueBaremacion> lista = desactivarTodosBloques();
			Integer codNum = ModeloBaremacionBloques.obtenerInstancia().insertaBloque(bloque, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			BloqueBaremacion creado = ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(codNum);			
			activarBloquesLista(lista);
			
			assertEquals(creado.getCodigo(), CODIGO);
			assertEquals(creado.getNombre(), NOMBRE);
			assertEquals(creado.isActivo(), true);
			assertEquals(creado.getNumeroMaximoMeritos(), NUMERO_MAXIMO_MERITOS);
			assertEquals(creado.getApartadoBaremacion(), apartado);
			
			desactivarBloque(codNum);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * actualizaBloque.
	 */
	@Test
	public void testA06actualizaBloque() {
		try {					
			ApartadoBaremacion apartado = this.obtenerPrimerApartado();
			BloqueBaremacion bloque = ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(CODNUM); 
			assertEquals(bloque.getCodNum(), CODNUM);
			
			bloque.setApartadoBaremacion(apartado);
			bloque.setCodigo(CODIGO);
			bloque.setNombre(NOMBRE);
			bloque.setActivo(true);
			bloque.setNumeroMaximoMeritos(NUMERO_MAXIMO_MERITOS);
			
			ModeloBaremacionBloques.obtenerInstancia().actualizaBloque(bloque, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			BloqueBaremacion bloqueUpd = ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(CODNUM);
			
			assertEquals(bloqueUpd.getCodigo(), CODIGO);
			assertEquals(bloqueUpd.getNombre(), NOMBRE);
			assertEquals(bloqueUpd.isActivo(), true);
			assertEquals(bloqueUpd.getNumeroMaximoMeritos(), NUMERO_MAXIMO_MERITOS);
			assertEquals(bloqueUpd.getApartadoBaremacion(), apartado);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listaBloquesBaremacionActivosOrdenadosPorCodigo.
	 */
	@Test
	public void testA07listaBloquesBaremacionActivosOrdenadosPorCodigo() {
		try {
			List<BloqueBaremacion> lista = ModeloBaremacionBloques.obtenerInstancia().listaBloqueBaremacion();
			for (BloqueBaremacion a : lista) {
				assertTrue(a.isActivo());
			}
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	// ERRORES
	
	/**
	 * getBloqueBaremacionById null.
	 */
	@Test
	public void testE01getBloqueBaremacionById() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(null));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionBloques.ERROR_BLOQUE_REQUERIDO, throwable.getMessage());
	}
	
	/**
	 * getBloqueBaremacionById.
	 */
	@Test
	public void testE02getBloqueBaremacionById() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(CODNUM_NOEXISTE));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionBloques.ERROR_BLOQUE_NOEXITE, throwable.getMessage());
	}
	
	/**
	 * insertaBloque.
	 */
	@Test
	public void testE03insertaBloque() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloBaremacionBloques.obtenerInstancia().insertaBloque(null, UtilsTestBolsaEmpleo.getUsuario("personal1")));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionBloques.ERROR_BLOQUE_REQUERIDO, throwable.getMessage());
	}
	
	/**
	 * existeOtroBloqueActivoPorCodigo.
	 */
	@Test
	public void testE04existeOtroBloqueActivoPorCodigo() {
		Throwable throwable = assertThrows(Throwable.class, () -> {
			BloqueBaremacion bloque = ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(CODNUM);
			bloque.setCodNum(CODNUM_NOEXISTE);
			ModeloBaremacionBloques.obtenerInstancia().insertaBloque(bloque, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		});
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionBloques.ERROR_BLOQUE_MISMO_CODIGO, throwable.getMessage());
	}
	
	/**
	 * error editar bloque usado.
	 */
	@Test
	public void testE05nosepuedeeditarbloqueusandose() {
		Throwable throwable = assertThrows(Throwable.class, () -> {
			// bloque del item = 1
			Integer codnum = 19;
			BloqueBaremacion bloque = ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(codnum); 
			assertEquals(bloque.getCodNum(), codnum);
			bloque.setNombre("foo");
			
			ModeloBaremacionBloques.obtenerInstancia().actualizaBloque(bloque, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		});
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionBloques.ERROR_APARTADO_USADO, throwable.getMessage());
	}
	
	// UTILS
	
	private ApartadoBaremacion obtenerPrimerApartado() throws SQLException, UVException {
		return ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(CODNUM);
	}
	
	private List<BloqueBaremacion> desactivarTodosBloques() throws UVException {
		List<BloqueBaremacion> lista = null;
		
		try {
			lista = ModeloBaremacionBloques.obtenerInstancia().listaBloqueBaremacion();
			for (BloqueBaremacion a : lista) {
				desactivarBloque(a.getCodNum());
			}
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
		
		return lista;
	}
	
	private void activarBloquesLista(List<BloqueBaremacion> lista) {
		for (BloqueBaremacion a : lista) {
			activarBloque(a.getCodNum());
		}
	}
	
	private void desactivarBloque(Integer codNum) {
		try {
			ModeloBaremacionBloques modelo = ModeloBaremacionBloques.obtenerInstancia();
			BloqueBaremacion bloque = modelo.getBloqueBaremacionById(codNum);
			assertEquals(bloque.getCodNum(), codNum);
			modelo.desactivarBloque(bloque, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			bloque = modelo.getBloqueBaremacionById(codNum);
			assertFalse(bloque.isActivo());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	private void activarBloque(Integer codNum) {
		try {
			ModeloBaremacionBloques modelo = ModeloBaremacionBloques.obtenerInstancia();
			BloqueBaremacion bloque = modelo.getBloqueBaremacionById(codNum);
			assertEquals(bloque.getCodNum(), codNum);
			modelo.activarBloque(bloque, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			bloque = modelo.getBloqueBaremacionById(codNum);
			assertTrue(bloque.isActivo());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
}
