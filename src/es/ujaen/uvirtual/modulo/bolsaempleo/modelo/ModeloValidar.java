package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de las validaciones.
 * 
 * @author ATISoluciones 2021
 */
public class ModeloValidar {
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_CODIGO_AREA = 2;
	public static final int ORDER_COLUMN_INDEX_AREA = 3;
	
	public static final String TOTAL_NO_VALIDADO = "TOTAL_NO_VALIDADO";
	public static final String TOTAL_VALIDADO = "TOTAL_VALIDADO";
	public static final String TOTAL_EXCLUIDO = "TOTAL_EXCLUIDO";
	public static final String TOTAL_MERITOS = "TOTAL_MERITOS";

	protected static ModeloValidar eInstancia = null;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloValidar();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloValidar obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**
	 * Listado de bolsas sujetas a afinidad en una solicitud.
	 * 
	 * @param convocatoria .
	 * @param params    .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<BolsaValidacion> listadoAreasSujetasAfinidad(Convocatoria convocatoria, Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoAreas(convocatoria, params);
	}

	/**
	 * Listado de bolsas NO sujetas a afinidad en una solicitud.
	 * 
	 * @param convocatoria .
	 * @param params    .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<BolsaValidacion> listadoAreasNoSujetasAfinidad(Convocatoria convocatoria, Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoAreas(convocatoria, params);
	}
	
	/**
	 * Devuelve los totales de méritos de los candidatos para un convocatoria.
	 * @param convocatoria .
	 * @return un hashmap con los pares: "idbolsa" - "número de meritos"
	 * @throws SQLException .
	 */
	public HashMap<Integer, Integer> getTotalesMeritosConvocatoria(Convocatoria convocatoria) throws SQLException {
		HashMap<Integer, Integer> totales = new HashMap<Integer, Integer>();
					
		try (Connection con = ConexionUvirtual.obtenerInstancia();) {
			String sql =
					"SELECT bepbol.CODNUM, COUNT(*) AS COUNT "
					+ "FROM TBEP_BOLSAS bepbol "
					+ "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
					+ "INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.BEPBOL_CODNUM = bepbol.CODNUM "
					+ "INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM "
					+ "INNER JOIN TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM "
					+ "INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM "
					+ "INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM "
					+ "INNER JOIN TBEP_AFINIDADES bepafi ON bepafi.CODNUM = bepite.AFINIDAD "
					+ "WHERE 1=1 "
					+ "AND bepsol.BEPCON_CODNUM = ? "
					+ "GROUP BY bepbol.CODNUM ";
								
			try (PreparedStatement stmt = con.prepareStatement(sql);) {
				int param = 1;
				stmt.setInt(param++, convocatoria.getCodNum());
				
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						totales.put(rs.getInt("CODNUM"), rs.getInt("COUNT"));
					}
				}
			}			
		}
		
		return totales;
	}
	
	private BolsaEmpleoDataTable<BolsaValidacion> listadoAreas(Convocatoria convocatoria, Map<String, String[]> params) 
			throws SQLException, UVException {
		List<BolsaValidacion> rows = new ArrayList<>();
		BolsaEmpleoDataTable<BolsaValidacion> dataTable = new BolsaEmpleoDataTable<BolsaValidacion>(params);
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();

		if (convocatoria == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		// leemos el total de méritos por bolsa
		HashMap<Integer, Integer> totalMeritosPorBolsa = this.getTotalesMeritosConvocatoria(convocatoria);

		// seleccionamos las bolsas, con meritos, en la convocatoria pasada
		String consulta = 
				"SELECT DISTINCT bepbol.* " 
				+ "FROM TBEP_BOLSAS bepbol "
				+ "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
				+ "INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.BEPBOL_CODNUM = bepbol.CODNUM "
				+ "INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM "				
				+ "INNER JOIN TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM "
				+ "INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM "
				+ "INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM "
				+ "INNER JOIN TBEP_AFINIDADES bepafi ON bepafi.CODNUM = bepite.AFINIDAD " + "WHERE 1=1 "
				+ "AND bepsol.BEPCON_CODNUM = ? ";

		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepbol.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO_AREA, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");

		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());) {
			int paramIndex = 1;
			stmt.setInt(paramIndex, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex++, convocatoria.getCodNum());

			dataTable.setFiltersParams(stmt, stmtCount, 1);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = modeloBolsa.createFromResultSet(rs);
					BolsaValidacion bolsaValidacion = new BolsaValidacion(bolsa);
					bolsaValidacion.setTotalMeritos(totalMeritosPorBolsa.get(bolsa.getCodNum()));
					
					rows.add(bolsaValidacion);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
}
