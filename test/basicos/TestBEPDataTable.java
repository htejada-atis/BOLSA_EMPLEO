package basicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/** test datatable.
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
	
	/**
	 * Class mock para testear datatable.
	 */
	class Mock {
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
	
    /** Creación de dt correcta.
     * @throws UVException .
     */
    @Test 
    public void testA01() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<Mock>(params);
    	
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
    	
    	params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {""});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<Mock>(params);
    	
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
    	
    	params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	//params.put(DataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<Mock>(params);
    	
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
    	
    	params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"dummy"});
    	params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new BolsaEmpleoDataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
    }
    
    /** Error, parámetro current page vacio.
     * @throws UVException .
     */
    @Test 
    public void testA05() {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {""});
    	params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new BolsaEmpleoDataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
    }
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test
    public void testA06() {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"ddddd"});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new BolsaEmpleoDataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
    }
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test
    public void testA07() {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {""});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new BolsaEmpleoDataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
    }
    
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test
    public void testA08() {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {"dfdfddfd"});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new BolsaEmpleoDataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_NO_VALIDO, throwable.getMessage());
    }
        
    /** Creación de dt incorrecta.
     * @throws UVException .
     */
    @Test
    public void testA09() {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {"dddd"});
    	
    	Throwable throwable = assertThrows(Throwable.class, () -> { 
    		new BolsaEmpleoDataTable<Mock>(params); 
    	});
    	assertEquals(UVException.class, throwable.getClass());
    	assertEquals(BolsaEmpleoDataTable.ERROR_MSG_PARAMETRO_TIPO_ORDENACION_NO_VALIDO, throwable.getMessage());
    }
    
    /** Asignación de query correcta.
     * @throws UVException .
     */
    @Test
    public void testA10() throws UVException {
    	final String sql = "SELECT 'c1' AS C1, 'c2' AS C2"
    			+ "FROM DUAL"
    			+ "connect by level <= 100";
    	
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {ORDER_DIRECTION});
    	
    	BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<Mock>(params);
    	dt.setColumn(ORDER_BY, "C1");
    	dt.setColumn(ORDER_BY + 1, "C2");
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
    public void testA11() throws UVException {
    	HashMap<String, String[]> params = new HashMap<String, String[]>();
    	
    	params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {CURRENT_PAGE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {PAGE_SIZE.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {ORDER_BY.toString()});
    	params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_DESC});
    	
    	BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<Mock>(params);
    	
    	assertEquals(CURRENT_PAGE, dt.getCurrentPage());
    	assertEquals(PAGE_SIZE, dt.getPageSize());
    	assertEquals(ORDER_BY, dt.getOrderBy());
    	assertEquals(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_DESC, dt.getOrderDirection());    	
    }
    
    /** Asignación de query correcta.
     * @throws UVException .
     */
    @Test
    public void testA12() throws UVException {
    	final String sql = "SELECT 'c1' AS C1, 'c2' AS C2"
    			+ "FROM DUAL"
    			+ "connect by level <= 100";
    	
    	HashMap<String, String[]> params = new HashMap<String, String[]>();    	
    	BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<Mock>(params);
    	dt.setColumn(ORDER_BY, "C1");
    	dt.setColumn(ORDER_BY + 1, "C2");
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
    
    /** Generar json a partir de dt.
     * @throws UVException .
     */
    @Test
    public void testA13() throws UVException {
    	final String sql = "SELECT 'c1' AS C1, 'c2' AS C2"
    			+ "FROM DUAL"
    			+ "connect by level <= 100";
    	
		HashMap<String, String[]> params = new HashMap<String, String[]>();
		BolsaEmpleoDataTable<Mock> dt = new BolsaEmpleoDataTable<Mock>(params);
		dt.setColumn(ORDER_BY, "C1");
		dt.setColumn(ORDER_BY + 1, "C2");
		dt.setQuery(sql);

		ArrayList<Mock> lista = new ArrayList<Mock>();
		lista.add(new Mock("1", EDAD_1));
		lista.add(new Mock("2", EDAD_2));
		lista.add(new Mock("3", EDAD_3));
		dt.setData(lista);

		Gson gson = new GsonBuilder().setExclusionStrategies(BolsaEmpleoDataTable.GSONEXCLUSIONSTRATEGY).create();
		String json = gson.toJson(dt);
		
		assertNotEquals(-1, json.indexOf("{\"codNum\":\"1\",\"edad\":" + EDAD_1 + "}")); 
		assertNotEquals(-1, json.indexOf("{\"codNum\":\"2\",\"edad\":" + EDAD_2 + "}"));
		assertNotEquals(-1, json.indexOf("{\"codNum\":\"3\",\"edad\":" + EDAD_3 + "}"));
				
		// System.out.println(json);
    }
    
}
