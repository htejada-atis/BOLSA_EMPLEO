package basicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import basicos.TestDataTable.Mock;
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
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test(expected = UVException.class) 
    public void testA02() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {"dummy"});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    }
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test(expected = UVException.class) 
    public void testA03() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {""});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    }
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test(expected = UVException.class) 
    public void testA04() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {"ddddd"});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    }
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test(expected = UVException.class) 
    public void testA05() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {""});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    }
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test(expected = UVException.class) 
    public void testA06() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {"dfdfddfd"});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    }
        
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test(expected = UVException.class) 
    public void testA07() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {"dddd"});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    }
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test(expected = UVException.class) 
    public void testA08() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {""});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);
    }
    
    /** Error al no poner ordenación.
     * @throws UVException .
     */
    @Test(expected = UVException.class)  
    public void testA09() throws UVException {
    	String sql = "SELECT 'c1' AS C1, 'c2' AS C2"
    			+ "FROM DUAL"
    			+ "connect by level <= 100";
    	
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(DataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(DataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(DataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	DataTable<Mock> dt = new DataTable<Mock>(params);    	
    	dt.setQuery(sql);
    	
    	assertNotNull(dt.getQuery());
    	assertNotNull(dt.getQueryCount());
    	
    	ArrayList<Mock> lista = new ArrayList();
    	lista.add(new Mock());
    	lista.add(new Mock());
    	lista.add(new Mock());
    	
    	dt.setData(lista);
    	
    	assertEquals(lista.size(), dt.getData().size());
    }
    
    /** Asignación de query correcta.
     * @throws UVException .
     */
    public void testA10() throws UVException {
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
}
