package es.ujaen.uvirtual.modulo.bolsaempleo.utilidades;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Utilidad para la gestión de los parámetros del datatable.
 * @author ATISoluciones 2021
 * @param <T> Modelo que gestiona el datatable.
 */
public class BolsaEmpleoDataTable<T> {
	public static final boolean VERBOSE = false;
	private static final String NOMBREDEESTACLASE = BolsaEmpleoDataTable.class.getName();	
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	public static final String PARAM_CURRENT_PAGE = "page";
	public static final String PARAM_PAGE_SIZE = "pageSize";
	public static final String PARAM_FILTER = "filter";
	public static final String PARAM_ORDER_BY = "orderBy";
	public static final String PARAM_ORDER_DIRECTION = "orderDirection";
	public static final String PARAM_ORDER_DIRECTION_VALUE_ASC = "asc";
	public static final String PARAM_ORDER_DIRECTION_VALUE_DESC = "desc";
	
	public static final String PARAM_CURRENT_PAGE_VALUE_DEFAULT = "0";
	public static final String PARAM_PAGE_SIZE_VALUE_DEFAULT = "10";
	
	public static final String ERROR_MSG_PARAMETRO_NO_VALIDO = "Datatable parámetro no válido";
	public static final String ERROR_MSG_PARAMETRO_BUSQUEDA_NO_VALIDO = "Datatable parámetro búsqueda no válido";
	public static final String ERROR_MSG_PARAMETRO_TIPO_ORDENACION_NO_VALIDO = "Datatable tipo de ordenación no válido";
	public static final String ERROR_MSG_COLUMNA_ORDENACION_NO_VALIDA = "La columna de ordenación es no válida";
	public static final String ERROR_MSG_COLUMNA_FILTRADO_NO_VALIDA = "La columna de filtrado es no válida";
	
	private String consulta;
	private String query;
	private String queryCount;
	
	private Integer pageSize;
	private Integer currentPage;
	private Integer recordsTotal; // número de filas totales de la consulta
	private Integer pagesTotal;
	private Integer orderBy;
	private String orderDirection;
	private Map<Integer, String> filters;
	private List<T> data;
	
	/** clase .
	 */
	public class DataTableColumn {
		private String columnName;
		private int columnType;
		
		public static final int COLUMN_TYPE_TEXT = 0;
		public static final int COLUMN_TYPE_NUMBER = 1;
		public static final int COLUMN_TYPE_DATE = 2;
		public static final int COLUMN_TYPE_BOOLEAN = 3;
		public static final int COLUMN_TYPE_OPTION = 4;
		public static final int COLUMN_TYPE_IS_NULL = 5;
		
		/** Constructor por parámetros .
		 * @param pcolumnName .
		 */
		public DataTableColumn(String pcolumnName) {
			this.columnName = pcolumnName;
			this.columnType = COLUMN_TYPE_TEXT;
		}
		
		/** Constructor por parámetros .
		 * @param pcolumnName .
		 * @param pcolumnType .
		 */
		public DataTableColumn(String pcolumnName, int pcolumnType) {
			this.columnName = pcolumnName;
			this.columnType = pcolumnType;
		}
		
		public String getName() {
			return columnName;
		}
		
		public int getType() {
			return columnType;
		}
	}
	
	private Map<Integer, DataTableColumn> columns = new HashMap<Integer, DataTableColumn>();
	
	public static final ExclusionStrategy GSONEXCLUSIONSTRATEGY = new ExclusionStrategy() {
		@Override
		public boolean shouldSkipClass(Class<?> arg0) {
			return false;
		}

		@Override
		public boolean shouldSkipField(FieldAttributes arg0) {
			List<String> list = Arrays.asList(new String[]{"consulta", "query", "queryCount", "orderDirection", "columns"});
			
			if (list.contains(arg0.getName())) {
				return true;
	        }
			
			return false;
		}		
	};
		
