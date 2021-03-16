package es.ujaen.uvirtual.utilidades;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilidad para la gestión de los parámetros del datatable.
 * @author ATISoluciones
 */
public class DataTable {
	public static final String PARAM_CURRENT_PAGE = "page";
	
	private Integer currentPage;
	
	/**
	 * Crear un DT a partir del request.
	 * @param request .
	 * @throws UVException .
	 */
	public DataTable(HttpServletRequest request) throws UVException {
		try {
			this.currentPage = Integer.parseInt(request.getParameter(DataTable.PARAM_CURRENT_PAGE));	
		} catch (NumberFormatException err) {
			throw new UVException("Datatable parámetro no válido");
		}			
	}
	
	public Integer getCurrentPage() {
		return this.currentPage;
	}
}
