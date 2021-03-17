package es.ujaen.uvirtual.utilidades;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilidad para la gestión de los parámetros del datatable.
 * @author ATISoluciones
 * @param <T> Modelo que gestiona el datatable.
 */
public class DataTable<T> {
	public static final boolean VERBOSE = true;
	private static final String NOMBREDEESTACLASE = DataTable.class.getName();	
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	public static final String PARAM_CURRENT_PAGE = "page";
	public static final String PARAM_PAGE_SIZE = "pageSize";
	public static final String PARAM_ORDER_BY = "orderBy";
	public static final String PARAM_ORDER_DIRECTION = "orderDirection";
	public static final String PARAM_ORDER_DIRECTION_VALUE_ASC = "asc";
	public static final String PARAM_ORDER_DIRECTION_VALUE_DESC = "desc";
	
	private String consulta;
	private String query;
	private String queryCount;
		
	private Integer pageSize;
	private Integer currentPage;	
	private Integer recordsTotal; // número de filas totales de la consulta
	private Integer orderBy;
	private String orderDirection;
	private Map<Integer, String> orderColumns = new HashMap<Integer, String>();
	
	private List<T> data;
		
	/**
	 * Crear un DT a partir del request.
	 * @param request .
	 * @throws UVException .
	 */
	public DataTable(HttpServletRequest request) throws UVException {
		try {
			this.currentPage = Integer.parseInt(request.getParameter(DataTable.PARAM_CURRENT_PAGE));
			this.pageSize = Integer.parseInt(request.getParameter(DataTable.PARAM_PAGE_SIZE));
			
			String paramOrderBy = request.getParameter(DataTable.PARAM_ORDER_BY);
			if (!paramOrderBy.isBlank()) {
				this.orderBy = Integer.parseInt(paramOrderBy);
			}
			
			this.orderDirection = request.getParameter(DataTable.PARAM_ORDER_DIRECTION);			
		} catch (NumberFormatException err) {
			LOGGER.log(Level.WARNING, "Error Datatable" + err);
			throw new UVException("Datatable parámetro no válido");
		}
		
		if (!this.orderDirection.equals(PARAM_ORDER_DIRECTION_VALUE_ASC) && !this.orderDirection.equals(PARAM_ORDER_DIRECTION_VALUE_DESC)) {
			throw new UVException("Datatable tipo de ordenación no válido");
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
		}		
	}	
	
	public void setData(List<T> data) {
		this.data = data;
	}
	
	public List<T> getData() {
		return this.data;
	}
	
	public Integer getRecordsTotal() {
		return recordsTotal;
	}

	public Integer getPagesTotal() {
		return this.recordsTotal / this.pageSize;
	}
		
	public Boolean isOrderable() {
		return this.orderBy != null;
	}
	
	/**
	 * Establece la lista de columnas ordenables, con su columna para la ordenación.
	 * @param index .
	 * @param column .
	 */
	public void setOrderColumn(Integer index, String column) {
		orderColumns.put(index, column);		
	}
	
	private String prepareQuery(String pconsulta) throws UVException {
		String consultaResult = "";
		
		if (this.isOrderable()) {
			String column = this.orderColumns.get(this.orderBy);
			if (column == null) {
				throw new UVException("La columna de ordenación es no válida");
			}
			
			consultaResult += " ORDER BY " + column + " " + this.orderDirection;
		}
		
		return pconsulta + consultaResult;
	}

	
}