	/**
	 * Crear un DT a partir de los parámetros del request.
	 * @param params .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable(Map<String, String[]> params) throws UVException {
		try {
			this.currentPage = Integer.parseInt(String.join("", params.getOrDefault(PARAM_CURRENT_PAGE, new String[] {PARAM_CURRENT_PAGE_VALUE_DEFAULT})));
			this.pageSize = Integer.parseInt(String.join("", params.getOrDefault(PARAM_PAGE_SIZE, new String[] {PARAM_PAGE_SIZE_VALUE_DEFAULT})));
			
			String paramOrderBy = String.join("", params.getOrDefault(PARAM_ORDER_BY, new String[] {""}));
			if (!paramOrderBy.isBlank() && paramOrderBy != null) {
				this.orderBy = Integer.parseInt(paramOrderBy);
			}
			
			this.orderDirection = String.join("", params.getOrDefault(PARAM_ORDER_DIRECTION, new String[] {""}));			
		} catch (NumberFormatException err) {
			LOGGER.log(Level.WARNING, "Error Datatable" + err);
			throw new UVException(ERROR_MSG_PARAMETRO_NO_VALIDO);
		}
		
		try {
			String paramFilters = String.join("", params.getOrDefault(PARAM_FILTER, new String[] {""}));
			if (!paramFilters.isBlank() && paramFilters != null) {
				filters = new GsonBuilder().create().fromJson(paramFilters, new TypeToken<HashMap<Integer, String>>() { }.getType());
			}
		} catch (Exception ex) {
			LOGGER.log(Level.WARNING, "Error Datatable" + ex);
			throw new UVException(ERROR_MSG_PARAMETRO_BUSQUEDA_NO_VALIDO);
		}
		
		if (!this.orderDirection.isBlank() && this.orderDirection != null) {
			if (!this.orderDirection.equals(PARAM_ORDER_DIRECTION_VALUE_ASC) && !this.orderDirection.equals(PARAM_ORDER_DIRECTION_VALUE_DESC)) {
				throw new UVException(ERROR_MSG_PARAMETRO_TIPO_ORDENACION_NO_VALIDO);
			}
		}
	}
		
	/**
	 * Establece la consulta a ejecutar en el datatable.
	 * @param pconsulta sql a ejectuar.
	 * @throws UVException .
	 */
	public void setQuery(String pconsulta) throws UVException {
		// https://www.oracletutorial.com/oracle-basics/oracle-fetch/
		this.consulta = this.prepareQuery(pconsulta);
		this.queryCount = "SELECT COUNT(*) AS count FROM (" + this.consulta + ")";		
		// this.query = this.consulta + " OFFSET " + (this.pageSize * this.currentPage) + " ROWS FETCH NEXT " + this.pageSize + " ROWS ONLY";				
		this.query = this.consulta + " OFFSET " + (this.pageSize * this.currentPage) + " ROWS FETCH NEXT " + this.pageSize + " ROWS ONLY";
			
		if (VERBOSE) {
			LOGGER.log(Level.INFO, "DATATABLE ---------------------------------------");
			LOGGER.log(Level.INFO, this.queryCount);
			LOGGER.log(Level.INFO, this.query);
			LOGGER.log(Level.INFO, "-------------------------------------------------");
		}
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public String getQueryCount() {
		return this.queryCount;
	}
	
	public Integer getCurrentPage() {
		return this.currentPage;
	}
	
	public Integer getPageSize() {
		return this.pageSize;
	}
	
	public Integer getOrderBy() {
		return this.orderBy;
	}
	
	public String getOrderDirection() {
		return this.orderDirection;
	}
	
	public Integer getPagesTotal() {
		return this.pagesTotal;
	}
	
	/** Método para agregar parámetros de los filtros a la consulta .
	 * @param stmt .
	 * @param stmtCount .
	 * @param index .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void setFiltersParams(PreparedStatement stmt, PreparedStatement stmtCount, int index) throws SQLException, UVException {
		if (Boolean.TRUE.equals(this.isFilterable())) {
			for (Map.Entry<Integer, String> filter : filters.entrySet()) {
				Integer key = filter.getKey();
				String value = filter.getValue();
				
				if (this.columns.get(key).getType() == DataTableColumn.COLUMN_TYPE_DATE) {
					stmt.setString(index, "%" + new Date(Formateador.leeParametroFecha(value, Formateador.FORMATO_FECHA_DDMMYYYY, "/").getTime()));
					stmtCount.setString(index++, "%" + new Date(Formateador.leeParametroFecha(value, Formateador.FORMATO_FECHA_DDMMYYYY, "/").getTime()));
				} else if (this.columns.get(key).getType() == DataTableColumn.COLUMN_TYPE_NUMBER) {
					stmt.setInt(index, Integer.parseInt(value));
					stmtCount.setInt(index++, Integer.parseInt(value));
				} else if (this.columns.get(key).getType() == DataTableColumn.COLUMN_TYPE_BOOLEAN) {
					stmt.setString(index, Boolean.parseBoolean(value) ? "S" : "N");
					stmtCount.setString(index++, Boolean.parseBoolean(value) ? "S" : "N");
				} else if (this.columns.get(key).getType() == DataTableColumn.COLUMN_TYPE_OPTION) {
					stmt.setString(index, value);
					stmtCount.setString(index++, value);
				} else if (this.columns.get(key).getType() == DataTableColumn.COLUMN_TYPE_TEXT) {
					stmt.setString(index, "%" + value + "%");
					stmtCount.setString(index++, "%" + value + "%");
				}
			}
		}
	}
	
	/**
	 * Ejecuta la consulta para obtener el total de filas.
	 * @param stmt .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void setRecordsTotalFromQuery(PreparedStatement stmt) throws SQLException, UVException {
		try (ResultSet rs = stmt.executeQuery()) {
			if (!rs.next()) {
				throw new UVException("Error obteniendo número total de filas");
			}
			this.recordsTotal = rs.getInt("count");
			this.pagesTotal = this.recordsTotal / this.pageSize;
			if (this.recordsTotal % this.pageSize == 0) {
				this.pagesTotal -= 1;
			}
		}
	}
	
	public void setData(List<T> data) {
		this.data = data;
	}
	
	public List<T> getData() {
		return this.data;
	}
		
	public Boolean isOrderable() {
		return this.orderBy != null;
	}
	
	public Boolean isFilterable() {
		return this.filters != null;
	}
	
	/**
	 * Establece la lista de columnas ordenables, con su columna para la ordenación.
	 * @param index .
	 * @param column .
	 */
	public void setColumn(Integer index, String column) {
		columns.put(index, new DataTableColumn(column));	
	}
	
	/**
	 * Establece la lista de columnas ordenables, con su columna para la ordenación.
	 * @param index .
	 * @param column .
	 * @param type .
	 */
	public void setColumn(Integer index, String column, int type) {
		columns.put(index, new DataTableColumn(column, type));	
	}
	
	private String prepareQuery(String pconsulta) throws UVException {
		String consultaResult = "";
		if (this.isFilterable()) {
			consultaResult += filterByQuery();
		}
		
		if (this.isOrderable()) {
			consultaResult += orderByQuery();
		}
		
		return pconsulta + consultaResult;
	}
	
	private String orderByQuery() throws UVException {
		String column = this.columns.get(this.orderBy).getName();
		if (column == null) {
			throw new UVException(ERROR_MSG_COLUMNA_ORDENACION_NO_VALIDA);
		}
		
		return " ORDER BY " + column + " " + this.orderDirection;
	}
	
	private String filterByQuery() throws UVException {
		String consultaResult = "";
		for (Map.Entry<Integer, String> filter : filters.entrySet()) {
	        Integer key = filter.getKey();
			String column = this.columns.get(key).getName();
			
			if (column == null) {
				throw new UVException(ERROR_MSG_COLUMNA_FILTRADO_NO_VALIDA);
			}
			
			switch (this.columns.get(key).getType()) {
				case DataTableColumn.COLUMN_TYPE_DATE:
					consultaResult += " AND TO_CHAR(" + column + ",'yyyy-mm-dd') LIKE ? ";
					break;
				case DataTableColumn.COLUMN_TYPE_NUMBER:
				case DataTableColumn.COLUMN_TYPE_BOOLEAN:
				case DataTableColumn.COLUMN_TYPE_OPTION:
					consultaResult += " AND " + column + " = ? ";
					break;
				case DataTableColumn.COLUMN_TYPE_IS_NULL:
					consultaResult += " AND " + column + " IS" + (Boolean.parseBoolean(filter.getValue()) ? " NOT" : "") + " NULL";
					break;
				case DataTableColumn.COLUMN_TYPE_TEXT:
					consultaResult += " AND lower(" + column + ") LIKE lower(?) ";
					break;
				default:
					consultaResult += " AND lower(" + column + ") LIKE lower(?) ";
					break;
			}
	    }
		return consultaResult;
	}
	
}

