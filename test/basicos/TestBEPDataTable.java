package basicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * test datatable.
 * 
 * @author ATISoluciones
 */
public class TestBEPDataTable {
	public static final Integer CURRENT_PAGE = 0;
	public static final Integer PAGE_SIZE = 10;
	public static final Integer ORDER_BY = 0;
	public static final String ORDER_DIRECTION = BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC;
	public static final int EDAD_1 = 10;
	public static final int EDAD_2 = 21;
	public static final int EDAD_3 = 34;
	public static final String SQL_10 = "SELECT 'c1' AS C1, 1 AS C2 FROM DUAL connect by level <= 10";
	public static final String SQL_15 = "SELECT 'c1' AS C1, 1 AS C2 FROM DUAL connect by level <= 15";
	public static final String SQL_20 = "SELECT 'c1' AS C1, 1 AS C2 FROM DUAL connect by level <= 20";
	public static final String SQL_100 = "SELECT 'c1' AS C1, 1 AS C2 FROM DUAL connect by level <= 100";
	public static final String SQL_3 = "SELECT * FROM ( "
			+ " SELECT 'c1' C1, 1 C2 FROM DUAL "
			+ "	UNION "
			+ "	SELECT 'c2' C1, 2 C2 FROM DUAL "
			+ "	UNION "
			+ "	SELECT 'c3' C1, 3 C2 FROM DUAL) "
			+ "WHERE 1=1";
	public static final Integer COUNT_1 = 1;
	public static final Integer COUNT_2 = 2;
	public static final Integer COUNT_3 = 3;
	public static final Integer COUNT_10 = 10;
		
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";

	/**
	 * Class mock para testear datatable.
	 */
	static class Mock {
		private String codNum;
		private Integer edad;

		public Mock() {
			this.codNum = "1";
			this.edad = 1;
		}

		public Mock(String pcodNum, Integer pedad) {
			this.codNum = pcodNum;
			this.edad = pedad;
		}

		public String getCodNum() {
			return codNum;
		}

		public void setCodNum(String codNum) {
			this.codNum = codNum;
		}

		public Integer getEdad() {
			return edad;
		}

		public void setEdad(Integer edad) {
			this.edad = edad;
		}
	}
	
	/** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
	 * @throws ParseException si error fecha
     */
	@BeforeClass
	public static void preparaBd() {
		BbddRunner.conectarBd();
		DataSource ds = BbddRunner.obtenerDataSourceUv();
		Conexion.setConexionUvirtual(ds);
	}

