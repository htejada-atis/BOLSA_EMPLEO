package basicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/** test datatable.
 * @author ATISoluciones
 */
public class TestDataTable {
	public static final Integer CURRENT_PAGE = 0;
	public static final Integer PAGE_SIZE = 10;
	public static final Integer ORDER_BY = 0;
	public static final String ORDER_DIRECTION = DataTable.PARAM_ORDER_DIRECTION_VALUE_ASC;
	
	/**
	 * Class mock para testear datatable.
	 */
	class Mock { }
	
	class MockPreparedStatement { }
	
    /** Creación de dt correcta.
     * @throws UVException .
     */
    @Test 
    public void testA01() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    	
    	assertEquals(CURRENT_PAGE, dt.getCurrentPage());
    	assertEquals(PAGE_SIZE, dt.getPageSize());
    	assertEquals(ORDER_BY, dt.getOrderBy());
    	assertEquals(ORDER_DIRECTION, dt.getOrderDirection());    	
    }
    
    /** Creación de dt correcta sin ordenación de columnas.
     * @throws UVException .
     */
    @Test 
    public void testA02() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {""});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    	
    	assertEquals(CURRENT_PAGE, dt.getCurrentPage());
    	assertEquals(PAGE_SIZE, dt.getPageSize());
    	assertEquals(null, dt.getOrderBy());
    	assertEquals(ORDER_DIRECTION, dt.getOrderDirection());    	
    }
    
    /** Creación de dt correcta sin dirección de ordenación.
     * @throws UVException .
     */
    @Test 
    public void testA03() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	//params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    	
    	assertEquals(CURRENT_PAGE, dt.getCurrentPage());
    	assertEquals(PAGE_SIZE, dt.getPageSize());
    	assertEquals(ORDER_BY, dt.getOrderBy());
    	assertEquals("", dt.getOrderDirection());    	
    }
    
    /** Error, parámetro current page incorrecto.
     * @throws UVException .
     */
    @Test 
    public void testA04() {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {"dummy"});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new DataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(DataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
    }
    
    /** Error, parámetro current page vacio.
     * @throws UVException .
     */
    @Test 
    public void testA05() {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {""});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new DataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(DataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
    }
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test
    public void testA06() {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {"ddddd"});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new DataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(DataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
    }
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test
    public void testA07() {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {""});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new DataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(DataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
    }
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test
    public void testA08() {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {"dfdfddfd"});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new DataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(DataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
    }
        
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test
    public void testA09() {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {"dddd"});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new DataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(DataTable.ERROR_MSG_PARAMETRO_TIPO_ORDENACION_NO_VALIDO, throwable.getMessage());
    }
   
    /** Error al no poner la columna de ordenacion ordenación.
     * @throws UVException .
     */
    @Test
    public void testA10() throws UVException {
    	String sql = "SELECT 'c1' AS C1, 'c2' AS C2"
    			+ "FROM DUAL"
    			+ "connect by level <= 100";
    	
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		DataTable<Mock> dt = new DataTable<Mock>(params);
    		dt.setQuery(sql);
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(DataTable.ERROR_MSG_COLUMNA_ORDENACION_NO_VALIDA, throwable.getMessage());
    }
    
    /** Asignación de query correcta.
     * @throws UVException .
     */
    @Test
    public void testA11() throws UVException {
    	final String sql = "SELECT 'c1' AS C1, 'c2' AS C2"
    			+ "FROM DUAL"
    			+ "connect by level <= 100";
    	
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    	dt.setOrderColumn(ORDER_BY, "C1");
    	dt.setOrderColumn(ORDER_BY + 1, "C2");
    	dt.setQuery(sql);
    	
    	assertNotNull(dt.getQuery());
    	assertNotNull(dt.getQueryCount());
    	
    	ArrayList<Mock> lista = new ArrayList<Mock>();
    	lista.add(new Mock());
    	lista.add(new Mock());
    	lista.add(new Mock());
    	
    	dt.setData(lista);
    	
    	assertEquals(lista.size(), dt.getData().size());
    }
    
    /** Creación de dt correcta ordenación descendente.
     * @throws UVException .
     */
    @Test 
    public void testA12() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {DataTable.PARAM_ORDER_DIRECTION_VALUE_DESC});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    	
    	assertEquals(CURRENT_PAGE, dt.getCurrentPage());
    	assertEquals(PAGE_SIZE, dt.getPageSize());
    	assertEquals(ORDER_BY, dt.getOrderBy());
    	assertEquals(DataTable.PARAM_ORDER_DIRECTION_VALUE_DESC, dt.getOrderDirection());    	
    }
    
    /** Asignación de query correcta.
     * @throws UVException .
     */
    @Test
    public void testA13() throws UVException {
    	final String sql = "SELECT 'c1' AS C1, 'c2' AS C2"
    			+ "FROM DUAL"
    			+ "connect by level <= 100";
    	
    	HashMap<String, String[]> params = new HashMap<String, String[]>();    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    	dt.setOrderColumn(ORDER_BY, "C1");
    	dt.setOrderColumn(ORDER_BY + 1, "C2");
    	dt.setQuery(sql);
    	
    	assertNotNull(dt.getQuery());
    	assertNotNull(dt.getQueryCount());
    	
    	ArrayList<Mock> lista = new ArrayList<Mock>();
    	lista.add(new Mock());
    	lista.add(new Mock());
    	lista.add(new Mock());
    	
    	dt.setData(lista);
    	
    	assertEquals(lista.size(), dt.getData().size());
    }
    
    /** Ejecutar consulta para obtener el total de filas.
     * @throws UVException .
     */
    @Test
    public void testA14() throws UVException {
    	final String sql = "SELECT 'c1' AS C1, 'c2' AS C2"
    			+ "FROM DUAL"
    			+ "connect by level <= 100";
    	
    	HashMap<String, String[]> params = new HashMap<String, String[]>();    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    	dt.setOrderColumn(ORDER_BY, "C1");
    	dt.setOrderColumn(ORDER_BY + 1, "C2");
    	dt.setQuery(sql);
    	
    	assertNotNull(dt.getQuery());
    	assertNotNull(dt.getQueryCount());
    	
    	MockPreparedStatement ps = new MockPreparedStatement();
    	dt.setRecordsTotalFromQuery(ps);
    	
    	ArrayList<Mock> lista = new ArrayList<Mock>();
    	lista.add(new Mock());
    	lista.add(new Mock());
    	lista.add(new Mock());
    	
    	dt.setData(lista);
    	
    	assertEquals(lista.size(), dt.getData().size());
    }
    
}
