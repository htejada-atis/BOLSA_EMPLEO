package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionApartados;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * test modelo apartado baremacion.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloBaremacionApartados {
	private static final Integer CODNUM = 1;
	private static final Integer CODNUM_NOEXISTE = 111_111_111;
	private static final String CODIGO = "1";
	private static final String NOMBRE = "NOMBRE APARTADO";
	private static final Double PORCENTAJE_MAXIMO = (double) 0.2;
	private static final Double PORCENTAJE_MAXIMO2 = (double) 0.1;
	private static final Double PORCENTAJE_MAXIMO3 = (double) 0.9;
	
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
	 * getApartadoBaremacionById.
	 */
	@Test
	public void testA01getApartadoBaremacionById() {
		try {
			ApartadoBaremacion apartado = ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(CODNUM);
			assertEquals(apartado.getCodNum(), CODNUM);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * getUltimoCodigoApartado.
	 */
	@Test
	public void testA02getUltimoCodigoApartado() {
		try {
			String codigo = ModeloBaremacionApartados.obtenerInstancia().getUltimoCodigoApartado();
			assertNotNull(codigo);
			assertNotEquals(codigo, "0");
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * getUltimoCodigoApartado.
	 */
	@Test
	public void testA03getUltimoCodigoApartado() {
		try {
			List<ApartadoBaremacion> lista = desactivarTodosApartados();
			
			// ultimo codigo = 0
			String codigo = ModeloBaremacionApartados.obtenerInstancia().getUltimoCodigoApartado();
			assertNotNull(codigo);	
			assertEquals(codigo, "0");
			
			activarApartadosLista(lista);
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listadoApartadosGeneralesBaremacionDatatable.
	 */
	@Test
	public void testA04listadoApartadosGeneralesBaremacionDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {String.valueOf(ModeloBaremacionApartados.ORDER_COLUMN_INDEX_APARTADOS_CODIGO)});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			BolsaEmpleoDataTable<ApartadoBaremacion> dt = ModeloBaremacionApartados.obtenerInstancia().listadoApartadosGeneralesBaremacionDatatable(params);
			assertFalse(dt.getData().isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * insertaApartado por porcentaje.
	 */
	@Test
	public void testA05insertaApartado() {
		try {					
			ApartadoBaremacion apartado = new ApartadoBaremacion();
			apartado.setCodigo(CODIGO);
			apartado.setNombre(NOMBRE);
			apartado.setActivo(true);
			apartado.setPuntuacionMaxima(null);
			apartado.setPorcentajeMaximo(PORCENTAJE_MAXIMO);
			
			List<ApartadoBaremacion> lista = desactivarTodosApartados();
			Integer codNum = ModeloBaremacionApartados.obtenerInstancia().insertaApartado(apartado, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			ApartadoBaremacion creado = ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(codNum);
			assertEquals(creado.getCodigo(), CODIGO);
			assertEquals(creado.getNombre(), NOMBRE);
			assertEquals(creado.getActivo(), true);
			assertNull(creado.getPuntuacionMaxima());
			assertEquals(creado.getPorcentajeMaximo(), PORCENTAJE_MAXIMO);
			
			desactivarApartado(codNum);
			activarApartadosLista(lista);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * actualizaApartado.
	 */
	@Test
	public void testA07actualizaApartado() {
		try {					
			List<ApartadoBaremacion> lista = desactivarTodosApartados();
			ApartadoBaremacion apartado = getApartadoBaremacionPorCodigo(CODIGO);
			assertEquals(apartado.getCodigo(), CODIGO);
			
			apartado.setCodigo("2");
			apartado.setNombre("test");
			apartado.setActivo(true);
			apartado.setPuntuacionMaxima(null);
			apartado.setPorcentajeMaximo(PORCENTAJE_MAXIMO2);
			
			ModeloBaremacionApartados.obtenerInstancia().actualizaApartado(apartado, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			ApartadoBaremacion apartadoUpd = getApartadoBaremacionPorCodigo("2");
			
			assertEquals(apartadoUpd.getCodigo(), "2");
			assertEquals(apartadoUpd.getNombre(), "test");
			assertEquals(apartadoUpd.getActivo(), true);
			assertEquals(apartadoUpd.getPorcentajeMaximo(), PORCENTAJE_MAXIMO2);
			
			desactivarApartado(apartado.getCodNum());
			activarApartadosLista(lista);			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listaApartadoBaremacionActivosOrdenadosPorCodigo.
	 */
	@Test
	public void testA08listaApartadoBaremacionActivosOrdenadosPorCodigo() {
		try {
			List<ApartadoBaremacion> lista = ModeloBaremacionApartados.obtenerInstancia().listaApartadoBaremacionActivosOrdenadosPorCodigo();
			for (ApartadoBaremacion a : lista) {
				assertTrue(a.getActivo());
			}			
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * checkSumaPorcentagesApartadosInvalido.
	 */
	@Test
	public void testA09checkSumaPorcentagesApartadosInvalido() {
		try {
			assertFalse(ModeloBaremacionApartados.obtenerInstancia().checkSumaPorcentagesApartadosInvalido());
			desactivarApartado(CODNUM);
			assertTrue(ModeloBaremacionApartados.obtenerInstancia().checkSumaPorcentagesApartadosInvalido());		
			activarApartado(CODNUM);
			assertFalse(ModeloBaremacionApartados.obtenerInstancia().checkSumaPorcentagesApartadosInvalido());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	// ERRORES
	
	/**
	 * getApartadoBaremacionById.
	 */
	@Test
	public void testE01getApartadoBaremacionById() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(null));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionApartados.ERROR_BLOQUE_REQUERIDO, throwable.getMessage());
	}
	
	/**
	 * getApartadoBaremacionById.
	 */
	@Test
	public void testE02getApartadoBaremacionById() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(CODNUM_NOEXISTE));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionApartados.ERROR_APARTADO_NOEXITE, throwable.getMessage());
	}
	
	/**
	 * insertaApartado.
	 */
	@Test
	public void testE03insertaApartado() {
		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloBaremacionApartados.obtenerInstancia().insertaApartado(null, UtilsTestBolsaEmpleo.getUsuario("personal1")));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionApartados.ERROR_BLOQUE_REQUERIDO, throwable.getMessage());
	}
	
	/**
	 * existeOtroApartadoActivoPorCodigo.
	 */
	@Test
	public void testE04existeOtroApartadoActivoPorCodigo() {
		Throwable throwable = assertThrows(Throwable.class, () -> {
			ApartadoBaremacion a = ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(CODNUM);
			a.setCodNum(CODNUM_NOEXISTE);
			ModeloBaremacionApartados.obtenerInstancia().insertaApartado(a, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		});
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionApartados.ERROR_APARTADO_MISMO_CODIGO, throwable.getMessage());
	}
	
	/**
	 * actualiza partado usado.
	 */
	@Test
	public void testE05actualizarApartadoQueSeEstaUsando() {
		Throwable throwable = assertThrows(Throwable.class, () -> {
			ApartadoBaremacion a = ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(CODNUM);
			a.setNombre("nombre nuevo");			
			ModeloBaremacionApartados.obtenerInstancia().actualizaApartado(a, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		});
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionApartados.ERROR_APARTADO_USANDOSE, throwable.getMessage());
	}
	
	/**
	 * error porcenaje inválido.
	 */
	@Test
	public void testE06sumanPorcentajeInvalido() {
		desactivarApartado(CODNUM);	
		desactivarApartadoPorCodigo("2");
		
		Throwable throwable = assertThrows(Throwable.class, () -> {
			ApartadoBaremacion a = getApartadoBaremacionPorCodigo("2");
			a.setPorcentajeMaximo(PORCENTAJE_MAXIMO3);
			ModeloBaremacionApartados.obtenerInstancia().actualizaApartado(a, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			ModeloBaremacionApartados.obtenerInstancia().activarApartado(a, UtilsTestBolsaEmpleo.getUsuario("personal1"));			
		});
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloBaremacionApartados.ERROR_SUMA_FACTORES_INVALIDA, throwable.getMessage());
		
		activarApartado(CODNUM);
	}
	
	// UTILS
	
	private List<ApartadoBaremacion> desactivarTodosApartados() {
		List<ApartadoBaremacion> lista = null;
		
		try {
			lista = ModeloBaremacionApartados.obtenerInstancia().getApartadosActivos(); 
			for (ApartadoBaremacion a : lista) {
				desactivarApartado(a.getCodNum());
			}
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
		
		return lista;
	}
	
	private void activarApartadosLista(List<ApartadoBaremacion> lista) {
		for (ApartadoBaremacion a : lista) {
			activarApartado(a.getCodNum());
		}
	}
	
	private void desactivarApartado(Integer codNum) {
		try {
			ModeloBaremacionApartados modelo = ModeloBaremacionApartados.obtenerInstancia();
			ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
			assertEquals(apartado.getCodNum(), codNum);
			modelo.desactivarApartado(apartado, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			apartado = modelo.getApartadoBaremacionById(codNum);
			assertFalse(apartado.getActivo());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	private void activarApartado(Integer codNum) {
		try {
			ModeloBaremacionApartados modelo = ModeloBaremacionApartados.obtenerInstancia();
			ApartadoBaremacion apartado = modelo.getApartadoBaremacionById(codNum);
			assertEquals(apartado.getCodNum(), codNum);
			modelo.activarApartado(apartado, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			apartado = modelo.getApartadoBaremacionById(codNum);
			assertTrue(apartado.getActivo());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	private void desactivarApartadoPorCodigo(String codigo) {
		try {
			ApartadoBaremacion a = getApartadoBaremacionPorCodigo(codigo);
			assertEquals(a.getCodigo(), codigo);
			
			a.setActivo(false);
			ModeloBaremacionApartados.obtenerInstancia().actualizaApartado(a, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			ApartadoBaremacion apartado = getApartadoBaremacionPorCodigo(codigo);
			assertFalse(apartado.getActivo());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	private ApartadoBaremacion getApartadoBaremacionPorCodigo(String codigo) {
		try {
			List<ApartadoBaremacion> lista = ModeloBaremacionApartados.obtenerInstancia().getApartados(null); 
			for (ApartadoBaremacion a : lista) {
				if (a.getCodigo().equals(codigo)) {
					return a;
				}
			}			
		} catch (SQLException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
		
		return null;
	}
}