	/**
	 * Creación de dt correcta.
	 */
	@Test
	public void testA01() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});

		try {
			BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<>(params);

			assertEquals(CURRENT_PAGE, dt.getCurrentPage());
			assertEquals(PAGE_SIZE, dt.getPageSize());
			assertEquals(ORDER_BY, dt.getOrderBy());
			assertEquals(ORDER_DIRECTION, dt.getOrderDirection());
		} catch (UVException e) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, e.toString()));
		}		
	}

	/**
	 * Creación de dt correcta sin ordenación de columnas.
	 */
	@Test
	public void testA02() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {""});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});

		try {
			BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<>(params);
	
			assertEquals(CURRENT_PAGE, dt.getCurrentPage());
			assertEquals(PAGE_SIZE, dt.getPageSize());
			assertEquals(null, dt.getOrderBy());
			assertEquals(ORDER_DIRECTION, dt.getOrderDirection());
		} catch (UVException e) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, e.toString()));
		}
	}

	/**
	 * Creación de dt correcta sin dirección de ordenación.
	 */
	@Test
	public void testA03() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});

		try {
			BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<>(params);
	
			assertEquals(CURRENT_PAGE, dt.getCurrentPage());
			assertEquals(PAGE_SIZE, dt.getPageSize());
			assertEquals(ORDER_BY, dt.getOrderBy());
			assertEquals("", dt.getOrderDirection());
		} catch (UVException e) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, e.toString()));
		}
	}

	/**
	 * Asignación de query correcta.
	 */
	@Test
	public void testA04() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});

		try {
			BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<>(params);
			dt.setColumn(ORDER_BY, "C1");
			dt.setColumn(ORDER_BY + 1, "C2");
			dt.setQuery(SQL_100);
	
			assertNotNull(dt.getQuery());
			assertNotNull(dt.getQueryCount());
	
			ArrayList<Mock> lista = new ArrayList<>();
			lista.add(new Mock());
			lista.add(new Mock());
			lista.add(new Mock());
	
			dt.setData(lista);
	
			assertEquals(lista.size(), dt.getData().size());
		} catch (UVException e) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, e.toString()));
		}
	}

	/**
	 * Creación de dt correcta ordenación descendente.
	 */
	@Test
	public void testA05() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_DESC});

		try {
			BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<>(params);
	
			assertEquals(CURRENT_PAGE, dt.getCurrentPage());
			assertEquals(PAGE_SIZE, dt.getPageSize());
			assertEquals(ORDER_BY, dt.getOrderBy());
			assertEquals(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_DESC, dt.getOrderDirection());
		} catch (UVException e) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, e.toString()));
		}
	}

	/**
	 * Filtro columnas.
	 */
	@Test
	public void testA06() {
		BolsaEmpleoDataTable<Mock> dt = getDatatableByQuery(SQL_3, "{'0': 'c1'}");
		assertNotNull(dt);
			
		assertNotNull(dt.getQuery());
		assertNotNull(dt.getQueryCount());
		assertFalse(dt.getData().isEmpty());
		assertEquals(dt.getData().size(), 1);
		assertEquals(dt.getPagesTotal(), Integer.valueOf(1));
		
		Mock row = dt.getData().get(0);
		assertEquals(row.getCodNum(), "c1");
		assertEquals(row.getEdad(), Integer.valueOf(1));
	}
	
	/**
	 * Calculo correcto del número de páginas.
	 */
	@Test
	public void testA07() {
		BolsaEmpleoDataTable<Mock> dt = getDatatableByQuery(SQL_10);
		assertNotNull(dt);
		assertEquals(dt.getPagesTotal(), COUNT_1);
							
		dt = getDatatableByQuery(SQL_15);
		assertNotNull(dt);
		assertEquals(dt.getPagesTotal(), COUNT_2);
		
		dt = getDatatableByQuery(SQL_20);
		assertNotNull(dt);
		assertEquals(dt.getPagesTotal(), COUNT_2);
		
		dt = getDatatableByQuery(SQL_100);
		assertNotNull(dt);
		assertEquals(dt.getPagesTotal(), COUNT_10);
	}
	
	/**
	 * Json text.
	 */
	@Test
	public void testA08() {
		BolsaEmpleoDataTable<Mock> dt = getDatatableByQuery(SQL_3);
		assertNotNull(dt);
		
		assertEquals(dt.getData().size(), (int) COUNT_3);
		assertEquals(dt.getPagesTotal(), COUNT_1);
		
		String json = dt.toJson();
		assertNotNull(json);
		assertTrue(json.indexOf("\"pageSize\":10") != -1);
		assertTrue(json.indexOf("\"currentPage\":0") != -1);
		assertTrue(json.indexOf("\"recordsTotal\":3") != -1);
		assertTrue(json.indexOf("\"pagesTotal\":1") != -1);
		assertTrue(json.indexOf("{\"codNum\":\"c1\",\"edad\":1}") != -1);
		assertTrue(json.indexOf("{\"codNum\":\"c2\",\"edad\":2}") != -1);
		assertTrue(json.indexOf("{\"codNum\":\"c3\",\"edad\":3}") != -1);				
	}

	// ERRORES //	
	
	/**
	 * Error, parámetro current page incorrecto.
	 * 
	 * @throws UVException .
	 */
	@Test
	public void testE01() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"dummy"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});

		Throwable throwable = assertThrows(Throwable.class, () -> 
			new BolsaEmpleoDataTable<>(params)
		);
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
	}
	
	/**
	 * Error, parámetro current page vacio.
	 */
	@Test
	public void testE02() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {""});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});

		Throwable throwable = assertThrows(Throwable.class, () -> 
			new BolsaEmpleoDataTable<>(params)
		);
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
	}
	
	/**
	 * Creación de dt incorrecta.
	 * 
	 * @throws UVException .
	 */
	@Test
	public void testE03() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {""});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});

		Throwable throwable = assertThrows(Throwable.class, () -> 
			new BolsaEmpleoDataTable<>(params)
		);
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
	}
	
	/**
	 * Creación de dt incorrecta.
	 * 
	 * @throws UVException .
	 */
	@Test
	public void testE04() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"ddddd"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});

		Throwable throwable = assertThrows(Throwable.class, () -> 
			new BolsaEmpleoDataTable<>(params)
		);
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
	}
	
	/**
	 * Creación de dt incorrecta.
	 * 
	 * @throws UVException .
	 */
	@Test
	public void testE05() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {"dfdfddfd"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});

		Throwable throwable = assertThrows(Throwable.class, () -> 
			new BolsaEmpleoDataTable<>(params)
		);
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
	}
	
	/**
	 * Creación de dt incorrecta.
	 * 
	 * @throws UVException .
	 */
	@Test
	public void testE06() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {"dddd"});

		Throwable throwable = assertThrows(Throwable.class, () -> 
			new BolsaEmpleoDataTable<>(params)
		);
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_TIPO_ORDENACION_NO_VALIDO, throwable.getMessage());
	}
	
	/**
	 * Filtro de búsqueda no válido.
	 */
	@Test
	public void testE07() {
		String filter = "hola";
		
		HashMap<String, String[]> params = new HashMap<>();
		params.put(BolsaEmpleoDataTable.PARAM_FILTER, new String[] {filter});
		
		Throwable throwable = assertThrows(Throwable.class, () -> new BolsaEmpleoDataTable<>(params));
		
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_BUSQUEDA_NO_VALIDO, throwable.getMessage());
		
		filter = "{1: 1}";		
		HashMap<String, String[]> params2 = new HashMap<>();
		params2.put(BolsaEmpleoDataTable.PARAM_FILTER, new String[] {filter});
		
		throwable = assertThrows(Throwable.class, () -> new BolsaEmpleoDataTable<>(params2));
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_BUSQUEDA_NO_VALIDO, throwable.getMessage());
		
	}
	
	/**
	 * Columna no válida.
	 */
	@Test
	public void testE08() {
		HashMap<String, String[]> params = new HashMap<>();
		params.put(BolsaEmpleoDataTable.PARAM_FILTER, new String[] {"{'5': 'hola'}"});
		
		Throwable throwable = assertThrows(Throwable.class, () -> {
			BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<>(params);
			dt.setQuery(SQL_3);
		});
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(BolsaEmpleoDataTable.ERROR_MSG_COLUMNA_FILTRADO_NO_VALIDA, throwable.getMessage());
	}
	
	private BolsaEmpleoDataTable<Mock> getDatatableByQuery(String sql) {
		return getDatatableByQuery(sql, null);
	}
	
	private BolsaEmpleoDataTable<Mock> getDatatableByQuery(String sql, String filter) {
		try {
			ArrayList<Mock> lista = new ArrayList<>();			
			HashMap<String, String[]> params = new HashMap<>();	
			
			if (filter != null) {
				params.put(BolsaEmpleoDataTable.PARAM_FILTER, new String[] {filter});
			}
			
			BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<>(params);
			dt.setColumn(ORDER_BY, "C1");
			dt.setColumn(ORDER_BY + 1, "C2", DataTableColumn.COLUMN_TYPE_NUMBER);
			dt.setQuery(sql);
			
			try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
					PreparedStatement stmtCount = conexion.prepareStatement(dt.getQueryCount());
					PreparedStatement stmt = conexion.prepareStatement(dt.getQuery())
			) {
				dt.setFiltersParams(stmt, stmtCount, 1);
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						Mock m = new Mock();
						m.setCodNum(rs.getString("C1"));
						m.setEdad(rs.getInt("C2"));						
						lista.add(m);
					}
				}
				
				dt.setRecordsTotalFromQuery(stmtCount);
				dt.setData(lista);
			}
			return dt;
		} catch (UVException | SQLException e) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, e.toString()));
		}
		
		return null;	
	}
}
