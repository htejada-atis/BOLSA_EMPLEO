package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
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
	public BolsaEmpleoDataTable<Bolsa> listadoAreasSujetasAfinidad(Convocatoria convocatoria, Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoAreas(convocatoria, true, params);
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
	public BolsaEmpleoDataTable<Bolsa> listadoAreasNoSujetasAfinidad(Convocatoria convocatoria, Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoAreas(convocatoria, false, params);
	}

	private BolsaEmpleoDataTable<Bolsa> listadoAreas(Convocatoria convocatoria, Boolean sujetasAfinidad, Map<String, String[]> params) 
			throws SQLException, UVException {
		List<Bolsa> rows = new ArrayList<>();
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<Bolsa>(params);
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();

		if (convocatoria == null) {
			dataTable.setData(rows);
			return dataTable;
		}

		// seleccionamos las bolsas, con meritos con afinidad, en la solicitud pasada
		String consulta = "SELECT bepbol.* " + "FROM TBEP_BOLSAS bepbol "
				+ "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
				+ "INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.BEPBOL_CODNUM = bepbol.CODNUM "
				+ "INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM "				
				+ "INNER JOIN TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM "
				+ "INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM "
				+ "INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM "
				+ "INNER JOIN TBEP_AFINIDADES bepafi ON bepafi.CODNUM = bepite.AFINIDAD " + "WHERE 1=1 "
				+ "AND bepsol.BEPCON_CODNUM = ? "
				+ "AND bepafi.FLGSUJETOAFINIDAD = '" + (sujetasAfinidad ? "S" : "N") + "' ";

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
					rows.add(modeloBolsa.createFromResultSet(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
}
